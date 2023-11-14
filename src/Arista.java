import java.util.Vector;

public class Arista {
    private int verticeOrigen;
    private int verticeDestino;

    public Arista(int vO, int vD){
        this.verticeOrigen  = vO;
        this.verticeDestino = vD;
    }
    public int getVerticeOrigen() {
        return verticeOrigen;
    }
    public int getVerticeDestino() {
        return verticeDestino;
    }
}
