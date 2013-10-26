package com.phoenix.worldgen.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen{
	private Texture splashTexture;
	private Sprite splashSprite;
	private SpriteBatch batch;
	private float curTime = 0f;
	private float fadeInTime = 0.5f;
	private float idleTime = 1f;
	private float fadeOutTime = 0.5f;
	public boolean done = false;
	
	public SplashScreen() {
	}

	@Override
	public void render(float delta) {
		curTime = curTime + delta;
		float alpha = 0f;
		
		// Interpolate using cosine interpolation (much smoother than linear)
		if (curTime < fadeInTime && curTime >= 0) {
			alpha = 1f - (float) Math.cos(Math.PI/2 * curTime/fadeInTime);
		}
		else if (curTime >= fadeInTime && curTime < fadeInTime + idleTime) {
			alpha = 1f;
		}
		else if (curTime >= fadeInTime + idleTime && curTime < fadeInTime + idleTime + fadeOutTime) {
			alpha = 1f - (float) Math.cos(Math.PI/2 * (1-(curTime - fadeInTime - idleTime)/fadeOutTime));
		}
		else {
			alpha = 0f;
			done = true;
		}
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		splashSprite.setColor(1, 1, 1, alpha);
		
		batch.begin();
		splashSprite.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		splashTexture = new Texture("data/demo.png");
		splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		splashSprite = new Sprite(splashTexture);
		splashSprite.setOrigin(splashSprite.getWidth()/2, splashSprite.getHeight()/2);
		splashSprite.setPosition(Gdx.graphics.getWidth()/2 - splashSprite.getWidth()/2, Gdx.graphics.getHeight()/2 - splashSprite.getHeight()/2);
		splashSprite.setColor(1, 1, 1, 0);
		
		batch = new SpriteBatch();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
