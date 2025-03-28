package GraphicEditor;

import javax.swing.JMenuBar;

public class GMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private GFileMenu fileMenu;
	private GEditMenu editMenu;
	private GGraphicMenu graphicMenu;

	
	public GMenuBar() {
		this.fileMenu = new GFileMenu();
		this.add(this.fileMenu);
		
		this.editMenu = new GEditMenu();
		this.add(this.editMenu);
		
		this.graphicMenu = new GGraphicMenu();
		this.add(this.graphicMenu);
	}


	public void initialize() {
		// TODO Auto-generated method stub
		
	}
}
