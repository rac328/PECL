package org.example;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Pausa {
    private ReentrantLock lockDetener = new ReentrantLock();
    private Condition condicion = lockDetener.newCondition();
    private boolean parar = false;

    public void pararEjecucion() {
        parar = true;
    }

    public void continuarEjecucion(){
        parar = false;
        lockDetener.lock();
        condicion.signalAll();
        lockDetener.unlock();
    }

    public void comprobarPausa(){
        try {
            lockDetener.lock();
            if (parar) {
                condicion.await();
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            lockDetener.unlock();
        }
    }
}
