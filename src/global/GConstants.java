package global;

import java.awt.Cursor;
import java.lang.reflect.InvocationTargetException;

import Shapes.GEllipse;
import Shapes.GLine;
import Shapes.GPolygon;
import Shapes.GRectangle;
import Shapes.GShape;
import Shapes.GTriangle;
import Shapes.GShape.EPoints;

public class GConstants {
	public final class Gmainframe{
		public final static int START_X = 100;
		public final static int START_Y = 200;
		
		public final static int START_W = 600;
		public final static int START_H = 400;
		
		public enum EShapeTool{ // 순서와 값을 가진 숫자들이자 객체

			eSelect("select", EPoints.e2P, GRectangle.class),
			eRectangle("rectangle", EPoints.e2P, GRectangle.class), // 자기가 자신의 객체를 만듦. 클래스가 아님.
			eEllipse("Ellipse", EPoints.e2P, GEllipse.class), // 프로그램이 실행되면서 4개의 객체가 new가 된 것임.
			eLine("Line", EPoints.e2P, GLine.class), 
			eTriangle("triangle", EPoints.e2P, GTriangle.class), 
			ePolygon("Polygon", EPoints.eNP, GPolygon.class);
			
			private String name;
			private EPoints eDrawingType; 
			private Class<?> classShape; // 파라미터로 오니까 물읖표 ?
			private EShapeTool(String name, EPoints eDrawingType ,Class<?> classShape) {
				this.name = name;
				this.eDrawingType = eDrawingType;
				this.classShape = classShape;
			}
			
			public String getName() {
				return this.name;
			}
			
			public EPoints getEPoints() {
				return this.eDrawingType;
			}
			public GShape newShape(){
				GShape shape;
				try {
					shape = (GShape) classShape.getConstructor().newInstance();
					return shape;
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				return null;
			}
		} // 값도 있고 순서도 있음 사실상 4개의 객체가 생성됨.
		
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
			private EAnchor (Cursor cursor) {
				this.cursor = cursor;
			}
			
			public Cursor getCursor() {
				return this.cursor;
			}
		}
		
		public enum EFileMenuItem{
			eNew("새 파일","newPanel"),
			eOpen("열기","open"),
			eSave("저장","save"),
			eSaveAs("다른 이름으로 저장", "saveAS"),
			ePrint("프린트", "print"),
			eClose("닫기", "close"),
			eQuit("종료", "quit");
			
			
			private String name;
			private String methodName;
			private EFileMenuItem(String name, String methodName) {
				this.name = name;
				this.methodName = methodName;
			}
			
			public String getName() {
				return this.name;
			}
			
			public String getMethodName(){
				return this.methodName;
			}
		}
	}
}
