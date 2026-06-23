public class ArbolBMas {
    private NodoArbolBMas raiz;
    private int orden; // Orden del árbol B+

    public ArbolBMas(int orden) {
        this.raiz = new NodoArbolBMas(orden, true);
        this.orden = orden;
    }

    // Clase auxiliar para devolver resultados durante la división de nodos
    private class ResultadoDivision {
        int clavePromocionada;
        NodoArbolBMas nuevoHermano;

        public ResultadoDivision(int clavePromocionada, NodoArbolBMas nuevoHermano) {
            this.clavePromocionada = clavePromocionada;
            this.nuevoHermano = nuevoHermano;
        }
    }

    // Método principal de inserción
    public void insertar(int k) {
        ResultadoDivision resultado = insertarRecursivo(raiz, k);
        if (resultado != null) {
            // Si la raíz se dividió, creamos una nueva raíz
            NodoArbolBMas nuevaRaiz = new NodoArbolBMas(orden, false);
            nuevaRaiz.claves[0] = resultado.clavePromocionada;
            nuevaRaiz.hijos[0] = raiz;
            nuevaRaiz.hijos[1] = resultado.nuevoHermano;
            nuevaRaiz.numeroClaves = 1;
            raiz = nuevaRaiz;
        }
    }

    // Inserción recursiva que maneja la búsqueda de la hoja y divisiones hacia arriba
    private ResultadoDivision insertarRecursivo(NodoArbolBMas nodo, int k) {
        if (nodo.esHoja) {
            // Inserción en nodo hoja
            int pos = 0;
            while (pos < nodo.numeroClaves && nodo.claves[pos] < k) {
                pos++;
            }
            
            // Mover claves para hacer espacio
            for (int i = nodo.numeroClaves; i > pos; i--) {
                nodo.claves[i] = nodo.claves[i - 1];
            }
            nodo.claves[pos] = k;
            nodo.numeroClaves++;

            // Verificar si hay sobreflujo (se alcanzó el orden máximo temporal)
            if (nodo.numeroClaves == orden) {
                return dividirHoja(nodo);
            }
            return null;
        } else {
            // Inserción en nodo interno: buscar el hijo correspondiente
            int pos = 0;
            while (pos < nodo.numeroClaves && k >= nodo.claves[pos]) {
                pos++;
            }

            ResultadoDivision resultadoHijo = insertarRecursivo(nodo.hijos[pos], k);
            if (resultadoHijo != null) {
                // Insertar clave promocionada y el nuevo hermano en este nodo interno
                for (int i = nodo.numeroClaves; i > pos; i--) {
                    nodo.claves[i] = nodo.claves[i - 1];
                    nodo.hijos[i + 1] = nodo.hijos[i];
                }
                nodo.claves[pos] = resultadoHijo.clavePromocionada;
                nodo.hijos[pos + 1] = resultadoHijo.nuevoHermano;
                nodo.numeroClaves++;

                // Verificar si este nodo interno también se desborda
                if (nodo.numeroClaves == orden) {
                    return dividirInterno(nodo);
                }
            }
            return null;
        }
    }

    // Método para dividir un nodo hoja
    private ResultadoDivision dividirHoja(NodoArbolBMas nodo) {
        NodoArbolBMas nuevoNodo = new NodoArbolBMas(orden, true);
        int mitad = orden / 2;
        int clavesAEnviar = orden - mitad;

        // Mover la segunda mitad de las claves al nuevo nodo
        for (int i = 0; i < clavesAEnviar; i++) {
            nuevoNodo.claves[i] = nodo.claves[mitad + i];
        }
        nuevoNodo.numeroClaves = clavesAEnviar;
        nodo.numeroClaves = mitad;

        // Mantener la lista enlazada de las hojas
        nuevoNodo.siguiente = nodo.siguiente;
        nodo.siguiente = nuevoNodo;

        // La clave promocionada para el padre es la primera clave del nuevo nodo hoja (se copia)
        int clavePromocionada = nuevoNodo.claves[0];
        return new ResultadoDivision(clavePromocionada, nuevoNodo);
    }

    // Método para dividir un nodo interno
    private ResultadoDivision dividirInterno(NodoArbolBMas nodo) {
        NodoArbolBMas nuevoNodo = new NodoArbolBMas(orden, false);
        int mitad = orden / 2;
        
        // La clave de la mitad se promueve y no se copia al nuevo nodo
        int clavePromocionada = nodo.claves[mitad];

        int clavesAEnviar = orden - mitad - 1;
        for (int i = 0; i < clavesAEnviar; i++) {
            nuevoNodo.claves[i] = nodo.claves[mitad + 1 + i];
            nuevoNodo.hijos[i] = nodo.hijos[mitad + 1 + i];
        }
        nuevoNodo.hijos[clavesAEnviar] = nodo.hijos[orden]; // último hijo

        nuevoNodo.numeroClaves = clavesAEnviar;
        nodo.numeroClaves = mitad; 

        return new ResultadoDivision(clavePromocionada, nuevoNodo);
    }

    // Imprimir el árbol por niveles (BFS)
    public void imprimirPorNiveles() {
        if (raiz == null || raiz.numeroClaves == 0) {
            System.out.println("El árbol B+ está vacío.");
            return;
        }

        java.util.Queue<NodoArbolBMas> cola = new java.util.LinkedList<>();
        cola.add(raiz);

        int nivel = 0;
        while (!cola.isEmpty()) {
            int nodosEnNivel = cola.size();
            System.out.print("Nivel " + nivel + ": ");
            
            while (nodosEnNivel > 0) {
                NodoArbolBMas actual = cola.poll();
                
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

    // Funciones adicionales para ELIMINAR en el Árbol B+
    public void eliminar(int k) {
        if (raiz == null || raiz.numeroClaves == 0) {
            System.out.println("El árbol B+ está vacío.");
            return;
        }
        eliminarRecursivo(raiz, null, -1, k);

        // Si la raíz se queda sin claves pero tiene un hijo (después de fusiones)
        if (raiz.numeroClaves == 0 && !raiz.esHoja) {
            raiz = raiz.hijos[0];
        }
    }

    // Método para buscar un valor en el Árbol B+
    public NodoArbolBMas buscar(int k) {
        if (raiz == null || raiz.numeroClaves == 0) {
            return null;
        }
        return buscarRecursivo(raiz, k);
    }

    private NodoArbolBMas buscarRecursivo(NodoArbolBMas nodo, int k) {
        int pos = 0;
        while (pos < nodo.numeroClaves && nodo.claves[pos] < k) {
            pos++;
        }

        if (nodo.esHoja) {
            if (pos < nodo.numeroClaves && nodo.claves[pos] == k) {
                return nodo;
            } else {
                return null;
            }
        } else {
            // En nodo interno de un B+, la búsqueda de una clave igual baja por la derecha
            if (pos < nodo.numeroClaves && nodo.claves[pos] == k) {
                pos++;
            }
            return buscarRecursivo(nodo.hijos[pos], k);
        }
    }

    private void eliminarRecursivo(NodoArbolBMas nodo, NodoArbolBMas padre, int indiceEnPadre, int k) {
        int pos = 0;
        while (pos < nodo.numeroClaves && nodo.claves[pos] < k) {
            pos++;
        }

        if (nodo.esHoja) {
            // Eliminar de la hoja
            if (pos < nodo.numeroClaves && nodo.claves[pos] == k) {
                for (int i = pos; i < nodo.numeroClaves - 1; i++) {
                    nodo.claves[i] = nodo.claves[i + 1];
                }
                nodo.numeroClaves--;
                
                // Manejar subflujo si no es la raíz
                if (nodo != raiz && nodo.numeroClaves < (orden) / 2) {
                    manejarSubflujoHoja(nodo, padre, indiceEnPadre);
                }
            } else {
                System.out.println("La clave " + k + " no existe en el árbol B+.");
            }
        } else {
            // Nodo interno: En B+, si la clave coincide, se busca en la rama derecha (pos + 1) o izquierda dependiendo
            // de la convención de <=. Aquí la búsqueda en inserción fue >=, por lo tanto si la clave coincide
            // se baja por el hijo pos+1. Sin embargo, para no complicar, usamos la misma lógica que buscar.
            if (pos < nodo.numeroClaves && nodo.claves[pos] == k) {
                pos++;
            }
            
            eliminarRecursivo(nodo.hijos[pos], nodo, pos, k);
            
            // Manejar subflujo del hijo interno
            if (nodo.hijos[pos].numeroClaves < (orden) / 2) {
                manejarSubflujoInterno(nodo.hijos[pos], nodo, pos);
            }
        }
    }

    private void manejarSubflujoHoja(NodoArbolBMas hoja, NodoArbolBMas padre, int indiceEnPadre) {
        int minimo = orden / 2;
        
        // 1. Prestar del hermano izquierdo
        if (indiceEnPadre > 0) {
            NodoArbolBMas hermanoIzquierdo = padre.hijos[indiceEnPadre - 1];
            if (hermanoIzquierdo.numeroClaves > minimo) {
                for (int i = hoja.numeroClaves; i > 0; i--) {
                    hoja.claves[i] = hoja.claves[i - 1];
                }
                hoja.claves[0] = hermanoIzquierdo.claves[hermanoIzquierdo.numeroClaves - 1];
                hoja.numeroClaves++;
                hermanoIzquierdo.numeroClaves--;
                padre.claves[indiceEnPadre - 1] = hoja.claves[0];
                return;
            }
        }

        // 2. Prestar del hermano derecho
        if (indiceEnPadre < padre.numeroClaves) {
            NodoArbolBMas hermanoDerecho = padre.hijos[indiceEnPadre + 1];
            if (hermanoDerecho.numeroClaves > minimo) {
                hoja.claves[hoja.numeroClaves] = hermanoDerecho.claves[0];
                hoja.numeroClaves++;
                for (int i = 0; i < hermanoDerecho.numeroClaves - 1; i++) {
                    hermanoDerecho.claves[i] = hermanoDerecho.claves[i + 1];
                }
                hermanoDerecho.numeroClaves--;
                padre.claves[indiceEnPadre] = hermanoDerecho.claves[0];
                return;
            }
        }

        // 3. Fusionar hojas
        if (indiceEnPadre > 0) {
            NodoArbolBMas hermanoIzquierdo = padre.hijos[indiceEnPadre - 1];
            for (int i = 0; i < hoja.numeroClaves; i++) {
                hermanoIzquierdo.claves[hermanoIzquierdo.numeroClaves + i] = hoja.claves[i];
            }
            hermanoIzquierdo.numeroClaves += hoja.numeroClaves;
            hermanoIzquierdo.siguiente = hoja.siguiente;
            eliminarClaveDePadre(padre, indiceEnPadre - 1);
        } else {
            NodoArbolBMas hermanoDerecho = padre.hijos[indiceEnPadre + 1];
            for (int i = 0; i < hermanoDerecho.numeroClaves; i++) {
                hoja.claves[hoja.numeroClaves + i] = hermanoDerecho.claves[i];
            }
            hoja.numeroClaves += hermanoDerecho.numeroClaves;
            hoja.siguiente = hermanoDerecho.siguiente;
            eliminarClaveDePadre(padre, indiceEnPadre);
        }
    }

    private void manejarSubflujoInterno(NodoArbolBMas nodo, NodoArbolBMas padre, int indiceEnPadre) {
        int minimo = orden / 2;
        
        // 1. Prestar del hermano izquierdo
        if (indiceEnPadre > 0) {
            NodoArbolBMas hermanoIzquierdo = padre.hijos[indiceEnPadre - 1];
            if (hermanoIzquierdo.numeroClaves > minimo) {
                for (int i = nodo.numeroClaves; i > 0; i--) {
                    nodo.claves[i] = nodo.claves[i - 1];
                    nodo.hijos[i + 1] = nodo.hijos[i];
                }
                nodo.hijos[1] = nodo.hijos[0];
                
                nodo.claves[0] = padre.claves[indiceEnPadre - 1];
                nodo.hijos[0] = hermanoIzquierdo.hijos[hermanoIzquierdo.numeroClaves];
                nodo.numeroClaves++;
                
                padre.claves[indiceEnPadre - 1] = hermanoIzquierdo.claves[hermanoIzquierdo.numeroClaves - 1];
                hermanoIzquierdo.numeroClaves--;
                return;
            }
        }

        // 2. Prestar del hermano derecho
        if (indiceEnPadre < padre.numeroClaves) {
            NodoArbolBMas hermanoDerecho = padre.hijos[indiceEnPadre + 1];
            if (hermanoDerecho.numeroClaves > minimo) {
                nodo.claves[nodo.numeroClaves] = padre.claves[indiceEnPadre];
                nodo.hijos[nodo.numeroClaves + 1] = hermanoDerecho.hijos[0];
                nodo.numeroClaves++;
                
                padre.claves[indiceEnPadre] = hermanoDerecho.claves[0];
                
                for (int i = 0; i < hermanoDerecho.numeroClaves - 1; i++) {
                    hermanoDerecho.claves[i] = hermanoDerecho.claves[i + 1];
                    hermanoDerecho.hijos[i] = hermanoDerecho.hijos[i + 1];
                }
                hermanoDerecho.hijos[hermanoDerecho.numeroClaves - 1] = hermanoDerecho.hijos[hermanoDerecho.numeroClaves];
                hermanoDerecho.numeroClaves--;
                return;
            }
        }

        // 3. Fusionar nodos internos
        if (indiceEnPadre > 0) {
            NodoArbolBMas hermanoIzquierdo = padre.hijos[indiceEnPadre - 1];
            hermanoIzquierdo.claves[hermanoIzquierdo.numeroClaves] = padre.claves[indiceEnPadre - 1];
            hermanoIzquierdo.numeroClaves++;
            
            for (int i = 0; i < nodo.numeroClaves; i++) {
                hermanoIzquierdo.claves[hermanoIzquierdo.numeroClaves + i] = nodo.claves[i];
                hermanoIzquierdo.hijos[hermanoIzquierdo.numeroClaves + i] = nodo.hijos[i];
            }
            hermanoIzquierdo.hijos[hermanoIzquierdo.numeroClaves + nodo.numeroClaves] = nodo.hijos[nodo.numeroClaves];
            hermanoIzquierdo.numeroClaves += nodo.numeroClaves;
            
            eliminarClaveDePadre(padre, indiceEnPadre - 1);
        } else {
            NodoArbolBMas hermanoDerecho = padre.hijos[indiceEnPadre + 1];
            nodo.claves[nodo.numeroClaves] = padre.claves[indiceEnPadre];
            nodo.numeroClaves++;
            
            for (int i = 0; i < hermanoDerecho.numeroClaves; i++) {
                nodo.claves[nodo.numeroClaves + i] = hermanoDerecho.claves[i];
                nodo.hijos[nodo.numeroClaves + i] = hermanoDerecho.hijos[i];
            }
            nodo.hijos[nodo.numeroClaves + hermanoDerecho.numeroClaves] = hermanoDerecho.hijos[hermanoDerecho.numeroClaves];
            nodo.numeroClaves += hermanoDerecho.numeroClaves;
            
            eliminarClaveDePadre(padre, indiceEnPadre);
        }
    }

    private void eliminarClaveDePadre(NodoArbolBMas padre, int indice) {
        for (int i = indice; i < padre.numeroClaves - 1; i++) {
            padre.claves[i] = padre.claves[i + 1];
            padre.hijos[i + 1] = padre.hijos[i + 2];
        }
        padre.numeroClaves--;
    }
}
