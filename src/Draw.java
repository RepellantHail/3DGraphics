import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.round;

public class Draw extends JFrame {
    private BufferedImage buffer;
    private Graphics graPixel;
    public Draw(String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        // Create a larger BufferedImage
        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        graPixel = (Graphics2D) buffer.createGraphics();

        setVisible(true);
    }
    public void putPixel(int x, int y, Color color) {
        buffer.setRGB(0, 0, color.getRGB());
        this.getGraphics().drawImage(buffer, x, y, this);
    }
    public int getPixel(int x, int y){
        return buffer.getRGB(x,y);
    }

    public void drawLine(int x0, int y0, int x1, int y1, Color color) {
        int dx = x1 - x0;
        int dy = y1 - y0;

        double m = (double) dy / dx;
        double b = y0 - m * x0;

        for (int x = x0; x <= x1; x++) {
            int y = (int) round(m * x + b);
            putPixel(x, y, color);
        }
    }

}
