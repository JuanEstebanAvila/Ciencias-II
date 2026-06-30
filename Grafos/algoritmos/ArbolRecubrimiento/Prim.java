package Grafos.algoritmos.ArbolRecubrimiento;

import java.util.ArrayList;
import java.util.List;

import Grafos.core.Arista;
import Grafos.core.GrafoUniversal;

public class Prim {
    public static List<Arista> ejecutar(GrafoUniversal grafo) {
        List<Arista> mst = new ArrayList<>();
        int n = grafo.getCantidadNodos();
        if (n == 0)
            return mst;

        boolean[] inMST = new boolean[n];
        inMST[0] = true;

        for (int count = 1; count < n; count++) {
            double minWeight = Double.POSITIVE_INFINITY;
            Arista nextEdge = null;

            for (Arista a : grafo.obtenerAristas()) {
                if (inMST[a.origen] && !inMST[a.destino]) {
                    if (a.peso < minWeight) {
                        minWeight = a.peso;
                        nextEdge = a;
                    }
                } else if (inMST[a.destino] && !inMST[a.origen]) {
                    if (a.peso < minWeight) {
                        minWeight = a.peso;
                        nextEdge = a;
                    }
                }
            }

            if (nextEdge != null) {
                mst.add(nextEdge);
                inMST[nextEdge.origen] = true;
                inMST[nextEdge.destino] = true;
            } else {
                break;
            }
        }
        return mst;
    }
}
