import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main implements KeyListener {
    static DrawShapes shapes;
    public static void main(String[] args) {
        //Draw Cube with events
//        Draw painter = new Draw("Cube Shape");
//        shapes = new DrawShapes(painter, 1, 1);
//        shapes.draw();

        //Draw automatic Cube
//        Draw canvas = new Draw("Automatic Cube");
//        DrawShapes rotationCube = new DrawShapes(canvas, 1, 1);
//        rotationCube.automaticRotation();

        //Add key events
//        painter.addKeyListener(new Main());
//        painter.setFocusable(true);

        //Draw Surface
        Draw surfaceCanvas = new Draw("Surface");
        shapes = new DrawShapes(surfaceCanvas, 3);
        shapes.draw();

        surfaceCanvas.addKeyListener(new Main());
        surfaceCanvas.setFocusable(true);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int step = 1;
        int angle = 5;

        switch (keyCode) {
            case KeyEvent.VK_UP:
                shapes.translateFigure(0, -step); // Move up
                break;
            case KeyEvent.VK_DOWN:
                shapes.translateFigure(0, step); // Move down
                break;
            case KeyEvent.VK_LEFT:
                shapes.translateFigure(-step, 0); // Move left
                break;
            case KeyEvent.VK_RIGHT:
                shapes.translateFigure(step, 0); // Move right
                break;
            case KeyEvent.VK_ADD:
                shapes.scaleFigure(1.02);
                break;
            case KeyEvent.VK_MINUS:
                shapes.scaleFigure(0.98);
                break;
            case KeyEvent.VK_X:
                shapes.rotateFigureX(angle);
                break;
            case KeyEvent.VK_Y:
                shapes.rotateFigureY(angle);
                break;
            case KeyEvent.VK_Z:
                shapes.rotateFigureZ(angle);
                break;
            default:
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}