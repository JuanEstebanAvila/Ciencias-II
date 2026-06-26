package Incidencia_Adyacencia;

public class Arista {
    public int origen, destino;
    public double peso;
    public double capacidad;
    public double flujo;

    public Arista(int origen, int destino, double peso, double capacidad) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
        this.capacidad = capacidad;
        this.flujo = 0;
    }
}
