package Grafos.algoritmos.Coloreado;

import Grafos.core.Arista;
import Grafos.core.GrafoUniversal;
import Grafos.core.Nodo;

public class ColoreadoVoraz {
    public static void ejecutar(GrafoUniversal grafo) {
        int n = grafo.getCantidadNodos();
        for (Nodo node : grafo.obtenerNodos()) {
            if (node != null)
                node.color = -1;
        }

        for (Nodo node : grafo.obtenerNodos()) {
            if (node == null)
                continue;
            boolean[] colorUsado = new boolean[n];
            for (Arista a : grafo.obtenerAristasDeNodo(node)) {
                if (a.origen == node.id) {
                    Nodo neighbor = grafo.obtenerNodo(a.destino);
                    if (neighbor != null && neighbor.color != -1) {
                        colorUsado[neighbor.color] = true;
                    }
                } else if (a.destino == node.id) {
                    Nodo neighbor = grafo.obtenerNodo(a.origen);
                    if (neighbor != null && neighbor.color != -1) {
                        colorUsado[neighbor.color] = true;
                    }
                }
            }
            int c;
            for (c = 0; c < n; c++) {
                if (!colorUsado[c])
                    break;
            }
            node.color = c;
        }
    }
}
