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
	private int n;
	private int numPics = 3;

	public Controller() {
		debayerPlane=new DebayerPlane("res/pco.png");
		debayerPlane.setGamma(gamma);
		debayerPlane.setFirstRed(new int[]{0,0});
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
		if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
			wbR += 0.01;
			debayerPlane.setWB(wbR, wbG, wbB);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
			wbR -= 0.01;
			debayerPlane.setWB(wbR, wbG, wbB);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
			wbB += 0.01;
			debayerPlane.setWB(wbR, wbG, wbB);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
			wbB -= 0.01;
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
				if (Keyboard.getEventKey() == (Keyboard.KEY_X)) {
					debayerPlane.reload("/Users/andreasrettig/Desktop/pco/pco0"+(n++%numPics )+".png");
					n = Math.max(Math.min(numPics, n), 0);
				}
				if (Keyboard.getEventKey() == (Keyboard.KEY_Y)) {
					debayerPlane.reload("/Users/andreasrettig/Desktop/pco/pco0"+(n--)+".png");
					n = Math.max(Math.min(numPics, n), 0);
				}
			} else {
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {
				}
				
			}
		}

	}
	
	public void tick() {
		pollInput();
		debayerPlane.draw();
	}

}