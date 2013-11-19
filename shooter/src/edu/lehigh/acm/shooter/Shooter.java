package edu.lehigh.acm.shooter;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import edu.lehigh.acm.shooter.actors.Bullet;
import edu.lehigh.acm.shooter.actors.Enemy;
import edu.lehigh.acm.shooter.actors.Player;
import edu.lehigh.acm.shooter.actors.ShooterShape;

public class Shooter implements ApplicationListener {
	
	// ===================================================================
	// FIELDS AND PROPERTIES
	// ===================================================================
	
	private OrthographicCamera mCamera;
	private ShapeRenderer mShapeRenderer;
	private SpriteBatch mSpriteBatch;
	private Sprite mBackground;
	private Player mPlayer;
	private Timer mEnemyTimer;
	
	private ArrayList<Enemy> mEnemies;
	private ArrayList<Bullet> mBullets;
	
	private boolean mGameOver; 
	
	
	// ===================================================================
	// INITIALIZATION
	// ===================================================================
	
	public void init() {
		// Initialize our lists to be empty.
		mEnemies = new ArrayList<Enemy>();
		mBullets = new ArrayList<Bullet>();
		
		// Initialize the camera. An orthographic camera is one that doesn't skew based
		// on perspective. This is ideal for 2D games with no depth, but a 3D game would
		// want to use something that more naturally mimics the human eye (like a 
		// PerspectiveCamera). If you look at the documentation for the OrthographicCamera,
		// you'll notice that it puts the origin in the center, with the y-axis decreasing as
		// you go down. This is a classic Cartesian plane. We'll just set the camera to be
		// the width and height of the game window.
		mCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Initialize a ShapeRenderer. This is what we'll use to render our player,
		// enemies, and bullets to the screen as vector graphics.
		mShapeRenderer = new ShapeRenderer();
		
		// Initialize a SpriteBatch. A SpriteBatch is something the draws sprites to the screen.
		mSpriteBatch = new SpriteBatch();
		
		// Create a new texture from a picture we have in our assets folder. With LibGDX,
		// (as well as almost any game engine - although they may abstract it away), all
		// of your texture dimensions need to be a power of 2 (e.g. 1024x512). There are 
		// mathematical and hardware reasons why this is the case. As far as where the assets
		// should be kept, you should keep it in the assets folder. All of your projects
		// will share one assets folder.
		Texture texture = new Texture(Gdx.files.internal("data/bkg.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// You can store lots of stuff in a Texture. Unless your asset is exactly a power of
		// 2 in dimensions, then you'll likely have empty space around it. You can put your
		// other assets in that empty space. Because look here - we can make a TextureRegion
		// that represents a chunk from that original Texture. In this case, we're grabbing
		// just the part of the Texture that represents our background.
		TextureRegion region = new TextureRegion(texture, 0, 0, 768, 512);
		
		// Then, all we do is initialize a Sprite with the TextureRegion that defines it, and then
		// position it in the middle of the screen. Position is dictated by the top left corner,
		// so we need to offset it by half the screen height and width (since the origin is in
		// the center).
		mBackground = new Sprite(region);
		mBackground.setPosition(-mBackground.getWidth()/2, -mBackground.getHeight()/2);

		// Create a new instance of a player and put it in the middle of the screen.
		mPlayer = new Player();
		mPlayer.setX(0);
		mPlayer.setY(0);
		
		// Setup a timer to spawn an enemy every second.
		mEnemyTimer = new Timer();
		mEnemyTimer.scheduleTask(new EnemyTimerTask(), 1, 1);
	}
	
	
	// ===================================================================
	// UPDATE ROUTINES
	// ===================================================================
	
	/**
	 * Unlike CreateJS, LibGDX isn't event-driven. You actually have to check upon
	 * each engine update if an event occurred.
	 */
	public void checkInput() {
		// In this case, we can detect if a touch/mouse-click just occurred.
		if (Gdx.input.justTouched()) {
			// While the origin is in the center of the stage for rendering purposes,
			// touch events still work on a coordinate system where the origin is in the
			// top left, and y increases going down. Therefore, we have to translate our
			// touch coordinates before we use them.
			float px = Gdx.input.getX() - Gdx.graphics.getWidth()/2;
			float py = -(Gdx.input.getY() - Gdx.graphics.getHeight()/2);
			shoot(px, py);
		}		
	}
	
	/**
	 * This checks for collisions between bullets and enemies. There are smarter ways
	 * to do it, but here we'll simply look at every bullet and check if it's touching
	 * any enemy.
	 */
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
	
	/**
	 * Just goes through and checks if any enemy is colliding with the player.
	 * If it is, then we set the game over flag.
	 */
	public void checkEnemyHeroCollision() {
		for (int i = mEnemies.size() - 1; i >= 0; i--) {
			if (circleCollision(mPlayer, mEnemies.get(i))) {
				mGameOver = true;
				return;
			}
		}
	}
	
	public void draw() {
		// For right now, if the game is over, we just don't draw.
		if (mGameOver)
			return;
		
		// Clear the background out by drawing white to the entire screen.
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// Draws the background to the SpriteBatch. In essence, a SpriteBatch is the
		// middle man between you and OpenGL. Instead of making a bunch of individual
		// draw calls, you can put a bunch of things into a SpriteBatch and have it 
		// drawn only once. It's more complicated then that, but that's the idea.
		mSpriteBatch.setProjectionMatrix(mCamera.combined);
		mSpriteBatch.begin();
		mBackground.draw(mSpriteBatch);
		mSpriteBatch.end();
		
		
		// The rest of this is just rendering all of our shapes to the screen. After 
		// setting the projection matrix (which is the mathematical transformation
		// applied before drawing and is affected by the camera), we simply draw
		// each object.
		
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
	
	
	// ===================================================================
	// HELPER METHODS
	// ===================================================================
	
	/**
	 * The handles creating new bullets and giving them the proper velocity.
	 * We don't cover the math here, because it's already been covered in a 
	 * previous workshop (https://github.com/luacm/createjs-intro)
	 * @param px
	 * 		The x coordinate of the point we're shooting towards.
	 * @param py
	 * 		The y coordinate of the point we're shooting towards.
	 */
	public void shoot(float px, float py) {
		// Because our origin is in the center of the screen, our dy an dx
		// are equal to py and px.
		double radians = Math.atan2(py, px);
		double vx = Math.cos(radians) * Bullet.SPEED;
		double vy = Math.sin(radians) * Bullet.SPEED;
		
		// We don't want to bullet to spawn on top of our player, so we position
		// it just outside the player.
		double x = Math.cos(radians) * mPlayer.getWidth()/2;
		double y = Math.sin(radians) * mPlayer.getWidth()/2;
		
		// Create the bullet and add it to our list
		Bullet b = new Bullet((float)x, (float)y, (float)vx, (float)vy);
		mBullets.add(b);
	}
	
	/**
	 * This is a simple way to detect collision between two circular objects.
	 * If the distance between their centers is less than or equal to the sum
	 * of their radii, then they must be touching.
	 * @param obj1
	 * 		The first object we're testing.
	 * @param obj2
	 * 		The second object we're testing.
	 * @return
	 * 		True if the circles are touching/overlapping, otherwise false.
	 */
	public boolean circleCollision(ShooterShape obj1, ShooterShape obj2) {
		float dx = obj1.getX() - obj2.getX();
		float dy = obj1.getY() - obj2.getY();
		float dist = (float)Math.sqrt(dx * dx + dy * dy);
		return dist <= (obj1.getWidth()/2 + obj2.getWidth()/2);
	}

	/**
	 * This is a class we've made to handle what happens when an enemy 
	 * spawns. We're just going to put it somewhere outside the stage
	 * (by picking a random angle) and then give it a velocity that points
	 * it towards the player.
	 */
	private class EnemyTimerTask extends Task {
		@Override
		public void run() {
			// Store the width and height of the screen for easy access
			float w = Gdx.graphics.getWidth();
			float h = Gdx.graphics.getHeight();
			
			// Get a random angle to position the enemy
			double radians = Math.random() * (2 * Math.PI);
			
			// The radius has to be great than w/2 (because of the corners), so we 
			// just add in h/2 to be safe.
			float radius = w/2 + h/2;
			
			// Get the (x, y) coordinates of the spawn point
			double x = Math.cos(radians) * radius;
			double y = Math.sin(radians) * radius;
			
			// Get the angle the enemy should have to move towards the player.
			// Remember, we do source-target, and because the source is (0, 0),
			// dy and dx are just -y and -x.
			double moveRadians = Math.atan2(-y, -x);
			double vx = Math.cos(moveRadians) * Enemy.SPEED;
			double vy = Math.sin(moveRadians) * Enemy.SPEED;
			
			// Create the enemy and add it to our list
			Enemy e = new Enemy((float)x, (float)y, (float)vx, (float)vy);
			mEnemies.add(e);
		}
	}
	
	
	// ===================================================================
	// LIBGDX LIFECYCLE EVENTS
	// ===================================================================
	
	/**
	 * This event is called right when the game is made.
	 */
	@Override
	public void create() {		
		init();
	}
	
	/**
	 * This event is called when the game is destroyed.
	 */
	@Override
	public void dispose() {
		mShapeRenderer.dispose();
		mSpriteBatch.dispose();
	}

	/**
	 * This is called whenever the engine updates. We want to do all of our core
	 * logic here, including drawing.
	 */
	@Override
	public void render() {		
		checkInput();
		checkBulletEnemyCollision();
		checkEnemyHeroCollision();
		draw();
	}

	/**
	 * This is called when the window is resized. We currently just do the weak
	 * approach of making our camera the same size as the stage. You may want to
	 * try to scale things or letterbox instead.
	 */
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
