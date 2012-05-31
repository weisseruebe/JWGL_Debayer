package de.rettig.jwgl;

import org.lwjgl.input.Keyboard;

public class Controller {
	private DebayerPlane debayerPlane;
	private float gamma = 0.6f;
	private float wbR = 1;
	private float wbG = 1;
	private float wbB = 1;
	private int x;
	private int y;

	public Controller() {
		debayerPlane=new DebayerPlane("res/DSC_0303.png");
		debayerPlane.setGamma(gamma);
		debayerPlane.setFirstRed(new int[]{0,1});
	}

	private void pollInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			debayerPlane.zoom(-0.1f);

		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			debayerPlane.zoom(0.1f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			debayerPlane.setPos(--x,y);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			
			debayerPlane.setPos(++x,y);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			debayerPlane.setPos(x,++y);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			debayerPlane.setPos(x,--y);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
			gamma += 0.01f;
			debayerPlane.setGamma(gamma);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
			gamma -= 0.01f;
			debayerPlane.setGamma(gamma);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
			wbR += 0.01;
			debayerPlane.setWB(wbR, wbG, wbB);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
			wbR -= 0.01;
			debayerPlane.setWB(wbR, wbG, wbB);
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				
				if (Keyboard.getEventKey() == Keyboard.KEY_1) {
					debayerPlane.setRotate(0);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_2) {
					debayerPlane.setRotate(90);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_3) {
					debayerPlane.setRotate(180);
				}
				
				
			} else {
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {
					System.out.println("A Key Released");
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_S) {
					System.out.println("S Key Released");
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_D) {
					System.out.println("D Key Released");
				}
			}
		}

	}
	
	public void tick() {
		pollInput();
		debayerPlane.draw();
	}

}