package org.example;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Tunel {
    private Semaphore esperarAntes = new Semaphore(3, true);
    private CyclicBarrier esperar = new CyclicBarrier(3);
    private Semaphore pasar = new Semaphore(1,true);
    private ArrayList listaPasar = new ArrayList<Humano>();
    private ArrayList listaPasando = new ArrayList<Humano>();
    private ArrayList listaRegresar = new ArrayList<Humano>();
    private ZonaRiesgo zonaRiesgo;
    private ReentrantLock candado = new ReentrantLock();
    private Condition condicion = candado.newCondition();

    public Tunel(ZonaRiesgo zr){
        zonaRiesgo = zr;
    }

    public void irExterior(Humano hu){
        try {
            esperarAntes.acquire();
            listaPasar.add(hu);
            esperar.await();

            candado.lock();
            while(!listaRegresar.isEmpty()){
                condicion.await();
            }
            candado.unlock();

            pasar.acquire();
            esperarAntes.release();
            listaPasar.remove(hu);
            listaPasando.add(hu);
            sleep(1000);
            listaPasando.remove(hu);
            pasar.release();
            zonaRiesgo.entrarHumano(hu);
            sleep((long) (Math.random()*3000+5000));
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    public void venirDelExterior(Humano hu){
        try {
            zonaRiesgo.salirHumano(hu);
            System.out.println("Vuelve el humano" + hu.getIdHumano());
            listaRegresar.add(hu);
            pasar.acquire();
            listaRegresar.remove(hu);
            sleep(1000);
            pasar.release();
            candado.lock();
            condicion.signal();
            candado.unlock();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }




}
