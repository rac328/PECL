package org.example;

import java.util.concurrent.Semaphore;

public class Zombie extends Thread {

    private int contadorMuertes;
    private String[] id = new String[5];
    private ZonaRiesgo[] arrayZonaRiesgo;

    public Zombie(String[] identificador, int contadorM, ZonaRiesgo[] zr) {
        id = identificador;
        contadorMuertes = contadorM;
        arrayZonaRiesgo = zr;
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
                int rand1 = (int) (4 * Math.random());
                ZonaRiesgo zonaActual = arrayZonaRiesgo[rand1];
                zonaActual.entrarZombie(this);
                Humano huAtacado = zonaActual.devolverHumanoAleatorio(zonaActual);
                if(!(huAtacado == null)){
                    huAtacado.setEsperandoAtaque(true);
                    huAtacado.interrupt();
                    atacar(huAtacado, zonaActual);
                }
                sleep(2000 + (int) (1000 * Math.random()));
                zonaActual.salirZombie(this);
            }
        } catch (InterruptedException ie) {
            System.out.println("Error");
        }
    }

    public void atacar(Humano hu, ZonaRiesgo zonaActual) {
        try {
            sleep(500 + (int) (1000 * Math.random()));
            int prob = (int) (3 * Math.random());
            if (prob == 0) {
                String[] idH = hu.getIdHumano();
                matarHumano(hu, zonaActual);
                String[] idZ = new String[]{"Z", idH[1], idH[2], idH[3], idH[4]};

                new Zombie(idZ, 0, arrayZonaRiesgo).start();
                contadorMuertes++;
            }
            else {
                Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha sido marcado por el zombie " + this.getIdZombie());
                hu.setMarcado(true);
                //El zombie realiza un await en la cyclic barrier para que el humano y el zombie sigan con la ejecución
                hu.Defensa();
                //El humano ya no está esperando para el ataque, lo marcamos a false
                hu.setEsperandoAtaque(false);
            }
        } catch (InterruptedException ie) {
            System.out.println("Error");
        }
    }

    public void matarHumano(Humano hu, ZonaRiesgo zonaActual) {
        Logger.escribir("Humano " + hu.getIdHumanoStr() + " ha muerto a manos del zombie " + this.getIdZombie() + " y ahora tambien es un zombie.");
        hu.morir();
    }

}
