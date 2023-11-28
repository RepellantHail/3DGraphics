import java.awt.*;
import java.util.Vector;

public class Arista {
    private int verticeOrigen;
    private int verticeDestino;
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
    public int getVerticeOrigen() {
        return verticeOrigen;
    }
    public int getVerticeDestino() {
        return verticeDestino;
    }
    public void setColor(Color color) {
        this.pxColor = color;
    }
    protected Color getColor(){
        return this.pxColor;
    }
}
