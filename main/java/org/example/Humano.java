package org.example;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Humano extends Thread {
    private String[] id = new String[5];
    private boolean marcado = false;
    private boolean llevaComida = false;
    private Comedor comedor;
    private Tunel[] arrayTunel;
    private ZonaDescanso zonaDescanso;

    public Humano(String[] identificador, Comedor come, Tunel[] at, ZonaDescanso zd){
        id = identificador;
        comedor = come;
        arrayTunel = at;
        zonaDescanso = zd;
    }

    public String[] getIdHumano(){
        return id;
    }

    public String getIdHumanoStr() {
        String str = "";
        for (int i = 0; i<5; i++){
            str += id[i];
        }
        return str;
    }

    public void setIdHumano(String[] id) {
        this.id = id;
    }

    public boolean llevaComida(){
           return llevaComida;
    }

    public void setComida(boolean bool){
        llevaComida = bool;
    }

    public boolean getMarcado(){
        return marcado;
    }
    public void setMarcado(boolean bool){
        marcado = bool;
    }
    
    public void run(){
        try {
            while (true) {
                zonaDescanso.entrarZonaDescanso(this);
                zonaDescanso.prepararse();
                zonaDescanso.vidaFueraRefugio(this);
                sleep(1);
                comedor.depositarComida(this);
                //zonaDescanso.descansar();
                comedor.comer();
                if (marcado){
                    //zonaDescanso.descansar();
                }

            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
