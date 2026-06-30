package Grafos.core;

public class Nodo {
    public int id;
    public double x, y;
    public int color = -1; // -1 indica sin colorear

    public Nodo(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
}
