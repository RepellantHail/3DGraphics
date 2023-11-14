public class Figure {
    Arista aristas[];
    Vertice vertices;
    public Figure(int figureID){
        switch (figureID){
            case 1 -> initializeCube();
            case 2 -> initializePyramid();
            default -> initializeCube();
        }
    }
    private void initializeCube(){
    }
    private void initializePyramid(){
        Vertice vertices = new Vertice();
        vertices.initializePyramid();//Initialize vertice

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
        return aristas;
    }

    public Point3D[] getVertices(){
        return vertices.getVertices();
    }
}
