package org.example;

import static java.lang.Thread.sleep;
import java.util.ArrayList;

public class ZonaComun {
    private Tunel[] arrayTunel;
    private ArrayList listaHumanos = new ArrayList<Humano>();

    public ZonaComun(Tunel[] at){
        arrayTunel = at;

    }

    public void entrarZonaComun(Humano hu){
        listaHumanos.add(hu);
    }

    public void prepararse(){
        try {
            sleep((long) (Math.random()*1000+1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void vidaFueraRefugio(Humano hu){
        int eleccion = (int) (Math.random()*0+3);
        listaHumanos.remove(hu);
        arrayTunel[eleccion].irExterior(hu);
        if (!hu.getVuelveMarcado()&&!hu.isInterrupted()){
            arrayTunel[eleccion].venirDelExterior(hu);
        }

    }
}
