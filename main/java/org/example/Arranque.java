package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class Arranque {
    private int num = 0;
    private ZonaDescanso zonaDescanso;
    private ZonaComun zonaComun;
    private Tunel[] arrayTunel = new Tunel[4];
    private ZonaRiesgo[] arrayZonaRiesgo = new ZonaRiesgo[4];
    private Comedor comedor = new Comedor();
    private Pausa pausa = new Pausa();
    private String[] idHumanos = new String[5];
    private String[] idZombie = new String[5];
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private void iniciarSimulacion() {

        for (int i = 0; i < 4; i++) {
            arrayZonaRiesgo[i] = new ZonaRiesgo();
        }

        for (int i = 0; i < 4; i++) {
            arrayTunel[i] = new Tunel(i, arrayZonaRiesgo[i]);
        }

        zonaComun = new ZonaComun(arrayTunel);
        zonaDescanso = new ZonaDescanso();
        //creacion zombie
        idZombie[0] = "Z";
        for (int i = 1; i <= 4; i++) {
            idZombie[i] = "0";
        }
        new Zombie(idZombie, 0, arrayZonaRiesgo, pausa).start();

        //creacion humanos
        int contadorHumano = 1;

        for (int t = 0; t < 10; t++) {
            for (int k = 0; k < 10; k++) {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {

                        String num = String.format("%04d", contadorHumano);
                        idHumanos[0] = "H";
                        idHumanos[1] = String.valueOf(num.charAt(0));
                        idHumanos[2] = String.valueOf(num.charAt(1));
                        idHumanos[3] = String.valueOf(num.charAt(2));
                        idHumanos[4] = String.valueOf(num.charAt(3));

                        System.out.println(idHumanos[0] + idHumanos[1] + idHumanos[2] + idHumanos[3] + idHumanos[4]);
                        new Humano(idHumanos.clone(), comedor, arrayTunel, zonaComun, zonaDescanso, pausa).start();
                        contadorHumano++;

                        try {
                            sleep(500 + (int) (1500 * Math.random()));
                        } catch (InterruptedException ie) {
                            System.out.println("Error creando humano");
                        }
                    }
                }
            }
        }
    }

    public void crearSimulacionSegundoPlano() {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                // Llamar a la creación de zombies y humanos en segundo plano
                iniciarSimulacion();
            }
        });
    }

    public void pausarEjecucion(){
        pausa.pararEjecucion();
    }
    
    public void reanudarEjecucion(){
        pausa.continuarEjecucion();
    }

    public ZonaDescanso getZonaDescanso() {
        return zonaDescanso;
    }

    public ZonaComun getZonaComun() {
        return zonaComun;
    }

    public Tunel[] getArrayTunel() {
        return arrayTunel;
    }

    public ZonaRiesgo[] getArrayZonaRiesgo() {
        return arrayZonaRiesgo;
    }

    public Comedor getComedor() {
        return comedor;
    }
}
