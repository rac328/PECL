package org.example;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ZonaComun {
    private Tunel[] arrayTunel;
    private LinkedBlockingQueue<Humano> listaHumanos = new LinkedBlockingQueue<>();

    public ZonaComun(Tunel[] at){
        arrayTunel = at;

    }

    public void entrarZonaComun(Humano hu){
        listaHumanos.add(hu);
        Logger.escribir("Humano "+hu.getIdHumanoStr()+" ha entrado a la Zona Común.");
    }

    public void prepararse(Humano hu){
        try {
            Logger.escribir("Humano "+hu.getIdHumanoStr()+" se esta preparando.");
            sleep((long) (Math.random()*1000+1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void vidaFueraRefugio(Humano hu){
        int eleccion = (int) (Math.random()*4);
        System.out.println("La elección del tunel"+ eleccion);
        Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha decidido salir de la Zona Común por el tunel " + eleccion);
        listaHumanos.remove(hu);
        arrayTunel[eleccion].irExterior(hu);
        if (!hu.isMuerto()){
            arrayTunel[eleccion].venirDelExterior(hu);
        }

    }
}
