public class Vertice {
    private Point3D[] vertices;
    int size ;  // Size of the cube
    public Vertice(){

    }

    public void setVertices(Point3D[] vertices) {
        this.vertices = vertices;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public Point3D[] getVertices(){
        return this.vertices;
    }
    public Point3D getVertices(int i){
        return this.vertices[i];
    }

    public void initializeCube() {
        this.vertices = new Point3D[]{
                new Point3D(0, 0, 0),
                new Point3D(size, 0, 0),
                new Point3D(size, size, 0),
                new Point3D(0, size, 0),
                new Point3D(0, 0, size),
                new Point3D(size, 0, size),
                new Point3D(size, size, size),
                new Point3D(0, size, size)
        };

    }

    public void initializePyramid() {
        this.vertices = new Point3D[]{
                new Point3D(0, 0, 0),
                new Point3D(size, 0, 0),
                new Point3D(size, size, 0),
                new Point3D(0, size, 0),
                new Point3D(size / 2, size / 2, size)  // Apex of the pyramid
        };
    }
}
