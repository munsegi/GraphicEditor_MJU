package Shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;

public class GEllipse extends GShape {
    private Ellipse2D.Double ellipse;
    private double startX, startY;

    public GEllipse() {
    	super(new Ellipse2D.Double());
        this.ellipse = (Double) this.getShape();
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
        ellipse.setFrame(x, y, 0, 0);
    }

    @Override
    public void drag(int x, int y) {
        
    	double x0 = Math.min(startX, x);
        double y0 = Math.min(startY, y);
        double w  = (x - startX);
        double h  = (y - startY);
        
        ellipse.setFrame(x0, y0, w, h);
    }

    public void move(int dx, int dy) {
    	translate(dx, dy);
    }

    public boolean contains(Point p) {
        return ellipse.contains(p);
    }

	@Override
	public void finishPoint(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPoint(int x, int y) {
		// TODO Auto-generated method stub
		
	}


}