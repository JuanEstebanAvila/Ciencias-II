package Grafos.algoritmos.EnvolventeConexo;

import java.util.ArrayList;
import java.util.List;

import Grafos.core.GrafoUniversal;
import Grafos.core.Nodo;

public class QuickHull {
    public static List<Nodo> ejecutar(GrafoUniversal grafo) {
        List<Nodo> pts = GeometriaUtils.getNonNullNodos(grafo.obtenerNodos());
        List<Nodo> hull = new ArrayList<>();
        if (pts.size() < 3)
            return pts;

        int minX = 0, maxX = 0;
        for (int i = 1; i < pts.size(); i++) {
            if (pts.get(i).x < pts.get(minX).x)
                minX = i;
            if (pts.get(i).x > pts.get(maxX).x)
                maxX = i;
        }

        Nodo A = pts.get(minX);
        Nodo B = pts.get(maxX);
        hull.add(A);
        hull.add(B);
        pts.remove(A);
        pts.remove(B);

        List<Nodo> leftSet = new ArrayList<>();
        List<Nodo> rightSet = new ArrayList<>();

        for (Nodo p : pts) {
            if (GeometriaUtils.puntoLado(A, B, p) == 1)
                leftSet.add(p);
            else if (GeometriaUtils.puntoLado(A, B, p) == -1)
                rightSet.add(p);
        }

        hullPart(A, B, leftSet, hull);
        hullPart(B, A, rightSet, hull);

        hull.sort((p1, p2) -> {
            double cx = 0, cy = 0;
            for (Nodo n : hull) {
                cx += n.x;
                cy += n.y;
            }
            cx /= hull.size();
            cy /= hull.size();
            return Double.compare(Math.atan2(p1.y - cy, p1.x - cx), Math.atan2(p2.y - cy, p2.x - cx));
        });

        return hull;
    }

    private static void hullPart(Nodo A, Nodo B, List<Nodo> set, List<Nodo> hull) {
        if (set.isEmpty())
            return;
        int insertPosition = hull.indexOf(B);
        double maxDist = -1;
        Nodo furthestPoint = null;
        for (Nodo p : set) {
            double distance = GeometriaUtils.distPuntoLinea(A, B, p);
            if (distance > maxDist) {
                maxDist = distance;
                furthestPoint = p;
            }
        }
        if (furthestPoint == null)
            return;
        set.remove(furthestPoint);
        hull.add(insertPosition, furthestPoint);

        List<Nodo> A_FP = new ArrayList<>();
        List<Nodo> FP_B = new ArrayList<>();
        for (Nodo p : set) {
            if (GeometriaUtils.puntoLado(A, furthestPoint, p) == 1)
                A_FP.add(p);
            else if (GeometriaUtils.puntoLado(furthestPoint, B, p) == 1)
                FP_B.add(p);
        }
        hullPart(A, furthestPoint, A_FP, hull);
        hullPart(furthestPoint, B, FP_B, hull);
    }
}
