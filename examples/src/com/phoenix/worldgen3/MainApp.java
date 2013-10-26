package com.phoenix.worldgen3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.phoenix.worldgen.Screen.*;

public class MainApp extends Game {
	private SplashScreen splashScreen;
	private MainScreen mainScreen;
	
	@Override
	public void create() {
		splashScreen = new SplashScreen();
		mainScreen = new MainScreen();
		
		setScreen(splashScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		
	}

	@Override
	public void render() {
		super.render();
		if (splashScreen.done == true) {
			setScreen(mainScreen);
			splashScreen.done = false;
		}
		Gdx.graphics.setTitle("FPS: " + Gdx.graphics.getFramesPerSecond());
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
	
	public boolean needsGL20 () {
		return true;
	}
}
