/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.reportador;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class ImpresoraFinal {

    private static final String LOG_FILE_NAME = "log.txt";

    public static void logError(Exception e) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_NAME, true))) {
            writer.write(new Date().toString() + ": " + e.getMessage());
            writer.newLine();
            for (StackTraceElement element : e.getStackTrace()) {
                writer.write(element.toString());
                writer.newLine();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

