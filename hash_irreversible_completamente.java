package Conteo;

import java.util.Scanner;

// aun no evita las colisiones

public class HashSimulado {

    public static long generarHash(String palabra, String sal) {

        String entrada = palabra + sal;

        long estado = 0x12345678L;

        for (int i = 0; i < entrada.length(); i++) {

            int ascii = entrada.charAt(i);

            // Pérdida de información:
            // Se reemplaza el ASCII por la suma de sus dígitos.
            int sumaDigitos = 0;
            int temp = ascii;

            while (temp > 0) {
                sumaDigitos += temp % 10;
                temp /= 10;
            }

            // Mezcla del estado
            estado = (estado * 131) ^ sumaDigitos;

            // Rotación para dispersar los bits
            estado = Long.rotateLeft(estado, 5);

            // Segunda mezcla
            estado ^= (estado >>> 7);
        }

        return Math.abs(estado);
    }

    public static void main(String[] args) {

        System.out.println("Digite la palabra que quiere cifrar:");
        Scanner palabrasc = new Scanner(System.in);

        String palabraSecreta = palabrasc.nextLine().toUpperCase();

        String sal = "Z9xY";

        long hashGenerado = generarHash(palabraSecreta, sal);

        System.out.println("Código: " + hashGenerado);

        System.out.println("\n¡Iniciando Ataque de Diccionario!");
        System.out.println("Objetivo: Encontrar la palabra detrás del código " + hashGenerado + "\n");

        String[] diccionario = {
                "HOLA", "SISTEMAS", "COMPUTACION", "JAVA",
                "PROGRAMACION", "CIENCIAS", "12345", "ADMIN", "SECRETO"
        };

        boolean encontrado = false;

        for (String intento : diccionario) {

            long hashCalculado = generarHash(intento, sal);

            System.out.println(
                    "Probando: " + intento +
                    " -> Código resultante: " + hashCalculado);

            if (hashCalculado == hashGenerado) {

                System.out.println("\n¡BINGO! SEGURIDAD VULNERADA.");
                System.out.println("La palabra original es: " + intento);

                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("\nAtaque fallido. La palabra segura no estaba en el diccionario del atacante.");
        }

        palabrasc.close();
    }
}
