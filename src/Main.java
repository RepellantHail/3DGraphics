import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Draw painter = new Draw("Dibujar Cubo");
        DrawShapes shapes = new DrawShapes(painter, 2);
        shapes.initializeFigure(Color.magenta);
    }

}