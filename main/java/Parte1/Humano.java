package Parte1;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Humano extends Thread {

    private String[] id = new String[5];
    private boolean marcado = false;
    private Comedor comedor;
    private Tunel[] arrayTunel;
    private ZonaDescanso zonaDescanso;
    private ZonaComun zonaComun;
    private boolean muerto = false;
    private CyclicBarrier esperarAtaque = new CyclicBarrier(2);
    private AtomicBoolean esperandoAtaque = new AtomicBoolean(false);
    private Pausa pausa;

    public Humano(String[] identificador, Comedor come, Tunel[] at, ZonaComun zc, ZonaDescanso zd, Pausa pa) {
        id = identificador;
        comedor = come;
        arrayTunel = at;
        zonaComun = zc;
        zonaDescanso = zd;
        pausa = pa;
    }

    public boolean getEsperandoAtaque() {
        return esperandoAtaque.get();
    }

    public void setEsperandoAtaque(boolean esperandoAtaque) {
        this.esperandoAtaque.set(esperandoAtaque);
    }

    public String[] getIdHumano() {
        return id;
    }

    public String getIdHumanoStr() {
        String str = "";
        for (int i = 0; i < 6; i++) {
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

    public boolean getMarcado() {
        return marcado;
    }

    public void setMarcado(boolean bool) {
        marcado = bool;
    }

    public boolean isMuerto(){
        return muerto;
    }
    
    public void morir(){
        muerto = true;
    }

    public void defensa(){
        try {
            esperarAtaque.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    public void comprobarPausaHumano(){
        pausa.comprobarPausa();
    }

    public void run() {

        while (!muerto) {
            pausa.comprobarPausa();
            zonaComun.entrarZonaComun(this);
            pausa.comprobarPausa();
            zonaComun.prepararse(this);
            pausa.comprobarPausa();
            zonaComun.vidaFueraRefugio(this);
            if(!muerto){
                pausa.comprobarPausa();
                comedor.depositarComida(this);
                pausa.comprobarPausa();
                zonaDescanso.descansarVuelta(this);
                pausa.comprobarPausa();
                comedor.comer(this);
                pausa.comprobarPausa();
                if (marcado) {
                    zonaDescanso.descansarMarcado(this);
                    pausa.comprobarPausa();
                }
            }
        }
        System.out.println("Terminó el humano" + getIdHumanoStr());
    }
}
