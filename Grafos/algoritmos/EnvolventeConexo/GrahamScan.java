package Grafos.algoritmos.EnvolventeConexo;

import java.util.ArrayList;
import java.util.List;

import Grafos.core.GrafoUniversal;
import Grafos.core.Nodo;

public class GrahamScan {
    public static List<Nodo> ejecutar(GrafoUniversal grafo) {
        List<Nodo> pts = GeometriaUtils.getNonNullNodos(grafo.obtenerNodos());
        if (pts.size() < 3)
            return pts;

        int minY = 0;
        for (int i = 1; i < pts.size(); i++) {
            if (pts.get(i).y < pts.get(minY).y || (pts.get(i).y == pts.get(minY).y && pts.get(i).x < pts.get(minY).x)) {
                minY = i;
            }
        }
        Nodo p0 = pts.get(minY);
        pts.remove(minY);

        pts.sort((p1, p2) -> {
            double angle1 = Math.atan2(p1.y - p0.y, p1.x - p0.x);
            double angle2 = Math.atan2(p2.y - p0.y, p2.x - p0.x);
            if (angle1 < angle2)
                return -1;
            if (angle1 > angle2)
                return 1;
            double d1 = Math.pow(p1.x - p0.x, 2) + Math.pow(p1.y - p0.y, 2);
            double d2 = Math.pow(p2.x - p0.x, 2) + Math.pow(p2.y - p0.y, 2);
            return Double.compare(d1, d2);
        });

        List<Nodo> stack = new ArrayList<>();
        stack.add(p0);
        stack.add(pts.get(0));
        stack.add(pts.get(1));

        for (int i = 2; i < pts.size(); i++) {
            while (stack.size() >= 2 && GeometriaUtils.orientacion(stack.get(stack.size() - 2),
                    stack.get(stack.size() - 1), pts.get(i)) != 2) {
                stack.remove(stack.size() - 1);
            }
            stack.add(pts.get(i));
        }
        return stack;
    }
}
