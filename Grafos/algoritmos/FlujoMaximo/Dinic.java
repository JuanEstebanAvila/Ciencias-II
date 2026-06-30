package Grafos.algoritmos.FlujoMaximo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import Grafos.core.Arista;
import Grafos.core.GrafoUniversal;

public class Dinic {
    public static double ejecutar(GrafoUniversal grafo, int fuente, int sumidero) {
        int n = grafo.getCantidadNodos();
        for (Arista a : grafo.obtenerAristas())
            a.flujo = 0;

        double maxFlow = 0;
        int[] level = new int[n];

        while (bfsDinic(grafo, fuente, sumidero, level)) {
            int[] ptr = new int[n];
            while (true) {
                double pushed = dfsDinic(grafo, fuente, sumidero, Double.POSITIVE_INFINITY, level, ptr);
                if (pushed == 0)
                    break;
                maxFlow += pushed;
            }
        }
        return maxFlow;
    }

    private static boolean bfsDinic(GrafoUniversal grafo, int s, int t, int[] level) {
        Arrays.fill(level, -1);
        level[s] = 0;
        Queue<Integer> q = new LinkedList<>();
        q.add(s);

        while (!q.isEmpty()) {
            int u = q.poll();
            for (Arista a : grafo.obtenerAristas()) {
                if (a.origen == u && a.capacidad - a.flujo > 0 && level[a.destino] == -1) {
                    level[a.destino] = level[u] + 1;
                    q.add(a.destino);
                }
            }
        }
        return level[t] != -1;
    }

    private static double dfsDinic(GrafoUniversal grafo, int u, int t, double push, int[] level, int[] ptr) {
        if (push == 0)
            return 0;
        if (u == t)
            return push;

        int edgeCount = 0;
        for (Arista a : grafo.obtenerAristas()) {
            if (a.origen == u) {
                if (edgeCount >= ptr[u]) {
                    int v = a.destino;
                    if (level[v] == level[u] + 1 && a.capacidad - a.flujo > 0) {
                        double tr = dfsDinic(grafo, v, t, Math.min(push, a.capacidad - a.flujo), level, ptr);
                        if (tr > 0) {
                            a.flujo += tr;
                            for (Arista rev : grafo.obtenerAristas()) {
                                if (rev.origen == v && rev.destino == u) {
                                    rev.flujo -= tr;
                                }
                            }
                            return tr;
                        }
                    }
                    ptr[u]++;
                }
                edgeCount++;
            }
        }
        return 0;
    }
}
