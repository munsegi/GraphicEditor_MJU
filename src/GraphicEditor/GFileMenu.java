package GraphicEditor;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GFileMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	private JMenuItem fileMenu;

	public GFileMenu() {
		super("File");
		
		this.fileMenu = new JMenuItem("New");
		this.add(this.fileMenu);
		
		this.fileMenu = new JMenuItem("Open");
		this.add(this.fileMenu);
		
		this.fileMenu = new JMenuItem("Save");
		this.add(this.fileMenu);
		
		this.fileMenu = new JMenuItem("Save As");
		this.add(this.fileMenu);
		
		this.fileMenu = new JMenuItem("Quit");
		this.add(this.fileMenu);
	}

}
