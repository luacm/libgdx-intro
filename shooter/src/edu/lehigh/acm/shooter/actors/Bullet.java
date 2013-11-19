package edu.lehigh.acm.shooter.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Bullet extends ShooterShape {

	private float mVx;
	private float mVy;
	
	public static final float SPEED = 150;
	
	public Bullet(float x, float y, float vx, float vy) {
		mX = x;
		mY = y;
		mVx = vx;
		mVy = vy;
		mWidth = 5;
	}
	
	@Override
	public void draw(ShapeRenderer renderer) {
		mX += mVx * Gdx.graphics.getDeltaTime();
		mY += mVy * Gdx.graphics.getDeltaTime();
		
		renderer.begin(ShapeType.Filled);
		renderer.setColor(0, 0, 1, 1);
		renderer.circle(mX, mY, mWidth/2);
		renderer.end();
	}

}
