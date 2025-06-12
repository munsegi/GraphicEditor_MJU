package frames;

import javax.swing.JMenuBar;

import menus.GEditMenu;
import menus.GFileMenu;
import menus.GGraphicMenu;

public class GMenuBar extends JMenuBar {
	
	//component 자식
	private static final long serialVersionUID = 1L;
	private GFileMenu fileMenu;
	private GEditMenu editMenu;
	private GGraphicMenu graphicMenu;
	
	//association 친구
	GDrawingPanel drawingPanel;
	
	public GMenuBar() {
		this.fileMenu = new GFileMenu();
		this.add(this.fileMenu);
		
		this.editMenu = new GEditMenu();
		this.add(this.editMenu);
		
		this.graphicMenu = new GGraphicMenu();
		this.add(this.graphicMenu);
	}


	public void initialize() {
		this.fileMenu.associate(drawingPanel);
	}


	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
			
	}
}
