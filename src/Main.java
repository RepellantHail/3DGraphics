import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main implements KeyListener {
    static DrawShapes shapes;
    static int typeShape;
    static int projectionType;
    public static void main(String[] args) {
        Draw painter = new Draw();
        typeShape = 1;
        projectionType = 1;
        shapes = new DrawShapes(painter, typeShape);
        shapes.initializeFigure(Color.magenta, projectionType);

        painter.addKeyListener(new Main());
        painter.setFocusable(true);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int step = 2;

        switch (keyCode) {
            case KeyEvent.VK_UP:
                shapes.translateFigure(1, -step);
                shapes.draw();
                break;
            case KeyEvent.VK_DOWN:
                shapes.translateFigure(1, step);
                shapes.draw();
                break;
            case KeyEvent.VK_LEFT:
                shapes.translateFigure(0, -step);
                shapes.draw();
                break;
            case KeyEvent.VK_RIGHT:
                shapes.translateFigure(0, step);
                shapes.draw();
                break;
            case KeyEvent.VK_ADD:
                shapes.scaleFigure(1.01);
                shapes.draw();
                break;
            case KeyEvent.VK_MINUS:
                shapes.scaleFigure(1.0 / Math.sqrt(1.01));
                shapes.draw();
                break;
            case KeyEvent.VK_X:
                shapes.rotateFigureX(5);
                shapes.draw();
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