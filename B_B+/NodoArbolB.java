public class NodoArbolB {
    int[] claves;           // Arreglo que almacena las llaves
    int gradoMinimo;        // Grado mínimo (t) que define el rango de claves permitidas
    NodoArbolB[] hijos;     // Arreglo de punteros a los hijos
    int numeroClaves;       // Número actual de claves almacenadas en el nodo
    boolean esHoja;         // Verdadero si el nodo es hoja

    public NodoArbolB(int gradoMinimo, boolean esHoja) {
        this.gradoMinimo = gradoMinimo;
        this.esHoja = esHoja;
        this.claves = new int[2 * gradoMinimo - 1]; // Máximo de 2t-1 claves
        this.hijos = new NodoArbolB[2 * gradoMinimo]; // Máximo de 2t hijos
        this.numeroClaves = 0;
    }

    // Imprime todos los nodos en un recorrido inorden
    public void recorrer() {
        int i;
        for (i = 0; i < this.numeroClaves; i++) {
            if (!this.esHoja) {
                hijos[i].recorrer();
            }
            System.out.print(" " + claves[i]);
        }
        if (!this.esHoja) {
            hijos[i].recorrer();
        }
    }

    // Busca una clave en el subárbol con raíz en este nodo
    public NodoArbolB buscar(int k) {
        int i = 0;
        while (i < numeroClaves && k > claves[i]) {
            i++;
        }
        if (i < numeroClaves && claves[i] == k) {
            return this;
        }
        if (esHoja) {
            return null;
        }
        return hijos[i].buscar(k);
    }

    // Inserta una nueva clave en este nodo asumiendo que NO está lleno
    public void insertarNoLleno(int k) {
        int i = numeroClaves - 1;

        if (esHoja) {
            // Mover todas las claves mayores un espacio hacia adelante para hacer lugar
            while (i >= 0 && claves[i] > k) {
                claves[i + 1] = claves[i];
                i--;
            }
            // Insertar la nueva clave
            claves[i + 1] = k;
            numeroClaves++;
        } else {
            // Encontrar el hijo que va a recibir la nueva clave
            while (i >= 0 && claves[i] > k) {
                i--;
            }
            // Revisar si el hijo encontrado está lleno
            if (hijos[i + 1].numeroClaves == 2 * gradoMinimo - 1) {
                // Dividir el hijo si está lleno
                dividirHijo(i + 1, hijos[i + 1]);
                // Determinar cuál de los dos hijos divididos va a recibir la clave
                if (claves[i + 1] < k) {
                    i++;
                }
            }
            hijos[i + 1].insertarNoLleno(k);
        }
    }

    // Divide al hijo y (lleno) en este nodo (que debe estar no lleno)
    public void dividirHijo(int i, NodoArbolB y) {
        // Crear un nuevo nodo para almacenar (t-1) claves de y
        NodoArbolB z = new NodoArbolB(y.gradoMinimo, y.esHoja);
        z.numeroClaves = gradoMinimo - 1;

        // Copiar las últimas (t-1) claves de y hacia z
        for (int j = 0; j < gradoMinimo - 1; j++) {
            z.claves[j] = y.claves[j + gradoMinimo];
        }

        // Si y no es hoja, copiar también sus hijos correspondientes
        if (!y.esHoja) {
            for (int j = 0; j < gradoMinimo; j++) {
                z.hijos[j] = y.hijos[j + gradoMinimo];
            }
        }

        // Reducir el número de claves en y
        y.numeroClaves = gradoMinimo - 1;

        // Mover los hijos de este nodo para hacer espacio para z
        for (int j = numeroClaves; j >= i + 1; j--) {
            hijos[j + 1] = hijos[j];
        }
        hijos[i + 1] = z;

        // Mover las claves de este nodo para hacer espacio para la clave de y
        for (int j = numeroClaves - 1; j >= i; j--) {
            claves[j + 1] = claves[j];
        }
        
        // Subir la clave mediana de y a este nodo
        claves[i] = y.claves[gradoMinimo - 1];
        numeroClaves++;
    }

    // Funciones adicionales para ELIMINAR en el Árbol B

    public int encontrarClave(int k) {
        int idx = 0;
        while (idx < numeroClaves && claves[idx] < k) {
            idx++;
        }
        return idx;
    }

    public void eliminar(int k) {
        int idx = encontrarClave(k);

        if (idx < numeroClaves && claves[idx] == k) {
            if (esHoja) {
                eliminarDeHoja(idx);
            } else {
                eliminarDeNoHoja(idx);
            }
        } else {
            if (esHoja) {
                System.out.println("La clave " + k + " no existe en el árbol B.");
                return;
            }

            boolean esUltimoHijo = (idx == numeroClaves);

            if (hijos[idx].numeroClaves < gradoMinimo) {
                llenar(idx);
            }

            if (esUltimoHijo && idx > numeroClaves) {
                hijos[idx - 1].eliminar(k);
            } else {
                hijos[idx].eliminar(k);
            }
        }
    }

    private void eliminarDeHoja(int idx) {
        for (int i = idx + 1; i < numeroClaves; ++i) {
            claves[i - 1] = claves[i];
        }
        numeroClaves--;
    }

    private void eliminarDeNoHoja(int idx) {
        int k = claves[idx];

        if (hijos[idx].numeroClaves >= gradoMinimo) {
            int pred = getPredecesor(idx);
            claves[idx] = pred;
            hijos[idx].eliminar(pred);
        } else if (hijos[idx + 1].numeroClaves >= gradoMinimo) {
            int suc = getSucesor(idx);
            claves[idx] = suc;
            hijos[idx + 1].eliminar(suc);
        } else {
            fusionar(idx);
            hijos[idx].eliminar(k);
        }
    }

    private int getPredecesor(int idx) {
        NodoArbolB actual = hijos[idx];
        while (!actual.esHoja) {
            actual = actual.hijos[actual.numeroClaves];
        }
        return actual.claves[actual.numeroClaves - 1];
    }

    private int getSucesor(int idx) {
        NodoArbolB actual = hijos[idx + 1];
        while (!actual.esHoja) {
            actual = actual.hijos[0];
        }
        return actual.claves[0];
    }

    private void llenar(int idx) {
        if (idx != 0 && hijos[idx - 1].numeroClaves >= gradoMinimo) {
            pedirPrestadoAnterior(idx);
        } else if (idx != numeroClaves && hijos[idx + 1].numeroClaves >= gradoMinimo) {
            pedirPrestadoSiguiente(idx);
        } else {
            if (idx != numeroClaves) {
                fusionar(idx);
            } else {
                fusionar(idx - 1);
            }
        }
    }

    private void pedirPrestadoAnterior(int idx) {
        NodoArbolB hijo = hijos[idx];
        NodoArbolB hermano = hijos[idx - 1];

        for (int i = hijo.numeroClaves - 1; i >= 0; --i) {
            hijo.claves[i + 1] = hijo.claves[i];
        }

        if (!hijo.esHoja) {
            for (int i = hijo.numeroClaves; i >= 0; --i) {
                hijo.hijos[i + 1] = hijo.hijos[i];
            }
        }

        hijo.claves[0] = claves[idx - 1];

        if (!hijo.esHoja) {
            hijo.hijos[0] = hermano.hijos[hermano.numeroClaves];
        }

        claves[idx - 1] = hermano.claves[hermano.numeroClaves - 1];

        hijo.numeroClaves += 1;
        hermano.numeroClaves -= 1;
    }

    private void pedirPrestadoSiguiente(int idx) {
        NodoArbolB hijo = hijos[idx];
        NodoArbolB hermano = hijos[idx + 1];

        hijo.claves[hijo.numeroClaves] = claves[idx];

        if (!hijo.esHoja) {
            hijo.hijos[hijo.numeroClaves + 1] = hermano.hijos[0];
        }

        claves[idx] = hermano.claves[0];

        for (int i = 1; i < hermano.numeroClaves; ++i) {
            hermano.claves[i - 1] = hermano.claves[i];
        }

        if (!hermano.esHoja) {
            for (int i = 1; i <= hermano.numeroClaves; ++i) {
                hermano.hijos[i - 1] = hermano.hijos[i];
            }
        }

        hijo.numeroClaves += 1;
        hermano.numeroClaves -= 1;
    }

    private void fusionar(int idx) {
        NodoArbolB hijo = hijos[idx];
        NodoArbolB hermano = hijos[idx + 1];

        hijo.claves[gradoMinimo - 1] = claves[idx];

        for (int i = 0; i < hermano.numeroClaves; ++i) {
            hijo.claves[i + gradoMinimo] = hermano.claves[i];
        }

        if (!hijo.esHoja) {
            for (int i = 0; i <= hermano.numeroClaves; ++i) {
                hijo.hijos[i + gradoMinimo] = hermano.hijos[i];
            }
        }

        for (int i = idx + 1; i < numeroClaves; ++i) {
            claves[i - 1] = claves[i];
        }

        for (int i = idx + 2; i <= numeroClaves; ++i) {
            hijos[i - 1] = hijos[i];
        }

        hijo.numeroClaves += hermano.numeroClaves + 1;
        numeroClaves--;
    }
}
