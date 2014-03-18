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
import com.towertest.scenes.GameScene;

public class GamePausedWindow extends Rectangle {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private ButtonSprite resumeButton;
	private ButtonSprite levelSelectButton;
	private ButtonSprite menuButton;

	// ===========================================================
	// Constructors
	// ===========================================================
	public GamePausedWindow(VertexBufferObjectManager vbom) {
		super(0, 0, GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT, vbom);

		resumeButton = new ButtonSprite(0, 0, 
				ResourceManager.getInstance().btnResumeTexture, vbom,
				new OnClickListener() {
					@Override
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
					}
				});
		
		resumeButton.setWidth(280);
		resumeButton.setHeight(87);
		
		levelSelectButton = new ButtonSprite(0, 0,
				ResourceManager.getInstance().btnSelectLevelTexture, vbom,
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

		resumeButton.setPosition(
				GameActivity.CAMERA_WIDTH / 2 - resumeButton.getWidth() / 2,
				GameActivity.CAMERA_HEIGHT / 2 - levelSelectButton.getHeight() - 20);
		levelSelectButton.setPosition(GameActivity.CAMERA_WIDTH / 2
				- levelSelectButton.getWidth() / 2,
				GameActivity.CAMERA_HEIGHT / 2);
		menuButton.setPosition(
				GameActivity.CAMERA_WIDTH / 2 - menuButton.getWidth() / 2,
				GameActivity.CAMERA_HEIGHT / 2 + levelSelectButton.getHeight() + 20);

		attachChild(resumeButton);
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
	public void show(final Scene scene, Camera camera) {
		setColor(new Color(0.8f, 0.8f, 0.8f, 0.5f));
		
		resumeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				((GameScene) scene).togglePauseGame();
			}
		});

		camera.getHUD().registerTouchArea(resumeButton);
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
