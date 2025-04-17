package org.example;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class ZonaDescanso {

    private Tunel[] arrayTunel;
    private ArrayList listaHumanos = new ArrayList<Humano>();

    public ZonaDescanso(Tunel[] at){
        arrayTunel = at;

    }

    public void entrarZonaDescanso(Humano hu){
        listaHumanos.add(hu);
    }

    public void prepararse(){
        try {
            sleep((long) (Math.random()*1000+2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void vidaFueraRefugio(Humano hu){
        int eleccion = (int) (Math.random()*0+3);
        listaHumanos.remove(hu);
        arrayTunel[eleccion].irExterior(hu);
        arrayTunel[eleccion].venirDelExterior(hu);
    }
}
