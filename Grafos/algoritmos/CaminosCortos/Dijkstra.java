package Grafos.algoritmos.CaminosCortos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Grafos.core.Arista;
import Grafos.core.GrafoUniversal;
import Grafos.core.Nodo;

public class Dijkstra {
    public static List<Arista> ejecutar(GrafoUniversal grafo, int origen, int destino) {
        int n = grafo.getCantidadNodos();
        double[] dist = new double[n];
        int[] parent = new int[n];
        boolean[] visitado = new boolean[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);

        if (origen >= 0 && origen < n) {
            dist[origen] = 0;
        } else {
            return new ArrayList<>();
        }

        for (int count = 0; count < n; count++) {
            double min = Double.POSITIVE_INFINITY;
            int u = -1;
            for (int i = 0; i < n; i++) {
                if (!visitado[i] && dist[i] < min) {
                    min = dist[i];
                    u = i;
                }
            }
            if (u == -1)
                break;
            visitado[u] = true;

            Nodo nodoU = grafo.obtenerNodo(u);
            if (nodoU != null) {
                for (Arista a : grafo.obtenerAristasDeNodo(nodoU)) {
                    int v = -1;
                    if (a.origen == u) v = a.destino;
                    else if (!grafo.isDirigido() && a.destino == u) v = a.origen;

                    if (v != -1) {
                        if (!visitado[v] && dist[u] + a.peso < dist[v]) {
                            dist[v] = dist[u] + a.peso;
                            parent[v] = u;
                        }
                    }
                }
            }
        }

        return reconstruirCamino(grafo, parent, destino);
    }

    public static List<Arista> reconstruirCamino(GrafoUniversal grafo, int[] parent, int destino) {
        List<Arista> camino = new ArrayList<>();
        int curr = destino;
        while (curr != -1 && parent[curr] != -1) {
            int p = parent[curr];
            Arista found = null;
            double minPeso = Double.POSITIVE_INFINITY;
            for (Arista a : grafo.obtenerAristas()) {
                if ((a.origen == p && a.destino == curr) || (!grafo.isDirigido() && a.destino == p && a.origen == curr)) {
                    if (a.peso < minPeso) {
                        minPeso = a.peso;
                        found = a;
                    }
                }
            }
            if (found != null) {
                camino.add(0, found);
            }
            curr = p;
        }
        return camino;
    }
}
