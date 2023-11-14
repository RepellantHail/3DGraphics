import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Draw extends JFrame {
    private BufferedImage offscreenBuffer;
    private Graphics2D offscreenGraphics;
    public Draw(String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        offscreenBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        offscreenGraphics = offscreenBuffer.createGraphics();
        clearBuffer();

        setVisible(true);
    }
    private void clearBuffer() {
        offscreenGraphics.setColor(Color.WHITE);
        offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());
    }
    @Override
    public void paint(Graphics g) {
        g.drawImage(offscreenBuffer, 0, 0, this);
    }
    public void putPixel(int x, int y, Color color) {
        offscreenBuffer.setRGB(x, y, color.getRGB());
    }
    public void drawLine(int x0, int y0, int x1, int y1, Color color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            putPixel(x0, y0, color);

            if (x0 == x1 && y0 == y1) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }
}
