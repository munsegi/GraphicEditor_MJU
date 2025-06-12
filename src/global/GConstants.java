package global;

import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Shapes.GEllipse;
import Shapes.GPolygon;
import Shapes.GRectangle;
import Shapes.GShape;
import Shapes.GShape.EPoints;

public final class GConstants {
	
	static {
		initialize();
    }
	
	public GConstants() {

	}

	
	private static void initialize() {
		try (InputStream is = GConstants.class.getResourceAsStream("/global/config.xml")) {
	        if (is == null) {
	            throw new IllegalStateException("config.xml not found");
	        }
	        readFromFile(is);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public static void readFromFile(InputStream fileName) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// Load the input XML document, parse it and return an instance of the
			// Document class.
			
			Document document = builder.parse(fileName);
			document.getDocumentElement().normalize();
			
			NodeList nodeList = document.getDocumentElement().getChildNodes();
			for (int i=0; i<nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (node.getNodeName().equals(EMainFrame.class.getSimpleName())) {
						EMainFrame.setValues(node);
					} else if (node.getNodeName().equals(EFileMenuItem.class.getSimpleName())) {
						EFileMenuItem.setValues(node);
					} 
				}
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public enum EMainFrame {
		eX(0),
		eY(0),
		eW(0),
		eH(0);
		
		private int value;
		
		private EMainFrame(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
		public static void setValues(Node node) {
			for (EMainFrame eMainFrame: EMainFrame.values()) {
				Node attribute = node.getAttributes().getNamedItem(eMainFrame.name());
				eMainFrame.value = Integer.parseInt(attribute.getNodeValue());
				System.out.println("setted");
			}
			
		}
	}
	
	public enum EAnchor {
		eNN(new Cursor(Cursor.N_RESIZE_CURSOR)),
		eNE(new Cursor(Cursor.NE_RESIZE_CURSOR)),
		eNW(new Cursor(Cursor.NW_RESIZE_CURSOR)),
		eSS(new Cursor(Cursor.S_RESIZE_CURSOR)),
		eSE(new Cursor(Cursor.SE_RESIZE_CURSOR)),
		eSW(new Cursor(Cursor.SW_RESIZE_CURSOR)),
		eEE(new Cursor(Cursor.E_RESIZE_CURSOR)),
		eWW(new Cursor(Cursor.W_RESIZE_CURSOR)),
		eRR(new Cursor(Cursor.HAND_CURSOR)),
		eMM(new Cursor(Cursor.MOVE_CURSOR));		
		private Cursor cursor;
		private EAnchor(Cursor cursor) {
			this.cursor = cursor;
		}
		public Cursor getCursor() {
			return this.cursor;
		}
	}
	
	public enum EFileMenuItem {
		eNew(null, null),
		eOpen(null, null),
		eSave(null, null),
		eSaveAs(null, null),
		ePrint(null, null),
		eClose(null, null),
		eQuit(null, null);
		
		private String name;
		private String methodName;
		private EFileMenuItem(String name, String methodName) {
			this.name = name;
			this.methodName = methodName;
		}
		public String getName() {
			return this.name;
		}
		public String getMethodName() {
			return this.methodName;
		}
		public static void setValues(Node node) {
			NodeList nodeList = node.getChildNodes();
			
			 for (int i = 0; i < nodeList.getLength(); i++) {
			        Node child = nodeList.item(i);
			        if (child.getNodeType() != Node.ELEMENT_NODE) continue;
			        
			        String tag = child.getNodeName();
			        try {
			            EFileMenuItem item = EFileMenuItem.valueOf(tag);   // enum 매칭
			            NamedNodeMap attrs = child.getAttributes();

			            Node n = attrs.getNamedItem("name");
			            Node t = attrs.getNamedItem("methodName");

			            if (n != null) item.name        = n.getNodeValue().trim();
			            if (t != null) item.methodName = t.getNodeValue().trim();
			        } catch (IllegalArgumentException ignore) {
			            // enum에 없는 태그면 무시
			        }
			 }
			 
		
			
		}
	}
	
	public enum EShapeTool {
		
		eSelect("select", EPoints.e2P, GRectangle.class),
		eRectnalge("rectangle", EPoints.e2P, GRectangle.class),
		eEllipse("ellipse", EPoints.e2P, GEllipse.class),
		ePolygon("polygon", EPoints.eNP, GPolygon.class);
		
		private String name;
		private EPoints ePoints;
		private Class<?> classShape;
		private EShapeTool(String name, EPoints ePoints, Class<?> classShape) {
			this.name = name;
			this.ePoints = ePoints;
			this.classShape = classShape;
		}
		public String getName() {
			return this.name;
		}
		public EPoints getEPoints() {
			return this.ePoints;
		}
		public GShape newShape() {
			try {
				GShape shape = (GShape) classShape.getConstructor().newInstance();
				return shape;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			return null;
		}
	}	// components

	public static void ensureLoaded() {
		// TODO Auto-generated method stub
		
	}
	

}
