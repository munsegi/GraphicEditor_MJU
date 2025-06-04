package menus;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GGraphicMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	private JMenuItem graphicMenu;
	
	public GGraphicMenu() {
		super("Graphic");
		
		this.graphicMenu = new JMenuItem("Line thickness");
		this.add(this.graphicMenu);
		this.graphicMenu = new JMenuItem("Line Style");
		this.add(this.graphicMenu);
		this.graphicMenu = new JMenuItem("Font style");
		this.add(this.graphicMenu);
		this.graphicMenu = new JMenuItem("Font size");
		this.add(this.graphicMenu);
	}
}
