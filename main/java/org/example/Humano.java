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
    private ZonaComun zonaComun;
    private boolean vuelveMarcado = false;
    private boolean muerto = false;

    public Humano(String[] identificador, Comedor come, Tunel[] at, ZonaComun zc, ZonaDescanso zd) {
        id = identificador;
        comedor = come;
        arrayTunel = at;
        zonaComun = zc;
        zonaDescanso = zd;
    }

    public String[] getIdHumano() {
        return id;
    }

    public String getIdHumanoStr() {
        String str = "";
        for (int i = 0; i < 5; i++) {
            str += id[i];
        }
        return str;
    }

    public Tunel[] getArrayTunel() {
        return arrayTunel;
    }

    public void setIdHumano(String[] id) {
        this.id = id;
    }

    public boolean llevaComida() {
        return llevaComida;
    }

    public void setComida(boolean bool) {
        llevaComida = bool;
    }

    public boolean getMarcado() {
        return marcado;
    }

    public void setMarcado(boolean bool) {
        marcado = bool;
    }

    public void setVuelveMarcado(boolean bool) {
        vuelveMarcado = bool;
    }

    public boolean getVuelveMarcado() {
        return vuelveMarcado;
    }

    public boolean isMuerto(){
        return muerto;
    }
    
    public void morir(){
        muerto = true;
        interrupt();
    }
    
    public void run() {

        while (!muerto) {
            try {
                if (!this.isInterrupted()) {
                    zonaComun.entrarZonaComun(this);
                    zonaComun.prepararse();
                    zonaComun.vidaFueraRefugio(this);
                    sleep(1);
                    comedor.depositarComida(this);
                    zonaDescanso.descansarVuelta(this);
                    comedor.comer(this);
                    if (marcado) {
                        zonaDescanso.descansarMarcado(this);
                    }
                } else {
                    break;
                }
            } catch (InterruptedException ie) {
                System.out.println(getIdHumanoStr() + " ha muerto.");
                morir();
                break;
            }

        }
    }

}
