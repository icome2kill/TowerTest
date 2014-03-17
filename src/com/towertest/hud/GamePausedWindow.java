package com.towertest.hud;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import com.towertest.GameActivity;
import com.towertest.managers.ResourceManager;
import com.towertest.managers.SceneManager;

public class GamePausedWindow extends Rectangle {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private ButtonSprite restartButton;
	private ButtonSprite levelSelectButton;
	private ButtonSprite menuButton;

	// ===========================================================
	// Constructors
	// ===========================================================
	public GamePausedWindow(VertexBufferObjectManager vbom) {
		super(0, 0, GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT, vbom);

		restartButton = new ButtonSprite(0, 0,
				ResourceManager.getInstance().btnRestartTexture, vbom,
				new OnClickListener() {
					@Override
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						detachSelf();
						SceneManager.getInstance().loadGameScene(null);
					}
				});
		levelSelectButton = new ButtonSprite(0, 0,
				ResourceManager.getInstance().btnLvlSelectTexture, vbom,
				new OnClickListener() {

					@Override
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						SceneManager.getInstance().loadLevelSelectScene();
					}
				});
		menuButton = new ButtonSprite(0, 0,
				ResourceManager.getInstance().btnMainMenuTexture, vbom,
				new OnClickListener() {
					@Override
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						SceneManager.getInstance().loadMainMenuScene();
					}
				});

		restartButton.setPosition(
				GameActivity.CAMERA_WIDTH / 2 - restartButton.getWidth() / 2,
				GameActivity.CAMERA_HEIGHT / 2 - levelSelectButton.getHeight() - 20);
		levelSelectButton.setPosition(GameActivity.CAMERA_WIDTH / 2
				- levelSelectButton.getWidth() / 2,
				GameActivity.CAMERA_HEIGHT / 2);
		menuButton.setPosition(
				GameActivity.CAMERA_WIDTH / 2 - menuButton.getWidth() / 2,
				GameActivity.CAMERA_HEIGHT / 2 + levelSelectButton.getHeight() + 20);

		attachChild(restartButton);
		attachChild(levelSelectButton);
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
	public void show(Scene scene, Camera camera) {
		setColor(new Color(0.8f, 0.8f, 0.8f, 0.5f));

		camera.getHUD().registerTouchArea(restartButton);
		camera.getHUD().registerTouchArea(levelSelectButton);
		camera.getHUD().registerTouchArea(menuButton);

		camera.getHUD().attachChild(this);
	}

	@Override
	public boolean detachSelf() {
		return super.detachSelf();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
