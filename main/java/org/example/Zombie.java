package org.example;

import java.io.Serializable;

public class Zombie extends Thread implements Serializable {

    private int contadorMuertes = 0;
    private String[] id = new String[6];
    private ZonaRiesgo[] arrayZonaRiesgo;
    private Pausa pausa;
    private Logger logger;

    public Zombie(String[] identificador, ZonaRiesgo[] zr, Pausa pa, Logger log) {
        id = identificador;
        arrayZonaRiesgo = zr;
        pausa = pa;
        logger = log;
    }

    public String getIdZombie() {
        String str = "";
        for (int i = 0; i < 6; i++) {
            str += id[i];
        }
        return str;
    }

    public int getContadorMuertes() {
        return contadorMuertes;
    }

    public void run() {
        try {
            while (true) {
                pausa.comprobarPausa(); // Comprueba la pausa
                int rand1 = (int) (4 * Math.random()); // Elige aleatoriamente la zona de riesgo
                logger.escribir("El zombie " + getIdZombie() + " entra a la zona de riesgo " + rand1);
                ZonaRiesgo zonaActual = arrayZonaRiesgo[rand1];

                pausa.comprobarPausa();
                zonaActual.entrarZombie(this); // Se añade a la lista de la zona de riesgo
                pausa.comprobarPausa();

                // Coge a un humano aleatoriamente
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
                sleep(2000 + (int) (1000 * Math.random())); // Se espera en la zona de riesgo

                pausa.comprobarPausa();
                logger.escribir("El zombie " + getIdZombie() + " sale a la zona de riesgo " + rand1);
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
                String[] idZ = new String[]{"Z", idH[1], idH[2], idH[3], idH[4], idH[5]};

                new Zombie(idZ, arrayZonaRiesgo, pausa, logger).start();
                pausa.comprobarPausa();
                contadorMuertes++;
            }
            else {
                pausa.comprobarPausa();
                logger.escribir("Humano " + hu.getIdHumanoStr() + " ha sido marcado por el zombie " + this.getIdZombie());
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
        logger.escribir("Humano " + hu.getIdHumanoStr() + " ha muerto a manos del zombie " + this.getIdZombie() + " en la zona " + zonaActual.getId() + "y ahora tambien es un zombie.");
        hu.morir();
    }

}
