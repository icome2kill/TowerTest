package com.towertest.hud;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.towertest.managers.ResourceManager;
import com.towertest.managers.SceneManager;
import com.towertest.scenes.GameScene;

public class GameOverWindow extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private Text statusText;
	private Text scoreText;

	private ButtonSprite retryButton;
	private ButtonSprite quitButton;

	// ===========================================================
	// Constructors
	// ===========================================================
	public GameOverWindow(VertexBufferObjectManager vbom) {
		super(100, 50, 600, 380, ResourceManager.getInstance().backgroundTexture, vbom);

		statusText = new Text(0, 0, ResourceManager.getInstance().font40,
				"You lose! won", vbom);
		scoreText = new Text(0, 0, ResourceManager.getInstance().font40,
				"0123456789", vbom);
		
		retryButton = new ButtonSprite(0, 0,
				ResourceManager.getInstance().btnPlayTexture, vbom,
				new OnClickListener() {

					@Override
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						GameOverWindow.this.detachSelf();
						SceneManager.getInstance().loadGameScene(ResourceManager.getInstance().engine);
					}
				});
		quitButton = new ButtonSprite(0, 0,
				ResourceManager.getInstance().btnOptionsTexture, vbom,
				new OnClickListener() {

					@Override
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						GameOverWindow.this.detachSelf();
						SceneManager.getInstance().loadMainMenuScene();
					}
				});
		
		statusText.setPosition(getWidth() / 2 - statusText.getWidth() / 2, 50);
		scoreText.setPosition(getWidth() / 2 - scoreText.getWidth() / 2, 150);
		
		retryButton.setPosition(getWidth() / 4 - retryButton.getWidth() / 2, 250);
		quitButton.setPosition(getWidth() * 3 / 4 - quitButton.getWidth() / 2, 250);

		attachChild(statusText);
		attachChild(scoreText);
		attachChild(retryButton);
		attachChild(quitButton);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public void showResult(GameScene scene, Camera camera, boolean isVictory) {
		if (isVictory) {
			statusText.setText("You won!");
		}
		else {
			statusText.setText("You lose!");
		}
		scoreText.setText("Score: " + scene.getScores());
		setPosition((camera.getWidth() - getWidth()) / 2, 0);
		
		scene.clearTouchAreas();
		camera.getHUD().clearTouchAreas();
		
		scene.registerTouchArea(quitButton);
		scene.registerTouchArea(retryButton);
		
		scene.attachChild(this);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
