package org.example;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class ZonaDescanso {
    private ArrayList<Humano> listaDescansando = new ArrayList<>();
   
    public ZonaDescanso(){
    
    }
    
    public void descansarVuelta(Humano hu){
        if(!hu.isMuerto()){
            try{
            synchronized(this){
                listaDescansando.add(hu);
                System.out.println("Humano " + hu.getIdHumanoStr() + " está descansando tras volver del exterior.");
            }
            sleep(2000+(int)(2000*Math.random()));
            synchronized(this){
                listaDescansando.remove(hu);
                System.out.println("Humano " + hu.getIdHumanoStr() + " ha terminado de descansar.");
            }
        }catch(InterruptedException ie){
            System.out.println("Error descansar al volver humano " + hu.getIdHumanoStr());
        }
        }else{
            return;
        }
    }
    
    public void descansarMarcado(Humano hu){
        try{
            synchronized(this){
                listaDescansando.add(hu);
                System.out.println("Humano marcado " + hu.getIdHumanoStr() + " está descansando para curarse.");
                hu.setMarcado(false);
            }
            sleep(3000+(int)(2000*Math.random()));
            synchronized(this){
                listaDescansando.remove(hu);
                System.out.println("Humano " + hu.getIdHumanoStr() + " se ha curado y ya no esta marcado.");
            }
        }catch(InterruptedException ie){
            System.out.println("Error descansar humano marcado " + hu.getIdHumanoStr());
        }
    }
}
