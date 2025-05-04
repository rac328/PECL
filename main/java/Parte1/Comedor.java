package Parte1;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.SwingUtilities;

import Visuals.ApocalipsisZombi.*;

public class Comedor {

    private AtomicInteger comidaDisponible = new AtomicInteger(0);
    private Semaphore comer = new Semaphore(1, true);
    private ReentrantLock comidaEsperar = new ReentrantLock();
    private Condition noComida = comidaEsperar.newCondition();
    private VentanaServ ventana;
    private LinkedBlockingQueue<Humano> listaComedor = new LinkedBlockingQueue<>();
    private Logger logger;

    public Comedor(Logger log, VentanaServ vent) {
        ventana = vent;
        logger = log;
    }

    public void depositarComida(Humano hu) {
        if (!hu.getMarcado() && !hu.isMuerto()) {
            hu.comprobarPausaHumano();
            comidaDisponible.addAndGet(2); // Si todo ha ido bien depositan comida

            logger.escribir("El humano " + hu.getIdHumanoStr() + " ha traido 2 piezas de comida. Comida restante: " + comidaDisponible.toString());
            hu.comprobarPausaHumano();
            comidaEsperar.lock(); //Toman el lock para realizar el signall
            try {
                noComida.signalAll(); // Al hacer el signal despiertan a los que estuvieran esperando por comida
            }
            finally {
                comidaEsperar.unlock();
            }

        }
    }

    public void comer(Humano hu) {
        if (!hu.isMuerto()) {
            try {
                hu.comprobarPausaHumano(); // Comprueba la pausa
                listaComedor.add(hu); // Se añaden a la lista del comedor
                SwingUtilities.invokeLater(new Runnable() { // Se actualiza la ventana
                    public void run() {
                            ventana.actualizarHumanosComedor();
                        }
                });

                comer.acquire(); // Toma el semáforo para la espera ordenada
                comidaEsperar.lock(); //Se toma el lock para comprobar la comida
                hu.comprobarPausaHumano(); // Comprueba la pausa
                try {
                    while (comidaDisponible.get() == 0) { // Comprueban si no hay comida para quedarse esperando
                    logger.escribir("El humano " + hu.getIdHumanoStr() + " esta esperando para comer pero no hay comida.");
                    noComida.await(); // Se queda esperando a que traigan comida
                }
                }finally {
                    comidaEsperar.unlock();
                }
                hu.comprobarPausaHumano();
                comer.release(); // Dejan el semáforo para que pase el siguiente
                comidaDisponible.decrementAndGet(); // Se decrementa en una unidad ya que comen
                hu.comprobarPausaHumano();

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                            ventana.actualizarComida();
                        }
                });
                logger.escribir("El humano " + hu.getIdHumanoStr() + " esta comiendo. Comida restante: " + comidaDisponible.toString());

                sleep(3000 + (int) (2000 * Math.random())); // Tiempo que tardan en comer
                logger.escribir("El humano " + hu.getIdHumanoStr() + " ha terminado de comer.");

                listaComedor.remove(hu); // Se eliminan de la lista
                hu.comprobarPausaHumano();

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ventana.actualizarHumanosComedor();
                    }
                });

            } catch (InterruptedException e) {
                return; //ha muerto
            }
        } else {
            return;
        }
    }

    public LinkedBlockingQueue<Humano> getListaHumanosComedor() {
        return listaComedor;
    }

    public AtomicInteger getComida() {
        return comidaDisponible;
    }
}
