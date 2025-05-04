package Parte1;

import Visuals.ApocalipsisZombi.VentanaServ;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;
import javax.swing.SwingUtilities;

public class Tunel {

    private int id;
    private Semaphore esperarAntes = new Semaphore(3, true);
    private CyclicBarrier esperar = new CyclicBarrier(3);
    private Semaphore pasar = new Semaphore(1, true);
    private LinkedBlockingQueue<Humano> listaPasar = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Humano> listaPasando = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Humano> listaRegresar = new LinkedBlockingQueue<>();
    private ZonaRiesgo zonaRiesgo;
    private ReentrantLock candado = new ReentrantLock();
    private Condition condicion = candado.newCondition();
    private Logger logger;
    private VentanaServ ventana;

    public Tunel(int pid, ZonaRiesgo zr, Logger log, VentanaServ vServ) {
        id = pid;
        zonaRiesgo = zr;
        logger = log;
        ventana = vServ;
    }

    public LinkedBlockingQueue<Humano> getListaPasar() {
        return listaPasar;
    }

    public LinkedBlockingQueue<Humano> getListaPasando() {
        return listaPasando;
    }

    public LinkedBlockingQueue<Humano> getListaRegresar() {
        return listaRegresar;
    }

    public int getId() {
        return id;
    }

    public void irExterior(Humano hu) {
        try {
            hu.comprobarPausaHumano(); //Comprueba la pausa de la ejecución
            esperarAntes.acquire(); //Mira si hay hueco para pasar a la zona de espera
            listaPasar.add(hu); // Se añade en la zona de espera
            SwingUtilities.invokeLater(new Runnable() { //Se actualiza la ventana del servidor
                public void run() {
                    ventana.actualizarListaPasarTunel(getId());
                }
            });
            // En función de la gente esperando se guarda en el log un mensaje u otro
            if (esperar.getNumberWaiting() + 1 == 3) {
                logger.escribir("Humano " + hu.getIdHumanoStr() + " ha llegado al tunel " + id + " y ya son 3. Listos para salir!");
            } else {
                logger.escribir("Humano " + hu.getIdHumanoStr() + " esta esperando para salir por el tunel " + id + ". Humanos esperando: " + (1 + esperar.getNumberWaiting()));
            }
            hu.comprobarPausaHumano(); // Comprueba la pausa
            esperar.await(); // Cyclic Barrier para esperar a que sean 3
            hu.comprobarPausaHumano(); // Comprueba la pausa

            candado.lock();
            try {
                hu.comprobarPausaHumano(); // Comprueba la pausa

                // Mientras que la lista de gente que quiere regresar al refugio no esté vacía, entran al while para esperar
                while (!listaRegresar.isEmpty()) {
                    logger.escribir("Humano " + hu.getIdHumanoStr() + " esta esperando a que entre otro humano al refugio por el tunel " + id);
                    condicion.await();
                }
            } finally {
                candado.unlock();
            }

            hu.comprobarPausaHumano(); // Comprueba la pausa
            pasar.acquire(); // Toma el semáforo para que pasen de 1 en 1
            logger.escribir("Humano " + hu.getIdHumanoStr() + " está pasando por el tunel " + id);
            esperarAntes.release(); // Se libera un token del semáforo de la zona de espera del tunel
            listaPasar.remove(hu); // Se elimina de la lista que se muestra en la interfaz
            SwingUtilities.invokeLater(new Runnable() { // Se actualiza la interfaz
                public void run() {
                        ventana.actualizarListaPasarTunel(getId());
                    }
            });
            listaPasando.add(hu); //Se añade en la lista de personas pasando
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                        ventana.actualizarListaPasandoTunel(getId());
                    }
            });
            sleep(1000); // 1000 milisegundos que es el tiempo que tarda en pasar
            listaPasando.remove(hu); //Se elimina de la lista de personas pasando
            SwingUtilities.invokeLater(new Runnable() { // Se actualiza la ventana
                public void run() {
                        ventana.actualizarListaPasandoTunel(getId());
                    }
            });
            logger.escribir("Humano " + hu.getIdHumanoStr() + " ha terminado de pasar por el tunel " + id);
            pasar.release(); // Se libera el semáforo para que pase el siguiente
            hu.comprobarPausaHumano(); //Comprueba la pausa
            hu.comprobarPausaHumano();
            logger.escribir("Humano " + hu.getIdHumanoStr() + " ha entrado a la zona de riesgo " + id);
            zonaRiesgo.entrarHumano(hu);
            SwingUtilities.invokeLater(new Runnable() { // Se actualiza la ventana
                public void run() {
                    ventana.actualizarHumanosZP(getId());
                }
            });
            sleep((long) (Math.random() * 3000 + 2000));
            hu.comprobarPausaHumano();

        } catch (InterruptedException | BrokenBarrierException e) {
            if (hu.getEsperandoAtaque()) {
                System.out.println(hu.getIdHumanoStr() + " Está siendo atacado y puede morir.");
                hu.defensa();
            }
        }
    }

    public void venirDelExterior(Humano hu) {
        try {
            hu.comprobarPausaHumano(); //Se comprueba si necesita parar la ejecución
            zonaRiesgo.salirHumano(hu); //Sale de la Zona de Riesgo y se elimina de la lista

            logger.escribir("Humano " + hu.getIdHumanoStr() + " ha terminado en la zona de riesgo " + id + " y vuelve al refugio");

            //Si sale marcado se registra en el log
            if (hu.getMarcado()) {
                logger.escribir("Vuelve el humano " + hu.getIdHumanoStr() + " marcado y sin comida.");
            } else {
                logger.escribir("Vuelve el humano " + hu.getIdHumanoStr() + " con comida.");
            }

            listaRegresar.add(hu); // Se añade en la lista de personas que quieren regresar

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ventana.actualizarListaRegresarTunel(getId());
                }
            });

            logger.escribir("Humano " + hu.getIdHumanoStr() + " esta esperando a entrar por el tunel " + id);

            pasar.acquire(); //Toma el semáforo para pasar por la zona estrecha del túnel

            listaRegresar.remove(hu); //Sale de lista de personas esperando para regresar

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ventana.actualizarListaRegresarTunel(getId());
                }
            });

            listaPasando.add(hu); //Se añade a la lista de gente pasando por la zona estrecha

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ventana.actualizarListaPasandoTunel(getId());
                }
            });

            logger.escribir("Humano " + hu.getIdHumanoStr() + " volviendo por el tunel " + id);

            sleep(1000); // Tiempo que tarda en pasar

            listaPasando.remove(hu); // Se elimina de la lista de personas pasando una vez ha pasado el tiempo

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ventana.actualizarListaPasandoTunel(getId());
                }
            });

            logger.escribir("Humano " + hu.getIdHumanoStr() + " pasando por el tunel " + id);

            pasar.release(); // Libera el semáforo para que pase el siguiente

            // Despierta a los hilos que estuvieran esperando para pasar a las zonas de riesgo
            candado.lock();
            try {
                condicion.signalAll();
            } finally {
                candado.unlock();
            }

            hu.comprobarPausaHumano(); // Comprueba la pausa de la ejecución

        } catch (InterruptedException e) {
            System.out.println("muerto");
        }
    }
}
