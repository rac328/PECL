package org.example;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;
import static java.lang.Thread.sleep;
import javax.swing.SwingUtilities;
import Visuals.ApocalipsisZombi.*;

public class ZonaDescanso{

    private LinkedBlockingQueue<Humano> listaDescansando = new LinkedBlockingQueue<>();
    private VentanaServ ventana;
    private Logger logger;

    public ZonaDescanso(Logger log, VentanaServ vent) {
        ventana = vent;
        logger = log;
    }

    public LinkedBlockingQueue<Humano> getListaHumanosDescansando() {
        return listaDescansando;
    }

    public void descansarVuelta(Humano hu) {
        if (!hu.isMuerto()) {
            try {
                listaDescansando.add(hu); //Se añade a la lista de descansar
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                            ventana.actualizarHumanosDescansando();
                        }
                });
                logger.escribir("Humano " + hu.getIdHumanoStr() + " está descansando tras volver del exterior.");
                sleep(2000 + (int) (2000 * Math.random())); // Tiempo en el que está descansando
                listaDescansando.remove(hu); // Sale de la lista de descansar
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                            ventana.actualizarHumanosDescansando();
                        }
                });
                logger.escribir("Humano " + hu.getIdHumanoStr() + " ha terminado de descansar.");
            } catch (InterruptedException ie) {
                System.out.println("Error descansar al volver humano " + hu.getIdHumanoStr());
            }
        }
    }

    public void descansarMarcado(Humano hu) {
        try {
            listaDescansando.add(hu); // Se añade a la lista de descansar, misma lista para los sanos y marcados

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                        ventana.actualizarHumanosDescansando();
                    }
            });

            logger.escribir("Humano marcado " + hu.getIdHumanoStr() + " está descansando tras ser atacado para curarse.");

            hu.setMarcado(false); // Se le quita la condición de marcado

            sleep(3000 + (int) (2000 * Math.random()));

            listaDescansando.remove(hu); // Sale de la lista de marcados

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                        ventana.actualizarHumanosDescansando();
                    }
            });

            logger.escribir("Humano " + hu.getIdHumanoStr() + " se ha curado y ya no esta marcado.");
        } catch (InterruptedException ie) {
            System.out.println("Error descansar humano marcado " + hu.getIdHumanoStr());
        }
    }
}
