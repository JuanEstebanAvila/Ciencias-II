package Grafos.algoritmos.ArbolRecubrimiento;

import java.util.ArrayList;
import java.util.List;

import Grafos.core.Arista;
import Grafos.core.GrafoUniversal;

public class Kruskal {
    public static List<Arista> ejecutar(GrafoUniversal grafo) {
        List<Arista> mst = new ArrayList<>();
        List<Arista> edges = new ArrayList<>(grafo.obtenerAristas());
        edges.sort((a, b) -> Double.compare(a.peso, b.peso));

        int n = grafo.getCantidadNodos();
        int[] parent = new int[n];
        for (int i = 0; i < n; i++)
            parent[i] = i;

        for (Arista a : edges) {
            int rootX = findSet(parent, a.origen);
            int rootY = findSet(parent, a.destino);
            if (rootX != rootY) {
                mst.add(a);
                parent[rootX] = rootY;
            }
        }
        return mst;
    }

    private static int findSet(int[] parent, int i) {
        if (parent[i] == i)
            return i;
        return parent[i] = findSet(parent, parent[i]);
    }
}
