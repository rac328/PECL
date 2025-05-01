package org.example;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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

            while (true) {

                System.out.println("PRIMERO "+parar);

                parar = (Boolean) ois.readObject();
                System.out.println("SEGUNDO "+parar);

                if(parar){
                    arranque.pausarEjecucion();
                    System.out.println("dENTRO DEL IF: " + parar);
                }
                else{
                    arranque.reanudarEjecucion();
                    System.out.println("DENTRO DEL ELSE: " + parar);
                }

                //listas de la Zona Segura
                LinkedBlockingQueue<Humano> listaComedor = arranque.getComedor().getListaHumanosComedor();
                System.out.println(listaComedor.toString());
                oos.writeObject(listaComedor);
                LinkedBlockingQueue<Humano> listaZonaComun = arranque.getZonaComun().getListaHumanosZonaComun();
                oos.writeObject(listaZonaComun);
                LinkedBlockingQueue<Humano> listaZonaDescanso = arranque.getZonaDescanso().getListaHumanosDescansando();
                oos.writeObject(listaZonaDescanso);

                //Listas de zonas de Riesgo
                ArrayList<Humano> listaZonaRiesgo1 = arranque.getArrayZonaRiesgo()[0].getListaHumanos();
                oos.writeObject(listaZonaRiesgo1);
                System.out.println(listaZonaRiesgo1.toString());
                ArrayList<Humano> listaZonaRiesgo2 = arranque.getArrayZonaRiesgo()[1].getListaHumanos();
                oos.writeObject(listaZonaRiesgo2);
                ArrayList<Humano> listaZonaRiesgo3 = arranque.getArrayZonaRiesgo()[2].getListaHumanos();
                oos.writeObject(listaZonaRiesgo3);
                ArrayList<Humano> listaZonaRiesgo4 = arranque.getArrayZonaRiesgo()[3].getListaHumanos();
                oos.writeObject(listaZonaRiesgo4);

                //Lista de humanos para pasar en los Túneles
                LinkedBlockingQueue<Humano> listaPasarTunel1 = arranque.getArrayTunel()[0].getListaPasar();
                oos.writeObject(listaPasarTunel1);
                LinkedBlockingQueue<Humano> listaPasarTunel2 = arranque.getArrayTunel()[1].getListaPasar();
                oos.writeObject(listaPasarTunel2);
                LinkedBlockingQueue<Humano> listaPasarTunel3 = arranque.getArrayTunel()[2].getListaPasar();
                oos.writeObject(listaPasarTunel3);
                LinkedBlockingQueue<Humano> listaPasarTunel4 = arranque.getArrayTunel()[3].getListaPasar();
                oos.writeObject(listaPasarTunel4);

                //Listas de humanos pasando en cada momento por los túneles
                LinkedBlockingQueue<Humano> listaPasandoTunel1 = arranque.getArrayTunel()[0].getListaPasando();
                oos.writeObject(listaPasandoTunel1);
                LinkedBlockingQueue<Humano> listaPasandoTunel2 = arranque.getArrayTunel()[1].getListaPasando();
                oos.writeObject(listaPasandoTunel2);
                LinkedBlockingQueue<Humano> listaPasandoTunel3 = arranque.getArrayTunel()[2].getListaPasando();
                oos.writeObject(listaPasandoTunel3);
                LinkedBlockingQueue<Humano> listaPasandoTunel4 = arranque.getArrayTunel()[3].getListaPasando();
                oos.writeObject(listaPasandoTunel4);

                //Listas de humanos regresando en cada tunel
                LinkedBlockingQueue<Humano> listaRegresandoTunel1 = arranque.getArrayTunel()[0].getListaRegresar();
                oos.writeObject(listaRegresandoTunel1);
                LinkedBlockingQueue<Humano> listaRegresandoTunel2 = arranque.getArrayTunel()[1].getListaRegresar();
                oos.writeObject(listaRegresandoTunel2);
                LinkedBlockingQueue<Humano> listaRegresandoTunel3 = arranque.getArrayTunel()[2].getListaRegresar();
                oos.writeObject(listaRegresandoTunel3);
                LinkedBlockingQueue<Humano> listaRegresandoTunel4 = arranque.getArrayTunel()[3].getListaRegresar();
                oos.writeObject(listaRegresandoTunel4);

                //Listas de Zombies en cada zona de Riesgo
                LinkedBlockingQueue<Zombie> listaZombieZonaRiesgo1 = arranque.getArrayZonaRiesgo()[0].getListaZombies();
                oos.writeObject(listaZombieZonaRiesgo1);
                LinkedBlockingQueue<Zombie> listaZombieZonaRiesgo2 = arranque.getArrayZonaRiesgo()[1].getListaZombies();
                oos.writeObject(listaZombieZonaRiesgo2);
                LinkedBlockingQueue<Zombie> listaZombieZonaRiesgo3 = arranque.getArrayZonaRiesgo()[2].getListaZombies();
                oos.writeObject(listaZombieZonaRiesgo3);
                LinkedBlockingQueue<Zombie> listaZombieZonaRiesgo4 = arranque.getArrayZonaRiesgo()[3].getListaZombies();
                oos.writeObject(listaZombieZonaRiesgo4);

                oos.flush();
                oos.reset();
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
