package proyecto2_compiladores;

import java.util.List;

public class Validador {

    // Valida identificadores (variables) en una línea de código PHP
    public static void validarIdentificadores(String linea, int numeroLinea, List<String> errores) {
        // Divide la línea en palabras o tokens usando espacios como separador
        String[] palabras = linea.split("\\s+");

        // Recorre cada token para verificar si es una variable (comienza con $)
        for (String palabra : palabras) {
            if (palabra.startsWith("$")) {
                // Si la variable es solo "$", falta el identificador
                if (palabra.length() < 2) {
                    agregarError(errores, numeroLinea, "Error 700. Falta el identificador de la variable.");
                }
                // Si el primer carácter después de $ es un número, es inválido
                if (Character.isDigit(palabra.charAt(1))) {
                    agregarError(errores, numeroLinea, "Error 299. La variable no debe comenzar con un número.");
                }
                // Si el primer carácter después de $ no es letra ni _, es inválido
                if (!Character.isLetter(palabra.charAt(1)) && palabra.charAt(1) != '_' && !Character.isDigit(palabra.charAt(1))) {
                    agregarError(errores, numeroLinea, "Error 300. La variable no debe comenzar con un carácter especial.");
                }
                // Si el nombre de la variable (sin $) es una palabra reservada, es inválido
                if (isPalabraReservada(palabra.substring(1))) {
                    agregarError(errores, numeroLinea, "Error 301. La variable " + palabra + " es una palabra reservada.");
                }
            }
        }
    }

    // Verifica si una palabra es reservada en PHP
    private static boolean isPalabraReservada(String palabra) {
        // Lista de palabras reservadas en PHP según las reglas del proyecto
        List<String> palabrasReservadas = List.of("class", "function", "if", "while", "echo",
                "else", "continue", "for", "return", "class", "public", "null", "implode", "false",
                "true", "array_rand", "str_repeat", "strlen", "str_split", "strtolower", "trim",
                "readline", "ctype_alpha", "in_array", "strpos");
        // Devuelve true si la palabra está en la lista
        return palabrasReservadas.contains(palabra);
    }

    // Encuentra el índice del primer comentario en una línea (// o #)
    public static int getIndiceComentario(String trimmedLinea) {
        // Inicializa el índice del comentario como -1 (no encontrado)
        int indiceComentario = -1;
        int indexDobleSlash = trimmedLinea.indexOf("//"); // Busca //
        int indexHash = trimmedLinea.indexOf("#");        // Busca #

        // Determina cuál comentario aparece primero
        if (indexDobleSlash != -1 && (indexHash == -1 || indexDobleSlash < indexHash)) {
            indiceComentario = indexDobleSlash; // Si // aparece primero, usa su índice
        } else if (indexHash != -1) {
            indiceComentario = indexHash;       // Si # aparece primero, usa su índice
        }

        // Devuelve el índice del comentario o -1 si no hay ninguno
        return indiceComentario;
    }

    // Valida que los comentarios no interrumpan instrucciones de control
    public static void validarComentarios(String linea, int numeroLinea, List<String> errores) {
        // Quita espacios y tabulaciones de la línea
        String trimmedLinea = linea.trim();

        // Verifica si la línea contiene una instrucción de control (if, while, for, switch)
        if (trimmedLinea.matches(".*(if|while|for|switch).*")) {
            // Busca el índice del primer comentario
            int comentarioIndex = getIndiceComentario(trimmedLinea);

            // Si hay un comentario en la línea
            if (comentarioIndex != -1) {
                // Extrae el código antes del comentario
                String codigoAntesComentario = trimmedLinea.substring(0, comentarioIndex).trim();
                // Si el código antes del comentario contiene una instrucción de control, está interrumpida
                if (codigoAntesComentario.matches(".*(if|while|for|switch).*")) {
                    agregarError(errores, numeroLinea, "Error 400. Comentario interrumpe una instrucción de control.");
                }
            }
        }
    }

    // Agrega un error a la lista con el formato especificado
    public static void agregarError(List<String> errores, int linea, String mensaje) {
        errores.add(String.format("%04d: %s", linea, mensaje));
    }
}