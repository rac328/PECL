package org.example;

import static java.lang.Thread.sleep;

public class ZonaDescanso {

    private Tunel[] arrayTunel;

    public ZonaDescanso(Tunel[] at){
        arrayTunel = at;

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
        arrayTunel[eleccion].irExterior(hu);
    }
}
