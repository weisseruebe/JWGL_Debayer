package de.rettig.jwgl;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Game {
	private Box box;
	private float gamma = 0.6f;

	public Game() {
		box=new Box("res/DSC_0303.png");
	}

	private void pollInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			box.zoom(-0.1f);

		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			box.zoom(0.1f);

		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_O) {
					box.setTexFilter(GL11.GL_LINEAR);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_L) {
					box.setTexFilter(GL11.GL_NEAREST);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_G) {
					gamma += 0.01f;
					box.setGamma(gamma);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_H) {
					gamma -= 0.01f;
					box.setGamma(gamma);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_1) {
					box.setRotate(0);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_2) {
					box.setRotate(90);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_3) {
					box.setRotate(180);
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
		box.draw();
	}

}