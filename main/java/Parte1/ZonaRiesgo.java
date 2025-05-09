package Parte1;

import Visuals.ApocalipsisZombi.VentanaServ;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.SwingUtilities;

public class ZonaRiesgo {

    private LinkedBlockingQueue<Zombie> listaZombies = new LinkedBlockingQueue<>();
    private ArrayList<Humano> listaHumanos = new ArrayList<>();
    private int id;
    private VentanaServ ventana;
    private Logger logger;

    public ZonaRiesgo(int pid, Logger log, VentanaServ vent) {
        id = pid;
        ventana = vent;
        logger = log;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Humano> getListaHumanos() {
        return listaHumanos;
    }

    public LinkedBlockingQueue<Zombie> getListaZombies() {
        return listaZombies;
    }

    public void entrarHumano(Humano hu) {
        listaHumanos.add(hu);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ventana.actualizarHumanosZP(id);
            }
        });
    }

    public void salirHumano(Humano hu) {
        listaHumanos.remove(hu);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ventana.actualizarHumanosZP(id);
            }
        });
    }

    public void entrarZombie(Zombie zo) {
        listaZombies.add(zo);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ventana.actualizarZombiesZP(id);
            }
        });
    }

    public void salirZombie(Zombie zo) {
        listaZombies.remove(zo);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ventana.actualizarZombiesZP(id);
            }
        });
    }

    public synchronized Humano devolverHumanoAleatorio(ZonaRiesgo zonaActual) {
        if (zonaActual.getListaHumanos().isEmpty()) {
            return null;
        } else {
            int numHumanos = zonaActual.getListaHumanos().size();
            int rand2 = (int) (numHumanos * Math.random());
            Humano huAtacado = (Humano) zonaActual.getListaHumanos().get(rand2);
            zonaActual.getListaHumanos().remove(huAtacado);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ventana.actualizarHumanosZP(id);
                }
            });
            return huAtacado;
        }
    }

}
