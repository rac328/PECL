/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example;

import java.io.BufferedWriter;
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
    private static final String FICHERO = "apocalipsis.txt"; //static = que la variable pertenece a la clase y no al objeto, unica y compartida entre todos los que usen la clase
    private static final Semaphore sem = new Semaphore(1, true); //final = la variable no puede cambiarse despues de ser inicializada
    
    public static void escribir(String msg){
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
