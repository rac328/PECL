package org.example;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Tunel {

    private int id;
    private Semaphore esperarAntes = new Semaphore(3, true);
    private CyclicBarrier esperar = new CyclicBarrier(3);
    private Semaphore pasar = new Semaphore(1, true);
    private ArrayList listaPasar = new ArrayList<Humano>();
    private ArrayList listaPasando = new ArrayList<Humano>();
    private ArrayList listaRegresar = new ArrayList<Humano>();
    private ZonaRiesgo zonaRiesgo;
    private ReentrantLock candado = new ReentrantLock();
    private Condition condicion = candado.newCondition();

    public Tunel(int pid, ZonaRiesgo zr) {
        id = pid;
        zonaRiesgo = zr;
    }

    public void irExterior(Humano hu) {
        try {
            esperarAntes.acquire();
            listaPasar.add(hu);
            if (esperar.getNumberWaiting() + 1 == 3) {
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha llegado al tunel "+id+" y ya son 3. Listos para salir!");
            } else {
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " esta esperando para salir por el tunel "+id+". Humanos esperando: " + (1 + esperar.getNumberWaiting()));
            }
            esperar.await();

            candado.lock();
            try {
                while (!listaRegresar.isEmpty()) {
                    condicion.await();
                    Logger.escribir("Humano " + hu.getIdHumanoStr() + " esta esperando a que entre otro humano al refugio por el tunel "+id);
                }

                pasar.acquire();
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " está pasando por el tunel "+id);
                esperarAntes.release();
                listaPasar.remove(hu);
                listaPasando.add(hu);
                sleep(1000);
                listaPasando.remove(hu);
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha terminado de pasar por el tunel "+id);
                pasar.release();
            } finally {
                candado.unlock();
            }
            Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha entrado a la zona de riesgo "+id);
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
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha terminado en la zona de riesgo " +id+" y vuelve al refugio");
                zonaRiesgo.salirHumano(hu);
                if (hu.getMarcado()) {
                    Logger.escribir("Vuelve el humano " + hu.getIdHumanoStr() + " marcado y sin comida.");
                    hu.setComida(false);
                } else {
                    Logger.escribir("Vuelve el humano " + hu.getIdHumanoStr() + " con comida.");
                    hu.setComida(true);
                }
                listaRegresar.add(hu);
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " esta esperando a entrar por el tunel "+id);
                pasar.acquire();
                listaRegresar.remove(hu);
                listaPasando.add(hu);
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " volviendo por el tunel "+id);
                sleep(1000);
                listaPasando.remove(hu);
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " pasando por el tunel "+id);
                pasar.release();
                candado.lock();
                try {
                    condicion.signalAll();
                } finally {
                    candado.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
