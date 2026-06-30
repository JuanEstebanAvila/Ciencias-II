package Grafos.algoritmos.CaminosCortos;

import java.util.Arrays;

import Grafos.core.Arista;
import Grafos.core.GrafoUniversal;

public class FloydWarshall {
    public static double[][] ejecutar(GrafoUniversal grafo) {
        int n = grafo.getCantidadNodos();
        double[][] dist = new double[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Double.POSITIVE_INFINITY);
            dist[i][i] = 0;
        }

        for (Arista a : grafo.obtenerAristas()) {
            dist[a.origen][a.destino] = Math.min(dist[a.origen][a.destino], a.peso);
            if (!grafo.isDirigido()) {
                dist[a.destino][a.origen] = Math.min(dist[a.destino][a.origen], a.peso);
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != Double.POSITIVE_INFINITY && dist[k][j] != Double.POSITIVE_INFINITY) {
                        if (dist[i][k] + dist[k][j] < dist[i][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                        }
                    }
                }
            }
        }
        return dist;
    }
}
