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
    LinkedBlockingQueue<Humano> listaComedorRecibida;
    LinkedBlockingQueue<Humano> listaZonacomunRecibida;
    LinkedBlockingQueue<Humano> listaZonaDescansoRecibida;

    ArrayList<Humano> listaZonaRiesgoRecibida1;
    ArrayList<Humano> listaZonaRiesgoRecibida2;
    ArrayList<Humano> listaZonaRiesgoRecibida3;
    ArrayList<Humano> listaZonaRiesgoRecibida4;

    LinkedBlockingQueue<Humano> listaPasarTunelRecibida1;
    LinkedBlockingQueue<Humano> listaPasarTunelRecibida2;
    LinkedBlockingQueue<Humano> listaPasarTunelRecibida3;
    LinkedBlockingQueue<Humano> listaPasarTunelRecibida4;

    LinkedBlockingQueue<Humano> listaPasandoTunelRecibida1;
    LinkedBlockingQueue<Humano> listaPasandoTunelRecibida2;
    LinkedBlockingQueue<Humano> listaPasandoTunelRecibida3;
    LinkedBlockingQueue<Humano> listaPasandoTunelRecibida4;

    LinkedBlockingQueue<Humano> listaRegresandoTunelRecibida1;
    LinkedBlockingQueue<Humano> listaRegresandoTunelRecibida2;
    LinkedBlockingQueue<Humano> listaRegresandoTunelRecibida3;
    LinkedBlockingQueue<Humano> listaRegresandoTunelRecibida4;

    LinkedBlockingQueue<Zombie> listaZombieZonaRiesgo1;
    LinkedBlockingQueue<Zombie> listaZombieZonaRiesgo2;
    LinkedBlockingQueue<Zombie> listaZombieZonaRiesgo3;
    LinkedBlockingQueue<Zombie> listaZombieZonaRiesgo4;

    public Cliente(VentanaCli vent) {
        ventana = vent;
    }



    public void conectarServ() {
        Socket cliente;
        try {
            System.out.println("Hola");
            cliente = new Socket(InetAddress.getLocalHost(), 5002); // Conectar al servidor
            System.out.println("Adios");


            ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());

            System.out.println("Adios");
            ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());

            System.out.println("Adios");
            while (true) {
                System.out.println(parar);
                oos.writeObject(parar);
                System.out.println(parar);
                oos.flush();
                oos.reset();

                //Listas de la zona segura
                listaComedorRecibida = (LinkedBlockingQueue<Humano>) ois.readObject();
                System.out.println(listaComedorRecibida.toString());
                listaZonacomunRecibida = (LinkedBlockingQueue<Humano>) ois.readObject();
                listaZonaDescansoRecibida = (LinkedBlockingQueue<Humano>) ois.readObject();

                //Listas de humanos en zonas de riesgo
                listaZonaRiesgoRecibida1 = (ArrayList<Humano>) ois.readObject();
                if(listaZonaRiesgoRecibida1 != null) {
                    System.out.println(listaZonaRiesgoRecibida1.toString());
                }
                listaZonaRiesgoRecibida2 = (ArrayList<Humano>) ois.readObject();
                listaZonaRiesgoRecibida3 = (ArrayList<Humano>) ois.readObject();
                listaZonaRiesgoRecibida4 = (ArrayList<Humano>) ois.readObject();

                //Listas de humanos para pasar por los túneles
                listaPasarTunelRecibida1 = (LinkedBlockingQueue<Humano>) ois.readObject();
                listaPasarTunelRecibida2 = (LinkedBlockingQueue<Humano>) ois.readObject();
                listaPasarTunelRecibida3 = (LinkedBlockingQueue<Humano>) ois.readObject();
                listaPasarTunelRecibida4 = (LinkedBlockingQueue<Humano>) ois.readObject();

                //Listas de humanos pasando por los tuneles
                listaPasandoTunelRecibida1 = (LinkedBlockingQueue<Humano>) ois.readObject();
                listaPasandoTunelRecibida2 = (LinkedBlockingQueue<Humano>) ois.readObject();
                listaPasandoTunelRecibida3 = (LinkedBlockingQueue<Humano>) ois.readObject();
                listaPasandoTunelRecibida4 = (LinkedBlockingQueue<Humano>) ois.readObject();

                //Listas de humanos regresando por los tuneles
                listaRegresandoTunelRecibida1 = (LinkedBlockingQueue<Humano>) ois.readObject();
                listaRegresandoTunelRecibida2 = (LinkedBlockingQueue<Humano>) ois.readObject();
                listaRegresandoTunelRecibida3 = (LinkedBlockingQueue<Humano>) ois.readObject();
                listaRegresandoTunelRecibida4 = (LinkedBlockingQueue<Humano>) ois.readObject();

                //Listas de Zombies en la zona de Riesgo
                listaZombieZonaRiesgo1 = (LinkedBlockingQueue<Zombie>) ois.readObject();
                listaZombieZonaRiesgo2 = (LinkedBlockingQueue<Zombie>) ois.readObject();
                listaZombieZonaRiesgo3 = (LinkedBlockingQueue<Zombie>) ois.readObject();
                listaZombieZonaRiesgo4 = (LinkedBlockingQueue<Zombie>) ois.readObject();

                cambiarEstadoParar();
                sleep(15000);
            }
        } catch (NullPointerException | IOException | ClassNotFoundException | InterruptedException e) {
            System.out.println("Error");
            throw new RuntimeException(e);
        }
    }

    public boolean isPausado() {
        return !seguir;
    }

    public void cambiarEstadoParar() {
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
    }
}
