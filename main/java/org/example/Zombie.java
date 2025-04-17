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

    public String[] getIdZombie(){
        return id;
    }
    
    public void run() {
        try {
            while (true) {
                int rand = (int) (4 * Math.random());
                arrayZonaRiesgo[rand].entrarZombie(this);
                if (!arrayZonaRiesgo[rand].getListaHumanos().isEmpty()) {
                    int numHumanos = arrayZonaRiesgo[rand].getListaHumanos().size();
                    int rand1 = (int) (numHumanos * Math.random());
                    Humano huAtacado = (Humano) arrayZonaRiesgo[rand].getListaHumanos().get(rand1);
                    atacar(huAtacado);
                }
                sleep(2000+(int)(1000*Math.random()));
            }
        }catch(InterruptedException ie){
            System.out.println("Error");
        }
    }

    public void atacar(Humano hu) {
        try {
            int prob = (int) (3 * Math.random());
            if (prob == 0) {
                String[] id = hu.getIdHumano();
                matarHumano(hu);
                new Zombie(id, 0, arrayZonaRiesgo).start();
                contadorMuertes++;
            } else {
                hu.setMarcado(true);
            }
            sleep(500 + (int) (1000 * Math.random()));
        }catch(InterruptedException ie){
            System.out.println("Error");
        }
    }
    
    public void matarHumano(Humano hu){
        System.out.println("Humano "+hu.getIdHumano()+" ha muerto a manos del zombie "+this.getIdZombie());
        hu.interrupt();
    }
    

}
