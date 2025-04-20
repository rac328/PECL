package mainAnterior;

import org.example.*;
import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) {
        String[] idHumanos = new String[5];
        String[] idZombie = new String[5];
        Comedor comedor = new Comedor();

        ZonaRiesgo[] arrayZonaRiesgo = new ZonaRiesgo[4];
        for (int i = 0; i < 4; i++) {
            arrayZonaRiesgo[i] = new ZonaRiesgo();
        }

        Tunel[] arrayTunel = new Tunel[4];
        for (int i = 0; i < 4; i++) {
            arrayTunel[i] = new Tunel(i, arrayZonaRiesgo[i]);
        }

        ZonaComun zonaComun = new ZonaComun(arrayTunel);
        ZonaDescanso zonaDescanso = new ZonaDescanso(new Ventana());

        //creacion zombie
        idZombie[0] = "Z";
        for (int i = 1; i <= 4; i++) {
            idZombie[i] = "0";
        }
        new Zombie(idZombie, 0, arrayZonaRiesgo).start();

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
                        
                        System.out.println(idHumanos[0]+idHumanos[1]+idHumanos[2]+idHumanos[3]+idHumanos[4]);
                        new Humano(idHumanos.clone(), comedor, arrayTunel, zonaComun, zonaDescanso).start();
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
}
