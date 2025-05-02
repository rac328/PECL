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

    private Integer listaComedorRecibida;
    private Integer listaZonacomunRecibida;
    private Integer listaZonaDescansoRecibida;

    private Integer listaZonaRiesgoRecibida1;
    private Integer listaZonaRiesgoRecibida2;
    private Integer listaZonaRiesgoRecibida3;
    private Integer listaZonaRiesgoRecibida4;

    private Integer listaPasarTunelRecibida1;
    private Integer listaPasarTunelRecibida2;
    private Integer listaPasarTunelRecibida3;
    private Integer listaPasarTunelRecibida4;

    private Integer listaPasandoTunelRecibida1;
    private Integer listaPasandoTunelRecibida2;
    private Integer listaPasandoTunelRecibida3;
    private Integer listaPasandoTunelRecibida4;

    private Integer listaRegresandoTunelRecibida1;
    private Integer listaRegresandoTunelRecibida2;
    private Integer listaRegresandoTunelRecibida3;
    private Integer listaRegresandoTunelRecibida4;

    private Integer listaZombieZonaRiesgo1;
    private Integer listaZombieZonaRiesgo2;
    private Integer listaZombieZonaRiesgo3;
    private Integer listaZombieZonaRiesgo4;

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
                System.out.println(parar);
                oos.writeObject(parar);
                System.out.println(parar);
                oos.flush();
                oos.reset();

                //Listas de la zona segura
                listaComedorRecibida = (Integer) ois.readObject();
                System.out.println("Tamaño de la lista de comedor: " + listaComedorRecibida);
                listaZonacomunRecibida = (Integer) ois.readObject();
                listaZonaDescansoRecibida = (Integer) ois.readObject();

                //Listas de humanos en zonas de riesgo
                listaZonaRiesgoRecibida1 = (Integer) ois.readObject();
                listaZonaRiesgoRecibida2 = (Integer) ois.readObject();
                listaZonaRiesgoRecibida3 = (Integer) ois.readObject();
                listaZonaRiesgoRecibida4 = (Integer) ois.readObject();

                //Listas de humanos para pasar por los túneles
                listaPasarTunelRecibida1 = (Integer) ois.readObject();
                listaPasarTunelRecibida2 = (Integer) ois.readObject();
                listaPasarTunelRecibida3 = (Integer) ois.readObject();
                listaPasarTunelRecibida4 = (Integer) ois.readObject();

                //Listas de humanos pasando por los tuneles
                listaPasandoTunelRecibida1 = (Integer) ois.readObject();
                listaPasandoTunelRecibida2 = (Integer) ois.readObject();
                listaPasandoTunelRecibida3 = (Integer) ois.readObject();
                listaPasandoTunelRecibida4 = (Integer) ois.readObject();

                //Listas de humanos regresando por los tuneles
                listaRegresandoTunelRecibida1 = (Integer) ois.readObject();
                listaRegresandoTunelRecibida2 = (Integer) ois.readObject();
                listaRegresandoTunelRecibida3 = (Integer) ois.readObject();
                listaRegresandoTunelRecibida4 = (Integer) ois.readObject();

                //Listas de Zombies en la zona de Riesgo
                listaZombieZonaRiesgo1 = (Integer) ois.readObject();
                listaZombieZonaRiesgo2 = (Integer) ois.readObject();
                listaZombieZonaRiesgo3 = (Integer) ois.readObject();
                listaZombieZonaRiesgo4 = (Integer) ois.readObject();

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
