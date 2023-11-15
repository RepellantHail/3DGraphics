public class Figure {
    Arista aristas[];
    Point3D vertices[];
    private int size;  // Size of the cube
    public Figure(int figureID){
        this.size = 100;
        switch (figureID){
            case 1 -> initializeCube();
            case 2 -> initializePyramid();
            default -> initializeCube();
        }
    }
    public void setSize(int size) {
        this.size = size;
    }
    private void initializeCube(){
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
    private void initializePyramid(){
        //Set vertices
        this.vertices = new Point3D[5];
            vertices[0] =  new Point3D(0, size, 0);
            vertices[1] =  new Point3D(0, size, size);
            vertices[2] =  new Point3D(size, size, size);
            vertices[3] =  new Point3D(size, size, 0);
            vertices[4] =  new Point3D(size / 2, 0, size/2);

        //Set arista
        aristas = new Arista[8];
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

    public Arista[] getAristas(){
        return this.aristas;
    }

    public Point3D[] getVertices(){
        return this.vertices;
    }
}