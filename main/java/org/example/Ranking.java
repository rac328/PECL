package org.example;

import java.util.ArrayList;
import java.util.Comparator;

public class Ranking {
    private ZonaRiesgo[] arrayZonaRiesgo;

    public Ranking(ZonaRiesgo[] zr){
        arrayZonaRiesgo = zr;
    }

    Comparator<Zombie> porMuertes = (z1, z2) -> z2.getContadorMuertes() - z1.getContadorMuertes();

    public ArrayList<String> hacerRanking(){
        ArrayList<Zombie> listaCompleta = new ArrayList<>(arrayZonaRiesgo[0].getListaZombies());
        listaCompleta.addAll(arrayZonaRiesgo[1].getListaZombies());
        listaCompleta.addAll(arrayZonaRiesgo[2].getListaZombies());
        listaCompleta.addAll(arrayZonaRiesgo[3].getListaZombies());
        listaCompleta.sort(porMuertes);
        ArrayList<String> listaMejores = new ArrayList<>(3);
        if(listaCompleta.size()==0){
            listaMejores.add("No hay muertos");
            listaMejores.add("");
            listaMejores.add("");
            return listaMejores;
        }
        else if (listaCompleta.size()==1){
            listaMejores.add(0, listaCompleta.get(0).getIdZombie() + " - " + listaCompleta.get(0).getContadorMuertes() + " muertes");
            listaMejores.add("");
            listaMejores.add("");
            return listaMejores;
        }else if(listaCompleta.size()==2){
            listaMejores.add(0, listaCompleta.get(0).getIdZombie() + " - " + listaCompleta.get(0).getContadorMuertes() + " muertes");
            listaMejores.add(1, listaCompleta.get(1).getIdZombie() + " - " + listaCompleta.get(1).getContadorMuertes() + " muertes");
            listaMejores.add("");
            return listaMejores;
        }else{
            listaMejores.add(0, listaCompleta.get(0).getIdZombie() + " - " + listaCompleta.get(0).getContadorMuertes() + " muertes");
            listaMejores.add(1, listaCompleta.get(1).getIdZombie() + " - " + listaCompleta.get(1).getContadorMuertes() + " muertes");
            listaMejores.add(2, listaCompleta.get(2).getIdZombie() + " - " + listaCompleta.get(2).getContadorMuertes() + " muertes");
            return listaMejores;

        }
    }
}
