package Parte1;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {


    private ServerSocket servidor;
    private Arranque arranque = new Arranque();
    private Boolean parar = false;

    public Servidor() {
    }

    public Arranque getArranque() {
        return arranque;
    }

    public void iniciarServ() {
        try {
            servidor = new ServerSocket(5002);
            System.out.println("Servidor Arrancando....");
            arranque.crearSimulacionSegundoPlano();
            while (true) {
                Socket cliente = servidor.accept();
                conexionCliente(cliente);
            }
        } catch (IOException e) {
            System.out.println("Error al iniciar server");
        }
    }

    public void conexionCliente(Socket cli) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(cli.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(cli.getInputStream());

            while (true) {

                System.out.println("PRIMERO " + parar);

                parar = (Boolean) ois.readObject();
                System.out.println("SEGUNDO " + parar);

                if (parar) {
                    arranque.pausarEjecucion();
                    System.out.println("dENTRO DEL IF: " + parar);
                } else {
                    arranque.reanudarEjecucion();
                    System.out.println("DENTRO DEL ELSE: " + parar);
                }

                //listas de la Zona Segura
                int listaComedor = arranque.getComedor().getListaHumanosComedor().size();
                oos.writeObject(listaComedor);
                int listaZonaComun = arranque.getZonaComun().getListaHumanosZonaComun().size();
                oos.writeObject(listaZonaComun);
                int listaZonaDescanso = arranque.getZonaDescanso().getListaHumanosDescansando().size();
                oos.writeObject(listaZonaDescanso);

                //Listas de zonas de Riesgo
                int listaZonaRiesgo1 = arranque.getArrayZonaRiesgo()[0].getListaHumanos().size();
                oos.writeObject(listaZonaRiesgo1);
                int listaZonaRiesgo2 = arranque.getArrayZonaRiesgo()[1].getListaHumanos().size();
                oos.writeObject(listaZonaRiesgo2);
                int listaZonaRiesgo3 = arranque.getArrayZonaRiesgo()[2].getListaHumanos().size();
                oos.writeObject(listaZonaRiesgo3);
                int listaZonaRiesgo4 = arranque.getArrayZonaRiesgo()[3].getListaHumanos().size();
                oos.writeObject(listaZonaRiesgo4);

                //Lista de humanos para pasar en los Túneles
                int listaPasarTunel1 = arranque.getArrayTunel()[0].getListaPasar().size();
                oos.writeObject(listaPasarTunel1);
                int listaPasarTunel2 = arranque.getArrayTunel()[1].getListaPasar().size();
                oos.writeObject(listaPasarTunel2);
                int listaPasarTunel3 = arranque.getArrayTunel()[2].getListaPasar().size();
                oos.writeObject(listaPasarTunel3);
                int listaPasarTunel4 = arranque.getArrayTunel()[3].getListaPasar().size();
                oos.writeObject(listaPasarTunel4);

                //Listas de humanos pasando en cada momento por los túneles
                int listaPasandoTunel1 = arranque.getArrayTunel()[0].getListaPasando().size();
                oos.writeObject(listaPasandoTunel1);
                int listaPasandoTunel2 = arranque.getArrayTunel()[1].getListaPasando().size();
                oos.writeObject(listaPasandoTunel2);
                int listaPasandoTunel3 = arranque.getArrayTunel()[2].getListaPasando().size();
                oos.writeObject(listaPasandoTunel3);
                int listaPasandoTunel4 = arranque.getArrayTunel()[3].getListaPasando().size();
                oos.writeObject(listaPasandoTunel4);

                //Listas de humanos regresando en cada tunel
                int listaRegresandoTunel1 = arranque.getArrayTunel()[0].getListaRegresar().size();
                oos.writeObject(listaRegresandoTunel1);
                int listaRegresandoTunel2 = arranque.getArrayTunel()[1].getListaRegresar().size();
                oos.writeObject(listaRegresandoTunel2);
                int listaRegresandoTunel3 = arranque.getArrayTunel()[2].getListaRegresar().size();
                oos.writeObject(listaRegresandoTunel3);
                int listaRegresandoTunel4 = arranque.getArrayTunel()[3].getListaRegresar().size();
                oos.writeObject(listaRegresandoTunel4);

                //Listas de Zombies en cada zona de Riesgo
                int listaZombieZonaRiesgo1 = arranque.getArrayZonaRiesgo()[0].getListaZombies().size();
                oos.writeObject(listaZombieZonaRiesgo1);
                int listaZombieZonaRiesgo2 = arranque.getArrayZonaRiesgo()[1].getListaZombies().size();
                oos.writeObject(listaZombieZonaRiesgo2);
                int listaZombieZonaRiesgo3 = arranque.getArrayZonaRiesgo()[2].getListaZombies().size();
                oos.writeObject(listaZombieZonaRiesgo3);
                int listaZombieZonaRiesgo4 = arranque.getArrayZonaRiesgo()[3].getListaZombies().size();
                oos.writeObject(listaZombieZonaRiesgo4);

                ArrayList<String> listaMejoresZombies = arranque.getRanking().hacerRanking();
                if (listaMejoresZombies != null && !listaMejoresZombies.isEmpty()) {
                    System.out.println(listaMejoresZombies);
                } else {
                    System.out.println("La lista de mejores zombies está vacía o es nula.");
                }
                oos.writeObject(listaMejoresZombies);

                oos.flush();
                oos.reset();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error en la conexion con el cliente");
            throw new RuntimeException(e);

        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.iniciarServ();
    }
}
