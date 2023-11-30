public class RubikCube {
    private Figure[][][] cubes;
    private int cubeSize;
    private int cubePadding;
    private int cubeLength;//length of each side
    public RubikCube(int cubeSize, int cubeSpacing) {
        this.cubeSize = cubeSize;
        this.cubePadding = cubeSpacing;
        this.cubes = new Figure[cubeSize][cubeSize][cubeSize];
        initializeCube();
    }
    private void initializeCube() {
        for (int x = 0; x < cubeSize; x++) {
            for (int y = 0; y < cubeSize; y++) {
                for (int z = 0; z < cubeSize; z++) {
                    Figure cube = new Figure(1);
                    int paddingX = x * (cubePadding + cube.getSize());
                    int paddingY = y * (cubePadding + cube.getSize());
                    int paddingZ = z * (cubePadding + cube.getSize());

                    translateVertices(cube, paddingX,paddingY, paddingZ);

                    cubes[x][y][z] = cube;
                }
            }
        }
    }
    private void translateVertices(Figure figure, int deltaX, int deltaY, int deltaZ) {
        for (Point3D vertex : figure.getVertices()) {
            vertex.setX(vertex.getX() + deltaX);
            vertex.setY(vertex.getY() + deltaY);
            vertex.setZ(vertex.getZ() + deltaZ);
        }
    }
    public Figure[][][] getFigures() {
        return this.cubes;
    }

}
