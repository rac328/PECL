package Parte2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import Visuals.ApocalipsisZombi.*;


public class Cliente {

    private VentanaCli ventana = new VentanaCli(this);

    private Boolean cambioEstado = false;
    private Boolean parar = false;

    private Integer listaComedorRecibida;
    private Integer listaZonaComunRecibida;
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
    private ArrayList<String> listaMejoresZombies;

    public Cliente() {
        ventana.setVisible(true);
    }



    public void conectarServ() {
        Socket cliente;
        try {
            cliente = new Socket(InetAddress.getLocalHost(), 5002); // Conectar al servidor
            ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());

            while (true) {

                oos.writeObject(parar);
                if(cambioEstado) {
                    oos.writeObject(cambioEstado);
                    setFalseCambiarEstado();
                }
                else{
                    oos.writeObject(cambioEstado);
                }
                oos.flush();
                oos.reset();

                //Listas de la zona segura
                listaComedorRecibida = (Integer) ois.readObject();
                listaZonaComunRecibida = (Integer) ois.readObject();
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

                //Mejores zombies
                listaMejoresZombies = (ArrayList<String>) ois.readObject();

                ventana.actualizarVentana();
            }
        } catch (NullPointerException | IOException | ClassNotFoundException e) {
            System.out.println("Error");
            throw new RuntimeException(e);
        }
    }

    public Boolean getCambioEstado() {
        return cambioEstado;
    }

    public Boolean getParar() {
        return parar;
    }

    public void cambiarEstadoParar() {
        parar = !parar;
    }

    public void setTrueCambiarEstado(){
       cambioEstado = true;
    }

    public void setFalseCambiarEstado(){
        cambioEstado = false;
    }

    public static void main(String args[]) {
        Cliente cliente = new Cliente();
        new Thread(() -> cliente.conectarServ()).start();
    }

    public Integer getListaZombieZonaRiesgo4() {
        return listaZombieZonaRiesgo4;
    }

    public Integer getListaZombieZonaRiesgo3() {
        return listaZombieZonaRiesgo3;
    }

    public Integer getListaZombieZonaRiesgo1() {
        return listaZombieZonaRiesgo1;
    }

    public Integer getListaZombieZonaRiesgo2() {
        return listaZombieZonaRiesgo2;
    }

    public Integer getListaRegresandoTunelRecibida4() {
        return listaRegresandoTunelRecibida4;
    }

    public Integer getListaRegresandoTunelRecibida3() {
        return listaRegresandoTunelRecibida3;
    }

    public Integer getListaRegresandoTunelRecibida2() {
        return listaRegresandoTunelRecibida2;
    }

    public Integer getListaRegresandoTunelRecibida1() {
        return listaRegresandoTunelRecibida1;
    }

    public Integer getListaPasandoTunelRecibida4() {
        return listaPasandoTunelRecibida4;
    }

    public Integer getListaPasandoTunelRecibida3() {
        return listaPasandoTunelRecibida3;
    }

    public Integer getListaPasandoTunelRecibida2() {
        return listaPasandoTunelRecibida2;
    }

    public Integer getListaPasandoTunelRecibida1() {
        return listaPasandoTunelRecibida1;
    }

    public Integer getListaPasarTunelRecibida4() {
        return listaPasarTunelRecibida4;
    }

    public Integer getListaPasarTunelRecibida3() {
        return listaPasarTunelRecibida3;
    }

    public Integer getListaPasarTunelRecibida2() {
        return listaPasarTunelRecibida2;
    }

    public Integer getListaPasarTunelRecibida1() {
        return listaPasarTunelRecibida1;
    }

    public Integer getListaZonaRiesgoRecibida4() {
        return listaZonaRiesgoRecibida4;
    }

    public Integer getListaZonaRiesgoRecibida3() {
        return listaZonaRiesgoRecibida3;
    }

    public Integer getListaZonaRiesgoRecibida2() {
        return listaZonaRiesgoRecibida2;
    }

    public Integer getListaZonaRiesgoRecibida1() {
        return listaZonaRiesgoRecibida1;
    }

    public Integer getListaZonaDescansoRecibida() {
        return listaZonaDescansoRecibida;
    }

    public Integer getListaZonaComunRecibida() {
        return listaZonaComunRecibida;
    }

    public Integer getListaComedorRecibida() {
        return listaComedorRecibida;
    }

    public ArrayList<String> getListaMejoresZombies() {
        return listaMejoresZombies;
    }
}
