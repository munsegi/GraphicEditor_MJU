package GraphicEditor;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Gmainframe extends JFrame {
	private static final long serialVersionUID = 1L; //8byte
	//extends JFrame 안에 속성의 정의가 포함됨.
	private GMenuBar menuBar; //8byte 자바는 주소를 8바이트 사용
	private GToolBar toolBar; //8byte
	private GDrawingPanel drawingPanel; //8byte
	
	//생성자 
	public Gmainframe() {
		// 속성 attributes 값 설정
		this.setLocation(100, 200);
		this.setSize(600, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//자식 components - 계층 구조

		
		this.menuBar = new GMenuBar();
		this.setJMenuBar(menuBar); //자식으로 등록

		this.setLayout(new BorderLayout()); //부모에서 자식들의 레이아웃을 세팅함.
		
		this.toolBar = new GToolBar();
		this.add(toolBar, BorderLayout.NORTH); //자식으로 등록
		
		this.drawingPanel = new GDrawingPanel();
		this.add(drawingPanel, BorderLayout.CENTER); //자식으로 등록
		

		
	}

	//헹위 - 메서드 영역
	public void initialize() {
		// associated attributes
		this.setVisible(true); //화면에 출력
		
		this.menuBar.initialize();
		this.toolBar.initialize();
		this.drawingPanel.initialize();
		
	}
}
