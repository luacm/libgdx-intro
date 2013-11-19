package edu.lehigh.acm.shooter.actors;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * This is an abstract class we're going to use to define a Shape
 * that has a height, width, and (x,y) position. Descendants of
 * this class will have to implement a draw method that we'll call
 * in the render loop. 
 */
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
