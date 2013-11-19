package edu.lehigh.acm.shooter;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import edu.lehigh.acm.shooter.actors.Bullet;
import edu.lehigh.acm.shooter.actors.Enemy;
import edu.lehigh.acm.shooter.actors.Player;
import edu.lehigh.acm.shooter.actors.ShooterShape;

public class Shooter implements ApplicationListener {
	private OrthographicCamera mCamera;
	private ShapeRenderer mShapeRenderer;
	private Player mPlayer;
	
	private ArrayList<Enemy> mEnemies;
	private ArrayList<Bullet> mBullets;
	
	private Timer mEnemyTimer;
	
	@Override
	public void create() {		
		// Initialize lists
		mEnemies = new ArrayList<Enemy>();
		mBullets = new ArrayList<Bullet>();
		
		// Initialize the camera and renderer
		mCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mShapeRenderer = new ShapeRenderer();
		
		// Draw the player on the screen
		mPlayer = new Player();
		mPlayer.setX(0);
		mPlayer.setY(0);
		
		// Schedule enemy timer
		mEnemyTimer = new Timer();
		mEnemyTimer.scheduleTask(new EnemyTimerTask(), 1, 1);
	}
	
	public void checkInput() {
		if (Gdx.input.justTouched()) {
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
		
		double x = Math.cos(radians) * mPlayer.getWidth()/2;
		double y = Math.sin(radians) * mPlayer.getWidth()/2;
		
		Bullet b = new Bullet((float)x, (float)y, (float)vx, (float)vy);
		mBullets.add(b);
	}
	
	public void checkBulletEnemyCollision() {
		for (int i = mBullets.size() - 1; i >= 0; i--) {
			Bullet b = mBullets.get(i);
			for (int j = mEnemies.size() - 1; j >= 0; j--) {
				Enemy e = mEnemies.get(j);
				if (circleCollision(b, e)) {
					mBullets.remove(i);
					mEnemies.remove(j);
				}
			}
		}
	}
	
	public void checkEnemyHeroCollision() {
		
	}
	
	public boolean circleCollision(ShooterShape obj1, ShooterShape obj2) {
		float dx = obj1.getX() - obj2.getX();
		float dy = obj1.getY() - obj2.getY();
		float dist = (float)Math.sqrt(dx * dx + dy * dy);
		return dist <= (obj1.getWidth()/2 + obj2.getWidth()/2);
	}
	
	public void draw() {
		// Clear the background
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// Setup the renderer
		mShapeRenderer.setProjectionMatrix(mCamera.combined);
		
		// Draw the player
		mPlayer.draw(mShapeRenderer);
		
		// Draw the enemies
		for (int i = 0; i < mEnemies.size(); i++) {
			mEnemies.get(i).draw(mShapeRenderer);
		}
		
		// Draw the bullets
		for (int i = 0; i < mBullets.size(); i++) {
			mBullets.get(i).draw(mShapeRenderer);
		}
	}

	@Override
	public void dispose() {
		mShapeRenderer.dispose();
	}

	@Override
	public void render() {		
		checkInput();
		checkBulletEnemyCollision();
		checkEnemyHeroCollision();
		draw();
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
	
	private class EnemyTimerTask extends Task {
		@Override
		public void run() {
			float w = Gdx.graphics.getWidth();
			float h = Gdx.graphics.getHeight();
			float radians = (float)(Math.random() * (2 * Math.PI));
			float radius = w/2 + h/2;
			float x = (float)(Math.cos(radians) * radius);
			float y = (float)(Math.sin(radians) * radius);
			
			float moveRadians = (float)Math.atan2(-y, -x);
			float vx = (float)(Math.cos(moveRadians) * Enemy.SPEED);
			float vy = (float)(Math.sin(moveRadians) * Enemy.SPEED);
			
			Enemy e = new Enemy(x, y, vx, vy);
			mEnemies.add(e);
		}
	}
}
