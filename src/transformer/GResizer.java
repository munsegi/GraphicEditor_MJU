package transformer;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import Shapes.GShape;
import global.GConstants.Gmainframe.EAnchor;

public class GResizer extends GTransformer {

	private int px, py;
	private int cx, cy;
	
	private EAnchor eResizeAnchor; 
	
	private AffineTransform origTransform;
	
	private Rectangle origBounds;
    private double origW, origH;

	public GResizer(GShape shape) {
		super(shape);
		this.shape = shape;
		this.eResizeAnchor = null;
	}

	@Override
	public void start(Graphics2D graphics, int x, int y) {
		
        this.px = x;
        this.py = y;
        //리사이징 전 '원본' AffineTransform과 바운드를 기록
        this.origTransform = this.shape.getAffineTransform();// 현재 변형 정보 복사
        
        Rectangle r = this.shape.getTransformedShape().getBounds();
        this.origBounds = r;
        this.origW = r.getWidth();
        this.origH = r.getHeight();
        
        // 선택할 앵커와 고정 대상 앵커를 결정
        EAnchor eSelectedAnchor = this.shape.getESelectedAnchor();
        switch (eSelectedAnchor) {
            case eNW:
                eResizeAnchor = EAnchor.eSE;
                cx = r.x + r.width;
                cy = r.y + r.height;
                break;
            case eWW:
                eResizeAnchor = EAnchor.eEE;
                cx = r.x + r.width;
                cy = r.y + r.height / 2;
                break;
            case eSW:
                eResizeAnchor = EAnchor.eNE;
                cx = r.x + r.width;
                cy = r.y;
                break;
            case eSS:
                eResizeAnchor = EAnchor.eNN;
                cx = r.x + r.width / 2;
                cy = r.y;
                break;
            case eSE:
                eResizeAnchor = EAnchor.eNW;
                cx = r.x;
                cy = r.y;
                break;
            case eEE:
                eResizeAnchor = EAnchor.eWW;
                cx = r.x;
                cy = r.y + r.height / 2;
                break;
            case eNE:
                eResizeAnchor = EAnchor.eSW;
                cx = r.x;
                cy = r.y + r.height;
                break;
            case eNN:
                eResizeAnchor = EAnchor.eSS;
                cx = r.x + r.width / 2;
                cy = r.y + r.height;
                break;
            default:
                break;
        }
		
	}

	public void drag(Graphics2D graphics, int x, int y) {
		// 현재 마우스(x, y)가 고정 앵커와 얼마나 떨어졌는지 계산
        // dx, dy는 '화면 좌표상'에서 cx, cy를 중심으로 얼마나 늘어났는지
		double dx = x - cx;
        double dy = y - cy;
        
        //고정 앵커→반대쪽 앵커 간 원래 폭·높이
        //origW, origH는 start()에서 구해둔 값이다.
        double newW = Math.abs(dx);
        double newH = Math.abs(dy);
		
        //스케일 비율 계산
        //(원래 폭 대비 현재 폭/높이를 계산)
        //단, 폭·높이가 0이 되지 않도록 최소치(예: 1)로 제한할 수도 있다.
        double scaleX = (origW != 0) ? (newW / origW) : 1;
        double scaleY = (origH != 0) ? (newH / origH) : 1;
        
        switch (eResizeAnchor) {
        case eNW:  
            scaleX = -scaleX;
            scaleY = -scaleY;
            break;

        case eWW:
            scaleX = -scaleX;
            break;

        case eSW:
            scaleX = -scaleX;
            break;

        case eSS:
            break;

        case eSE:
            break;

        case eEE:
            break;

        case eNE:
            scaleY = -scaleY;
            break;

        case eNN:
            scaleY = -scaleY;
            break;
        default:
            break;
    }
		
        //이제 ‘고정 앵커(cx, cy) 기준’ 스케일링 변환행렬을 만든다.
        //    1 translate(-cx, -cy) : 기준점을 좌표계 원점으로 이동
        //    2 scale(scaleX, scaleY)
        //    3 translate(cx, cy) : 다시 원래 좌표계로 이동
        AffineTransform at = new AffineTransform();
        at.translate(cx, cy);
        at.scale(scaleX, scaleY);
        at.translate(-cx, -cy);

        //원본 변형(origTransform) 위에 at를 합성(preConcatenate 또는 concatenate)
        //    “원본→스케일” 순서를 보장하도록 origTransform의 사본을 만들어 합성한다.
        AffineTransform newTransform = new AffineTransform(origTransform);
        newTransform.preConcatenate(at);

        // 7) GShape에 변형행렬 반영
        this.shape.setTransform(newTransform);

    }



	@Override
	public void finish(Graphics2D graphics, int x, int y) {
		drag(graphics, x, y);
	}

	@Override
	public void addPoint(Graphics2D graphics, int x, int y) {

	}

}
