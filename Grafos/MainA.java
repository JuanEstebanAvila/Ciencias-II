package Grafos;

import javax.swing.SwingUtilities;

import Grafos.core.GrafoUniversal;
import Grafos.core.ListaAdyacencia;
import Grafos.core.ListaIncidencia;
import Grafos.core.MatrizAdyacencia;
import Grafos.core.MatrizIncidencia;
import Grafos.ui.InterfazGrafo;

public class MainA {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Aquí se inyecta la dependencia: puedes cambiar la implementación a probar
            // GrafoUniversal grafo = new ListaAdyacencia();
            GrafoUniversal grafo = new MatrizAdyacencia();
            // GrafoUniversal grafo = new ListaIncidencia();
            // GrafoUniversal grafo = new MatrizIncidencia();

            InterfazGrafo frame = new InterfazGrafo(grafo);
            frame.setVisible(true);
        });
    }
}
