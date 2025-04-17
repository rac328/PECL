package org.example;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Comedor {
    private AtomicInteger comidaDisponible = new AtomicInteger(0);
    private Semaphore comer;
    private ReentrantLock comidaEsperar = new ReentrantLock();
    private Condition noComida = comidaEsperar.newCondition();

    public Comedor(){
    }

    public void depositarComida(Humano hu){
        //System.out.println("Comida RESTANTE: "+comidaDisponible.toString());
        if(hu.llevaComida()){
            comidaDisponible.incrementAndGet();
            comidaDisponible.incrementAndGet();
            System.out.println("Comida RESTANTE: "+comidaDisponible.toString());
            hu.setComida(false);
            comidaEsperar.lock();
            noComida.signal();
            comidaEsperar.unlock();
            
        }
    }

    public void comer(){
            try {
                comidaEsperar.lock();
                while(comidaDisponible.get() == 0){
                    noComida.await();
                }
                comidaDisponible.decrementAndGet();
                comidaEsperar.unlock();
                sleep((long) (Math.random()*3000+5000));

            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

    }
}
