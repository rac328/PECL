package org.example;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.SwingUtilities;

public class ZonaComun {

    private Tunel[] arrayTunel;
    private LinkedBlockingQueue<Humano> listaHumanos = new LinkedBlockingQueue<>();
    private Ventana ventana;

    public ZonaComun(Tunel[] at, Ventana vent) {
        arrayTunel = at;
        ventana = vent;
    }

    public void entrarZonaComun(Humano hu) {
        listaHumanos.add(hu);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ventana.actualizarHumanosZonaComun();
            }
        });
        Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha entrado a la Zona Común.");
    }

    public void prepararse(Humano hu) {
        try {
            Logger.escribir("Humano " + hu.getIdHumanoStr() + " se esta preparando.");
            sleep((long) (Math.random() * 1000 + 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void vidaFueraRefugio(Humano hu) {
        int eleccion = (int) (Math.random() * 4);
        System.out.println("La elección del tunel" + eleccion);
        Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha decidido salir de la Zona Común por el tunel " + eleccion);
        listaHumanos.remove(hu);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ventana.actualizarHumanosZonaComun();
            }
        });
        arrayTunel[eleccion].irExterior(hu);
        if (!hu.isMuerto()) {
            arrayTunel[eleccion].venirDelExterior(hu);
        }
    }

    public LinkedBlockingQueue<Humano> getListaHumanosZonaComun() {
        return listaHumanos;
    }
}
