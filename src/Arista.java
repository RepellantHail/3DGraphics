import java.awt.*;

public class Arista {
    private final int verticeOrigen;
    private final int verticeDestino;
    private Color pxColor = Color.black;
    public Arista(int vO, int vD){
        this.verticeOrigen  = vO;
        this.verticeDestino = vD;
    }
    public Arista(int vO, int vD, Color color){
        this.verticeOrigen  = vO;
        this.verticeDestino = vD;
        this.pxColor = color;
    }
    public Arista(Arista other) {
        this.verticeOrigen = other.verticeOrigen;
        this.verticeDestino = other.verticeDestino;
        this.pxColor = other.pxColor;
    }
    public int getVerticeOrigen() {
        return verticeOrigen;
    }
    public int getVerticeDestino() {
        return verticeDestino;
    }
    protected Color getColor(){
        return this.pxColor;
    }
}
