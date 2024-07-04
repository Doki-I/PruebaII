/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.reportador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;

public class Reportador {

    private static final String REPORT_FILE_NAME = "reporte_hallazgos.txt";

    public static void main(String[] args) {
        String rootDirectory = JOptionPane.showInputDialog("Ingrese el directorio raíz de los reportes:");
        if (rootDirectory == null || rootDirectory.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Directorio raíz no puede estar vacío.");
            return;
        }

        File rootDir = new File(rootDirectory);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            JOptionPane.showMessageDialog(null, "Directorio raíz no válido.");
            return;
        }

        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<String>> futures = new ArrayList<>();

        for (File monthYearDir : Objects.requireNonNull(rootDir.listFiles(File::isDirectory))) {
            futures.add(executor.submit(new ImpresoraReportes(monthYearDir)));
        }

        executor.shutdown();
        StringBuilder finalReport = new StringBuilder();

        try {
            for (Future<String> future : futures) {
                finalReport.append(future.get());
            }
            FilesX.writeToFile(REPORT_FILE_NAME, finalReport.toString());
            JOptionPane.showMessageDialog(null, "Reporte generado exitosamente: " + REPORT_FILE_NAME);
        } catch (InterruptedException | ExecutionException e) {
            ImpresoraFinal.logError(e);
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage());
        }
    }
}
