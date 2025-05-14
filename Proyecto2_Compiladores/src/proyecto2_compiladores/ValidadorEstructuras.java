package proyecto2_compiladores;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidadorEstructuras {

    // Define cuántos espacios debe tener cada nivel de indentación (4 espacios por nivel)
    private static final int NIVEL_INDENTACION = 4;
    
    // Conjunto para rastrear líneas con errores de indentación ya reportados (para evitar duplicados)
    private static Set<Integer> lineasConErrorIndentacion = new HashSet<>();

    // Método para validar un bucle 'while'
    // Recibe la línea actual, número de línea, lista de líneas, índice actual, lista de errores y nivel de indentación base
    public static void validarWhile(String linea, int numeroLinea, List<String> lineas, int indiceActual, List<String> errores, int nivelIndentacionBase) {
        // Elimina espacios de toda la línea
        String trimmedLinea = linea.trim();

        // Valida la indentación de la línea inicial antes de la llave de apertura '{'
        validarIndentacionLineaInicial(linea, numeroLinea, nivelIndentacionBase, errores, "while");

        // Valida si la línea comienza con 'while', si no, agrega un error
        if (!trimmedLinea.startsWith("while")) {
            Validador.agregarError(errores, numeroLinea, "Error 600. El bucle 'while' debe comenzar con la palabra reservada 'while'.");
            return;
        }
        // Valida si hay paréntesis de apertura '('
        if (!trimmedLinea.contains("(")) {
            Validador.agregarError(errores, numeroLinea, "Error 601. Falta paréntesis de apertura en el 'while'.");
            return;
        }
        // Valida si hay paréntesis de cierre ')'
        if (!trimmedLinea.contains(")")) {
            Validador.agregarError(errores, numeroLinea, "Error 602. Falta paréntesis de cierre en el 'while'.");
            return;
        }
        // Obtiene las posiciones de los paréntesis para validar su orden
        int inicioParentesis = trimmedLinea.indexOf("(");
        int finParentesis = trimmedLinea.indexOf(")");
        // Si el paréntesis de apertura está después del de cierre, hay un error
        if (inicioParentesis >= finParentesis) {
            Validador.agregarError(errores, numeroLinea, "Error 603. Los paréntesis están en orden incorrecto.");
            return;
        }
        // Verifica que la llave de apertura '{' esté después del paréntesis de cierre
        int aperturaLlave = trimmedLinea.indexOf("{");
        if (aperturaLlave != -1 && aperturaLlave < finParentesis) {
            Validador.agregarError(errores, numeroLinea, "Error 604. El paréntesis de cierre debe estar antes de la llave de apertura.");
            return;
        }
        // Extrae la condición entre paréntesis y valida que no esté vacía
        String condicion = trimmedLinea.substring(inicioParentesis + 1, finParentesis).trim();
        if (condicion.isEmpty()) {
            Validador.agregarError(errores, numeroLinea, "Error 605. Falta la condición dentro de los paréntesis del 'while'.");
            return;
        }
        // Valida que la línea termine con la llave de apertura '{'
        if (!trimmedLinea.endsWith("{")) {
            Validador.agregarError(errores, numeroLinea, "Error 606. Falta la llave de apertura '{' al final del 'while'.");
            return;
        }
        // Verifica que existan ambos paréntesis (estén balanceados)
        if (!esBalanceado(trimmedLinea, "(", ")")) {
            Validador.agregarError(errores, numeroLinea, "Error 607. Paréntesis no balanceados en el 'while'.");
            return;
        }

        // Valida la indentación y el contenido del bloque 'while' con el nivel base
        validarIndentacionYContenido(numeroLinea, lineas, indiceActual, errores, "while", nivelIndentacionBase);
    }

    // Método para validar la estructura if
    public static void validarIf(String linea, int numeroLinea, List<String> lineas, int indiceActual, List<String> errores, int nivelIndentacionBase) {
        String trimmedLinea = linea.trim(); //elimina los espacios de la linea

        // Valida la indentación de la línea inicial antes de la llave de apertura'{'
        validarIndentacionLineaInicial(linea, numeroLinea, nivelIndentacionBase, errores, "if");

        // Valida si la línea comienza con 'if'
        if (!trimmedLinea.startsWith("if")) {
            Validador.agregarError(errores, numeroLinea, "Error 610. La estructura debe comenzar con la palabra reservada 'if'.");
            return;
        }
        // Valida que haya un paréntesis de apertura justo después de 'if'
        if (!trimmedLinea.startsWith("if(") && !trimmedLinea.startsWith("if (")) {
            Validador.agregarError(errores, numeroLinea, "Error 611. Falta el paréntesis de apertura '(' en la estructura 'if'.");
            return;
        }
        // Obtiene las posiciones de los paréntesis
        int inicioParentesis = trimmedLinea.indexOf("(");
        int finParentesis = trimmedLinea.lastIndexOf(")");
        // Verifica que los paréntesis estén en orden correcto
        if (inicioParentesis >= finParentesis) {
            Validador.agregarError(errores, numeroLinea, "Error 612. Los paréntesis están en orden incorrecto o faltan.");
            return;
        }
        // Verifica que la llave de apertura esté después del paréntesis de cierre
        int aperturaLlave = trimmedLinea.indexOf("{");
        if (aperturaLlave != -1 && aperturaLlave < finParentesis) {
            Validador.agregarError(errores, numeroLinea, "Error 613. El paréntesis de cierre debe estar antes de la llave de apertura.");
            return;
        }
        // Extrae y valida que la condición no esté vacía
        String condicion = trimmedLinea.substring(inicioParentesis + 1, finParentesis).trim();
        if (condicion.isEmpty()) {
            Validador.agregarError(errores, numeroLinea, "Error 614. Falta la condición dentro de los paréntesis del 'if'.");
            return;
        }
        // Verifica que termine con '{'
        if (!trimmedLinea.endsWith("{")) {
            Validador.agregarError(errores, numeroLinea, "Error 615. Falta la llave de apertura '{' al final del 'if'.");
            return;
        }
        // Verifica el balance de paréntesis
        if (!esBalanceado(trimmedLinea, "(", ")")) {
            Validador.agregarError(errores, numeroLinea, "Error 616. Paréntesis no balanceados en el 'if'.");
            return;
        }

        // Valida la indentación y el contenido del bloque 'if'
        validarIndentacionYContenido(numeroLinea, lineas, indiceActual, errores, "if", nivelIndentacionBase);
    }

    // Método para validar una estructura 'else'
    public static void validarElse(String linea, int numeroLinea, List<String> lineas, int indiceActual, List<String> errores, int nivelIndentacionBase) {
        String trimmedLinea = linea.trim(); //elimina los espacios de la línea

        // Valida la indentación de la línea inicial antes de '{'
        validarIndentacionLineaInicial(linea, numeroLinea, nivelIndentacionBase, errores, "else");

        // Verifica si la línea comienza con 'else'
        if (!trimmedLinea.startsWith("else")) {
            Validador.agregarError(errores, numeroLinea, "Error 620. La Estructura debe comenzar con la palabra reservada 'else'.");
            return;
        }
        // Extrae lo que sigue a 'else' y elimina espacios
        String restoLinea = trimmedLinea.substring(4).trim();
        // Verifica que haya una llave de apertura '{'
        if (!restoLinea.startsWith("{")) {
            Validador.agregarError(errores, numeroLinea, "Error 621. Después de 'else' debe haber una llave de apertura '{'.");
        }
        // Si hay llave de apertura '{', valida el bloque completo
        if (restoLinea.startsWith("{")) {
            int llavesAbiertas = 1; // Contador de llaves abiertas, inicia con 1 por '{'
            boolean llaveCierreEncontrada = false; // Indica si se cerró el bloque

            // Recorre las líneas siguientes para verificar el balance de llaves
            for (int i = indiceActual + 1; i < lineas.size(); i++) {
                String lineaActual = lineas.get(i).trim();
                for (char c : lineaActual.toCharArray()) {
                    if (c == '{') {
                        llavesAbiertas++; // Nueva llave de apertura
                    } else if (c == '}') {
                        llavesAbiertas--; // Nueva llave de cierre
                        if (llavesAbiertas == 0) {
                            llaveCierreEncontrada = true; // Bloque cerrado
                            break;
                        }
                    }
                }
                if (llaveCierreEncontrada) {
                    break; // Sale si el bloque está cerrado
                }
            }
            // Si no se encontró la llave de cierre, reporta error
            if (!llaveCierreEncontrada) {
                Validador.agregarError(errores, numeroLinea, "Error 622. Falta la llave de cierre '}' en el bloque 'else'.");
            }
        } else {
            // Si no hay llave de apertura '{', verifica la siguiente línea
            if (indiceActual + 1 < lineas.size()) {
                String siguienteLinea = lineas.get(indiceActual + 1).trim();
                if (!siguienteLinea.equals("}")) {
                    Validador.agregarError(errores, numeroLinea + 1, "Error 623. Se esperaba una llave de cierre '}' o un bloque válido después de 'else'.");
                }
            }
        }

        // Valida la indentación y el contenido del bloque 'else'
        validarIndentacionYContenido(numeroLinea, lineas, indiceActual, errores, "else", nivelIndentacionBase);
    }

    // Método para validar la indentación de la línea inicial antes de la llave de apertura '{'
    private static void validarIndentacionLineaInicial(String linea, int numeroLinea, int nivelIndentacionBase, List<String> errores, String estructura) {
        // Calcula los espacios esperados según el nivel de indentación base
        //el nivel de indentación por nivel es de 4 espacios por defecto
        //Si existe anidamiento de funciones se multiplica esos 4 espacios por el nivel base
        int espaciosEsperados = nivelIndentacionBase * NIVEL_INDENTACION;
        // Extrae los espacios iniciales de la línea (antes del primer carácter (sin espacios))
        String espaciosActuales = linea.substring(0, linea.indexOf(linea.trim().charAt(0)));

        // Verifica que no haya tabulaciones en la indentación
        if (espaciosActuales.contains("\t")) {
            Validador.agregarError(errores, numeroLinea, "Error 624. No se permiten tabulaciones en la indentación de '" + estructura + "'.");
        }
        // Verifica que la cantidad de espacios sea exactamente la esperada
        if (espaciosActuales.length() != espaciosEsperados) {
            Validador.agregarError(errores, numeroLinea, "Error 625. La indentación de '" + estructura + " no es correcta.");
        }
    }

    // Método para validar la indentación y el contenido dentro de un bloque
    private static void validarIndentacionYContenido(int numeroLinea, List<String> lineas, int indiceActual, List<String> errores, String estructura, int nivelIndentacionBase) {
        // El nivel dentro del bloque es el base + 1 (la base son 4 espacios)
        int nivelActual = nivelIndentacionBase + 1;
        boolean llaveCierreEncontrada = false; // Indica si se cerró el bloque
        int lineasDentroBloque = 0; // Contador de líneas de código dentro del bloque
        

        // Recorre las líneas siguientes al bloque
        for (int i = indiceActual + 1; i < lineas.size(); i++) {
            String lineaBloque = lineas.get(i); // Línea completa
            String trimmedLineaBloque = lineaBloque.trim(); // Línea sin espacios iniciales/finales

            // Si la línea no está vacía, valida su indentación
            if (!trimmedLineaBloque.isEmpty()) {
                // Calcula los espacios esperados: nivelActual para código, nivelActual-1 para llave de cierre '}'
                String espaciosEsperados = trimmedLineaBloque.equals("}")
                        ? " ".repeat((nivelActual - 1) * NIVEL_INDENTACION)
                        : " ".repeat(nivelActual * NIVEL_INDENTACION);
                // Extrae los espacios iniciales de la línea
                String inicioLinea = lineaBloque.substring(0, Math.min(lineaBloque.length(), espaciosEsperados.length()));

                // Verifica que no haya tabulaciones
                if (lineaBloque.contains("\t") && !lineasConErrorIndentacion.contains(i + 1)) {
                    Validador.agregarError(errores, i + 1, "Error 624. No se permiten tabulaciones en la indentación.");
                    lineasConErrorIndentacion.add(i + 1); // Agrega la línea al conjunto
                }
                // Verifica que la indentación sea correcta
                if (!inicioLinea.equals(espaciosEsperados) && !lineasConErrorIndentacion.contains(i + 1)) {
                    Validador.agregarError(errores, i + 1, "Error 625. La indentación debe ser exactamente " + espaciosEsperados.length() + " espacios.");
                    lineasConErrorIndentacion.add(i + 1); // Agrega la línea al conjunto
                }

                // Cuenta líneas de código (ignora líneas con solo llaves)
                if (!trimmedLineaBloque.equals("{") && !trimmedLineaBloque.equals("}")) {
                    lineasDentroBloque++;
                }
            }

            // Actualiza el nivel de indentación según las llaves
            if (trimmedLineaBloque.endsWith("{")) {
                nivelActual++; // Incrementa por nueva llave de apertura
            }
            if (trimmedLineaBloque.startsWith("}")) {
                nivelActual--; // Decrementa por llave de cierre
                // Si el nivel vuelve al base, el bloque principal se cerró
                if (nivelActual == nivelIndentacionBase) {
                    llaveCierreEncontrada = true;
                    break;
                }
            }
        }

        // Verifica si falta la llave de cierre del bloque
        if (!llaveCierreEncontrada) {
            Validador.agregarError(errores, numeroLinea, "Error 626. Falta la llave de cierre '}' del '" + estructura + "'");
        } 
        // Verifica si el bloque está vacío (sin líneas de código)
        else if (lineasDentroBloque == 0) {
            Validador.agregarError(errores, numeroLinea, "Error 627. El bloque '" + estructura + "' debe contener al menos una línea de código.");
        }
    }

    // Método para verificar si los paréntesis tienen su par (están balanceados)
    private static boolean esBalanceado(String linea, String apertura, String cierre) {
        int contador = 0; // Contador para rastrear el balance
        for (char c : linea.toCharArray()) {
            if (c == apertura.charAt(0)) {
                contador++; // Incrementa por cada apertura
            } else if (c == cierre.charAt(0)) {
                contador--; // Decrementa por cada cierre
            }
            if (contador < 0) {
                return false; // Si hay más cierres que aperturas, no está balanceado
            }
        }
        return contador == 0; // True si el contador termina en 0 (balanceado)
    }

    // Método para validar la estructura de comando 'echo'
    public static void validarEcho(String linea, int numeroLinea, List<String> errores) {
        String trimmedLinea = linea.trim(); // Elimina todos los espacios

        // Si no comienza con 'echo', no se valida
        if (!trimmedLinea.startsWith("echo")) {
            return;
        }
        // Verifica que termine con punto y coma
        if (!trimmedLinea.endsWith(";")) {
            Validador.agregarError(errores, numeroLinea, "Error 632. La línea de 'echo' debe terminar con un punto y coma.");
        }
        // Maneja casos con '. implode', recortando la línea antes de esa parte
        //Cualquier instrucción después de ". implode" se da por correcta
        int implodeIndex = trimmedLinea.indexOf(". implode");
        if (implodeIndex != -1) {
            trimmedLinea = trimmedLinea.substring(0, implodeIndex).trim();
        }

        // Verifica la presencia de comillas dobles y simples
        boolean tieneComillasDobles = trimmedLinea.contains("\"");
        boolean tieneComillasSimples = trimmedLinea.contains("'");

        // Valida si hay comillas dobles y simples juntas
        if (tieneComillasDobles && tieneComillasSimples) {
            int countComillasDobles = 0;
            int countComillasSimples = 0;
            for (char c : trimmedLinea.toCharArray()) {
                if (c == '"') {
                    countComillasDobles++; // Cuenta comillas dobles
                } else if (c == '\'') {
                    countComillasSimples++; // Cuenta comillas simples
                }
            }
            // Verifica que las comillas dobles estén emparejadas
            if (countComillasDobles % 2 != 0) {
                Validador.agregarError(errores, numeroLinea, "Error 630. Comillas dobles no emparejadas en 'echo'.");
            }
            // Verifica que las comillas simples estén emparejadas
            if (countComillasSimples % 2 != 0) {
                Validador.agregarError(errores, numeroLinea, "Error 631. Comillas simples no emparejadas en 'echo'.");
            }
        } 
        // Valida solo comillas dobles
        else if (tieneComillasDobles) {
            int countComillasDobles = 0;
            for (char c : trimmedLinea.toCharArray()) {
                if (c == '"') {
                    countComillasDobles++;
                }
            }
            if (countComillasDobles % 2 != 0) {
                Validador.agregarError(errores, numeroLinea, "Error 630. Comillas dobles no emparejadas en 'echo'.");
            }
        } 
        // Valida solo comillas simples
        else if (tieneComillasSimples) {
            int countComillasSimples = 0;
            for (char c : trimmedLinea.toCharArray()) {
                if (c == '\'') {
                    countComillasSimples++;
                }
            }
            if (countComillasSimples % 2 != 0) {
                Validador.agregarError(errores, numeroLinea, "Error 631. Comillas simples no emparejadas en 'echo'.");
            }
        }
    }
}