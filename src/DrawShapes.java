import java.awt.*;
import java.util.Vector;

public class DrawShapes {
    Draw canvas;
    Figure  figure;
    int figureID;
    Color color;
    DrawShapes(Draw painter, int figureID){
        this.canvas = painter;
        this.figureID = figureID;
    }
    public void initializeFigure(Color color){
        figure = new Figure(figureID);
        this.color = color;
        drawFigura();
    }
    private Point[] parallelProjection(Point3D[] vertices) {
        Point[] _2dGraph = new Point[vertices.length];


        return _2dGraph;
    }

    private void drawFigura(){
        Arista[] figura = figure.getAristas();
        Point[] projectedFigure = parallelProjection(figure.getVertices());

        for(Arista a: figura) {
            //Dibujar Linea de arista a arista
            canvas.drawLine(projectedFigure[a.getVerticeOrigen ()].x, projectedFigure[a.getVerticeOrigen ()].y,
                            projectedFigure[a.getVerticeDestino()].x, projectedFigure[a.getVerticeDestino()].y, color
            );
        }
    }
}
