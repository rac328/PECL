/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Parte1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Alex y Raul
 */
public class Logger {
    private String FICHERO = "apocalipsis.txt"; 
    private Semaphore sem = new Semaphore(1, true); 
    
    public void inicializarArchivo() {
        try {
            File docLog = new File(FICHERO);
            // si el archivo existe, se eliminamos
            if (docLog.exists()) {
                docLog.delete();
            }
            // crear el archivo nuevo
            docLog.createNewFile();
        } catch (IOException e) {
            System.out.println("Error al inicializar el archivo.");
        }
    }
    
    public void escribir(String msg){
        try{
            sem.acquire();
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(FICHERO, true));
                String tiempo = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss").format(new Date());
                bw.write(tiempo + " - " + msg);
                bw.newLine();
                bw.close();
            }catch(IOException e){
                System.out.println("Error escribiendo en el log.");
            }
        }catch(InterruptedException ie){
            System.out.println("Interrumpido mientras escribia en el log.");
        }finally{
            sem.release();
        }
    }
}
