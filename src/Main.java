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
            case KeyEvent.VK_UP    -> shapes.translateFigure(0, -step, 0); // Move up
            case KeyEvent.VK_DOWN  -> shapes.translateFigure(0, step, 0); // Move down
            case KeyEvent.VK_LEFT  -> shapes.translateFigure(-step, 0, 0); // Move left
            case KeyEvent.VK_RIGHT -> shapes.translateFigure(step, 0, 0); // Move right
            //Rubik's Cube method
            case KeyEvent.VK_ADD   -> shapes.scaleCube(1.02);
            case KeyEvent.VK_MINUS -> shapes.scaleCube(0.98);
            case KeyEvent.VK_X -> shapes.rotateCube( angle  , 0, 0);
            case KeyEvent.VK_Y -> shapes.rotateCube(0,    angle, 0);
            case KeyEvent.VK_Z -> shapes.rotateCube(0, 0,    angle);
            case KeyEvent.VK_0 -> shapes.resetCube();
            case KeyEvent.VK_1 -> shapes.setRubikCubeSize(1);
            case KeyEvent.VK_2 -> shapes.setRubikCubeSize(2);
            case KeyEvent.VK_3 -> shapes.setRubikCubeSize(3);
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