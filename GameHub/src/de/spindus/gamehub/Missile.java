package de.spindus.gamehub;

public class Missile extends Sprite {

	private final int missile_speed = 2;

	public Missile(int x, int y) {
		super(x, y);

		initMissile();
	}

	private void initMissile() {
		loadImage("src/ressources/images/missile.png");
		getImageDimensions();
	}

	public void move() {
		x += missile_speed;
		if (x > GameHub.b_width) {
			visible = false;
		}
	}

}
