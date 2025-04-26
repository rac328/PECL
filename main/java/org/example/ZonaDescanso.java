package org.example;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;
import javax.swing.SwingUtilities;

public class ZonaDescanso {

    private LinkedBlockingQueue<Humano> listaDescansando = new LinkedBlockingQueue<>();
    private Ventana ventana;

    public ZonaDescanso() {}

    public LinkedBlockingQueue<Humano> getListaHumanosDescansando() {
        return listaDescansando;
    }

    public void descansarVuelta(Humano hu) {
        if (!hu.isMuerto()) {
            try {
                synchronized (this) {
                    listaDescansando.add(hu);
                    Logger.escribir("Humano " + hu.getIdHumanoStr() + " está descansando tras volver del exterior.");
                }
                sleep(2000 + (int) (2000 * Math.random()));
                synchronized (this) {
                    listaDescansando.remove(hu);
                    Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha terminado de descansar.");
                }
            } catch (InterruptedException ie) {
                System.out.println("Error descansar al volver humano " + hu.getIdHumanoStr());
            }
        } else {
            return;
        }
    }

    public void descansarMarcado(Humano hu) {
        try {
            synchronized (this) {
                listaDescansando.add(hu);
                Logger.escribir("Humano marcado " + hu.getIdHumanoStr() + " está descansando tras ser atacado para curarse.");
                hu.setMarcado(false);
            }
            sleep(3000 + (int) (2000 * Math.random()));
            synchronized (this) {
                listaDescansando.remove(hu);
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " se ha curado y ya no esta marcado.");
            }
        } catch (InterruptedException ie) {
            System.out.println("Error descansar humano marcado " + hu.getIdHumanoStr());
        }
    }
}
