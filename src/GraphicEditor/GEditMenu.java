package GraphicEditor;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GEditMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	private JMenuItem editMenu;
	
	public GEditMenu() {
		super("Edit");
		
		this.editMenu = new JMenuItem("Property");
		this.add(this.editMenu);
		
		this.editMenu = new JMenuItem("Undo");
		this.add(this.editMenu);
		
		this.editMenu = new JMenuItem("Redo");
		this.add(this.editMenu);
	}
}
