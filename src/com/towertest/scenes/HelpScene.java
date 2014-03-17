package com.towertest.scenes;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;

import com.towertest.managers.SceneManager;
import com.towertest.managers.SceneManager.SceneType;

public class HelpScene extends BaseScene {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int MENU_BACK = 0;
	// ===========================================================
	// Fields
	// ===========================================================
	private MenuScene menuChildScene;
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void createScene() {
		menuChildScene = new MenuScene(camera, new IOnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
					float pMenuItemLocalX, float pMenuItemLocalY) {
				switch (pMenuItem.getID()) {
				case MENU_BACK:
					onBackPressed();
					return true;
				}
				return false;
			}
		});
		menuChildScene.attachChild(new Sprite(0, 0, camera.getWidth(), camera.getHeight(),
				resourceManager.backgroundTexture, vbom));

		IMenuItem btnBack = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_BACK,
				resourceManager.btnBackTexture, vbom), 1.2f, 1f);

		btnBack.setPosition(30, camera.getHeight() - btnBack.getHeight() - 30);
		registerTouchArea(btnBack);
		menuChildScene.addMenuItem(btnBack);

		Sprite instructionPanel = new Sprite(0, 0, 351 * 2, 249 * 2,
				resourceManager.instructionPanelTexture, vbom);
		instructionPanel.setPosition(
				camera.getWidth() / 2 - instructionPanel.getWidth() / 2,
				camera.getHeight() / 2 - instructionPanel.getHeight() / 2);
		menuChildScene.attachChild(instructionPanel);
		menuChildScene.setBackgroundEnabled(false);
		setChildScene(menuChildScene);
	}

	@Override
	public void onBackPressed() {
		SceneManager.getInstance().loadMainMenuScene();
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.HELP_SCENE;
	}

	@Override
	public void disposeScene() {
		clearTouchAreas();
		menuChildScene.detachSelf();
		menuChildScene = null;
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
