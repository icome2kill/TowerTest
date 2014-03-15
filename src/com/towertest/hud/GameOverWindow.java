package com.towertest.hud;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

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
	private ButtonSprite quitButton;

	// ===========================================================
	// Constructors
	// ===========================================================
	public GameOverWindow(VertexBufferObjectManager vbom) {
		super(0, 0, 650, 400, vbom);

		statusText = new Text(300, 150, ResourceManager.getInstance().font40,
				"You lose! won", vbom);
		scoreText = new Text(300, 200, ResourceManager.getInstance().font40,
				"0123456789", vbom);

		retryButton = new ButtonSprite(0, 350,
				ResourceManager.getInstance().btnPlayTexture, vbom,
				new OnClickListener() {

					@Override
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						GameOverWindow.this.detachSelf();
						SceneManager.getInstance().loadGameScene(ResourceManager.getInstance().engine);
					}
				});
		quitButton = new ButtonSprite(300, 350,
				ResourceManager.getInstance().btnOptionsTexture, vbom,
				new OnClickListener() {

					@Override
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						GameOverWindow.this.detachSelf();
						SceneManager.getInstance().loadMainMenuScene();
					}
				});

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
		
		camera.getHUD().setVisible(false);
		camera.getHUD().registerTouchArea(quitButton);
		camera.getHUD().registerTouchArea(retryButton);
		
		scene.attachChild(this);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
