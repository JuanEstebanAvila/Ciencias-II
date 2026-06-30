package Grafos.core;

import java.util.ArrayList;
import java.util.List;

public class MatrizIncidencia implements GrafoUniversal {
    private List<Nodo> nodos = new ArrayList<>();
    private List<Arista> aristas = new ArrayList<>();
    private boolean dirigido = false;

    @Override
    public boolean isDirigido() {
        return dirigido;
    }

    @Override
    public void setDirigido(boolean dirigido) {
        this.dirigido = dirigido;
    }

    @Override
    public void agregarNodo(Nodo nodo) {
        if (obtenerNodo(nodo.id) == null) {
            nodos.add(nodo);
        }
    }

    @Override
    public void agregarArista(Arista arista) {
        asegurarNodo(arista.origen);
        asegurarNodo(arista.destino);
        aristas.add(arista);
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
                adyacentes.add(obtenerNodo(a.destino));
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
            if (n != null && n.id == id)
                return n;
        }
        return null;
    }

    @Override
    public int getCantidadNodos() {
        return nodos.size();
    }

    @Override
    public void eliminarNodo(int id) {
        for (int i = 0; i < nodos.size(); i++) {
            Nodo n = nodos.get(i);
            if (n != null && n.id == id) {
                nodos.set(i, null);
                break;
            }
        }
        aristas.removeIf(a -> a.origen == id || a.destino == id);
    }

    @Override
    public void eliminarArista(int origen, int destino) {
        aristas.removeIf(a -> a.origen == origen && a.destino == destino);
    }

    private void asegurarNodo(int id) {
        while (nodos.size() <= id) {
            nodos.add(new Nodo(nodos.size(), 50 + Math.random() * 650, 50 + Math.random() * 450));
        }
    }
}
