package com.towertest.hud;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.towertest.GameActivity;
import com.towertest.managers.ResourceManager;
import com.towertest.managers.SceneManager;
import com.towertest.scenes.GameScene;

public class GameOverWindow extends Rectangle {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private Text statusText;
	private Text scoreText;

	private ButtonSprite retryButton;
	private ButtonSprite menuButton;

	// ===========================================================
	// Constructors
	// ===========================================================
	public GameOverWindow(VertexBufferObjectManager vbom) {
		super(0, 0, GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT, vbom);

		statusText = new Text(0, 300, ResourceManager.getInstance().font20,
				"You lose! won!", vbom);
		scoreText = new Text(300, 300, ResourceManager.getInstance().font20,
				"Scores: 0123456789", vbom);

		retryButton = new ButtonSprite(0, 0,
				ResourceManager.getInstance().btnRestartTexture, vbom,
				new OnClickListener() {

					@Override
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						GameOverWindow.this.detachSelf();
						SceneManager.getInstance().loadGameScene(
								ResourceManager.getInstance().engine);
					}
				});
		menuButton = new ButtonSprite(0, 0,
				ResourceManager.getInstance().btnMainMenuTexture, vbom,
				new OnClickListener() {

					@Override
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						GameOverWindow.this.detachSelf();
						SceneManager.getInstance().loadMainMenuScene();
					}
				});

		statusText.setPosition(getWidth() / 2 - statusText.getWidth() / 2, 150);
		scoreText.setPosition(getWidth() / 2 - scoreText.getWidth() / 2, 250);

		retryButton.setPosition(getWidth() / 4 - retryButton.getWidth() / 2,
				350);
		menuButton.setPosition(getWidth() * 3 / 4 - menuButton.getWidth() / 2,
				350);

		attachChild(statusText);
		attachChild(scoreText);
		attachChild(retryButton);
		attachChild(menuButton);
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
		} else {
			statusText.setText("You lose!");
		}
		scoreText.setText("Score: " + scene.getScores());
		setColor(0.8f, 0.8f, 0.8f, 0.5f);

		scene.clearTouchAreas();
		camera.getHUD().clearTouchAreas();

		scene.registerTouchArea(menuButton);
		scene.registerTouchArea(retryButton);

		scene.attachChild(this);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
