package Incidencia_Adyacencia;

import java.util.ArrayList;
import java.util.List;

// Matriz de adyacencia n x n: la celda [i][j] guarda el peso de la arista i->j, o 0 si no hay.
// En grafos no dirigidos, [i][j] y [j][i] siempre tienen el mismo valor.
public class MatrizAdyacencia implements GrafoUniversal {

    private List<Nodo> nodos = new ArrayList<>();
    private List<Arista> aristas = new ArrayList<>();
    private double[][] matriz = new double[0][0];
    private final boolean dirigido;

    public MatrizAdyacencia() {
        this(true);
    }

    public MatrizAdyacencia(boolean dirigido) {
        this.dirigido = dirigido;
    }

    @Override
    public void agregarNodo(Nodo nodo) {
        if (obtenerNodo(nodo.id) == null) {
            nodos.add(nodo);
            reconstruirMatriz();
        }
    }

    @Override
    public void agregarArista(Arista arista) {
        asegurarNodo(arista.origen);
        asegurarNodo(arista.destino);
        aristas.add(arista);
        reconstruirMatriz();
    }

    @Override
    public List<Nodo> obtenerNodos() {
        return nodos;
    }

    @Override
    public List<Arista> obtenerAristas() {
        return aristas;
    }

    @Override
    public List<Nodo> obtenerAdyacentes(Nodo nodo) {
        List<Nodo> adyacentes = new ArrayList<>();
        int fila = indiceDeNodo(nodo.id);
        if (fila == -1) return adyacentes;

        for (int j = 0; j < nodos.size(); j++) {
            if (matriz[fila][j] != 0) {
                adyacentes.add(nodos.get(j));
            }
        }
        return adyacentes;
    }

    @Override
    public List<Arista> obtenerAristasDeNodo(Nodo nodo) {
        List<Arista> aristasDeNodo = new ArrayList<>();
        for (Arista a : aristas) {
            if (a.origen == nodo.id || (!dirigido && a.destino == nodo.id)) {
                aristasDeNodo.add(a);
            } else if (dirigido && a.destino == nodo.id) {
                aristasDeNodo.add(a);
            }
        }
        return aristasDeNodo;
    }

    @Override
    public Nodo obtenerNodo(int id) {
        for (Nodo n : nodos) {
            if (n.id == id) return n;
        }
        return null;
    }

    @Override
    public int getCantidadNodos() {
        return nodos.size();
    }

    public double[][] obtenerMatriz() {
        return matriz;
    }

    // devuelve el peso de la arista entre dos nodos, o 0 si no existe
    public double getPeso(int idOrigen, int idDestino) {
        int fila = indiceDeNodo(idOrigen);
        int col = indiceDeNodo(idDestino);
        if (fila == -1 || col == -1) return 0;
        return matriz[fila][col];
    }

    // reconstruyo la matriz entera cada vez que cambia algo, más simple que actualizar celda por celda
    private void reconstruirMatriz() {
        int n = nodos.size();
        matriz = new double[n][n];

        for (Arista a : aristas) {
            int fila = indiceDeNodo(a.origen);
            int col = indiceDeNodo(a.destino);
            if (fila == -1 || col == -1) continue;

            matriz[fila][col] += a.peso;
            if (!dirigido && fila != col) {
                matriz[col][fila] += a.peso;
            }
        }
    }

    // el id del nodo no siempre coincide con su posición en la lista
    private int indiceDeNodo(int id) {
        for (int i = 0; i < nodos.size(); i++) {
            if (nodos.get(i).id == id) return i;
        }
        return -1;
    }

    // si al agregar una arista algún nodo no existe todavía, lo creo con coordenadas aleatorias
    private void asegurarNodo(int id) {
        while (nodos.size() <= id) {
            nodos.add(new Nodo(nodos.size(), 50 + Math.random() * 650, 50 + Math.random() * 450));
        }
        reconstruirMatriz();
    }

}
