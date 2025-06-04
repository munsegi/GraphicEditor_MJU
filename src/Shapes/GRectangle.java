package Shapes;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.Graphics2D;
import java.awt.Point;

public class GRectangle extends GShape {
	private Rectangle2D.Float rectangle;	

	
	public GRectangle() {
		super(new Rectangle2D.Float(0, 0, 0, 0));
		this.rectangle = (Float) this.getShape();
		this.affineTransform = new AffineTransform();
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
	}

	@Override
	public void setPoint(int x, int y) {
		this.rectangle.setFrame(x, y, 0, 0);
	}

	@Override
	public void drag(int x, int y) {
		float width = x - (float) rectangle.getX();
		float height = y - (float) rectangle.getY();
		this.rectangle.setFrame(rectangle.getX(), rectangle.getY(), width, height);
	}

	public boolean contains(Point p) {
		return rectangle.contains(p);
	}

	@Override
	public void finishPoint(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPoint(int x, int y) {
		// TODO Auto-generated method stub

	}

	public Rectangle2D getRectangle() {
		// TODO Auto-generated method stub
		return null;
	}

	public Rectangle2D.Float getFrame() {
		return this.rectangle;
	}

	@Override
	public void move(int dx, int dy) {
		translate(dx, dy);
	}


}
