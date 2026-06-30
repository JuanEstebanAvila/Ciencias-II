package Grafos.algoritmos.FlujoMaximo;

import Grafos.core.Arista;
import Grafos.core.GrafoUniversal;

public class FordFulkerson {
    public static double ejecutar(GrafoUniversal grafo, int fuente, int sumidero) {
        int n = grafo.getCantidadNodos();
        for (Arista a : grafo.obtenerAristas())
            a.flujo = 0;

        double maxFlow = 0;
        while (true) {
            boolean[] visitado = new boolean[n];
            double flow = dfsAugment(grafo, fuente, sumidero, Double.POSITIVE_INFINITY, visitado);
            if (flow <= 0)
                break;
            maxFlow += flow;
        }
        return maxFlow;
    }

    private static double dfsAugment(GrafoUniversal grafo, int u, int t, double flow, boolean[] visitado) {
        if (u == t)
            return flow;
        visitado[u] = true;

        for (Arista a : grafo.obtenerAristas()) {
            if (a.origen == u && !visitado[a.destino]) {
                double capResidual = a.capacidad - a.flujo;
                if (capResidual > 0) {
                    double push = dfsAugment(grafo, a.destino, t, Math.min(flow, capResidual), visitado);
                    if (push > 0) {
                        a.flujo += push;
                        for (Arista rev : grafo.obtenerAristas()) {
                            if (rev.origen == a.destino && rev.destino == u) {
                                rev.flujo -= push;
                            }
                        }
                        return push;
                    }
                }
            }
        }
        return 0;
    }
}
