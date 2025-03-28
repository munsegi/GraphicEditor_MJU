package GraphicEditor;

import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JPanel;

public class GDrawingPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Rectangle rectToDraw = null;
	
	public GDrawingPanel(){
		//TODO 
		//마우스 이벤트를 받는 이벤트 핸들러 추가
	}
	
	
	
	@Override //부모가 해야 할 일을 대체함
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics); // 본래 부모가 할 일을 호출함. 오버라이딩 공식임.
		this.draw(graphics); //+네모 그려라.
		
	}
	
	
	public void draw(Graphics graphics) {
		graphics.drawRect(10, 10, 50, 50);
		
	}
	
	public void initialize() {
		
	}
	
}
