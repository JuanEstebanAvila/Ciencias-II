public class ArbolB {
    private NodoArbolB raiz; // Puntero a la raíz del árbol
    private int gradoMinimo; // Grado mínimo del árbol (t)

    public ArbolB(int gradoMinimo) {
        this.raiz = null;
        this.gradoMinimo = gradoMinimo;
    }

    // Método para recorrer y mostrar el árbol en inorden
    public void recorrer() {
        if (this.raiz != null) {
            this.raiz.recorrer();
        }
        System.out.println();
    }

    // Método para buscar una clave en el árbol
    public NodoArbolB buscar(int k) {
        if (this.raiz == null) {
            return null;
        } else {
            return this.raiz.buscar(k);
        }
    }

    // Método principal que inserta una nueva clave en el árbol B
    public void insertar(int k) {
        // Si el árbol está vacío
        if (raiz == null) {
            raiz = new NodoArbolB(gradoMinimo, true);
            raiz.claves[0] = k;
            raiz.numeroClaves = 1;
        } else {
            // Si la raíz está llena, el árbol crece en altura
            if (raiz.numeroClaves == 2 * gradoMinimo - 1) {
                NodoArbolB nuevaRaiz = new NodoArbolB(gradoMinimo, false);

                // La vieja raíz se vuelve hijo de la nueva
                nuevaRaiz.hijos[0] = raiz;

                // Dividir la vieja raíz y mover 1 clave a la nueva raíz
                nuevaRaiz.dividirHijo(0, raiz);

                // La nueva raíz tiene 2 hijos ahora. Decidir a cuál de ellos irá la nueva clave
                int i = 0;
                if (nuevaRaiz.claves[0] < k) {
                    i++;
                }
                nuevaRaiz.hijos[i].insertarNoLleno(k);

                // Cambiar la raíz del árbol
                raiz = nuevaRaiz;
            } else {
                // Si no está llena, insertar en ella directamente
                raiz.insertarNoLleno(k);
            }
        }
    }

    // Método para eliminar una clave en el árbol B
    public void eliminar(int k) {
        if (raiz == null) {
            System.out.println("El árbol B está vacío.");
            return;
        }

        raiz.eliminar(k);

        // Si la raíz se queda sin claves pero tiene un hijo,
        // su primer hijo se convierte en la nueva raíz.
        // Si no tiene hijos, la raíz se vuelve null.
        if (raiz.numeroClaves == 0) {
            if (raiz.esHoja) {
                raiz = null;
            } else {
                raiz = raiz.hijos[0];
            }
        }
    }

    // Método para imprimir el árbol por niveles
    public void imprimirPorNiveles() {
        if (raiz == null) {
            System.out.println("El árbol B está vacío.");
            return;
        }

        java.util.Queue<NodoArbolB> cola = new java.util.LinkedList<>();
        cola.add(raiz);

        int nivel = 0;
        while (!cola.isEmpty()) {
            int nodosEnNivel = cola.size();
            System.out.print("Nivel " + nivel + ": ");

            while (nodosEnNivel > 0) {
                NodoArbolB actual = cola.poll();

                System.out.print("[");
                for (int i = 0; i < actual.numeroClaves; i++) {
                    System.out.print(actual.claves[i] + (i < actual.numeroClaves - 1 ? "," : ""));
                }
                System.out.print("] ");

                if (!actual.esHoja) {
                    for (int i = 0; i <= actual.numeroClaves; i++) {
                        if (actual.hijos[i] != null) {
                            cola.add(actual.hijos[i]);
                        }
                    }
                }
                nodosEnNivel--;
            }
            System.out.println();
            nivel++;
        }
    }
}
