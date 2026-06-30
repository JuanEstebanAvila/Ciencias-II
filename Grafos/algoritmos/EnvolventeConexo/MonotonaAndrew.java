package Grafos.algoritmos.EnvolventeConexo;

import java.util.ArrayList;
import java.util.List;

import Grafos.core.GrafoUniversal;
import Grafos.core.Nodo;

public class MonotonaAndrew {
    public static List<Nodo> ejecutar(GrafoUniversal grafo) {
        List<Nodo> pts = GeometriaUtils.getNonNullNodos(grafo.obtenerNodos());
        if (pts.size() < 3)
            return pts;

        pts.sort((a, b) -> {
            if (a.x != b.x)
                return Double.compare(a.x, b.x);
            return Double.compare(a.y, b.y);
        });

        int n = pts.size();
        List<Nodo> lower = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            while (lower.size() >= 2 && GeometriaUtils.orientacion(lower.get(lower.size() - 2),
                    lower.get(lower.size() - 1), pts.get(i)) != 2) {
                lower.remove(lower.size() - 1);
            }
            lower.add(pts.get(i));
        }

        List<Nodo> upper = new ArrayList<>();
        for (int i = n - 1; i >= 0; i--) {
            while (upper.size() >= 2 && GeometriaUtils.orientacion(upper.get(upper.size() - 2),
                    upper.get(upper.size() - 1), pts.get(i)) != 2) {
                upper.remove(upper.size() - 1);
            }
            upper.add(pts.get(i));
        }

        lower.remove(lower.size() - 1);
        upper.remove(upper.size() - 1);
        lower.addAll(upper);
        return lower;
    }
}
