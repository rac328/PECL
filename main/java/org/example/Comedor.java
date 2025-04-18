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
                System.out.println("Comida RESTANTE: " + comidaDisponible.toString());
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
                    noComida.await();
                }
                comidaDisponible.decrementAndGet();
                comidaEsperar.unlock();
                sleep((long) (Math.random() * 3000 + 5000));
                System.out.println("Alguien ha comido. Comida RESTANTE: " + comidaDisponible.toString());
                comer.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            return;
        }
    }
}
