import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DrawShapes {
    private Draw canvas;
    private int projection;
    private Figure originalFigure;
    private Figure transformedFigure;
    private Point[] projectedFigure;
    private float projectionMatrix[][] = new float[4][4];
    private int figureID;
    private Color color;
    private float distance = 1;
    private int centerX;
    private int centerY;
    private double initialScale = 15;
    private double initialAngleX = 0;
    private double initialAngleY = 0;
    private double initialAngleZ = 0;

    public DrawShapes(Draw canvas, int figureID){
        this.canvas = canvas;
        this.figureID = figureID;
        this.color = Color.black;
        this.centerX = canvas.getWidth() / 2;
        this.centerY = canvas.getWidth() / 2;
        this.projection = 1;
        this.originalFigure = new Figure(figureID);
        this.transformedFigure = new Figure(figureID);
        this.projectedFigure = parallelProjection(originalFigure.getVertices());
        draw();
    }
    public DrawShapes(Draw canvas, int figureID, int projection) {
        this.canvas = canvas;
        this.figureID = figureID;
        this.projection = projection;
        this.originalFigure = new Figure(figureID);
        this.transformedFigure = new Figure(figureID);
        this.color = Color.black;
        this.centerX = canvas.getWidth() / 2;
        this.centerY = canvas.getWidth() / 2;

        //Initalize matrix
        /*float fNear = 0.1f;
        float fFar = 1000.0f;
        float fFov = 90.0f;
        float fAspectRatio = (float)canvas.getHeight() / (float) canvas.getWidth();
        float fFovRad = (float) (1.0f / Math.tan(fFov * 0.5 / 180.0f * 3.14159f));

        this.projectionMatrix[0][0] = fAspectRatio * fFovRad;
        this.projectionMatrix[1][1] = fFovRad;
        this.projectionMatrix[2][2] = fFar / (fFar * fNear);
        this.projectionMatrix[3][2] = (-fFar * fNear) / (fFar - fNear);
        this.projectionMatrix[2][3] = 1.0f;
        this.projectionMatrix[3][3] = 0.0f;*/

        // Initialize the projected figure
        if(projection == 1)
            this.projectedFigure = parallelProjection(originalFigure.getVertices());
        else
            this.projectedFigure = perspectiveProjection(originalFigure.getVertices());

        scaleFigure(1.0);
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
    private Point[] perspectiveProjection(Point3D[] vertices) {
        Point[] projectedPoints = new Point[vertices.length];

        /*for (int i = 0; i < vertices.length; i++) {
            // Homogeneous coordinates
            float x = (float) vertices[i].getX();
            float y = (float) vertices[i].getY();
            float z = (float) vertices[i].getZ();
            float w = 1.0f;

            // Apply projection matrix
            float projX = projectionMatrix[0][0] * x + projectionMatrix[1][0] * y + projectionMatrix[2][0] * z + projectionMatrix[3][0] * w;
            float projY = projectionMatrix[0][1] * x + projectionMatrix[1][1] * y + projectionMatrix[2][1] * z + projectionMatrix[3][1] * w;

            // Perspective divide
            projX /= w;
            projY /= w;

            // Map to screen coordinates
            int projectedX = (int) Math.round((projX + 1) * canvas.getWidth() / 2);
            int projectedY = (int) Math.round((-projY + 1) * canvas.getHeight() / 2);

            projectedPoints[i] = new Point(projectedX, projectedY);
        }*/

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
    private Point[] projectFigure(){
        return (projection == 1) ? parallelProjection(transformedFigure.getVertices()): perspectiveProjection(transformedFigure.getVertices());
    }
    public void scaleFigure(double factor) {
        copyFigure(originalFigure, transformedFigure);
        scaleVertices(transformedFigure, initialScale);
        scaleVertices(transformedFigure, factor);

        projectedFigure = projectFigure();

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
        copyFigure(originalFigure, transformedFigure);
        rotateVerticesX(transformedFigure, initialAngleX);
        rotateVerticesX(transformedFigure, angle);

        projectedFigure = projectFigure();

        draw();
        initialAngleX += angle;
    }
    public void rotateFigureY(double angle) {
        copyFigure(originalFigure, transformedFigure);

        rotateVerticesY(transformedFigure, initialAngleY);
        rotateVerticesY(transformedFigure, angle);

        projectedFigure = projectFigure();

        draw();
        initialAngleY += angle;
    }
    public void rotateFigureZ(double angle) {
        copyFigure(originalFigure, transformedFigure);
        rotateVerticesZ(transformedFigure, initialAngleZ);
        rotateVerticesZ(transformedFigure, angle);

        projectedFigure = projectFigure();

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