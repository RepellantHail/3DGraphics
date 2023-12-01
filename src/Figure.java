import java.awt.*;

public class Figure {
    Arista[] aristas;
    Point3D[] vertices;
    Figure[][][] cubes;
    protected int size;  // Size of the cube
    public Figure() {
        this.size = 0;
        this.vertices = new Point3D[0];
        this.aristas = new Arista[0];
    }
    public Figure(int figureID){
        this.size = 60;
        switch (figureID){
            case 1 -> initializeCube();
            case 2 -> initializePyramid();
            case 3-> initializeCurve();
            case 4-> initializeSurface();
            case 5-> initializeCylinder();
            case 6-> initializeRubik();
            default -> initializeCube();
        }
    }
    public Figure(Figure other)  {
        // Copy size
        this.size = other.size;

        // Copy vertices
        this.vertices = new Point3D[other.vertices.length];
        for (int i = 0; i < other.vertices.length; i++) {
            this.vertices[i] = new Point3D(other.vertices[i]);
        }

        // Copy aristas
        this.aristas = new Arista[other.aristas.length];
        for (int i = 0; i < other.aristas.length; i++) {
            this.aristas[i] = new Arista(other.aristas[i]);
        }

        // Copy cubes
        if (other.cubes != null) {
            this.cubes = new Figure[other.cubes.length][other.cubes[0].length][other.cubes[0][0].length];
            for (int x = 0; x < other.cubes.length; x++) {
                for (int y = 0; y < other.cubes[x].length; y++) {
                    for (int z = 0; z < other.cubes[x][y].length; z++) {
                        this.cubes[x][y][z] = new Figure(other.cubes[x][y][z]);
                    }
                }
            }
        }
    }
    public void setSize(int size) {
        this.size = size;
    }
    public int getSize() {
        return size;
    }
    private void initializeCube(){
        int l = size;//Length of the cube
        this.vertices = new Point3D[]{
                new Point3D(0, 0, 0),
                new Point3D(0, 0,   l),
                new Point3D(  l, 0,   l),
                new Point3D(  l, 0, 0),
                new Point3D(  l,   l, 0),
                new Point3D(  l,   l,    l),
                new Point3D(0,  l,    l),
                new Point3D(0,  l, 0),
        };
        this.aristas = new Arista[12];
        aristas[0]  = new Arista(0, 1);
        aristas[1]  = new Arista(0, 3);
        aristas[2]  = new Arista(0, 7);

        aristas[3]  = new Arista(4, 3);
        aristas[4]  = new Arista(4, 5);
        aristas[5]  = new Arista(4, 7);

        aristas[6]  = new Arista(2, 1);
        aristas[7]  = new Arista(2, 3);
        aristas[8]  = new Arista(2, 5);

        aristas[9 ] = new Arista(6, 1);
        aristas[10] = new Arista(6, 5);
        aristas[11] = new Arista(6, 7);
    }
    private void initializePyramid(){
        //Set vertices
        this.vertices = new Point3D[5];
            vertices[0] =  new Point3D(0, 10, 0);
            vertices[1] =  new Point3D(0, 10, 10);
            vertices[2] =  new Point3D(10, 10, 10);
            vertices[3] =  new Point3D(10, 10, 0);
            vertices[4] =  new Point3D(10 / 2, 0, 10/2);

        //Set arista
        this.aristas = new Arista[8];
        //Base piramide
        aristas[0] = new Arista(0, 1);
        aristas[1] = new Arista(1, 2);
        aristas[2] = new Arista(2, 3);
        aristas[3] = new Arista(3, 0);
        //Punta piramide
        aristas[4] = new Arista(4, 0);
        aristas[5] = new Arista(4, 1);
        aristas[6] = new Arista(4, 2);
        aristas[7] = new Arista(4, 3);
    }
    private void initializeCurve() {
        int curvePoints = 100;
        this.vertices = new Point3D[curvePoints];

        for (int i = 0; i < curvePoints; i++) {
            double t = (2 * Math.PI * i) / (curvePoints - 1); // t ranges from 0 to 2pi

            // Parametric equations for a heart shape
            double x = 16 * Math.pow(Math.sin(t), 3);
            double y = 13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t);

            this.vertices[i] = new Point3D((int) (x * 10), (int) (y * 10), 0); // Scaling by 80 for visualization
        }

        // Connect points to form edges
        this.aristas = new Arista[curvePoints];
        for (int i = 0; i < curvePoints; i++) {
            int nextIndex = (i + 1) % curvePoints; // Use modulo to wrap around

            this.aristas[i] = new Arista(i, nextIndex);
        }
    }
    private void initializeSurface() {
        int uPoints = 30; // Number of points along u direction
        int vPoints = 20; // Number of points along v direction
        this.vertices = new Point3D[uPoints * vPoints];

        double A = 2.0; // Amplitude of the sine function
        double B = 1.0; // Frequency of the sine function

        for (int i = 0; i < uPoints; i++) {
            for (int j = 0; j < vPoints; j++) {
                double x = (2 * Math.PI * i) / (uPoints - 1); // u ranges from 0 to 2pi
                double y = (2 * Math.PI * j) / (vPoints - 1); // v ranges from 0 to 2pi
                double z = A * Math.sin(B * x);

                int index = i * vPoints + j;
                int size = 20;
                this.vertices[index] = new Point3D((int) (x * size), (int) (y * size), (int) (z * size)); // Scaling for visualization
            }
        }

        // Connect points to form edges
        int numEdges = 2 * (uPoints - 1) * vPoints; // Assuming a rectangular grid
        this.aristas = new Arista[numEdges];

        int edgeIndex = 0;
        for (int i = 0; i < uPoints - 1; i++) {
            for (int j = 0; j < vPoints; j++) {
                int current = i * vPoints + j;
                int nextU = ((i + 1) % uPoints) * vPoints + j;

                // Set color gradient based on the position of vertices
                Color color = getColor(i, j);

                this.aristas[edgeIndex++] = new Arista(current, nextU, color);

                int nextV = i * vPoints + (j + 1) % vPoints;

                // Set color gradient based on the position of vertices
                color = getColor(i, (j + 1) % vPoints);

                this.aristas[edgeIndex++] = new Arista(current, nextV, color);
            }
        }
    }
    private void initializeCylinder() {
        int tPoints = 30; // Number of points along t direction
        int dPoints = 20; // Number of points along d direction
        this.vertices = new Point3D[tPoints * dPoints];

        for (int i = 0; i < tPoints; i++) {
            for (int j = 0; j < dPoints; j++) {
                double t = (2 * Math.PI * i) / (tPoints - 1); // t ranges from 0 to 2pi
                double d = (2 * Math.PI * j) / (dPoints - 1); // d ranges from 0 to 2pi

                // Parametric equations for a cylinder
                double x = (2 + Math.cos(t)) * Math.cos(d);
                double y = (2 + Math.cos(t)) * Math.sin(d);
                double z = t;

                int index = i * dPoints + j;
                int size = 20;
                this.vertices[index] = new Point3D((int) (x * size), (int) (y * size), (int) (z * size)); // Scaling for visualization
            }
        }

        // Connect points to form edges
        int numEdges = 2 * (tPoints - 1) * dPoints; // Assuming a rectangular grid
        this.aristas = new Arista[numEdges];

        int edgeIndex = 0;
        for (int i = 0; i < tPoints - 1; i++) {
            for (int j = 0; j < dPoints; j++) {
                int current = i * dPoints + j;
                int nextT = ((i + 1) % tPoints) * dPoints + j;

                // Set color gradient based on the position of vertices
                Color color = getColor(i, j);

                this.aristas[edgeIndex++] = new Arista(current, nextT, color);

                int nextD = i * dPoints + (j + 1) % dPoints;

                // Set color gradient based on the position of vertices
                color = getColor(i, (j + 1) % dPoints);

                this.aristas[edgeIndex++] = new Arista(current, nextD, color);
            }
        }
    }
    public void setVertices(Point3D[] vertices) {
        this.vertices = vertices;
    }
    public void setAristas(Arista[] aristas) {
        this.aristas = aristas;
    }
    public Arista[] getAristas(){
        return this.aristas;
    }
    public Point3D[] getVertices(){
        return this.vertices;
    }
    public void addVertex(int x, int y, int z) {
        // Create a new array to hold the updated vertices
        Point3D[] newVertices = new Point3D[vertices.length + 1];

        // Copy existing vertices to the new array
        System.arraycopy(vertices, 0, newVertices, 0, vertices.length);

        // Add the new vertex to the end of the array
        newVertices[vertices.length] = new Point3D(x, y, z);

        // Update the vertices array
        vertices = newVertices;
    }
    protected double getMinZ() {
        double minZ = Double.MAX_VALUE;

        for (Point3D vertex : vertices) {
            if (vertex.getZ() < minZ) {
                minZ = vertex.getZ();
            }
        }

        return minZ;
    }
    protected double getMaxZ() {
        double maxZ = Double.MIN_VALUE;

        for (Point3D vertex : vertices) {
            if (vertex.getZ() > maxZ) {
                maxZ = vertex.getZ();
            }
        }

        return maxZ;
    }
    private Color getColor(int i, int j) {
        // Adjust the factor to control the range of the color gradient
        double factor = 2.0;

        // Add epsilon to denominators to avoid division by zero
        int red = (i != 0) ? (int) (factor * 255 / (i + 1)) : 0;
        int blue = (j != 0) ? (int) (factor * 255 / (j + 1)) : 0;
        int green = 0;

        // Clamp color values to the range [0, 255]
        red   = Math.max(0, Math.min(255, red + 100));
        blue  = Math.max(0, Math.min(255, blue + 100));
        green = Math.max(0, Math.min(255, green));

        return new Color(red, green, blue);
    }
    private void translateVertices(Figure figure, int deltaX, int deltaY, int deltaZ) {
        for (Point3D vertex : figure.getVertices()) {
            vertex.setX(vertex.getX() + deltaX);
            vertex.setY(vertex.getY() + deltaY);
            vertex.setZ(vertex.getZ() + deltaZ);
        }
    }
    protected void initializeRubik(){
        int cubeSize = 3;
        int cubeSpacing = 20;
        this.cubes = new Figure[cubeSize][cubeSize][cubeSize];

        // Loop through each position in the Rubik's Cube
        for (int x = 0; x < cubeSize; x++) {
            for (int y = 0; y < cubeSize; y++) {
                for (int z = 0; z < cubeSize; z++) {
                    // Calculate the position of the current cube
                    int cubeX = x * cubeSpacing;
                    int cubeY = y * cubeSpacing;
                    int cubeZ = z * cubeSpacing;

                    Figure cube = new Figure(1);
                    translateVertices(cube, cubeX, cubeY, cubeZ);
                    cubes[x][y][z] = cube;
                }
            }
        }

    }
}
