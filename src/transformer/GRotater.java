package transformer;

import java.awt.Graphics2D;

import Shapes.GShape;

public class GRotater extends GTransformer{

	private int anchorX;
	private int anchorY;

	public GRotater(GShape shape, int aX, int aY) {
		super(shape);
		this.anchorX = aX;
		this.anchorY = aY;
	}

	@Override
	public void start(Graphics2D graphics, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish(Graphics2D graphics, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPoint(Graphics2D graphics, int x, int y) {
		// TODO Auto-generated method stub
		
	}

}
