import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DrawShapes {
    private Draw canvas;
    private int projection;
    private Figure originalFigure;
    private Figure transformedFigure;
    private Point[] projectedFigure;
    private int figureID;
    private Color color;
    private float distance = 1;
    private int centerX;
    private int centerY;
    private double initialScale = 10;
    private double initialAngleX = 15;
    private double initialAngleY = 15;
    private double initialAngleZ = 0;
    public DrawShapes(Draw canvas, int figureID, int projection) {
        this.canvas = canvas;
        this.figureID = figureID;
        this.projection = projection;
        this.originalFigure = new Figure(figureID);
        this.transformedFigure = new Figure(figureID);
        this.color = Color.black;
        this.centerX = canvas.getWidth() / 2;
        this.centerY = canvas.getWidth() / 2;

        // Initialize the projected figure
        this.projectedFigure = parallelProjection(originalFigure.getVertices());
        scaleFigure(1.0);
        centerFigure(); // Center the figure before drawing
        draw();
    }
    private Point[] parallelProjection(Point3D[] vertices) {
        Point[] projectedPoints = new Point[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            int projectedX = vertices[i].getX();
            int projectedY = vertices[i].getY();

            projectedPoints[i] = new Point(projectedX, projectedY);
        }

        return projectedPoints;
    }
    public void centerFigure() {
        int totalX = 0;
        int totalY = 0;
        for (Point point : projectedFigure) {
            totalX += point.x;
            totalY += point.y;
        }

        int averageX = totalX / projectedFigure.length;
        int averageY = totalY / projectedFigure.length;

        // Translate the projected vertices to center the figure
        int shiftX = centerX - averageX;
        int shiftY = centerY - averageY;

        for (Point point : projectedFigure) {
            point.x += shiftX;
            point.y += shiftY;
        }
    }
    public void draw() {
        centerFigure();
        Arista[] figura = transformedFigure.getAristas();

        canvas.clearBuffer();
        for (Arista a : figura) { // Draw lines between vertices
            canvas.drawLine(
                    projectedFigure[a.getVerticeOrigen()].x,
                    projectedFigure[a.getVerticeOrigen()].y,
                    projectedFigure[a.getVerticeDestino()].x,
                    projectedFigure[a.getVerticeDestino()].y,
                    color);
        }

        canvas.repaint();
    }
    private void copyFigure(Figure source, Figure destination) {
        // Copy the vertices from source to destination
        Point3D[] sourceVertices = source.getVertices();
        Point3D[] copiedVertices = new Point3D[sourceVertices.length];

        for (int i = 0; i < sourceVertices.length; i++) {
            copiedVertices[i] = new Point3D(
                    sourceVertices[i].getX(),
                    sourceVertices[i].getY(),
                    sourceVertices[i].getZ()
            );
        }

        // Copy the edges from source to destination
        Arista[] sourceAristas = source.getAristas();
        Arista[] copiedAristas = Arrays.copyOf(sourceAristas, sourceAristas.length);

        // Set the copied vertices and edges to the destination
        destination.setVertices(copiedVertices);
        destination.setAristas(copiedAristas);
    }
    public void scaleFigure(double factor) {
        copyFigure(originalFigure, transformedFigure);
        scaleVertices(transformedFigure, initialScale);
        scaleVertices(transformedFigure, factor);

        this.projectedFigure = parallelProjection(transformedFigure.getVertices()); // Re-project the scaled vertices
        draw(); // Draw the updated figure
        initialScale *= factor;
    }
    private void scaleVertices(Figure copy,double factor) {
        // Scale the 3D vertices of the figure
        for (Point3D vertex : copy.getVertices()) {
            double scaledX = vertex.getX() * factor;
            double scaledY = vertex.getY() * factor;
            double scaledZ = vertex.getZ() * factor;

            // Round to integers to avoid accumulation of rounding errors
            vertex.setX((int) Math.round(scaledX));
            vertex.setY((int) Math.round(scaledY));
            vertex.setZ((int) Math.round(scaledZ));
        }
    }
    public void translateFigure(int deltaX, int deltaY) {
        translateVertices(transformedFigure, deltaX, deltaY);
        //Update center
        centerX += deltaX;
        centerY += deltaY;

        this.projectedFigure = parallelProjection(transformedFigure.getVertices()); // Re-project the translated vertices
        draw(); // Draw the updated figure
    }
    private void translateVertices(Figure figure,int deltaX, int deltaY){
        for (Point3D vertex : figure.getVertices()) {
            vertex.setX(vertex.getX() + deltaX);
            vertex.setY(vertex.getY() + deltaY);
        }
    }
    public void rotateFigureX(double angle){
        rotateVerticesX(transformedFigure, initialAngleX);
        rotateVerticesX(transformedFigure, angle);
        this.projectedFigure = parallelProjection(transformedFigure.getVertices());
        draw();
        initialAngleX += angle;
    }
    public void rotateFigureY(double angle) {
        rotateVerticesY(transformedFigure, initialAngleY);
        rotateVerticesY(transformedFigure, angle);
        this.projectedFigure = parallelProjection(transformedFigure.getVertices());
        draw();
        initialAngleY += angle;
    }
    public void rotateFigureZ(double angle) {
        rotateVerticesZ(transformedFigure, initialAngleZ);
        rotateVerticesZ(transformedFigure, angle);
        this.projectedFigure = parallelProjection(transformedFigure.getVertices());
        draw();
        initialAngleZ += angle;
    }
    private void rotateVerticesX(Figure figure, double angle) {
        double radians = Math.toRadians(angle);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        for (Point3D vertex : figure.getVertices()) {
            double y = vertex.getY();
            double z = vertex.getZ();

            // Apply rotation matrix
            double newY = y * cosTheta - z * sinTheta;
            double newZ = y * sinTheta + z * cosTheta;

            // Round to integers to avoid accumulation of rounding errors
            vertex.setY((int) Math.round(newY));
            vertex.setZ((int) Math.round(newZ));
        }
    }
    private void rotateVerticesY(Figure figure, double angle) {
        double radians = Math.toRadians(angle);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        for (Point3D vertex : figure.getVertices()) {
            double x = vertex.getX();
            double z = vertex.getZ();

            // Apply rotation matrix
            double newX = x * cosTheta + z * sinTheta;
            double newZ = -x * sinTheta + z * cosTheta;

            // Round to integers to avoid accumulation of rounding errors
            vertex.setX((int) Math.round(newX));
            vertex.setZ((int) Math.round(newZ));
        }
    }
    private void rotateVerticesZ(Figure figure, double angle) {
        double radians = Math.toRadians(angle);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        for (Point3D vertex : figure.getVertices()) {
            double x = vertex.getX();
            double y = vertex.getY();

            // Apply rotation matrix
            double newX = x * cosTheta - y * sinTheta;
            double newY = x * sinTheta + y * cosTheta;

            // Round to integers to avoid accumulation of rounding errors
            vertex.setX((int) Math.round(newX));
            vertex.setY((int) Math.round(newY));
        }
    }

    public void automaticRotation() {
        scaleFigure(10);

        // Create a new thread for automatic rotation
        Thread rotationThread = new Thread(() -> {
            int angleStep = 1;
            int delay = 600;
            while (true) {
                while (initialAngleX <= 360) {
                    rotateFigureX(angleStep);
                    angleStep += 5;

                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                angleStep = 0;
                while (initialAngleY <= 360) {
                    rotateFigureY(angleStep);
                    angleStep += 5;

                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                angleStep = 0;
                while (initialAngleZ <= 360) {
                    rotateFigureZ(angleStep);
                    angleStep += 5;

                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //Set to 0
                initialAngleX = 0;
                initialAngleY = 0;
                initialAngleZ = 0;
            }
        });

        // Start the rotation thread
        rotationThread.start();
    }

}