package edu.lehigh.acm.shooter;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "shooter";
		cfg.useGL20 = false;
		cfg.width = 768;
		cfg.height = 512;
		
		new LwjglApplication(new Shooter(), cfg);
	}
}
