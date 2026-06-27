package Incidencia_Adyacencia;

import java.util.ArrayList;
import java.util.List;

// Clase para representar un grafo usando matriz de incidencia.
// Acá las filas son los nodos y las columnas son las aristas (no nodos x nodos
// como en la matriz de adyacencia, eso es otra cosa).
//
// Como las aristas pueden tener cualquier peso (no solo 1 o 0), uso esta regla:
// - si el nodo es el ORIGEN de la arista, pongo +peso
// - si el nodo es el DESTINO de la arista, pongo -peso
// - si el nodo no tiene nada que ver con esa arista, queda 0
//
// Ojo con los lazos (cuando origen y destino son el mismo nodo): si pongo
// +peso y -peso en la misma celda se cancelan y queda 0, y se pierde el
// dato de que la arista existe. Por eso en ese caso dejo 2*peso nomas,
// para que no se pierda la info (es una solucion que vi por ahi, no se si
// hay una mejor pero a mi me funciona).
//
// También dejé la opción de que el grafo sea no dirigido (con el booleano
// "dirigido" en el constructor). Ahí ya no hay signos, simplemente pongo
// +peso en los dos extremos de la arista, porque no hay "salida" ni
// "entrada", solo los dos nodos que toca la arista.
public class MatrizIncidencia implements GrafoUniversal {

    private List<Nodo> nodos = new ArrayList<>();
    private List<Arista> aristas = new ArrayList<>();

    // fila i = nodo i, columna j = arista j (en el orden que están en la lista aristas)
    private double[][] matriz = new double[0][0];

    // true = dirigido, false = no dirigido
    private final boolean dirigido;

    // por defecto lo dejo dirigido porque ListaAdyacencia también funciona como dirigido
    public MatrizIncidencia() {
        this(true);
    }

    public MatrizIncidencia(boolean dirigido) {
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
        for (Arista a : aristas) {
            if (a.origen == nodo.id) {
                // si el nodo es origen, el destino siempre es adyacente (en los dos modos)
                adyacentes.add(obtenerNodo(a.destino));
            } else if (!dirigido && a.destino == nodo.id) {
                // esto solo aplica si es no dirigido: también cuenta el otro sentido
                adyacentes.add(obtenerNodo(a.origen));
            }
        }
        return adyacentes;
    }

    @Override
    public List<Arista> obtenerAristasDeNodo(Nodo nodo) {
        List<Arista> aristasDeNodo = new ArrayList<>();
        for (Arista a : aristas) {
            if (a.origen == nodo.id || a.destino == nodo.id) {
                aristasDeNodo.add(a);
            }
        }
        return aristasDeNodo;
    }

    @Override
    public Nodo obtenerNodo(int id) {
        for (Nodo n : nodos) {
            if (n.id == id)
                return n;
        }
        return null;
    }

    @Override
    public int getCantidadNodos() {
        return nodos.size();
    }

    // devuelve la matriz tal cual está guardada (filas = nodos, columnas = aristas)
    public double[][] obtenerMatriz() {
        return matriz;
    }

    // esto recalcula toda la matriz de nuevo. Lo llamo cada vez que agrego
    // un nodo o una arista para que la matriz nunca quede desactualizada
    private void reconstruirMatriz() {
        int filas = nodos.size();
        int columnas = aristas.size();
        matriz = new double[filas][columnas];

        for (int j = 0; j < columnas; j++) {
            Arista a = aristas.get(j);
            int filaOrigen = indiceDeNodo(a.origen);
            int filaDestino = indiceDeNodo(a.destino);

            if (filaOrigen == -1 || filaDestino == -1) {
                continue; // esto no debería pasar si siempre se agrega con agregarArista, pero por si acaso
            }

            if (a.origen == a.destino) {
                // caso lazo, ver la explicación arriba en los comentarios de la clase
                matriz[filaOrigen][j] = 2 * a.peso;
            } else if (dirigido) {
                // dirigido: sale con + del origen, entra con - al destino
                matriz[filaOrigen][j] += a.peso;
                matriz[filaDestino][j] -= a.peso;
            } else {
                // no dirigido: + en los dos lados, sin signos raros
                matriz[filaOrigen][j] += a.peso;
                matriz[filaDestino][j] += a.peso;
            }
        }
    }

    // busca en qué posición (fila) está guardado el nodo con ese id
    // (lo necesito porque el id del nodo no siempre es igual a su posición en la lista)
    private int indiceDeNodo(int id) {
        for (int i = 0; i < nodos.size(); i++) {
            if (nodos.get(i).id == id) {
                return i;
            }
        }
        return -1;
    }

    private void asegurarNodo(int id) {
        while (nodos.size() <= id) {
            nodos.add(new Nodo(nodos.size(), 50 + Math.random() * 650, 50 + Math.random() * 450));
        }
        // como acá se agregan nodos sin pasar por agregarNodo(), si cambió la
        // cantidad de filas hay que reconstruir la matriz también
        reconstruirMatriz();
    }

}