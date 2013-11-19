package edu.lehigh.acm.shooter.actors;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class ShooterShape {
	
	protected float mX;
	protected float mY;
	
	protected float mWidth;
	protected float mHeight;
	
	public abstract void draw(ShapeRenderer renderer);

	
	public float getX() {
		return mX;
	}

	public void setX(float mX) {
		this.mX = mX;
	}

	public float getY() {
		return mY;
	}

	public void setY(float mY) {
		this.mY = mY;
	}

	public float getWidth() {
		return mWidth;
	}

	public float getHeight() {
		return mHeight;
	}	
}
