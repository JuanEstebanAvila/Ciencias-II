package Incidencia_Adyacencia;

import java.util.List;

public interface Grafo {
    void agregarArista(int origen, int destino, int peso, boolean dirigido);

    List<Arista> obtenerAdyacentes(int nodo);
}
