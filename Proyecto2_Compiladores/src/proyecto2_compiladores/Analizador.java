package proyecto2_compiladores;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Analizador {

    private String archivoPHP;
    private List<String> errores;
    private List<String> lineas;

    // Constructor que recibe el archivo .php
    public Analizador(String archivoPHP) {
        this.archivoPHP = archivoPHP;
        this.errores = new ArrayList<>();
        this.lineas = new ArrayList<>();
    }

    // Método para leer el archivo PHP línea por línea
    public void leerArchivo() {
        try (Scanner scanner = new Scanner(new File(archivoPHP))) {
            while (scanner.hasNextLine()) {
                lineas.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: No se pudo encontrar el archivo.");
        }
    }

    // Método para analizar el archivo
    public void analizar() {
        leerArchivo();

        // Variable para rastrear el nivel de indentación actual basado en llaves
        int nivelIndentacionActual = 0;

        // Analiza cada línea del archivo
        for (int i = 0; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            String trimmedLinea = linea.trim();

            // Valida que no haya espacios o texto antes de la etiqueta de apertura <?php
            if (i == 0 && !linea.equals("<?php")) {
                agregarError(i + 1, "Error 200. La etiqueta de apertura <?php no está al inicio del archivo o tiene espacios previos.");
            }

            // Valida que la etiqueta de cierre no esté en el lugar equivocado
            if (i != lineas.size() - 1 && (trimmedLinea.startsWith("?>") || trimmedLinea.endsWith("?>"))) {
                agregarError(i + 1, "Error 201. La etiqueta de cierre ?> está en un lugar que no le corresponde");
            }
            // Valida que haya etiqueta de cierre y sea la última línea
            if (i == lineas.size() - 1 && !trimmedLinea.endsWith("?>")) {
                agregarError(i + 1, "Error 202. No se encuentra la etiqueta de cierre ?>");
            }

            // Llama a la clase Validador para validaciones básicas (variables y comentarios)
            Validador.validarIdentificadores(linea, i + 1, errores);
            Validador.validarComentarios(linea, i + 1, errores);

            // Llama a la clase ValidadorEstructuras, pasando el nivel de indentación actual
            if (trimmedLinea.startsWith("while")) {
                ValidadorEstructuras.validarWhile(linea, i + 1, lineas, i, errores, nivelIndentacionActual);
            } else if (trimmedLinea.startsWith("if")) {
                ValidadorEstructuras.validarIf(linea, i + 1, lineas, i, errores, nivelIndentacionActual);
            } else if (trimmedLinea.startsWith("echo")) {
                ValidadorEstructuras.validarEcho(linea, i + 1, errores); // Este no valida su identacion base
            } else if (trimmedLinea.startsWith("else")) {
                ValidadorEstructuras.validarElse(linea, i + 1, lineas, i, errores, nivelIndentacionActual);
            }

            // Actualiza el nivel de indentación basado en llaves
            for (char c : trimmedLinea.toCharArray()) {
                if (c == '{') {
                    nivelIndentacionActual++; // Incrementa el nivel al abrir un bloque
                } else if (c == '}') {
                    nivelIndentacionActual--; // Decrementa el nivel al cerrar un bloque
                    if (nivelIndentacionActual < 0) {
                        nivelIndentacionActual = 0; // Evita niveles negativos
                        agregarError(i + 1, "Error 203. Llave de cierre '}' sin apertura correspondiente.");
                    }
                }
            }
        }

        // Verifica si quedaron llaves sin cerrar
        if (nivelIndentacionActual > 0) {
            agregarError(lineas.size(), "Error 204. Falta(n) llave(s) de cierre '}' en el archivo.");
        }
    }

    // Método para agregar los errores encontrados
    private void agregarError(int linea, String mensaje) {
        errores.add(String.format("%04d: %s", linea, mensaje));
    }

    // Método para generar el archivo de log con los errores
    public void generarLog() {
        try (FileWriter writer = new FileWriter(archivoPHP.replace(".php", "-errores.log"))) {
            // Copia todo el código PHP en el archivo de log
            for (int i = 0; i < lineas.size(); i++) {
                String numeroLinea = String.format("%04d", i + 1); // Formatea el número de línea con 4 dígitos
                writer.write(numeroLinea + ": " + lineas.get(i) + "\n");
            }

            // Si hay errores, los agrega al final
            if (!errores.isEmpty()) {
                writer.write("\n\n=======Se encontraron los siguientes errores=======\n\n");
                System.out.println("Se encontraron los siguientes errores: \n");
                for (String error : errores) {
                    writer.write(error + "\n");
                    System.out.println(error);
                }
            } else {
                writer.write("\n\n=====No se encontraron errores en archivo.=====");
                System.out.println("\nNo se encontraron errores en el archivo.");
            }

            System.out.println("");
        } catch (IOException e) {
            System.out.println("Error al generar el archivo de log.");
        }
    }
}