package Grafos.algoritmos.EnvolventeConexo;

import java.util.ArrayList;
import java.util.List;

import Grafos.core.GrafoUniversal;
import Grafos.core.Nodo;

public class JarvisMarch {
    public static List<Nodo> ejecutar(GrafoUniversal grafo) {
        List<Nodo> pts = GeometriaUtils.getNonNullNodos(grafo.obtenerNodos());
        List<Nodo> hull = new ArrayList<>();
        if (pts.size() < 3)
            return pts;

        int l = 0;
        for (int i = 1; i < pts.size(); i++) {
            if (pts.get(i).x < pts.get(l).x) {
                l = i;
            }
        }

        int p = l, q;
        do {
            hull.add(pts.get(p));
            q = (p + 1) % pts.size();
            for (int i = 0; i < pts.size(); i++) {
                if (GeometriaUtils.orientacion(pts.get(p), pts.get(i), pts.get(q)) == 2) {
                    q = i;
                }
            }
            p = q;
        } while (p != l);

        return hull;
    }
}
