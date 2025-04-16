package org.example;

import static java.lang.Thread.sleep;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String[] idHumanos = new String[5];
        Comedor comedor = new Comedor();

        ZonaRiesgo[] arrayZonaRiesgo = new ZonaRiesgo[4];
        for(int i = 0; i<4;i++){
            arrayZonaRiesgo[i] = new ZonaRiesgo();
        }

        Tunel[] arrayTunel = new Tunel[4];
        for (int i = 0;i<4;i++){
            arrayTunel[i] = new Tunel(arrayZonaRiesgo[i]);
        }

        ZonaDescanso zonaDescanso = new ZonaDescanso(arrayTunel);

        //Creacción de los humanos
            idHumanos[0]="H";
            for (int t = 0; t < 10; t++) {
                idHumanos[1] = Integer.toString(t);
                for (int k = 0; k < 10; k++) {
                    idHumanos[2] = Integer.toString(k);
                    for (int i = 0; i < 10; i++) {
                        idHumanos[3] = Integer.toString(i);
                        for (int j = 0; j < 10; j++) {
                            idHumanos[4] = Integer.toString(j);
                            System.out.println(arrayZonaRiesgo[3].getListaHumanos().toString());
                            System.out.println(idHumanos[0]+idHumanos[1]+idHumanos[2]+idHumanos[3]+idHumanos[4]);
                            new Humano(idHumanos, comedor, arrayTunel, zonaDescanso).start();
                            try {
                                sleep(2000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
    while (true){
        for(int i=0;i<4;i++){
        arrayZonaRiesgo[i].getListaHumanos().toString();
    }
    }
    }
}