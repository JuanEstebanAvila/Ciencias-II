package Incidencia_Adyacencia;

import java.util.List;

public interface GrafoUniversal {
    /**
     * Agrega un nuevo nodo al grafo.
     */
    void agregarNodo(Nodo nodo);

    /**
     * Agrega una nueva arista (conexión) entre dos nodos del grafo.
     */
    void agregarArista(Arista arista);

    /**
     * Obtiene la lista de todos los nodos presentes en el grafo.
     */
    List<Nodo> obtenerNodos();

    /**
     * Obtiene la lista de todas las aristas presentes en el grafo.
     */
    List<Arista> obtenerAristas();

    /**
     * Obtiene los nodos adyacentes (vecinos) de un nodo específico.
     */
    List<Nodo> obtenerAdyacentes(Nodo nodo);

    /**
     * Obtiene las aristas conectadas a un nodo específico.
     */
    List<Arista> obtenerAristasDeNodo(Nodo nodo);

    /**
     * Obtiene un nodo por su identificador numérico.
     */
    Nodo obtenerNodo(int id);

    /**
     * Retorna la cantidad total de nodos en el grafo.
     */
    int getCantidadNodos();
}
