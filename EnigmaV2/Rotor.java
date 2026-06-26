public class Rotor {
    private String cableado;
    private int posicion;
    private char muesca;
    private String nombre;

    /**
     * Constructor del Rotor.
     * @param nombre Identificador del rotor (ej. "I", "II").
     * @param cableado El alfabeto desordenado interno del rotor.
     * @param muesca Letra en la que el rotor provoca el avance del siguiente.
     * @param posicionInicial Letra inicial visible en la ventanilla.
     */
    public Rotor(String nombre, String cableado, char muesca, char posicionInicial) {
        this.nombre = nombre;
        this.cableado = cableado;
        this.muesca = muesca;
        // Convertimos la letra inicial ('A'-'Z') a un índice (0-25)
        this.posicion = posicionInicial - 'A';
    }

    /**
     * Avanza el rotor una posición (simulando la rotación de la rueda mecánica).
     */
    public void avanzar() {
        this.posicion = (this.posicion + 1) % 26;
    }

    /**
     * Verifica si la posición actual coincide con la muesca del rotor.
     * @return true si está en la muesca, false en caso contrario.
     */
    public boolean estaEnMuesca() {
        return (this.posicion + 'A') == this.muesca;
    }

    /**
     * Mapea la señal eléctrica en su camino de ida (Teclado -> Reflector).
     * @param indiceEntrada Índice del contacto de entrada (0-25).
     * @return Índice del contacto de salida (0-25).
     */
    public int cifrarIda(int indiceEntrada) {
        // La señal entra y se ve afectada por la rotación actual del rotor
        int entradaDesplazada = (indiceEntrada + this.posicion) % 26;
        
        // Pasa por el cableado interno para ser sustituida
        char letraSustituida = this.cableado.charAt(entradaDesplazada);
        int indiceSustituido = letraSustituida - 'A';
        
        // Sale del rotor contrarrestando el desplazamiento de la rotación
        int salida = (indiceSustituido - this.posicion + 26) % 26;
        
        return salida;
    }

    /**
     * Mapea la señal eléctrica en su camino de vuelta (Reflector -> Teclado).
     * @param indiceEntrada Índice del contacto de entrada (0-25).
     * @return Índice del contacto de salida (0-25).
     */
    public int cifrarVuelta(int indiceEntrada) {
        // La señal entra y se ve afectada por la rotación actual del rotor
        int entradaDesplazada = (indiceEntrada + this.posicion) % 26;
        
        // Búsqueda inversa en el cableado: ¿qué letra se convierte en esta entrada?
        char letraEntrada = (char) (entradaDesplazada + 'A');
        int indiceSustituido = this.cableado.indexOf(letraEntrada);
        
        // Sale del rotor contrarrestando el desplazamiento de la rotación
        int salida = (indiceSustituido - this.posicion + 26) % 26;
        
        return salida;
    }

    /**
     * @return La letra actualmente visible en la ventanilla del rotor.
     */
    public char getLetraVisible() {
        return (char) (this.posicion + 'A');
    }

    /**
     * @return El nombre identificador del rotor.
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Establece la posición del rotor (usado para cargar estado).
     * @param letra Letra visible a establecer.
     */
    public void setPosicion(char letra) {
        this.posicion = letra - 'A';
    }
}
