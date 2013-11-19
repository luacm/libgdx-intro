package edu.lehigh.acm.shooter.actors;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Player extends ShooterShape {

	public Player() {
		mWidth = 30;
	}
	
	@Override
	public void draw(ShapeRenderer renderer) {
		renderer.begin(ShapeType.Filled);
		renderer.setColor(1, 0, 0, 1);
		renderer.circle(mX, mY, mWidth/2);
		renderer.end();
	}

}
