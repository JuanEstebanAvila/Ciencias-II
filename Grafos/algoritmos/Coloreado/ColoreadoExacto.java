package Grafos.algoritmos.Coloreado;

import java.util.ArrayList;
import java.util.List;

import Grafos.core.Arista;
import Grafos.core.GrafoUniversal;
import Grafos.core.Nodo;

public class ColoreadoExacto {
    public static void ejecutar(GrafoUniversal grafo) {
        List<Nodo> validNodos = getNonNullNodos(grafo.obtenerNodos());
        for (Nodo node : validNodos)
            node.color = -1;

        int ColoredCount = 0;
        while (ColoredCount < validNodos.size()) {
            Nodo select = null;
            int maxSat = -1;
            int maxDegree = -1;

            for (Nodo node : validNodos) {
                if (node.color != -1)
                    continue;
                int sat = getSaturationDegree(grafo, node);
                int deg = getDegree(grafo, node);

                if (sat > maxSat || (sat == maxSat && deg > maxDegree)) {
                    maxSat = sat;
                    maxDegree = deg;
                    select = node;
                }
            }

            if (select == null)
                break;

            boolean[] colorUsado = new boolean[validNodos.size() + 1];
            for (Arista a : grafo.obtenerAristasDeNodo(select)) {
                if (a.origen == select.id) {
                    Nodo neighbor = grafo.obtenerNodo(a.destino);
                    if (neighbor != null && neighbor.color != -1) {
                        colorUsado[neighbor.color] = true;
                    }
                } else if (a.destino == select.id) {
                    Nodo neighbor = grafo.obtenerNodo(a.origen);
                    if (neighbor != null && neighbor.color != -1) {
                        colorUsado[neighbor.color] = true;
                    }
                }
            }

            int c;
            for (c = 0; c < colorUsado.length; c++) {
                if (!colorUsado[c])
                    break;
            }
            select.color = c;
            ColoredCount++;
        }
    }

    private static int getSaturationDegree(GrafoUniversal grafo, Nodo node) {
        List<Integer> colors = new ArrayList<>();
        for (Arista a : grafo.obtenerAristasDeNodo(node)) {
            if (a.origen == node.id) {
                Nodo neighbor = grafo.obtenerNodo(a.destino);
                if (neighbor != null && neighbor.color != -1 && !colors.contains(neighbor.color)) {
                    colors.add(neighbor.color);
                }
            } else if (a.destino == node.id) {
                Nodo neighbor = grafo.obtenerNodo(a.origen);
                if (neighbor != null && neighbor.color != -1 && !colors.contains(neighbor.color)) {
                    colors.add(neighbor.color);
                }
            }
        }
        return colors.size();
    }

    private static int getDegree(GrafoUniversal grafo, Nodo node) {
        int degree = 0;
        for (Arista a : grafo.obtenerAristasDeNodo(node)) {
            if (a.origen == node.id || a.destino == node.id) {
                degree++;
            }
        }
        return degree;
    }

    private static List<Nodo> getNonNullNodos(List<Nodo> input) {
        List<Nodo> result = new ArrayList<>();
        for (Nodo n : input) {
            if (n != null)
                result.add(n);
        }
        return result;
    }
}
