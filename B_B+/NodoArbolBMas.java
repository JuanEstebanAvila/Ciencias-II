public class NodoArbolBMas {
    boolean esHoja;             // Indica si el nodo es una hoja
    int maxClaves;              // Máximo de claves permitidas según el orden
    int[] claves;               // Arreglo de claves
    NodoArbolBMas[] hijos;      // Punteros a los hijos (para nodos internos)
    NodoArbolBMas siguiente;    // Puntero al siguiente nodo hoja (para mantener la lista enlazada)
    int numeroClaves;           // Número actual de claves

    public NodoArbolBMas(int orden, boolean esHoja) {
        this.esHoja = esHoja;
        this.maxClaves = orden - 1; // Un árbol B+ de orden m tiene a lo sumo m-1 claves
        
        // Se añade un espacio adicional para manejar temporalmente el sobreflujo antes de dividir
        this.claves = new int[orden]; 
        this.hijos = new NodoArbolBMas[orden + 1]; 
        
        this.numeroClaves = 0;
        this.siguiente = null;
    }
}
