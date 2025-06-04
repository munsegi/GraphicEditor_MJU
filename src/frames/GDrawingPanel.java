package frames;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import Shapes.GPolygon;
import Shapes.GShape;
import global.GConstants.Gmainframe.EAnchor;
import global.GConstants.Gmainframe.EShapeTool;
import transformer.GDrawer;
import transformer.GMover;
import transformer.GResizer;
import transformer.GTransformer;

public class GDrawingPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	// t
	public enum EDrawingState {
		eIdle, // 안그리는
		e2P, // 그리는 상태 2p
		eNp, // 그리는 상태 np(다각형 전)
	}

	//components
	private Vector<GShape> shapes; // 이놈을 저장해서 복구하면 되지 않을까?
	
	//working Objects
	private EShapeTool eShapeTool;
	private EDrawingState eDrawingState;
	private MouseHandler mouseHandler;
	private Point currentMousePoint;
	private GShape currentShape;
	
	private boolean bUpdated;

	private GShape selectedShape;

	//constructors
	public GDrawingPanel() {
		// 마우스 이벤트를 받는 이벤트 핸들러 추가
		this.mouseHandler = new MouseHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);

		this.currentShape = null;
		this.selectedShape = null;
		this.shapes = new Vector<GShape>();
		this.eShapeTool = null;
		this.eDrawingState = EDrawingState.eIdle;
		this.bUpdated = false;
	}

	@Override // 부모가 해야 할 일을 대체함
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics); // 본래 부모가 할 일을 호출함. 오버라이딩 공식임.
		Graphics2D g2 = (Graphics2D) graphics;

		// 저장된 도형 그리기
		for (GShape shapes : this.shapes) {
			if (eDrawingState == EDrawingState.eNp && shapes == currentShape) {
				// 현재 폴리곤은 나중에 미리보기를 따로 그릴 것
				continue;
			}
			shapes.draw((Graphics2D) graphics);
		}

		// N-Point 모드에서만, 현재 그리는 폴리곤에 한해
		if (eDrawingState == EDrawingState.eNp && currentShape instanceof GPolygon) {
			GPolygon poly = (GPolygon) currentShape;
			int n = poly.getPointCount();
			if (n > 0 && currentMousePoint != null) {
				int[] xs = poly.getXPoints();
				int[] ys = poly.getYPoints();
				// 저장된 점들끼리 선분 그리기
				for (int i = 0; i < n - 1; i++) {
					g2.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);
				}
				// 마지막 점 마우스 커서까지 미리보기 선 그리기
				g2.drawLine(xs[n - 1], ys[n - 1], currentMousePoint.x, currentMousePoint.y);
			}
		}

	}

	private GShape onShape(int x, int y) {
		// 가장 나중에 그린(벡터 끝쪽) 도형부터 검사
		for (GShape shape : this.shapes) {
			if (shape.contains(x, y)) {
				return shape;
			}
		}
		return null;
	}

	private void startTransform(int x, int y) {
		this.currentShape = eShapeTool.newShape();
		this.shapes.add(this.currentShape);
		
		if (eShapeTool == EShapeTool.eSelect) {
			this.selectedShape = onShape(x, y);
			if (this.selectedShape == null) {
				mouseHandler.transformer = new GDrawer(this.currentShape);
			} else  {
				this.selectedShape.setSelected(true);
			    EAnchor anchor = this.selectedShape.getESelectedAnchor();
			    if (anchor == EAnchor.eMM || anchor == EAnchor.eRR) {
			        mouseHandler.transformer = new GMover(this.selectedShape);
			    } else {
			        mouseHandler.transformer = new GResizer(this.selectedShape);
			    }
			}
		} else {
			this.mouseHandler.transformer = new GDrawer(this.currentShape);
		}
		this.mouseHandler.transformer.start((Graphics2D) getGraphics(), x, y);
	}

	private void keepTransform(int x, int y) {
		System.out.println("[keepTransform] x=" + x + ", y=" + y + 
				", transformer=" + (mouseHandler.transformer != null ? mouseHandler.transformer.getClass().getSimpleName() : "null"));
		this.mouseHandler.transformer.drag((Graphics2D) getGraphics(), x, y);
		repaint();
	}

	private void finishTransform(int x, int y) {
		mouseHandler.transformer.finish((Graphics2D) getGraphics(), x, y);
//		this.selectedShape(this.currentShape);
		
		//eShapeTool이 select 모드인 경우, selectedShape가 null이 아닐 때는
	    //‘이동 혹은 리사이즈된 도형’을 다시 선택 상태로 만들어줘야 함
		if (this.eShapeTool == EShapeTool.eSelect && this.selectedShape != null) {
	        //기존에 모두 unselected 처리
	        for (GShape shp : this.shapes) {
	            shp.setSelected(false);
	        }
	        //이동/리사이즈한 도형(selectedShape)만 다시 selected
	        this.selectedShape.setSelected(true);

	        //startTransform() 시점에 새로 생성된 임시 도형(currentShape) 제거
	        //(임시 선택 사각형 혹은 GRectangle)
	        this.shapes.remove(this.shapes.size() - 1);

	    } else {
	        //select 모드가 아니거나, selectedShape가 null인 경우: 
	        //기존의 ‘선택 사각형(drag-box)’ 로직을 그대로 사용
	        this.selectedShape(this.currentShape);
	        if (this.eShapeTool == EShapeTool.eSelect) {
	            //임시 사각형 제거
	            this.shapes.remove(this.shapes.size() - 1);
	            //모든 도형 중에서 currentShape가 포함하는 것만 selected
	            for (GShape shp : this.shapes) {
	                if (this.currentShape.contains(shp)) {
	                    shp.setSelected(true);
	                } else {
	                    shp.setSelected(false);
	                }
	            }
	        }
	    }
		
		// 다각형 모드 처리
		if (eDrawingState == EDrawingState.eNp) {
			addPoint(x, y);
		}
//		this.bUpdated = this.transformer.isUpdated();
		this.bUpdated = true;
		this.repaint();
	}

	private void selectedShape(GShape shape) {
		for (GShape otherShape: this.shapes) {
			otherShape.setSelected(false);
		}
		this.currentShape.setSelected(true);
	}

	private void addPoint(int x, int y) {
		this.mouseHandler.transformer.addPoint((Graphics2D) getGraphics(), x, y);

	}

	private void changeCursor(int x, int y) {
		if (this.eShapeTool == EShapeTool.eSelect) {
			for (GShape s : shapes) {
				if (s.contains(x, y)) {
					EAnchor anchor = s.getESelectedAnchor();
					if (anchor != null) {
						setCursor(anchor.getCursor());
						return;
					}
				}
			}
		}
		setCursor(Cursor.getDefaultCursor());
	}

	// 마우스 리스너 클래스를 이름만 바꿔서 등록함 매핑 펑션
	private class MouseHandler implements MouseListener, MouseMotionListener {
		private GTransformer transformer;

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 1) {
				this.mouse1Clicked(e);
			} else if (e.getClickCount() == 2) {
				this.mouse2Clicked(e);
			}
		}

		private void mouse1Clicked(MouseEvent e) {
			if (eDrawingState == EDrawingState.eIdle) {
				if (eShapeTool == EShapeTool.ePolygon) {
					startTransform(e.getX(), e.getY());
					eDrawingState = EDrawingState.eNp; // N-Point Mode
				} else {
					startTransform(e.getX(), e.getY());
					eDrawingState = EDrawingState.e2P;
				}
			} else if (eDrawingState == EDrawingState.e2P) {
				finishTransform(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			} else if (eDrawingState == EDrawingState.eNp) {
				addPoint(e.getX(), e.getY());
			}

		}

		private void mouse2Clicked(MouseEvent e) {
			if (eDrawingState == EDrawingState.eNp) {
				finishTransform(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			}
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {// 이벤트 타입
			// 타겟의 상태, 제약조건
			if (eDrawingState == EDrawingState.e2P) {
				keepTransform(e.getX(), e.getY());
			} else if (eDrawingState == EDrawingState.eNp) {
				currentMousePoint = e.getPoint();
				repaint();
			} else if (eDrawingState == EDrawingState.eIdle) {
				changeCursor(e.getX(), e.getY());
			} else {
				keepTransform(e.getX(), e.getY());
			}
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseDragged(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}


		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		

	}

	public void initialize() {
		this.shapes.clear();
		this.repaint();
	}
	
	//getters and setters
	public Vector<GShape> getShapes(){
		return this.shapes;
	} //file 메뉴에서 저장
	
	public void setShapes(Object shapes) {
		shapes.getClass();
		this.shapes = (Vector<GShape>) shapes; // 강제로 캐스팅 했기에 오류가 발생할 수 있음 이거 없애면 추가점수
		this.repaint();
	} //file 메뉴에서 저장한걸 읽음
	
	public void setEShapeType(EShapeTool eShapeType) {
		this.eShapeTool = eShapeType;
	}

	public boolean isUpdated() {
		return this.bUpdated;
	}

	public void setBUpdated(boolean bUpdated) {
		this.bUpdated = bUpdated;
	}
}
