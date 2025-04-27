package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Cliente {
    public static void main(String args[]) {
        Socket cliente;
        DataInputStream entrada;
        DataOutputStream salida;
        String mensaje, respuesta;
        boolean seguir = true;

        try {
            cliente = new Socket(InetAddress.getLocalHost(), 5002); //Creamos el socket para conectarnos al puerto 5000 del servidor
            ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
            System.out.println(InetAddress.getLocalHost().toString());
            while (seguir) {
                LinkedBlockingQueue<Humano> listaComedorRecibida = (LinkedBlockingQueue<Humano>) ois.readObject();
                System.out.println(listaComedorRecibida.toString());
            }
            cliente.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
