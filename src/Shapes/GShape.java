package Shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

import global.GConstants.EAnchor;

public abstract class GShape implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final static int ANCHOR_W = 10;
	private final static int ANCHOR_H = 10;
	int px, py;
	
	public enum EPoints {
		e2P, eNP,
	}

	

	protected AffineTransform affineTransform;
	private Shape shape;
	
	private Ellipse2D[] anchors;
	private boolean bSelected;
	private EAnchor eSelectedAnchor;

	
	public GShape(Shape shape) {
		
		this.shape = shape;
		this.affineTransform = new AffineTransform();
		this.anchors = new Ellipse2D[EAnchor.values().length - 1];
		for (int i = 0; i < this.anchors.length; i++) {
			this.anchors[i] = new Ellipse2D.Double();
		}

		this.bSelected = false;
		this.eSelectedAnchor = null;
	}
	
	//getters and setters
	public Shape getShape() {
		return this.shape;
	}
	
	public boolean isSelected() {
		return this.bSelected;
	}
	
	public void setSelected(boolean bSelected) {
		this.bSelected = bSelected;
	}
	
	public EAnchor getESelectedAnchor() {
		return this.eSelectedAnchor;
	}
	
	
	//methods
	private void setAnchors() {
		Shape transformed = this.affineTransform.createTransformedShape(shape);
		Rectangle bounds = transformed.getBounds();
		
		int bx = bounds.x, by = bounds.y;
		int bw = bounds.width, bh = bounds.height;

		int cx = 0, cy = 0;
		for (int i = 0; i < this.anchors.length; i++) {
			switch (EAnchor.values()[i]) {
			case eNN: cx = bx + bw / 2; cy = by; 			break;
			case eNE: cx = bx + bw; 	cy = by; 		break;
			case eNW: cx = bx; 			cy = by; 		break;
			case eSS: cx = bx + bw / 2; cy = by + bh; 		break;
			case eSE: cx = bx + bw; 	cy = by + bh; 		break;
			case eSW: cx = bx; 			cy = by + bh; 		break;
			case eEE: cx = bx + bw; 	cy = by + bh / 2; 	break;
			case eWW: cx = bx; 			cy = by + bh / 2; 	break;
			case eRR: cx = bx + bw / 2; cy = by - 30; 		break;
			default: break;
			}
			anchors[i].setFrame(cx-ANCHOR_W/2, cy-ANCHOR_H/2, ANCHOR_W, ANCHOR_H);		
		}
		
	}
	
	public EAnchor getOppositeAnchor(EAnchor anchor) {
	    switch(anchor) {
	        case eNW: return EAnchor.eSE;
	        case eNE: return EAnchor.eSW;
	        case eSW: return EAnchor.eNE;
	        case eSE: return EAnchor.eNW;
	        case eNN: return EAnchor.eSS;
	        case eSS: return EAnchor.eNN;
	        case eEE: return EAnchor.eWW;
	        case eWW: return EAnchor.eEE;
	        case eMM: 
	        case eRR: 
	        default:   return EAnchor.eMM;
	    }
	}

	public void draw(Graphics2D graphics2D) {
		Shape transformedShape = affineTransform.createTransformedShape(shape);
		graphics2D.draw(transformedShape);
		if (bSelected) {
			this.setAnchors();
			Color pen = graphics2D.getColor();
			for (Ellipse2D anchor : anchors) {
				graphics2D.setColor(graphics2D.getBackground());
				graphics2D.fill(anchor);          
				graphics2D.setColor(pen);
				graphics2D.draw(anchor);
	        }
		}
	}

	public boolean contains(GShape shape) {
		return this.shape.contains(shape.getBounds());
	}
	

	public Rectangle getBounds() {
		Shape transformed = this.affineTransform.createTransformedShape(shape);
		Rectangle bounds = transformed.getBounds();
		
		return bounds;
	}

	public boolean contains(int x, int y) {
		for (int i = 0; i < this.anchors.length; i++) {
	        // 변환을 한 번 더 줄 필요가 없다
	        if (anchors[i].contains(x, y)) {
	            this.eSelectedAnchor = EAnchor.values()[i];
	            return true;
	        }
	    }
		Shape transformed = affineTransform.createTransformedShape(shape);
		if (transformed.contains(x, y))  {
			this.eSelectedAnchor = EAnchor.eMM;
			return true;
		}
		return false;
	}
	//현재 변환을 복사해서 돌려줌 
	public AffineTransform getAffineTransform() {
//	    return (AffineTransform) affineTransform.clone();
	    return this.affineTransform;
	}
	
	// 외부에서 변환 전체를 교체할 때 사용 
	public void setTransform(AffineTransform tx) {
	    this.affineTransform = (AffineTransform) tx.clone();
	}
	
	//앵커를 기준으로 스케일링
	public void scale(double sx, double sy, double anchorX, double anchorY) {
		
	    affineTransform.translate(anchorX, anchorY); // 원점을 이동시키고
	    affineTransform.scale(sx, sy); // 변형시킴
	    affineTransform.translate(-anchorX, -anchorY); // 다시 원상복구
	    
	    // sx, sy만 가지고 계산을 할 수 있게 원점을 이동시키는 과정임
	}
	
	
	public abstract void setPoint(int x, int y);

	public abstract void drag(int x, int y);

	public abstract void finishPoint(int x, int y);

	public abstract void addPoint(int x, int y);
	
	public void translate(int tx, int ty) {
		this.affineTransform.translate(tx, ty);
	}

	public abstract void move(int dx, int dy);

	public Point getAnchorCenter(EAnchor anchor) {
        // 1) AffineTransform이 적용된 도형을 얻고
        Shape transformed = affineTransform.createTransformedShape(shape);
        // 2) 이 도형의 경계 사각형을 계산
        Rectangle bounds = transformed.getBounds();
        int bx = bounds.x, by = bounds.y;
        int bw = bounds.width, bh = bounds.height;

        int cx = bx, cy = by;
        switch (anchor) {
            case eNN: cx = bx + bw / 2;       cy = by;             break;
            case eNE: cx = bx + bw;           cy = by;             break;
            case eNW: cx = bx;                cy = by;             break;
            case eSS: cx = bx + bw / 2;       cy = by + bh;        break;
            case eSE: cx = bx + bw;           cy = by + bh;        break;
            case eSW: cx = bx;                cy = by + bh;        break;
            case eEE: cx = bx + bw;           cy = by + bh / 2;    break;
            case eWW: cx = bx;                cy = by + bh / 2;    break;
            case eRR: 
            case eMM:  
            default:
                break;
        }
        return new Point(cx, cy);
    }

	public Shape getTransformedShape() {
		Shape transformed = affineTransform.createTransformedShape(shape);
		return transformed;
	}
}
