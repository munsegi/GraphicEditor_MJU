package frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import global.GConstants.EShapeTool;

public class GShapeToolBar extends JToolBar {
	private static final long serialVersionUID = 1L;

	
	//component 내가 new를 해서 등록하는 것
	
	//association 부모가 이미 만들어진 자식을 나에게 연결해주는것 주소를 얼려줌
	GDrawingPanel drawingPanel;
	
	public GShapeToolBar() {
		ButtonGroup buttonGroup = new ButtonGroup();
		
		for(EShapeTool eShapeType: EShapeTool.values()) {
			JRadioButton radioButton = new JRadioButton(eShapeType.getName());
			ActionHandler actionHandler = new ActionHandler();
			radioButton.addActionListener(actionHandler);
			radioButton.setActionCommand(eShapeType.toString());
			
			buttonGroup.add(radioButton);
			this.add(radioButton);
		}
		//반복되는 내용을 설정 파일을 만들수도 있다
		//4개의 이벤트 핸들러가 각각의 radioButton에 달려있음
	}

	public void initialize() {
		JRadioButton button = (JRadioButton) this.getComponent(EShapeTool.eSelect.ordinal());
		button.doClick();
		
	}

	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		
	}
	
	private class ActionHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String sShapeType = e.getActionCommand();
			EShapeTool eShapeType = EShapeTool.valueOf(sShapeType);
			drawingPanel.setEShapeType(eShapeType);
		}
	} // 메인 프레임에 필요한 내용.
}
