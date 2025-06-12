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
    public void setPoint(int x, int y) {   // 첫 클릭
        polygon.reset();
        polygon.addPoint(x, y);            // 고정된 첫 점
        polygon.addPoint(x, y);            // 커서
    }

    @Override
    public void addPoint(int x, int y) {   // 단일 클릭(add vertex)
        int n = polygon.npoints;
        // 커서 위치를 “확정된 점”으로 대체
        polygon.xpoints[n-1] = x;
        polygon.ypoints[n-1] = y;
        // 그리고 다시 새 placeholder 추가
        polygon.addPoint(x, y);
        polygon.invalidate();
    }

    @Override
    public void drag(int x, int y) {       // mouseMoved
        int n = polygon.npoints;
        if (n == 0) return;
        polygon.xpoints[n-1] = x;          // 커서 갱신
        polygon.ypoints[n-1] = y;
        polygon.invalidate();
        
    }
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
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
	public void finishPoint(int x, int y) {
		// 더블 클릭 때 마지막 꼭짓점 추가
		this.polygon.addPoint(x, y);
	}



}