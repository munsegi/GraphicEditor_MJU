package global;

import frames.GMainframe;

public class GMain {

	public static void main(String[] args) {
		GMainframe mainFrame = new GMainframe();
		//포워드 그라운드에 paint - 어소시에트 컴포넌트로 이동
		
		mainFrame.initialize(); //트리를 한번 더 타고 내려감(two face initialize) - 있는게 좋다
		//이후에 eventLoop로 빠져 EventHandler가 이벤트를 기다리게 됨.
		

	}
}
