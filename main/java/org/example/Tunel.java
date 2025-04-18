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
    private Semaphore pasar = new Semaphore(1, true);
    private ArrayList listaPasar = new ArrayList<Humano>();
    private ArrayList listaPasando = new ArrayList<Humano>();
    private ArrayList listaRegresar = new ArrayList<Humano>();
    private ZonaRiesgo zonaRiesgo;
    private ReentrantLock candado = new ReentrantLock();
    private Condition condicion = candado.newCondition();

    public Tunel(ZonaRiesgo zr) {
        zonaRiesgo = zr;
    }

    public void irExterior(Humano hu) {
        try {
            esperarAntes.acquire();
            listaPasar.add(hu);
            esperar.await();

            candado.lock();
            try {
                while (!listaRegresar.isEmpty()) {
                    condicion.await();
                }

                pasar.acquire();
                esperarAntes.release();
                listaPasar.remove(hu);
                listaPasando.add(hu);
                sleep(1000);
                listaPasando.remove(hu);
                pasar.release();
            } finally {
                candado.unlock();
            }
            zonaRiesgo.entrarHumano(hu);
            sleep((long) (Math.random() * 3000 + 2000));

        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println(hu.getIdHumanoStr() + " ha muerto y ahora es un zombie.");

        }
    }

    public void venirDelExterior(Humano hu) {
        try {
            if (hu.isMuerto()) {
                return;
            } else {
                zonaRiesgo.salirHumano(hu);
                if (hu.getMarcado()) {
                    System.out.println("Vuelve el humano " + hu.getIdHumanoStr() + " marcado");
                    hu.setComida(false);
                } else {
                    System.out.println("Vuelve el humano " + hu.getIdHumanoStr());
                    hu.setComida(true);
                }
                listaRegresar.add(hu);
                pasar.acquire();
                listaRegresar.remove(hu);
                sleep(1000);
                pasar.release();
                candado.lock();
                try {
                    condicion.signal();
                } finally {
                    candado.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
