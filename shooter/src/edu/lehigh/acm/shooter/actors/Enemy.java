package edu.lehigh.acm.shooter.actors;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Enemy extends ShooterShape {

	public Enemy(float x, float y) {
		mWidth = 30;
		mX = x;
		mY = y;
	}
	
	@Override
	public void draw(ShapeRenderer renderer) {
		renderer.begin(ShapeType.Filled);
		renderer.setColor(1, 0, 0, 1);
		renderer.circle(mX, mY, mWidth/2);
		renderer.end();
	}

}
