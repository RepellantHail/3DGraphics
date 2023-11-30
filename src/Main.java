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
        int step = 5;
        int angle = 5;

        switch (keyCode) {
            //Rubik's Cube method
            case KeyEvent.VK_X -> shapes.transformCube      ( angle  ,0,0,1   ,0,0,0);
            case KeyEvent.VK_Y -> shapes.transformCube      (0,   angle,0,1   ,0,0,0);
            case KeyEvent.VK_Z -> shapes.transformCube      (0,0,   angle,1   ,0,0,0);
            case KeyEvent.VK_F -> shapes.transformCube      (0,0,0,1   ,0,0,   -step); // Move front
            case KeyEvent.VK_B -> shapes.transformCube      (0,0,0,1   ,0,0,    step); // Move front
            case KeyEvent.VK_ADD   -> shapes.transformCube  (0,0,0,1.02,0,0,0);
            case KeyEvent.VK_MINUS -> shapes.transformCube  (0,0,0,0.98,0,0,0);
            case KeyEvent.VK_UP    -> shapes.transformCube  (0,0,0,1   ,0,  -step,0); // Move up
            case KeyEvent.VK_DOWN  -> shapes.transformCube  (0,0,0,1   ,0,   step,0); // Move down
            case KeyEvent.VK_LEFT  -> shapes.transformCube  (0,0,0,1   ,  -step,0,0); // Move left
            case KeyEvent.VK_RIGHT -> shapes.transformCube  (0,0,0,1   ,   step,0,0); // Move right
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