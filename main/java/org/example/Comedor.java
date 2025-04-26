package org.example;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.SwingUtilities;

public class Comedor {

    private AtomicInteger comidaDisponible = new AtomicInteger(0);
    private Semaphore comer = new Semaphore(1);
    private ReentrantLock comidaEsperar = new ReentrantLock();
    private Condition noComida = comidaEsperar.newCondition();
    private Ventana ventana;
    private LinkedBlockingQueue<Humano> listaComedor = new LinkedBlockingQueue<>();

    public Comedor() {

    }
    

    public void depositarComida(Humano hu) {
            if (!hu.getMarcado() && !hu.isMuerto()) {
                listaComedor.add(hu);

                comidaDisponible.addAndGet(2);

                Logger.escribir("El humano "+hu.getIdHumanoStr()+" ha traido 2 piezas de comida. Comida restante: "+comidaDisponible.toString());
                comidaEsperar.lock();
                noComida.signalAll();
                comidaEsperar.unlock();
                listaComedor.remove(hu);

        }
    }
    

    public void comer(Humano hu) {
        if (!hu.isMuerto()) {
            try {
                comer.acquire();
                comidaEsperar.lock();
                listaComedor.add(hu);

                while (comidaDisponible.get() == 0) {
                    Logger.escribir("El humano "+hu.getIdHumanoStr()+" esta esperando para comer pero no hay comida.");
                    noComida.await();
                }
                comidaDisponible.decrementAndGet();

                comidaEsperar.unlock();
                comer.release();
                Logger.escribir("El humano "+hu.getIdHumanoStr()+" esta comiendo. Comida restante: "+comidaDisponible.toString());
                sleep(3000+(int)(2000*Math.random()));
                Logger.escribir("El humano "+hu.getIdHumanoStr()+" ha terminado de comer.");
                listaComedor.remove(hu);

            } catch (InterruptedException e) {
                return; //ha muerto
            }
        } else {
            return;
        }
    }
    
    public LinkedBlockingQueue<Humano> getListaHumanosComedor(){
        return listaComedor;
    }
    
    public AtomicInteger getComida(){
        return comidaDisponible;
    }
}
