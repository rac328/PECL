package org.example;

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
    private Semaphore comer = new Semaphore(1);
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
            listaComedor.add(hu);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ventana.actualizarHumanosComedor();
                }
            });

            comidaDisponible.addAndGet(2);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ventana.actualizarComida();
                }
            });
            logger.escribir("El humano " + hu.getIdHumanoStr() + " ha traido 2 piezas de comida. Comida restante: " + comidaDisponible.toString());
            comidaEsperar.lock();
            noComida.signalAll();
            comidaEsperar.unlock();
            listaComedor.remove(hu);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ventana.actualizarHumanosComedor();
                }
            });

        }
    }

    public void comer(Humano hu) {
        if (!hu.isMuerto()) {
            try {
                comer.acquire();
                try {
                    comidaEsperar.lock();
                    listaComedor.add(hu);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            ventana.actualizarHumanosComedor();
                        }
                    });

                    while (comidaDisponible.get() == 0) {
                        logger.escribir("El humano " + hu.getIdHumanoStr() + " esta esperando para comer pero no hay comida.");
                        noComida.await();
                    }
                    comidaDisponible.decrementAndGet();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            ventana.actualizarComida();
                        }
                    });
                    logger.escribir("El humano " + hu.getIdHumanoStr() + " esta comiendo. Comida restante: " + comidaDisponible.toString());
                } finally {
                    comidaEsperar.unlock();
                }
                comer.release();

                sleep(3000 + (int) (2000 * Math.random()));
                logger.escribir("El humano " + hu.getIdHumanoStr() + " ha terminado de comer.");
                listaComedor.remove(hu);
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
