package org.example;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Tunel {

    private int id;
    private Semaphore esperarAntes = new Semaphore(3, true);
    private CyclicBarrier esperar = new CyclicBarrier(3);
    private Semaphore pasar = new Semaphore(1, true);
    private LinkedBlockingQueue<Humano> listaPasar = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Humano> listaPasando = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Humano> listaRegresar = new LinkedBlockingQueue<>();
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
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha llegado al tunel " + id + " y ya son 3. Listos para salir!");
            }
            else {
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " esta esperando para salir por el tunel "+id+". Humanos esperando: " + (1 + esperar.getNumberWaiting()));
            }
            esperar.await();

            candado.lock();
            try {
                //Quitar
               // if(id == 2){System.out.println("negro");}

                while (!listaRegresar.isEmpty()) {
                    Logger.escribir("Humano " + hu.getIdHumanoStr() + " esta esperando a que entre otro humano al refugio por el tunel "+id);
                    condicion.await();
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
            }
            finally {
                candado.unlock();
            }
            Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha entrado a la zona de riesgo "+id);
            zonaRiesgo.entrarHumano(hu);
            sleep((long) (Math.random() * 3000 + 2000));

        }
        catch (InterruptedException | BrokenBarrierException e) {
            if(hu.getEsperandoAtaque()){
                System.out.println(hu.getIdHumanoStr() + " Está siendo atacado y puede morir.");
                hu.Defensa();
            }
        }
    }


    public void venirDelExterior(Humano hu) {
        try {
                zonaRiesgo.salirHumano(hu);
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha terminado en la zona de riesgo " + id + " y vuelve al refugio");

                if (hu.getMarcado()) {
                    Logger.escribir("Vuelve el humano " + hu.getIdHumanoStr() + " marcado y sin comida.");
                } else {
                    Logger.escribir("Vuelve el humano " + hu.getIdHumanoStr() + " con comida.");
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
                }
                finally {
                    candado.unlock();
                }
        } catch (InterruptedException e) {
            System.out.println("muerto");
        }
    }
}
