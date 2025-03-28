package GraphicEditor;

import java.awt.FlowLayout;

import javax.swing.JRadioButton;
import javax.swing.JToolBar;

public class GToolBar extends JToolBar {
	private static final long serialVersionUID = 1L;
	
	private JRadioButton rectangleButton;
	private JRadioButton TriangleButton;
	private JRadioButton OvalButton;
	private JRadioButton PolyGonButton;
	private JRadioButton TextBoxButton;
	
	//자바의 레이아웃 조사
	
	public GToolBar() {

		this.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		//반복되는 내용을 설정 파일을 만들수도 있다
		this.rectangleButton = new JRadioButton("Rectangle");
		this.add(this.rectangleButton);
		
		this.TriangleButton = new JRadioButton("Triangle");
		this.add(this.TriangleButton);
		
		this.OvalButton = new JRadioButton("Oval");
		this.add(this.OvalButton);
		
		this.PolyGonButton = new JRadioButton("PolyGon");
		this.add(this.PolyGonButton);
		
		this.TextBoxButton = new JRadioButton("Text");
		this.add(this.TextBoxButton);
		
		
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}
}
