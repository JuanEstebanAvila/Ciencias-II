import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== INICIALIZACIÓN DE ÁRBOLES ===");
        System.out.print("Ingrese el grado mínimo (t) para el Árbol B (ej. 3): ");
        int gradoMinimoB = scanner.nextInt();
        ArbolB arbolB = new ArbolB(gradoMinimoB);

        System.out.print("Ingrese el orden (m) para el Árbol B+ (ej. 4): ");
        int ordenBMas = scanner.nextInt();
        ArbolBMas arbolBMas = new ArbolBMas(ordenBMas);

        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Insertar valor (en ambos árboles)");
            System.out.println("2. Eliminar valor (de ambos árboles)");
            System.out.println("3. Ver estructura por niveles (Árbol B y Árbol B+)");
            System.out.println("4. Buscar valor (en ambos árboles)");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el valor a insertar: ");
                    int valorInsertar = scanner.nextInt();
                    
                    System.out.println("\nInsertando " + valorInsertar + " en Árbol B...");
                    arbolB.insertar(valorInsertar);
                    
                    System.out.println("Insertando " + valorInsertar + " en Árbol B+...");
                    arbolBMas.insertar(valorInsertar);
                    
                    System.out.println("\nEstructura actual (por niveles):");
                    System.out.println("--- ÁRBOL B ---");
                    arbolB.imprimirPorNiveles();
                    System.out.println("\n--- ÁRBOL B+ ---");
                    arbolBMas.imprimirPorNiveles();
                    break;

                case 2:
                    System.out.print("Ingrese el valor a eliminar: ");
                    int valorEliminar = scanner.nextInt();
                    
                    System.out.println("\nEliminando " + valorEliminar + " de Árbol B...");
                    arbolB.eliminar(valorEliminar);
                    
                    System.out.println("Eliminando " + valorEliminar + " de Árbol B+...");
                    arbolBMas.eliminar(valorEliminar);
                    
                    System.out.println("\nEstructura actual (por niveles):");
                    System.out.println("--- ÁRBOL B ---");
                    arbolB.imprimirPorNiveles();
                    System.out.println("\n--- ÁRBOL B+ ---");
                    arbolBMas.imprimirPorNiveles();
                    break;

                case 3:
                    System.out.println("\n--- ESTRUCTURA ÁRBOL B ---");
                    arbolB.imprimirPorNiveles();
                    
                    System.out.println("\n--- ESTRUCTURA ÁRBOL B+ ---");
                    arbolBMas.imprimirPorNiveles();
                    break;

                case 4:
                    System.out.print("Ingrese el valor a buscar: ");
                    int valorBuscar = scanner.nextInt();
                    
                    System.out.println("\nBuscando " + valorBuscar + "...");
                    
                    // Buscar en Árbol B
                    NodoArbolB encontradoB = arbolB.buscar(valorBuscar);
                    if (encontradoB != null) {
                        int posClaveB = -1;
                        for (int i = 0; i < encontradoB.numeroClaves; i++) {
                            if (encontradoB.claves[i] == valorBuscar) {
                                posClaveB = i;
                                break;
                            }
                        }
                        System.out.println("✅ Árbol B: El valor " + valorBuscar + " SÍ fue encontrado.");
                        System.out.println("   - Claves en este nodo: " + java.util.Arrays.toString(java.util.Arrays.copyOf(encontradoB.claves, encontradoB.numeroClaves)));
                        System.out.println("   - Posición (índice) dentro del nodo: " + posClaveB);
                        System.out.println("   - ¿Es un nodo hoja?: " + (encontradoB.esHoja ? "Sí" : "No"));
                    } else {
                        System.out.println("❌ Árbol B: El valor " + valorBuscar + " NO existe en el árbol.");
                    }
                    
                    // Buscar en Árbol B+
                    NodoArbolBMas encontradoBMas = arbolBMas.buscar(valorBuscar);
                    if (encontradoBMas != null) {
                        int posClaveBMas = -1;
                        for (int i = 0; i < encontradoBMas.numeroClaves; i++) {
                            if (encontradoBMas.claves[i] == valorBuscar) {
                                posClaveBMas = i;
                                break;
                            }
                        }
                        System.out.println("✅ Árbol B+: El valor " + valorBuscar + " SÍ fue encontrado.");
                        System.out.println("   - Claves en este nodo hoja: " + java.util.Arrays.toString(java.util.Arrays.copyOf(encontradoBMas.claves, encontradoBMas.numeroClaves)));
                        System.out.println("   - Posición (índice) dentro de la hoja: " + posClaveBMas);
                    } else {
                        System.out.println("❌ Árbol B+: El valor " + valorBuscar + " NO existe en el árbol.");
                    }
                    break;

                case 0:
                    salir = true;
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
        
        scanner.close();
    }
}
