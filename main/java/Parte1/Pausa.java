package Parte1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Pausa {
    private ReentrantLock lockDetener = new ReentrantLock();
    private Condition condicion = lockDetener.newCondition();
    private boolean parar = false;
    private Logger logger;

    public Pausa(Logger log){
        logger = log;
    }

    public void pararEjecucion() {
        logger.escribir("SE PARA LA EJECUCIÓN DEL PROGRAMA");
        parar = true;
    }

    public void continuarEjecucion(){
        logger.escribir("SE REANUDA LA EJECUCIÓN DEL PROGRAMA");
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
