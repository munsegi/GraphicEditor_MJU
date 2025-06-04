package Shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

public class GTriangle extends GShape {
    private Polygon triangle;
    private int startX, startY;

    public GTriangle() {
    	super(new Polygon());
        this.triangle = (Polygon) this.getShape();
        this.affineTransform = new AffineTransform();
    }

    @Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
	}

    @Override
    public void setPoint(int x, int y) {
        this.startX = x;
        this.startY = y;
        this.triangle.reset();
        this.triangle.addPoint(x, y);
        this.triangle.addPoint(x, y);
        this.triangle.addPoint(x, y);
    }

    @Override
    public void drag(int x, int y) {
        triangle.xpoints[0] = startX;
        triangle.ypoints[0] = y;

        triangle.xpoints[1] = x;
        triangle.ypoints[1] = y;

        triangle.xpoints[2] = (startX + x) / 2;
        triangle.ypoints[2] = startY;

        triangle.invalidate();
    }
    

    public boolean contains(Point p) {
        return triangle.contains(p);
    }

	@Override
	public void finishPoint(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPoint(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move(int dx, int dy) {
		translate(dx, dy);
	}

}