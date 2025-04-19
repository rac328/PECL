package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ZonaRiesgo {

    private LinkedBlockingQueue<Zombie> listaZombies = new LinkedBlockingQueue<>();
    private ArrayList<Humano> listaHumanos = new ArrayList<>();

    public ZonaRiesgo(){

    }

    public List<Humano> getListaHumanos() {
        return listaHumanos;
    }

    public LinkedBlockingQueue<Zombie> getListaZombies() {
        return listaZombies;
    }

    public synchronized void entrarHumano(Humano hu){
        notifyAll();
        listaHumanos.add(hu);
    }

    public void salirHumano(Humano hu){
        listaHumanos.remove(hu);
    }

    public void entrarZombie(Zombie zo){
        listaZombies.add(zo);
    }

    public void salirZombie(Zombie zo){
        listaZombies.remove(zo);
    }

    public synchronized Humano devolverHumanoAleatorio(ZonaRiesgo zonaActual) {
        if (zonaActual.getListaHumanos().isEmpty()){
            return null;
        }else{
            int numHumanos = zonaActual.getListaHumanos().size();
            int rand2 = (int) (numHumanos * Math.random());
            Humano huAtacado = (Humano) zonaActual.getListaHumanos().get(rand2);
            zonaActual.getListaHumanos().remove(huAtacado);
            return huAtacado;
        }
    }
}
