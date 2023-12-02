import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
public class DrawShapes {
    private final Draw canvas;
    private Figure originalFigure;
    private Figure transformedFigure;
    private Point[] projectedFigure;
    private int figureID;
    private Color color;
    private int centerX = 0;
    private int centerY = 0;
    private int centerZ = 0;
    private double initialScale;
    private double initialAngleX = 0;
    private double initialAngleY = 0;
    private double initialAngleZ = 0;
    private RubikCube rubikCube;
    private double max_XRubik = Double.NEGATIVE_INFINITY;
    private double min_XRubik = Double.POSITIVE_INFINITY;
    private double max_YRubik = Double.NEGATIVE_INFINITY;
    private double min_YRubik = Double.POSITIVE_INFINITY;
    private double max_ZRubik = Double.NEGATIVE_INFINITY;
    private double min_ZRubik = Double.POSITIVE_INFINITY;

    public DrawShapes(Draw canvas){//Contructor for rubik Cube
        this.canvas = canvas;
        this.color = Color.black;
        this.initialScale = 1.0;
        //Parameters to create Cube
        int cubeSize = 3;//Number of cubes 3x3x3
        int cubeSpacing = 2; //Space between each cube

        this.rubikCube = new RubikCube(cubeSize,cubeSpacing);

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
    public void resetCube() {
        this.initialAngleX = 0;
        this.initialAngleY = 0;
        this.initialAngleZ = 0;
        this.initialScale  = 1.0;
        this.centerX = 0;
        this.centerY = 0;
        this.centerZ = 0;
        transformCube(0,0,0, 1.0, 0, 0,0);
    }
    public void setRubikCubeSize(int size){
        rubikCube.setCubeSize(size);
    }
    protected void transformCube(double angleX, double angleY, double angleZ, double scaleFactor, int deltaX, int deltaY, int deltaZ){
        canvas.clearBuffer();

        min_XRubik = Double.POSITIVE_INFINITY;
        max_XRubik = Double.NEGATIVE_INFINITY;
        min_YRubik = Double.POSITIVE_INFINITY;
        max_YRubik = Double.NEGATIVE_INFINITY;
        max_ZRubik = Double.NEGATIVE_INFINITY;
        min_ZRubik = Double.POSITIVE_INFINITY;

        initialAngleX += angleX; initialAngleY += angleY; initialAngleZ += angleZ;
        initialScale *= scaleFactor;

        for (int x = 0; x < rubikCube.getCubeSize(); x++) {
            for (int y = 0; y < rubikCube.getCubeSize(); y++) {
                for (int z = 0; z < rubikCube.getCubeSize(); z++) {
                    Figure cube = rubikCube.getFigures()[x][y][z];
                    originalFigure = cube;

                    // Copy the cube before applying rotation
                    Figure copyCube = new Figure(cube);

                    rotateVerticesX (copyCube, initialAngleX);
                    rotateVerticesY (copyCube, initialAngleY);
                    rotateVerticesZ (copyCube, initialAngleZ);
                    scaleVertices (copyCube, initialScale);
                    translateVertices(cube, deltaX, deltaY, deltaZ);

                    transformedFigure = new Figure(copyCube);
                        drawCube(transformedFigure);
                }
            }
        }
    }
    private void initializeRubikCubeAndDraw() {
        min_XRubik = Double.POSITIVE_INFINITY;
        max_XRubik = Double.NEGATIVE_INFINITY;
        min_YRubik = Double.POSITIVE_INFINITY;
        max_YRubik = Double.NEGATIVE_INFINITY;
        Figure[][][] rubikFigures = rubikCube.getFigures();

        for (Figure[][] rubikFigure : rubikFigures) {
            for (Figure[] figures : rubikFigure) {
                for (Figure figure : figures) {
                    Figure cube = new Figure(figure);
                        drawCube(cube);
                }
            }
        }
    }
    private Boolean isCubeVisible(Figure cube){
        double minDepthCube = cube.getMinZ();
        double minXCube = cube.getMinX();

        if (minDepthCube > max_ZRubik && minXCube > max_XRubik) {
            max_ZRubik = Math.max(max_ZRubik, minDepthCube);
            max_XRubik = Math.max(max_XRubik, minXCube);
            return false; // Cube is not visible
        }
        return true;
    }
    private ArrayList<Arista> horizonteFlotante(Figure cube, Arista[] edges){
        Point3D[] vertices = cube.getVertices(); //Get vertices
        double maxDepthCube = Double.NEGATIVE_INFINITY;
        double minDepthCube = Double.POSITIVE_INFINITY;

        for(Point3D vertice : vertices) {
            maxDepthCube = Math.max(maxDepthCube, vertice.getZ());
            minDepthCube = Math.min(minDepthCube, vertice.getZ());
        }

        ArrayList<Arista> visibleAristas = new ArrayList<>();
        for (Arista edge : edges) {
            int vtx0 = edge.getVerticeOrigen();
            int vtx1 = edge.getVerticeDestino();
            if (vertices[vtx0].getZ() < maxDepthCube && vertices[vtx1].getZ() < maxDepthCube) {//Depth for a single cube
                visibleAristas.add(edge);
            }
        }

        return visibleAristas;
    }
    private void drawCube(Figure cube) {
        //Calculate Center
        int centerX = (canvas.getWidth()  / 2) - (rubikCube.getCubeSize() * 60 / 2);
        int centerY = (canvas.getHeight() / 2) - (rubikCube.getCubeSize() * 60 / 2);

        // Calculate visible edges and projected figure
        ArrayList<Arista> visibleAristas = horizonteFlotante(cube, cube.getAristas());
        projectedFigure = parallelProjection(cube.getVertices());

        /*int x0 = 0; int y0 = 0;
        int x1 = 0; int y1 = 0;
        Boolean updateAreaFirstCube = true;*/

        for (Arista edge : visibleAristas ) {
        int x0 = projectedFigure[edge.getVerticeOrigen()].x  + centerX;
        int y0 = projectedFigure[edge.getVerticeOrigen()].y  + centerY;
        int x1 = projectedFigure[edge.getVerticeDestino()].x + centerX;
        int y1 = projectedFigure[edge.getVerticeDestino()].y + centerY;
            canvas.drawLine(x0, y0, x1, y1, edge.getColor());

            //paintFace( x0 +1,  y0 +1,  x1+1,  y1+1,  Color.magenta);

           /* if(updateAreaFirstCube){//Update Area
                min_XRubik = Math.min(min_XRubik, x0);
                min_YRubik = Math.min(min_YRubik, y0);
                max_XRubik = Math.max(max_XRubik, x0);
                max_YRubik = Math.max(max_YRubik, y0);
                min_XRubik = Math.min(min_XRubik, x1);
                min_YRubik = Math.min(min_YRubik, y1);
                max_XRubik = Math.max(max_XRubik, x1);
                max_YRubik = Math.max(max_YRubik, y1);
                updateAreaFirstCube = false;
            }*/

            /*if(!isEdgeInside(x0, y0, x1, y1)) {
            }*/
        }

/*        min_XRubik = Math.min(min_XRubik, x0);
        min_YRubik = Math.min(min_YRubik, y0);
        max_XRubik = Math.max(max_XRubik, x0);
        max_YRubik = Math.max(max_YRubik, y0);
        min_XRubik = Math.min(min_XRubik, x1);
        min_YRubik = Math.min(min_YRubik, y1);
        max_XRubik = Math.max(max_XRubik, x1);
        max_YRubik = Math.max(max_YRubik, y1);*/

        canvas.repaint();
    }
    private void paintFace(int x0, int y0, int x1, int y1, Color color) {
        // Sort the points based on their y-coordinates
        if (y0 > y1) {
            int temp = y0;
            y0 = y1;
            y1 = temp;

            temp = x0;
            x0 = x1;
            x1 = temp;
        }

        // Iterate through each scanline and fill the pixels
        for (int y = y0; y <= y1; y++) {
            // Calculate the intersection point with the scanline
            int xIntersection = (int) (x0 + (double) (y - y0) / (y1 - y0) * (x1 - x0));

            // Fill the pixels between x0 and xIntersection on the scanline
            for (int x = x0; x <= xIntersection; x++) {
                canvas.putPixel(x, y, color);
            }
        }
    }

    private Boolean isEdgeInside(int x0, int y0, int x1, int y1){
        Point origin = new Point(x0, y0);
        Point destin = new Point(x1, y1);

        return !( isVertexInside(origin) || isVertexInside(destin) );
    }
    private Boolean isVertexInside(Point vertex) {
        double x = vertex.getX();
        double y = vertex.getY();
        return (x >= min_XRubik && x <= max_XRubik) && (y >= min_YRubik && y <= max_YRubik);
    }
}