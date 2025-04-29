package org.example;

import Visuals.ApocalipsisZombi.VentanaServ;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;
import javax.swing.SwingUtilities;

public class Tunel implements Serializable {

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
    private VentanaServ ventana;

    public Tunel(int pid, ZonaRiesgo zr, VentanaServ vServ) {
        id = pid;
        zonaRiesgo = zr;
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
            hu.comprobarPausaHumano();
            esperarAntes.acquire();
            listaPasar.add(hu);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ventana.actualizarListaPasarTunel(getId());
                }
            });
            if (esperar.getNumberWaiting() + 1 == 3) {
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha llegado al tunel " + id + " y ya son 3. Listos para salir!");
            } else {
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " esta esperando para salir por el tunel " + id + ". Humanos esperando: " + (1 + esperar.getNumberWaiting()));
            }
            hu.comprobarPausaHumano();
            esperar.await();
            hu.comprobarPausaHumano();

            candado.lock();
            try {
                //Quitar
                // if(id == 2){System.out.println("negro");}
                hu.comprobarPausaHumano();
                while (!listaRegresar.isEmpty()) {
                    Logger.escribir("Humano " + hu.getIdHumanoStr() + " esta esperando a que entre otro humano al refugio por el tunel " + id);
                    condicion.await();
                }
                hu.comprobarPausaHumano();
                pasar.acquire();
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " está pasando por el tunel " + id);
                esperarAntes.release();
                listaPasar.remove(hu);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ventana.actualizarListaPasarTunel(getId());
                    }
                });
                listaPasando.add(hu);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ventana.actualizarListaPasandoTunel(getId());
                    }
                });
                sleep(1000);
                listaPasando.remove(hu);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ventana.actualizarListaPasandoTunel(getId());
                    }
                });
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha terminado de pasar por el tunel " + id);
                pasar.release();
                hu.comprobarPausaHumano();
            } finally {
                candado.unlock();
            }
            hu.comprobarPausaHumano();
            Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha entrado a la zona de riesgo " + id);
            zonaRiesgo.entrarHumano(hu);
            sleep((long) (Math.random() * 3000 + 2000));
            hu.comprobarPausaHumano();

        } catch (InterruptedException | BrokenBarrierException e) {
            if (hu.getEsperandoAtaque()) {
                System.out.println(hu.getIdHumanoStr() + " Está siendo atacado y puede morir.");
                hu.Defensa();
            }
        }
    }

    public void venirDelExterior(Humano hu) {
        try {
            hu.comprobarPausaHumano();
            zonaRiesgo.salirHumano(hu);
            Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha terminado en la zona de riesgo " + id + " y vuelve al refugio");

            if (hu.getMarcado()) {
                Logger.escribir("Vuelve el humano " + hu.getIdHumanoStr() + " marcado y sin comida.");
            } else {
                Logger.escribir("Vuelve el humano " + hu.getIdHumanoStr() + " con comida.");
            }
            listaRegresar.add(hu);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ventana.actualizarListaRegresarTunel(getId());
                }
            });
            Logger.escribir("Humano " + hu.getIdHumanoStr() + " esta esperando a entrar por el tunel " + id);
            pasar.acquire();
            listaRegresar.remove(hu);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ventana.actualizarListaRegresarTunel(getId());
                }
            });
            listaPasando.add(hu);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ventana.actualizarListaPasandoTunel(getId());
                }
            });
            Logger.escribir("Humano " + hu.getIdHumanoStr() + " volviendo por el tunel " + id);
            sleep(1000);
            listaPasando.remove(hu);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ventana.actualizarListaPasandoTunel(getId());
                }
            });
            Logger.escribir("Humano " + hu.getIdHumanoStr() + " pasando por el tunel " + id);
            pasar.release();

            candado.lock();
            try {
                condicion.signalAll();
            } finally {
                candado.unlock();
            }
            hu.comprobarPausaHumano();
        } catch (InterruptedException e) {
            System.out.println("muerto");
        }
    }
}
