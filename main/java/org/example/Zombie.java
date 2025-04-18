package org.example;

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
                if (!zonaActual.getListaHumanos().isEmpty()) {
                    int numHumanos = zonaActual.getListaHumanos().size();
                    int rand2 = (int) (numHumanos * Math.random());
                    Humano huAtacado = (Humano) zonaActual.getListaHumanos().get(rand2);
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
            int prob = (int) (3 * Math.random());
            if (prob == 0) {
                String[] idH = hu.getIdHumano();
                matarHumano(hu, zonaActual);
                String[] idZ = new String[]{"Z", idH[1], idH[2], idH[3], idH[4]};
                
                new Zombie(idZ, 0, arrayZonaRiesgo).start();
                contadorMuertes++;
            } else {
                System.out.println("Humano " + hu.getIdHumanoStr() + " ha sido marcado por el zombie " + this.getIdZombie());
                hu.setMarcado(true);
                hu.setVuelveMarcado(true);
                hu.setComida(false);
                zonaActual.salirHumano(hu);
                for (int i = 0; i < hu.getArrayTunel().length; i++) {
                    Tunel t = hu.getArrayTunel()[i];
                    if (t != null) {
                        t.venirDelExterior(hu);
                        break;
                    }
                }

            }
            sleep(500 + (int) (1000 * Math.random()));
        } catch (InterruptedException ie) {
            System.out.println("Error");
        }
    }

    public void matarHumano(Humano hu, ZonaRiesgo zonaActual) {
        System.out.println("Humano " + hu.getIdHumanoStr() + " ha muerto a manos del zombie " + this.getIdZombie());
        zonaActual.salirHumano(hu);
        hu.morir();
        hu.interrupt();
    }

}
