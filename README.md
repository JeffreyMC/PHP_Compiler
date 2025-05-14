# PHP_Compiler
Compilador del lenguaje PHP hecho en Java

## Software utilizado

* JDK 23
* NetBeans 24

## INSTRUCCIONES DE USO

1. Al abrir la carpeta del proyecto, hay que dirigirse a la carpeta llamada "dist".

2. Estando dentro de la carpeta "dist", hay que dar click derecho en un lugar vacío y luego seleccionar la opción de "Abrir en terminal".

3. Dentro de la terminal se debe ejecutar el siguiente comando:

 java -jar .\Proyecto2_Compiladores.jar .\[nombre del archivo].php

Donde "nombre de archivo", es el archivo con extensión .php.

## ACLARACIONES DEL PROGRAMA

1. Si el archivo no contiene errores, se mostrará un aviso en la terminal e igualmente en el archivo de log generado. Del mismo modo, si el archivo contiene errores, los errores serán mostrados tanto en la terminal como el archivo log generado, indicando al final del archivo el tipo de error que ocurrió y el número de línea en el que se detectó.

2. Las tabulaciones no se validan si se edita el archivo desde VS Code, pero si se edita desde un editor como Notepad, sí los detecta. Parece que VS Code convierte las tabulaciones en espacios.
