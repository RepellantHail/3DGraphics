
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main implements KeyListener {
    static DrawShapes shapes;
    public static void main(String[] args) {
        //Draw Rubik Cube
        Draw painter = new Draw("Rubik Cube");
        shapes = new DrawShapes(painter);

        //Add key events
        painter.addKeyListener(new Main());
        painter.setFocusable(true);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int step = 1;
        int angle = 5;

        switch (keyCode) {
            case KeyEvent.VK_UP -> shapes.translateFigure(0, -step, 0); // Move up
            case KeyEvent.VK_DOWN -> shapes.translateFigure(0, step, 0); // Move down
            case KeyEvent.VK_LEFT -> shapes.translateFigure(-step, 0, 0); // Move left
            case KeyEvent.VK_RIGHT -> shapes.translateFigure(step, 0, 0); // Move right
            case KeyEvent.VK_ADD -> shapes.scaleFigure(1.02);
            case KeyEvent.VK_MINUS -> shapes.scaleFigure(0.98);
            case KeyEvent.VK_X -> shapes.rotateCubeX(angle);
            case KeyEvent.VK_Y -> shapes.rotateCubeY(angle);
            case KeyEvent.VK_Z -> shapes.rotateCubeZ(angle);
            default -> {
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}