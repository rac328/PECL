package org.example;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.Console.*;
import java.util.concurrent.LinkedBlockingQueue;
import Visuals.ApocalipsisZombi.*;

import static java.lang.Thread.sleep;

public class Cliente {

    private VentanaCli ventana;
    private boolean seguir = true;
    private Boolean parar = false;

    public Cliente(VentanaCli vent) {
        ventana = vent;
    }

    public void conectarServ() {
        Socket cliente;
        try {
            cliente = new Socket(InetAddress.getLocalHost(), 5002); // Conectar al servidor
            ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
            while (true) {
                if (parar){
                    oos.writeObject(parar);
                    oos.flush();
                    oos.reset();
                    sleep(8000);
                    cambiarEstadoParar();
                }
                else{
                    LinkedBlockingQueue<Humano> listaComedorRecibida = (LinkedBlockingQueue<Humano>) ois.readObject();
                    int rand = (int) (Math.random()*3+0);
                    System.out.println(rand);
                    if(rand == 0){
                        pausarServidor();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            System.out.println("Error");
            throw new RuntimeException(e);
        }
    }

    public boolean isPausado() {
        return !seguir;
    }

    public synchronized void cambiarEstadoParar() {
        parar = !parar;
    }

    public void pausarServidor() {
       parar = true;
    }

    public void reanudarServidor() {
       parar = true;
    }

    public static void main(String args[]) {
        VentanaCli ventana = new VentanaCli();
        Cliente cliente = new Cliente(ventana);
        new Thread(() -> cliente.conectarServ()).start();
        /* VentanaServ ventana = new VentanaServ();

        //crear cliente con ventana
        Cliente cliente = new Cliente(ventana);


        new Thread(() -> cliente.conectarServ()).start();

        //mostrar ventana
        ventana.setVisible(true);*/
        //crear ventana
    }
}
