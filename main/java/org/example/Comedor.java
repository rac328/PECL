package org.example;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Comedor {

    private AtomicInteger comidaDisponible = new AtomicInteger(0);
    private Semaphore comer = new Semaphore(1);
    private ReentrantLock comidaEsperar = new ReentrantLock();
    private Condition noComida = comidaEsperar.newCondition();

    public Comedor() {
    }

    public void depositarComida(Humano hu) {
        if (!hu.isMuerto()) {
            if (hu.llevaComida()) {
                
                comidaDisponible.incrementAndGet();
                comidaDisponible.incrementAndGet();
                Logger.escribir("El humano "+hu.getIdHumanoStr()+" ha traido 2 piezas de comida. Comida restante: "+comidaDisponible.toString());
                hu.setComida(false);
                comidaEsperar.lock();
                noComida.signal();
                comidaEsperar.unlock();
            }
        }else{return;}
    }
    

    public void comer(Humano hu) {
        if (!hu.isMuerto()) {
            try {
                comer.acquire();
                comidaEsperar.lock();
                while (comidaDisponible.get() == 0) {
                    Logger.escribir("El humano "+hu.getIdHumanoStr()+" esta esperando para comer pero no hay comida.");
                    noComida.await();
                }
                comidaDisponible.decrementAndGet();
                Logger.escribir("El humano "+hu.getIdHumanoStr()+" ha comido. Comida restante: "+comidaDisponible.toString());
                comidaEsperar.unlock();
                comer.release();
            } catch (InterruptedException e) {
                return; //ha muerto
            }
        } else {
            return;
        }
    }
}
