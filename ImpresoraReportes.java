/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.reportador;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;

class ImpresoraReportes implements Callable<String> {

    private final File directory;

    public ImpresoraReportes(File directory) {
        this.directory = directory;
    }

    @Override
    public String call() {
        StringBuilder report = new StringBuilder();
        String[] parts = directory.getName().split("_");

        if (parts.length < 2) {
            ImpresoraFinal.logError(new IllegalArgumentException("Nombre de directorio no válido: " + directory.getName()));
            return "";
        }

        String month = parts[0];
        String year = parts[1];

        report.append(capitalize(month)).append(" ").append(year).append("\n");

        for (File file : Objects.requireNonNull(directory.listFiles((dir, name) -> name.endsWith(".txt")))) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                String topic = "";
                boolean hallazgoSection = false;
                report.append("# Reporte del ").append(file.getName().substring(8, 10)).append("/").append(file.getName().substring(11, 13)).append("/").append(file.getName().substring(14, 18)).append("\n");

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("# Topico del reporte")) {
                        topic = reader.readLine();
                        report.append("## ").append(topic).append("\n");
                    } else if (line.startsWith("# Hallazgos")) {
                        hallazgoSection = true;
                    } else if (hallazgoSection) {
                        if (line.startsWith("- ")) {
                            report.append(line).append("\n");
                        } else {
                            hallazgoSection = false;
                        }
                    }
                }
            } catch (IOException e) {
                ImpresoraFinal.logError(e);
            }
        }

        return report.toString();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
