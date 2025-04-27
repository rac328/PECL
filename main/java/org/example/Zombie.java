package org.example;

import java.io.Serializable;
import java.util.concurrent.Semaphore;

public class Zombie extends Thread implements Serializable {

    private int contadorMuertes;
    private String[] id = new String[5];
    private ZonaRiesgo[] arrayZonaRiesgo;
    private transient Pausa pausa;

    public Zombie(String[] identificador, int contadorM, ZonaRiesgo[] zr, Pausa pa) {
        id = identificador;
        contadorMuertes = contadorM;
        arrayZonaRiesgo = zr;
        pausa = pa;
    }

    public String getIdZombie() {
        String str = "";
        for (int i = 0; i < 5; i++) {
            str += id[i];
        }
        return str;
    }

    public void run() {
        try {
            while (true) {
                pausa.comprobarPausa();
                int rand1 = (int) (4 * Math.random());
                ZonaRiesgo zonaActual = arrayZonaRiesgo[rand1];
                pausa.comprobarPausa();
                zonaActual.entrarZombie(this);
                pausa.comprobarPausa();
                Humano huAtacado = zonaActual.devolverHumanoAleatorio(zonaActual);
                pausa.comprobarPausa();
                if(!(huAtacado == null)){
                    pausa.comprobarPausa();
                    huAtacado.setEsperandoAtaque(true);
                    huAtacado.interrupt();
                    pausa.comprobarPausa();
                    atacar(huAtacado, zonaActual);
                    pausa.comprobarPausa();
                }
                sleep(2000 + (int) (1000 * Math.random()));
                pausa.comprobarPausa();
                zonaActual.salirZombie(this);
                pausa.comprobarPausa();
            }
        } catch (InterruptedException ie) {
            System.out.println("Error");
        }
    }

    public void atacar(Humano hu, ZonaRiesgo zonaActual) {
        try {
            pausa.comprobarPausa();
            sleep(500 + (int) (1000 * Math.random()));
            pausa.comprobarPausa();
            int prob = (int) (3 * Math.random());
            if (prob == 0) {
                pausa.comprobarPausa();
                String[] idH = hu.getIdHumano();
                pausa.comprobarPausa();
                matarHumano(hu, zonaActual);
                pausa.comprobarPausa();
                String[] idZ = new String[]{"Z", idH[1], idH[2], idH[3], idH[4]};

                new Zombie(idZ, 0, arrayZonaRiesgo, pausa).start();
                pausa.comprobarPausa();
                contadorMuertes++;
            }
            else {
                pausa.comprobarPausa();
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha sido marcado por el zombie " + this.getIdZombie());
                hu.setMarcado(true);
                //El zombie realiza un await en la cyclic barrier para que el humano y el zombie sigan con la ejecución
                hu.Defensa();
                //El humano ya no está esperando para el ataque, lo marcamos a false
                pausa.comprobarPausa();
                hu.setEsperandoAtaque(false);
            }
        } catch (InterruptedException ie) {
            System.out.println("Error");
        }
    }

    public void matarHumano(Humano hu, ZonaRiesgo zonaActual) {
        pausa.comprobarPausa();
        Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha muerto a manos del zombie " + this.getIdZombie() + " y ahora tambien es un zombie.");
        hu.morir();
    }

}
