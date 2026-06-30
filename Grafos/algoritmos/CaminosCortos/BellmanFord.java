package Grafos.algoritmos.CaminosCortos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Grafos.core.Arista;
import Grafos.core.GrafoUniversal;

public class BellmanFord {
    public static List<Arista> ejecutar(GrafoUniversal grafo, int origen, int destino) throws Exception {
        int n = grafo.getCantidadNodos();
        double[] dist = new double[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);

        if (origen >= 0 && origen < n) {
            dist[origen] = 0;
        } else {
            return new ArrayList<>();
        }

        for (int i = 1; i < n; i++) {
            for (Arista a : grafo.obtenerAristas()) {
                if (dist[a.origen] != Double.POSITIVE_INFINITY && dist[a.origen] + a.peso < dist[a.destino]) {
                    dist[a.destino] = dist[a.origen] + a.peso;
                    parent[a.destino] = a.origen;
                }
                if (!grafo.isDirigido() && dist[a.destino] != Double.POSITIVE_INFINITY && dist[a.destino] + a.peso < dist[a.origen]) {
                    dist[a.origen] = dist[a.destino] + a.peso;
                    parent[a.origen] = a.destino;
                }
            }
        }

        // Detectar ciclos de peso negativo
        for (Arista a : grafo.obtenerAristas()) {
            if (dist[a.origen] != Double.POSITIVE_INFINITY && dist[a.origen] + a.peso < dist[a.destino]) {
                throw new Exception("¡El grafo contiene un ciclo de peso negativo!");
            }
            if (!grafo.isDirigido() && dist[a.destino] != Double.POSITIVE_INFINITY && dist[a.destino] + a.peso < dist[a.origen]) {
                throw new Exception("¡El grafo contiene un ciclo de peso negativo!");
            }
        }

        return Dijkstra.reconstruirCamino(grafo, parent, destino);
    }
}
