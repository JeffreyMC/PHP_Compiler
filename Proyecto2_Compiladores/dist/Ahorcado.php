<?php
// Definimos un array con las palabras posibles para el juego
$palabras = ["compiladores", "sintaxis", "semantica"];

// Seleccionamos aleatoriamente una palabra secreta de la lista
$palabra_secreta = $palabras[array_rand($palabras)];

// Establecemos el número de intentos permitidos
$intentos = 6; // Número total de intentos que el jugador tiene para adivinar

// Inicializamos la variable que muestra las letras acertadas como guiones bajos
$letras_acertadas = str_repeat('_', strlen($palabra_secreta));
// Creamos un array para almacenar las letras que ya se han utilizado
$letras_usadas = [];

// Mostramos un mensaje de bienvenida y la palabra actual (oculta)
echo "¡Bienvenido al juego del ahorcado!\nPalabra: " . implode(' ', str_split($letras_acertadas)) . "\n";

// Iniciamos el bucle principal del juego
while ($intentos > 0 && $letras_acertadas != $palabra_secreta) {
    // Mostramos los intentos restantes y las letras ya usadas
    echo "Tienes $intentos intentos restantes. Letras usadas: " . implode(', ', $letras_usadas) . "\n";

    // Pedimos al usuario que ingrese una letra
    echo "Ingresa una letra: ";
    $letra = strtolower(trim(readline())); // Convertimos la letra a minúsculas y eliminamos espacios

    // Verificamos si la entrada es válida
    if (strlen($letra) != 1 || !ctype_alpha($letra) || in_array($letra, $letras_usadas)) {
        echo "Entrada inválida o letra ya usada.\n";
        // Volvemos al inicio del bucle para pedir otra letra
        continue;
    }

    // Agregamos la letra ingresada a la lista de letras utilizadas
    $letras_usadas[] = $letra;

    // Comprobamos si la letra está en la palabra secreta
    if (strpos($palabra_secreta, $letra) !== false) {
        // Si la letra es correcta, actualizamos las letras acertadas
        for ($i = 0; $i < strlen($palabra_secreta); $i++) {
            if ($palabra_secreta[$i] == $letra) {
                // Actualizamos la letra acertada en la posición correspondiente
                $letras_acertadas[$i] = $letra;
            }
        }
    } 
    else {
        // Si la letra es incorrecta, disminuimos el número de intentos
        $intentos--;
    }
    
    // Mostramos la palabra actual con las letras acertadas hasta el momento
    echo "Palabra: " . implode(' ', str_split($letras_acertadas)) . "\n";

}
// Al finalizar, mostramos si el usuario ganó o perdió
echo $letras_acertadas == $palabra_secreta ? "¡Felicidades! Adivinaste: $palabra_secreta\n" : "Perdiste. Era: $palabra_secreta\n";
?>