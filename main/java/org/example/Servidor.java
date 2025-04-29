package org.example;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import Visuals.ApocalipsisZombi.*;

public class Servidor {

    private boolean seguir = true;
    private LinkedBlockingQueue<Humano> listaComedor = new LinkedBlockingQueue<>();
    private ServerSocket servidor;
    private Arranque arranque = new Arranque();
    private VentanaServ ventana;
    
    public Servidor(){
    }

    public Arranque getArranque(){
        return arranque;
    }
    public void iniciarServ() {
        try {
            servidor = new ServerSocket(5002);
            System.out.println("Servidor Arrancando....");
            arranque.crearSimulacionSegundoPlano();
            System.out.println("Servidor Arrancando....1");
            /*while (true) {
                Socket cliente = servidor.accept();
                new Thread(() -> conexionCliente(cliente)).start();
            }*/
        } catch (IOException e) {
            System.out.println("Error al iniciar server");
        }
    }

    public void conexionCliente(Socket cli) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(cli.getOutputStream());
            DataInputStream dis = new DataInputStream(cli.getInputStream());
            while (true) {

                synchronized (this) {
                    while (!seguir) {
                        wait();
                    }
                }
                LinkedBlockingQueue<Humano> listaComedor = arranque.getComedor().getListaHumanosComedor();
                oos.writeObject(listaComedor);
                oos.flush();
                oos.reset();

                if (dis.available() > 0) {
                    String msg = dis.readUTF();
                    if (msg.equals("PAUSAR")) {
                        seguir = false;
                        arranque.pausarEjecucion();
                        System.out.println("Servidor pausado");
                    } else if (msg.equals("REANUDAR")) {
                        seguir = true;
                        arranque.reanudarEjecucion();
                        synchronized (this){
                            notify();
                        }
                        System.out.println("Servidor reanudado");
                    }
                }

            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error en la conexion con el cliente");
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.iniciarServ();
    }
}
