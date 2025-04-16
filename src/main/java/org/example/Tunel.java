package org.example;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class Tunel {
    private Semaphore esperarAntes = new Semaphore(3, true);
    private CyclicBarrier esperar = new CyclicBarrier(3);
    private Semaphore pasar = new Semaphore(1,true);
    private ArrayList listaPasar = new ArrayList<Humano>();
    private ArrayList listaPasando = new ArrayList<Humano>();
    private ArrayList listaRegresar = new ArrayList<Humano>();
    private ZonaRiesgo zonaRiesgo;

    public Tunel(ZonaRiesgo zr){
        zonaRiesgo = zr;
    }

    public void irExterior(Humano hu){
        try {
            esperarAntes.acquire();
            listaPasar.add(hu);
            esperar.await();
            pasar.acquire();
            esperarAntes.release();
            listaPasar.remove(hu);
            listaPasando.add(hu);
            sleep(1000);
            listaPasando.remove(hu);
            pasar.release();
            zonaRiesgo.entrarHumano(hu);
            sleep((long) (Math.random()*3000+5000));
            zonaRiesgo.salirHumano(hu);
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }




}
