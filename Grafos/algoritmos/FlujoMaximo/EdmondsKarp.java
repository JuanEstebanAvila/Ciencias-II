package Grafos.algoritmos.FlujoMaximo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import Grafos.core.Arista;
import Grafos.core.GrafoUniversal;

public class EdmondsKarp {
    public static double ejecutar(GrafoUniversal grafo, int fuente, int sumidero) {
        int n = grafo.getCantidadNodos();
        for (Arista a : grafo.obtenerAristas())
            a.flujo = 0;

        double maxFlow = 0;
        while (true) {
            Arista[] parentEdge = new Arista[n];
            int[] parent = new int[n];
            Arrays.fill(parent, -1);
            parent[fuente] = fuente;

            Queue<Integer> q = new LinkedList<>();
            q.add(fuente);

            while (!q.isEmpty()) {
                int u = q.poll();
                if (u == sumidero)
                    break;

                for (Arista a : grafo.obtenerAristas()) {
                    if (a.origen == u && parent[a.destino] == -1) {
                        double capResidual = a.capacidad - a.flujo;
                        if (capResidual > 0) {
                            parent[a.destino] = u;
                            parentEdge[a.destino] = a;
                            q.add(a.destino);
                        }
                    }
                }
            }

            if (parent[sumidero] == -1)
                break;

            double flow = Double.POSITIVE_INFINITY;
            int curr = sumidero;
            while (curr != fuente) {
                Arista a = parentEdge[curr];
                flow = Math.min(flow, a.capacidad - a.flujo);
                curr = parent[curr];
            }

            curr = sumidero;
            while (curr != fuente) {
                Arista a = parentEdge[curr];
                a.flujo += flow;
                for (Arista rev : grafo.obtenerAristas()) {
                    if (rev.origen == a.destino && rev.destino == a.origen) {
                        rev.flujo -= flow;
                    }
                }
                curr = parent[curr];
            }
            maxFlow += flow;
        }
        return maxFlow;
    }
}
