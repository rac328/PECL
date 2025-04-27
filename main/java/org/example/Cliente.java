package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import Visuals.ApocalipsisZombi.*;

public class Cliente {

    private Ventana ventana;
    private boolean seguir = true;

    public Cliente(Ventana vent) {
        ventana = vent;
    }

    public void conectarServ() {
        Socket cliente;
        try {
            cliente = new Socket(InetAddress.getLocalHost(), 5002); // Conectar al servidor
            ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());

            while (true) {
                synchronized (this) {
                    if (!seguir) {
                        while (!seguir) {
                        wait();
                    }
                    }
                }
                LinkedBlockingQueue<Humano> listaComedorRecibida = (LinkedBlockingQueue<Humano>) ois.readObject();
                ventana.actualizarHumanosComedor(listaComedorRecibida);
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            System.out.println("Error");
        }
    }

    public boolean isPausado() {
        return !seguir;
    }

    public synchronized void cambiarEstadoSeguir() {
        seguir = !seguir;
        if (seguir) {
            notify();
        }
    }

    public void pausarServidor() {
        try {
            Socket cliente = new Socket(InetAddress.getLocalHost(), 5002); //conectar
            DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());

            // enviar mensaje al servidor para pausar
            salida.writeUTF("PAUSAR");
            cliente.close();
        } catch (IOException e) {
            System.out.println("Error al pausar el servidor: " + e.getMessage());
        }
    }

    public void reanudarServidor() {
        try {
            Socket cliente = new Socket(InetAddress.getLocalHost(), 5002); //conectar
            DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());

            // enviar mensaje al servidor para seguir
            salida.writeUTF("REANUDAR");
            cliente.close();
        } catch (IOException e) {
            System.out.println("Error al reanudar el servidor: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        //crear ventana
        Ventana ventana = new Ventana();

        //crear cliente con ventana
        Cliente cliente = new Cliente(ventana);

        //crear cliente como hilo para no bloquear interfaz
        new Thread(() -> cliente.conectarServ()).start();

        //mostrar ventana
        ventana.setVisible(true);
    }
}
