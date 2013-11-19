package edu.lehigh.acm.shooter;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import edu.lehigh.acm.shooter.actors.Bullet;
import edu.lehigh.acm.shooter.actors.Enemy;
import edu.lehigh.acm.shooter.actors.Player;

public class Shooter implements ApplicationListener {
	private OrthographicCamera mCamera;
	private ShapeRenderer mShapeRenderer;
	private Player mPlayer;
	
	private ArrayList<Enemy> mEnemies;
	private ArrayList<Bullet> mBullets;
	
	@Override
	public void create() {		
		// Initialize lists
		mEnemies = new ArrayList<Enemy>();
		mBullets = new ArrayList<Bullet>();
		
		// Initialize the camera and renderer
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		mCamera = new OrthographicCamera(w, h);
		mShapeRenderer = new ShapeRenderer();
		
		// Draw the player on the screen
		mPlayer = new Player();
		mPlayer.setX(0);
		mPlayer.setY(0);
	}
	
	public void checkInput() {
		if (Gdx.input.isTouched()) {
			float px = Gdx.input.getX() - Gdx.graphics.getWidth()/2;
			float py = -(Gdx.input.getY() - Gdx.graphics.getHeight()/2);
			shoot(px, py);
		}		
	}
	
	public void shoot(float px, float py) {
		// Because our origin is in the center of the screen, our dy an dx
		// are equal to py and px.
		double radians = Math.atan2(py, px);
		double vx = Math.cos(radians) * Bullet.SPEED;
		double vy = Math.sin(radians) * Bullet.SPEED;
		Bullet b = new Bullet(0, 0, (float)vx, (float)vy);
		mBullets.add(b);
	}
	

	@Override
	public void dispose() {
		mShapeRenderer.dispose();
	}

	@Override
	public void render() {		
		checkInput();
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		mShapeRenderer.setProjectionMatrix(mCamera.combined);
		mPlayer.draw(mShapeRenderer);
		for (int i = 0; i < mEnemies.size(); i++) {
			mEnemies.get(i).draw(mShapeRenderer);
		}
		for (int i = 0; i < mBullets.size(); i++) {
			mBullets.get(i).draw(mShapeRenderer);
		}
		
	}

	@Override
	public void resize(int width, int height) {
		mCamera = new OrthographicCamera(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
