package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import Shapes.GShape;
import frames.GDrawingPanel;
import global.GConstants.Gmainframe.EFileMenuItem;


public class GFileMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	// 디렉토리(폴더)도 사실상 하나의 파일이라고 간주하는것
	
	private File file;
	private JFileChooser GfileChooser;
	private String baseDirectory;
	
	private GDrawingPanel drawingPanel;
	
	public GFileMenu() {
		super("File");
		
		String appData = System.getenv("APPDATA");
		if (appData == null || appData.isEmpty()) {
            // 만약 Windows 환경이 아니거나 APPDATA가 비어 있다면
            appData = System.getProperty("user.home") 
                    + File.separator + "AppData" 
                    + File.separator + "Roaming";
        }
		
		baseDirectory = appData + File.separator + "GraphicEditor_MJU";
		
		// 해당 폴더가 없으면 생성
		File folder = new File(baseDirectory);
		
		GfileChooser = new JFileChooser(new File(baseDirectory));
		GfileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        if (!folder.exists()) {
            boolean success = folder.mkdirs();
            if (!success) {
                // 폴더 생성에 실패했을 때의 처리 (로그 출력 등)
                System.err.println("경고: " + baseDirectory + " 폴더 생성에 실패했습니다.");
            }
        }
		
		ActionHandler actionHandler = new ActionHandler();
		
		for(EFileMenuItem eMenuItem: EFileMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eMenuItem.getName());
			menuItem.addActionListener(actionHandler);
			menuItem.setActionCommand(eMenuItem.name());
			this.add(menuItem);
		}
	}
	public void initialize() {
		this.file = null;
	}
	
	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}
	
	public void newPanel() {
		System.out.println("newPanel");
		if(!this.close()) {
			//new
			this.drawingPanel.initialize();
		}
	}
	
	public void open() {
		System.out.println("open");
		
		File openFile = new File(baseDirectory, "File.gvs");
		
		//한번도 저장한 적이 없으면 경고를 띄움
		if (!openFile.exists()) {
	        JOptionPane.showMessageDialog(
	            this.drawingPanel, "기본 디렉토리에 File.gvs 파일이 존재하지 않습니다.", "경고",
	            JOptionPane.WARNING_MESSAGE
	        );
	        return;
	    }
		try {
			FileInputStream fileInputStream = new FileInputStream(openFile);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);// 버퍼를 통해 팬딩을 방지 (다른 데이터 세그먼트에 저장)
			ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);//
			
			this.drawingPanel.setShapes(objectInputStream.readObject()); // 메모리 상의 객체를 파일에 쓸 수 있는 형태로 바꿔줌
			this.drawingPanel.repaint();
			
			objectInputStream.close();
			
			this.file = openFile;
	        this.drawingPanel.setBUpdated(false);
	        
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} //바이트 배열을 작성함
		
	}
	
	public void save() {
		System.out.println("save");
		
		if (this.file == null) {
	        File defaultDir = new File(baseDirectory);
	        
	        // 기본 디렉토리가 없으면 생성
	        if (!defaultDir.exists()) {
	            defaultDir.mkdirs();
	        }

	        // 기본 파일명을 File.gvs로 지정
	        this.file = new File(defaultDir, "File.gvs");       
		}
		
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(this.file); // 바이트 배열을 작성해서 this.file에 저장함
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);// 버퍼를 통해 팬딩을 방지 (다른 데이터 세그먼트에저장)
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);// 버퍼에 저장된 객체를 파일에저장하도록 함

			objectOutputStream.writeObject(this.drawingPanel.getShapes()); // 메모리 상의 객체를 파일에 쓸 수 있는 형태로 바꿔줌
			objectOutputStream.close();
			this.drawingPanel.setBUpdated(false);
			// 인스턴스를 자바의 바이트 코드 형태의 배열로 만들어버림
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean saveAS() {
		System.out.println("saveAS");
		
		boolean bCancel = false;
		GfileChooser.setDialogTitle("Save As");

		GfileChooser.setSelectedFile(this.file);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Graphics Data", "gvs");
		GfileChooser.setFileFilter(filter);
		
		int returnVal = GfileChooser.showSaveDialog(this.drawingPanel);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			this.file = GfileChooser.getSelectedFile();
			this.save();
		} else {
			bCancel = true;
		}
		return bCancel;
	    
	}
	
	public void print() {
		System.out.println("print");
	}
	
	public void quit() {
		System.out.println("quit");
		if(!this.close()) {
			System.exit(ABORT);
		}
	}
	
	public boolean close() {
		boolean bCancel = false;
		System.out.println("close");
		if(this.drawingPanel.isUpdated()) {
			int reply = JOptionPane.NO_OPTION;
			reply = JOptionPane.showConfirmDialog(this.drawingPanel, "변경사항을 저장할까요?");
			if (reply == JOptionPane.CANCEL_OPTION) {
				bCancel = true;
			} 
			else if (reply ==JOptionPane.OK_OPTION) {
				// 이미 저장된 적 있음 save as로
				// 아니면 일반 save
				bCancel = this.saveAS();
			}
		}
		
		return bCancel;
	}
	
	
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			EFileMenuItem eFileMenuItem = EFileMenuItem.valueOf(event.getActionCommand()); //이름을 통해 객체를 가지고 옴
			invokeMethod(eFileMenuItem.getMethodName());
			
		}
		
	}
	public void invokeMethod(String methodName) {
		// TODO Auto-generated method stub
		try {
			this.getClass().getMethod(methodName).invoke(this);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		} //여기서 this가 GFileMenu를 가리켜야 함
		//개많은 메뉴를 이렇게 줄여버림
	}
}
