package org.example;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Servidor {

        public static void main(String[] args) {
            ServerSocket servidor;
            Socket conexion;
            int num=0;
            boolean seguir = true;
            Arranque arranqueServidor = new Arranque();

            arranqueServidor.crearSimulacionSegundoPlano();

            try {
                System.out.println("Servidor Arrancado....");
                arranqueServidor.pausarEjecucion();
                servidor = new ServerSocket(5002); //Creamos un ServerSocket en el Puerto 5000
                conexion = servidor.accept(); //Esperamos una conexión
                ObjectOutputStream oos = new ObjectOutputStream(conexion.getOutputStream());
                while (seguir) {
                    LinkedBlockingQueue<Humano> listaComedor = arranqueServidor.getComedor().getListaHumanosComedor();
                    System.out.println(listaComedor.toString());
                    oos.writeObject(listaComedor);
                    oos.flush();
                    oos.reset();
                }
                conexion.close();
            } catch (IOException e) {
            }
        }
    }
