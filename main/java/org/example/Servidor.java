package org.example;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
    private Boolean parar = false;
    
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
            Socket cliente = servidor.accept();
            conexionCliente(cliente);
        }
        catch (IOException e) {
            System.out.println("Error al iniciar server");
        }
    }

    public void conexionCliente(Socket cli) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(cli.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(cli.getInputStream());

            DataInputStream dis = new DataInputStream(cli.getInputStream());

            while (true) {
                System.out.println(parar.booleanValue() + "Valor justo antes de entrar al IF");
                if (parar.booleanValue()){
                    System.out.println("SE PARAAAAAAAAAAAAAAA");
                    arranque.pausarEjecucion();
                    parar = (Boolean) ois.readObject();
                }
                else{
                    arranque.reanudarEjecucion();
                    LinkedBlockingQueue<Humano> listaComedor = arranque.getComedor().getListaHumanosComedor();
                    oos.writeObject(listaComedor);
                    LinkedBlockingQueue<Humano> listaZonaComun = arranque.getZonaComun().getListaHumanosZonaComun();
                    oos.writeObject(listaZonaComun);
                    LinkedBlockingQueue<Humano> listaZonaDescanso = arranque.getZonaDescanso().getListaHumanosDescansando();

                    LinkedBlockingQueue<Humano> listaZonaRiesgo1 = arranque.getArrayZonaRiesgo()[0].getListaHumanos();
                    LinkedBlockingQueue<Humano> listaZonaRiesgo2 = arranque.getArrayZonaRiesgo()[1].getListaHumanos();
                    LinkedBlockingQueue<Humano> listaZonaRiesgo3 = arranque.getArrayZonaRiesgo()[2].getListaHumanos();
                    LinkedBlockingQueue<Humano> listaZonaRiesgo4 = arranque.getArrayZonaRiesgo()[3].getListaHumanos();

                    


                    oos.flush();
                    oos.reset();
                    System.out.println("A");
                    parar = (Boolean) ois.readObject();
                    System.out.println("El cliente envia un "+parar.booleanValue());
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Error en la conexion con el cliente");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.iniciarServ();
    }
}
