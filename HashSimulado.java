package Conteo;

import java.util.Scanner;

public class HashSimulado {

    public static int generarHash(String palabra) {
        int semilla = 3;
        int a = 17;
        int c = 9;
        int w = 5;
        int m = 97;

        int x = semilla;
        for (int n = 1; n <= palabra.length(); n++) {
            int valorLetra = (int) palabra.charAt(n - 1);
            x = (a * x + c + n * w + valorLetra) % m;
        }

        return x;
    }

    public static void main(String[] args) {

        System.out.println("Digite la palabra que quiere cifrar: ");
        Scanner palabrasc = new Scanner(System.in);
        String palabraSecreta = palabrasc.nextLine().toUpperCase();

        int hashGenerado = generarHash(palabraSecreta);
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
            int hashCalculado = generarHash(intento);
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
