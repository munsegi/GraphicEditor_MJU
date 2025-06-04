package transformer;

import java.awt.Graphics2D;

import Shapes.GShape;

public abstract class GTransformer {
	
	protected GShape shape;
	
    public GTransformer(GShape shape) {
        this.shape = shape;
    }
	
    public abstract void start(Graphics2D graphics, int x, int y);
    public abstract void drag(Graphics2D graphics, int x, int y);
    public abstract void finish(Graphics2D graphics, int x, int y);
	public abstract void addPoint(Graphics2D graphics, int x, int y);
}
