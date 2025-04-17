package org.example;

import java.util.ArrayList;

public class ZonaRiesgo {

    private ArrayList listaHumanos = new ArrayList<Humano>();
    private ArrayList listaZombies = new ArrayList<Zombie>();

    public ZonaRiesgo(){

    }

    public ArrayList getListaHumanos() {
        return listaHumanos;
    }

    public ArrayList getListaZombies() {
        return listaZombies;
    }

    public void entrarHumano(Humano hu){
        listaHumanos.add(hu);
    }

    public void salirHumano(Humano hu){
        listaHumanos.remove(hu);
        hu.setComida(true);
    }

    public void entrarZombie(Zombie zo){
        listaZombies.add(zo);
    }

    public void salirZombie(Zombie zo){
        listaZombies.remove(zo);
    }

}
