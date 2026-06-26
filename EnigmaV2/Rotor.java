// Rueda de cifrado con cableado fijo. Gira una posición cada vez que se pulsa una tecla.
public class Rotor {
    private String cableado;
    private int posicion;
    private char muesca;  // al llegar aquí, hace avanzar al siguiente rotor
    private String nombre;

    public Rotor(String nombre, String cableado, char muesca, char posicionInicial) {
        this.nombre   = nombre;
        this.cableado = cableado;
        this.muesca   = muesca;
        this.posicion = posicionInicial - 'A';
    }

    public void avanzar() {
        this.posicion = (this.posicion + 1) % 26;
    }

    public boolean estaEnMuesca() {
        return (this.posicion + 'A') == this.muesca;
    }

    // Señal de ida (teclado → reflector). Compensa la rotación sumando y restando la posición.
    public int cifrarIda(int indiceEntrada) {
        int entradaDesplazada = (indiceEntrada + this.posicion) % 26;
        char letraSustituida  = this.cableado.charAt(entradaDesplazada);
        int indiceSustituido  = letraSustituida - 'A';
        return (indiceSustituido - this.posicion + 26) % 26;
    }

    // Señal de vuelta (reflector → teclado). Inverso de cifrarIda: busca en el cableado en vez de leerlo.
    public int cifrarVuelta(int indiceEntrada) {
        int entradaDesplazada = (indiceEntrada + this.posicion) % 26;
        char letraEntrada     = (char) (entradaDesplazada + 'A');
        int indiceSustituido  = this.cableado.indexOf(letraEntrada);
        return (indiceSustituido - this.posicion + 26) % 26;
    }

    public char getLetraVisible() { return (char) (this.posicion + 'A'); }

    public String getNombre() { return this.nombre; }

    public void setPosicion(char letra) { this.posicion = letra - 'A'; }
}
