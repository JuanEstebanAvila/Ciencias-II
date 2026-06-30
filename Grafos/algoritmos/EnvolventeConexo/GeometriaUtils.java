package Grafos.algoritmos.EnvolventeConexo;

import java.util.ArrayList;
import java.util.List;

import Grafos.core.Nodo;

public class GeometriaUtils {
    public static int orientacion(Nodo p, Nodo q, Nodo r) {
        double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0)
            return 0;
        return (val > 0) ? 1 : 2; // 1: horario, 2: antihorario
    }

    public static int puntoLado(Nodo A, Nodo B, Nodo P) {
        double cp = (B.x - A.x) * (P.y - A.y) - (B.y - A.y) * (P.x - A.x);
        if (cp > 0)
            return 1;
        if (cp < 0)
            return -1;
        return 0;
    }

    public static double distPuntoLinea(Nodo A, Nodo B, Nodo P) {
        return Math.abs((B.x - A.x) * (P.y - A.y) - (B.y - A.y) * (P.x - A.x));
    }

    public static List<Nodo> getNonNullNodos(List<Nodo> input) {
        List<Nodo> result = new ArrayList<>();
        for (Nodo n : input) {
            if (n != null)
                result.add(n);
        }
        return result;
    }
}
