package org.example;

import static java.lang.Thread.sleep;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.SwingUtilities;
import Visuals.ApocalipsisZombi.*;

public class ZonaComun{

    private Tunel[] arrayTunel;
    private LinkedBlockingQueue<Humano> listaHumanos = new LinkedBlockingQueue<>();
    private VentanaServ ventana;
    private Logger logger;

    public ZonaComun(Tunel[] at, Logger log, VentanaServ vServ) {
        arrayTunel = at;
        ventana = vServ;
        logger = log;
    }

    public void entrarZonaComun(Humano hu) {
        listaHumanos.add(hu);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ventana.actualizarHumanosZonaComun();
            }
        });
        logger.escribir("Humano " + hu.getIdHumanoStr() + " ha entrado a la Zona Común.");
    }

    public void prepararse(Humano hu) {
        try {
            logger.escribir("Humano " + hu.getIdHumanoStr() + " se esta preparando.");
            sleep((long) (Math.random() * 1000 + 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void vidaFueraRefugio(Humano hu) {
        int eleccion = (int) (Math.random() * 4);
        System.out.println("La elección del tunel" + eleccion);
        logger.escribir("Humano " + hu.getIdHumanoStr() + " ha decidido salir de la Zona Común por el tunel " + eleccion);
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
