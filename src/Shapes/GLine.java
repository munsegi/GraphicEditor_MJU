package Shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class GLine extends GShape {
    private Line2D.Double line;
    private double startX, startY;
    
    public GLine() {
    	super(new Line2D.Double());
        this.line = (Double) this.getShape();
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
        line.setLine(x, y, x, y);
    }

    
    @Override
    public void drag(int x, int y) {
        line.setLine(startX, startY, x, y);
    }

    public void move(int dx, int dy) {
    	translate(dx, dy);
    }

    public boolean contains(Point p) { 
    	Point2D pt;
		try {
			pt = affineTransform.inverseTransform(p, null);
			return line.ptSegDist(pt) <= 3.0;
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			return false;
		}
    	
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
