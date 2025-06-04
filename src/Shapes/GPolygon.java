package Shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class GPolygon extends GShape {
	private Polygon polygon;

	public GPolygon() {
		super(new Polygon());
		this.polygon = (Polygon) this.getShape();
		this.affineTransform = new AffineTransform();
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
	}

	@Override
	public void setPoint(int x, int y) {
		this.polygon.reset();
		this.polygon.addPoint(x, y);
	}

	@Override
	public void addPoint(int x, int y) {
		this.polygon.addPoint(x, y);
	}

	public void move(int dx, int dy) {
		translate(dx,dy);
	}

	public boolean contains(Point p) {
		Shape transformed = affineTransform.createTransformedShape(polygon);
		return transformed.contains(p);
	}

	// 추가된 점 개수 반환
	public int getPointCount() {
		return polygon.npoints;
	}

	// x 좌표 배열 반환 (0..n-1)
	public int[] getXPoints() {
		return polygon.xpoints;
	}

	// y 좌표 배열 반환 (0..n-1)
	public int[] getYPoints() {
		return polygon.ypoints;
	}

	@Override
	public void drag(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finishPoint(int x, int y) {
		// 더블 클릭 때 마지막 꼭짓점 추가
		this.polygon.addPoint(x, y);
	}



}