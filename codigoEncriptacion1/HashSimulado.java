package Conteo;

import java.util.Scanner;

public class HashSimulado {

    public static long generarHash(String palabra, String sal) {
        long semilla = 3;
        long a = 31;
        long m = 1000000007L;

        String palabraCombinada = palabra + sal;

        long x = semilla;
        for (int n = 1; n <= palabraCombinada.length(); n++) {
            int valorLetra = (int) palabraCombinada.charAt(n - 1);
            x = ((a * x) ^ valorLetra) % m; // Se modifico la ecuacion para que fuera mas dificil de adivinar con el XOR
        }

        return x;
    }

    public static void main(String[] args) {

        System.out.println("Digite la palabra que quiere cifrar: ");
        Scanner palabrasc = new Scanner(System.in);
        String palabraSecreta = palabrasc.nextLine().toUpperCase();

        String sal = "Z9xY";

        long hashGenerado = generarHash(palabraSecreta, sal);
        System.out.println("codigo: " + hashGenerado);

        System.out.println("¡Iniciando Ataque de Diccionario!");
        System.out.println("Objetivo: Encontrar la palabra detrás del código " + hashGenerado + "\n");

        // crear diccionario de contraseñas vulnerables
        String[] diccionario = {
                "HOLA", "SISTEMAS", "COMPUTACION", "JAVA",
                "PROGRAMACION", "CIENCIAS", "12345", "ADMIN", "SECRETO"
        };

        boolean encontrado = false;

        for (String intento : diccionario) {
            long hashCalculado = generarHash(intento, sal);
            System.out.println("Probando: " + intento + " -> Código resultante: " + hashCalculado);

            // Si los códigos coinciden, se encontro la palabra
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
