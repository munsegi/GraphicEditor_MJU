package transformer;

import java.awt.Graphics2D;

import Shapes.GShape;

public class GDrawer extends GTransformer{
	
	private GShape shape;
	
	public GDrawer(GShape shape) {
		super(shape);
		this.shape = shape;
	}

	@Override
    public void start(Graphics2D graphics, int x, int y) {
        shape.setPoint(x, y);
    }

	@Override
    public void drag(Graphics2D graphics, int x, int y) {
        shape.drag(x, y);
    }

	@Override
    public void finish(Graphics2D graphics, int x, int y) {
        shape.finishPoint(x, y);
    }

	@Override
	public void addPoint(Graphics2D graphics, int x, int y) {
		shape.addPoint(x, y);
	}

}
