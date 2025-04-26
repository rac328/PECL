package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

        public static void main(String[] args) {
            ServerSocket servidor;
            Socket conexion;
            DataOutputStream salida;
            DataInputStream entrada;
           int num=0;

            Arranque arranqueServidor = new Arranque();
            arranqueServidor.crearSimulacionSegundoPlano();

            try {
                servidor = new ServerSocket(5000); //Creamos un ServerSocket en el Puerto 5000
                System.out.println("Servidor Arrancado....");
                while (true) {
                    conexion = servidor.accept(); //Esperamos una conexión
                    num++;
                    entrada = new DataInputStream(conexion.getInputStream()); //Abrimos los canales de E/S
                    salida = new DataOutputStream(conexion.getOutputStream());

                    String mensaje = entrada.readUTF(); //Leemos el mensaje del cliente

                    salida.writeUTF("Buenos días " + mensaje); //Le respondemos
                    entrada.close(); //Cerramos los flujos de entrada y salida
                    salida.close();
                    conexion.close(); //Y cerramos la conexión
                }
            } catch (IOException e) {
            }
        }
    }
