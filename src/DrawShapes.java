import java.awt.*;
import java.util.Arrays;

public class DrawShapes {
    private final Draw canvas;
    private Figure originalFigure;
    private Figure transformedFigure;
    private Point[] projectedFigure;
    private int figureID;
    private Color color;
    private int centerX;
    private int centerY;
    private double initialScale = 15;
    private double initialAngleX = 0;
    private double initialAngleY = 0;
    private double initialAngleZ = 0;
    private RubikCube rubikCube;
    public DrawShapes(Draw canvas){//Contructor for rubik Cube
        this.canvas = canvas;
        this.color = Color.black;
        this.centerX = canvas.getWidth() / 2;
        this.centerY = canvas.getWidth() / 2;

        //Parameters to create Cube
        int cubeSize = 3;//Number of cubes 3x3x3
        int cubeSpacing = 5; //Space between each cube

        this.rubikCube = new RubikCube(cubeSize,cubeSpacing);

        //Each cube has a length of 60
        centerX -= (cubeSize * 60)/2;
        centerY -= (cubeSize * 60)/2;

        initializeRubikCubeAndDraw();
    }
    public DrawShapes(Draw canvas, int figureID){
        this.canvas = canvas;
        this.figureID = figureID;
        this.color = Color.black;
        this.centerX = canvas.getWidth() / 2;
        this.centerY = canvas.getWidth() / 2;
        this.originalFigure = new Figure(figureID);
        this.transformedFigure = new Figure(figureID);
        this.projectedFigure = parallelProjection(originalFigure.getVertices());
        draw();
    }
    public DrawShapes(Draw canvas, int figureID, int projection) {
        this.canvas = canvas;
        this.figureID = figureID;
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
    this.projectedFigure = parallelProjection(originalFigure.getVertices());

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
        for (Arista a : figura) {
            int x0 = projectedFigure[a.getVerticeOrigen()].x;
            int y0 = projectedFigure[a.getVerticeOrigen()].y;
            int x1 = projectedFigure[a.getVerticeDestino()].x;
            int y1 = projectedFigure[a.getVerticeDestino()].y;

            canvas.drawLine(x0, y0, x1, y1, a.getColor());
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

        projectedFigure = parallelProjection(transformedFigure.getVertices());

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
    public void translateFigure(int deltaX, int deltaY, int deltaZ) {
        translateVertices(transformedFigure, deltaX, deltaY, deltaZ);
        //Update center
        centerX += deltaX;
        centerY += deltaY;

        this.projectedFigure = parallelProjection(transformedFigure.getVertices()); // Re-project the translated vertices
        draw(); // Draw the updated figure
    }
    private void translateVertices(Figure figure,int deltaX, int deltaY, int deltaZ){
        for (Point3D vertex : figure.getVertices()) {
            vertex.setX(vertex.getX() + deltaX);
            vertex.setY(vertex.getY() + deltaY);
            vertex.setZ(vertex.getZ() + deltaZ);
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
    public void rotateFigureX(double angle){
        copyFigure(originalFigure, transformedFigure);
        rotateVerticesX(transformedFigure, initialAngleX);
        rotateVerticesX(transformedFigure, angle);

        projectedFigure = parallelProjection(transformedFigure.getVertices());

        draw();
        initialAngleX += angle;
    }
    public void rotateFigureY(double angle) {
        copyFigure(originalFigure, transformedFigure);

        rotateVerticesY(transformedFigure, initialAngleY);
        rotateVerticesY(transformedFigure, angle);

        projectedFigure = parallelProjection(transformedFigure.getVertices());

        draw();
        initialAngleY += angle;
    }
    public void rotateFigureZ(double angle) {
        copyFigure(originalFigure, transformedFigure);
        rotateVerticesZ(transformedFigure, initialAngleZ);
        rotateVerticesZ(transformedFigure, angle);

        projectedFigure = parallelProjection(transformedFigure.getVertices());

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
    private void initializeRubikCubeAndDraw() {
        Figure[][][] rubikFigures = rubikCube.getFigures();

        for (Figure[][] rubikFigure : rubikFigures) {
            for (int y = 0; y < rubikFigure.length; y++) {
                for (int z = 0; z < rubikFigure[y].length; z++) {
                    Figure cube = new Figure(rubikFigure[y][z]);
                    drawCube(cube);
                }
            }
        }
    }
    private void drawCube(Figure cube) {
        Arista[] cubo = cube.getAristas();
        projectedFigure = parallelProjection(cube.getVertices());

        //Calculate Center
        centerX = (canvas.getWidth()   /2) - (rubikCube.getCubeSize() * 60 / 2);
        centerY = (canvas.getHeight()  /2) - (rubikCube.getCubeSize() * 60 / 2);

        for (Arista a : cubo) {
            int x0 = projectedFigure[a.getVerticeOrigen()].x  + centerX;
            int y0 = projectedFigure[a.getVerticeOrigen()].y  + centerY;
            int x1 = projectedFigure[a.getVerticeDestino()].x + centerX;
            int y1 = projectedFigure[a.getVerticeDestino()].y + centerY;

            canvas.drawLine(x0, y0, x1, y1, a.getColor());
        }

        canvas.repaint();
    }
    public void rotateCube(double angleX, double angleY, double angleZ){
        canvas.clearBuffer();

        initialAngleX += angleX; initialAngleY += angleY; initialAngleZ += angleZ;

        for (int x = 0; x < rubikCube.getCubeSize(); x++) {
            for (int y = 0; y < rubikCube.getCubeSize(); y++) {
                for (int z = 0; z < rubikCube.getCubeSize(); z++) {
                    Figure cube = rubikCube.getFigures()[x][y][z];
                    originalFigure = cube;

                    // Copy the cube before applying rotation
                    Figure rotatedCube = new Figure(cube);

                    rotateVerticesX (rotatedCube, initialAngleX);
                    rotateVerticesY (rotatedCube, initialAngleY);
                    rotateVerticesZ (rotatedCube, initialAngleZ);

                    transformedFigure = new Figure(rotatedCube);
                    drawCube(transformedFigure);
                }
            }
        }
    }
    public void resetCube() {
        this.initialAngleX = 0;
        this.initialAngleY = 0;
        this.initialAngleZ = 0;
        rotateCube(0,0,0);
    }
    public void setRubikCubeSize(int size){
        rubikCube.setCubeSize(size);
    }
}