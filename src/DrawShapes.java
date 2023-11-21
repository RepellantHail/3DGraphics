import java.awt.*;

public class DrawShapes {
    private Draw canvas;
    private Figure figure;
    private Point[] projectedFigure;
    private int figureID;
    private Color color;
    private float distance = 1;
    DrawShapes(Draw painter, int figureID){
        this.figure = new Figure(figureID);
        this.figure.setSize(100);
        this.canvas = painter;
        this.figureID = figureID;
    }
    public void initializeFigure(Color color, int projection){
        this.color = color;

        if(projection == 1)
            projectedFigure = parallelProjection(figure.getVertices());
        else
            projectedFigure = perspectiveProjection(figure.getVertices());

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

        for (int i = 0; i < vertices.length; i++) {
            float z = 1 / (distance - vertices[i].getZ());
            int projectedX = (int) (vertices[i].getX() * z);
            int projectedY = (int) (vertices[i].getY() * z);

            if(projectedX < 0) projectedX = 0;
            if(projectedY < 0) projectedY = 0;

            projectedPoints[i] = new Point(projectedX, projectedY);
        }

        return projectedPoints;
    }
    public void draw(){
        Arista[] figura = figure.getAristas();

        canvas.clearBuffer();
        for(Arista a: figura) { //Dibujar Linea de arista a arista
            canvas.drawLine(projectedFigure[a.getVerticeOrigen ()].x, projectedFigure[a.getVerticeOrigen ()].y,
                            projectedFigure[a.getVerticeDestino()].x, projectedFigure[a.getVerticeDestino()].y, color
            );
        }
        canvas.repaint();
    }
    public void translateFigure(int direction, int step){
        for (Point p : projectedFigure){
            if(direction == 0)
                p.x += step;
            else
                p.y += step;
        }

    }
    public void scaleFigure(double factor){
        for (Point p : projectedFigure){
                p.x *= factor;
                p.y *= factor;
        }
    }
    public void rotateFigureX(double angle) {
        Figure tempShape = figure;
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);

        for (int i = 0; i < tempShape.getVertices().length; i++) {
            Point3D vertex = tempShape.getVertices()[i];
            int y = vertex.getY();
            int z = vertex.getZ();

            // Apply the 3D rotation matrix
            int newY = (int) (y * cosAngle - z * sinAngle);
            int newZ = (int) (y * sinAngle + z * cosAngle);

            // Update the Y and Z coordinates of the rotated point
            vertex.setY(newY);
            vertex.setZ(newZ);
        }

        // Redraw the rotated shape
        projectedFigure = perspectiveProjection(tempShape.getVertices());
    }

}
