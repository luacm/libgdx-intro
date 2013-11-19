package edu.lehigh.acm.shooter.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Enemy extends ShooterShape {

	public static final float SPEED = 25;
	private float mVx;
	private float mVy;
	
	public Enemy(float x, float y, float vx, float vy) {
		mWidth = 30;
		mX = x;
		mY = y;
		mVx = vx;
		mVy = vy;
	}
	
	@Override
	public void draw(ShapeRenderer renderer) {
		mX += mVx * Gdx.graphics.getDeltaTime();
		mY += mVy * Gdx.graphics.getDeltaTime();
		
		renderer.begin(ShapeType.Filled);
		renderer.setColor(1, 0, 0, 1);
		renderer.circle(mX, mY, mWidth/2);
		renderer.end();
	}

}
