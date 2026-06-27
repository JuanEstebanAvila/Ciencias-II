package Incidencia_Adyacencia;

public class TestMatrizIncidencia {
    public static void main(String[] args) {

        System.out.println("===== GRAFO DIRIGIDO =====");
        MatrizIncidencia dirigido = new MatrizIncidencia(); // por defecto: dirigido = true
        dirigido.agregarArista(new Arista(0, 1, 5.0, 0));   // 0 -> 1, peso 5.0
        dirigido.agregarArista(new Arista(1, 2, 3.5, 0));   // 1 -> 2, peso 3.5
        dirigido.agregarArista(new Arista(0, 2, 2.0, 0));   // 0 -> 2, peso 2.0
        dirigido.agregarArista(new Arista(2, 2, 4.0, 0));   // lazo en el nodo 2, peso 4.0
        dirigido.imprimirMatriz();

        System.out.println();
        System.out.println("Adyacentes de nodo 0 (dirigido): ");
        for (Nodo n : dirigido.obtenerAdyacentes(dirigido.obtenerNodo(0))) {
            System.out.println("  -> nodo " + n.id);
        }

        System.out.println();
        System.out.println("===== GRAFO NO DIRIGIDO =====");
        MatrizIncidencia noDirigido = new MatrizIncidencia(false); // no dirigido
        noDirigido.agregarArista(new Arista(0, 1, 5.0, 0));
        noDirigido.agregarArista(new Arista(1, 2, 3.5, 0));
        noDirigido.agregarArista(new Arista(0, 2, 2.0, 0));
        noDirigido.agregarArista(new Arista(2, 2, 4.0, 0)); // lazo
        noDirigido.imprimirMatriz();

        System.out.println();
        System.out.println("Adyacentes de nodo 1 (no dirigido): ");
        for (Nodo n : noDirigido.obtenerAdyacentes(noDirigido.obtenerNodo(1))) {
            System.out.println("  -> nodo " + n.id);
        }
    }
}