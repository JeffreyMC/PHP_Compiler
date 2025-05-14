
package proyecto2_compiladores;


public class Principal {

    public static void main(String[] args) {
        // Valida que el archivo contenga la extension .php
        if (args.length != 1 || !args[0].endsWith(".php")) {
            System.out.println("Error: El archivo debe tener extensión .php");
            return;
        }

        // Crea un objeto de la clase Analizador para analizar el archivo
        Analizador analizador = new Analizador(args[0]);

        // Lee el archivo PHP y lo analiza linea por linea
        analizador.analizar();

        // Genera el archivo de log con los errores correspondientes (si los hay)
        analizador.generarLog();
    }
}
