package com.towertest.scenes;

import org.andengine.entity.modifier.MoveByModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import com.towertest.managers.ResourceManager;
import com.towertest.managers.SceneManager;
import com.towertest.managers.SceneManager.SceneType;

public class LevelSelectScene extends BaseScene implements
		IOnSceneTouchListener, IOnMenuItemClickListener {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int MENU_ID_EASY = 0;
	private static final int MENU_ID_NORMAL = 1;
	private static final int MENU_ID_HARD = 2;
	private static final int MENU_ID_BACK = 4;
	
	private static final int MENU_ID_PREV = 8;
	private static final int MENU_ID_NEXT = 16;

	// ===========================================================
	// Fields
	// ===========================================================
	private Rectangle mapSelector;
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
		menuChildScene = new MenuScene(camera);
		createBackground();
		createMapSelector();
		createDifficultSelector();

		menuChildScene.setOnMenuItemClickListener(this);
		setChildScene(menuChildScene);
	}

	@Override
	public void onBackPressed() {
		SceneManager.getInstance().loadMainMenuScene();
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.LELEL_SELECT_SCENE;
	}

	@Override
	public void disposeScene() {
		menuChildScene.detachSelf();
		menuChildScene = null;
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// Catch swipe events
		
		return false;
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_ID_EASY:
		case MENU_ID_NORMAL:
		case MENU_ID_HARD:
			SceneManager.getInstance().loadGameScene(ResourceManager.getInstance().engine);
			return true;
		case MENU_ID_BACK:
			SceneManager.getInstance().loadMainMenuScene();
			return true;
		case MENU_ID_PREV:
			mapSelector.registerEntityModifier(new MoveByModifier(1f, 800, 0));
			return true;
		case MENU_ID_NEXT:
			mapSelector.registerEntityModifier(new MoveByModifier(1f, -800, 0));
			return true;
		}
		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	private void createBackground() {
		menuChildScene.attachChild(new Sprite(0, 0, 800, 480,
				resourceManager.backgroundTexture, vbom));
		
		menuChildScene.setBackgroundEnabled(false);
	}

	private void createMapSelector() {
		mapSelector = new Rectangle(0, 70, 2400, 240, vbom);
		mapSelector.setAlpha(0f);
		for (int i = 0; i < resourceManager.mapTextures.length; i++) {
			Sprite mapSprite = new Sprite(0, 0, 400, 200,resourceManager.mapTextures[i],
					vbom);
			mapSprite.setPosition(
					i * 800 + camera.getWidth() / 2 - mapSprite.getWidth() / 2, 0);
			mapSelector.attachChild(mapSprite);
		}
		menuChildScene.attachChild(mapSelector);
		
		IMenuItem nextBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(
				MENU_ID_EASY, resourceManager.nextButtonTexture, vbom),
				1.2f, 1.0f);
		
		IMenuItem prevBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(
				MENU_ID_EASY, resourceManager.prevButtonTexture, vbom),
				1.2f, 1.0f);
		
		prevBtn.setPosition(10, 134);
		nextBtn.setPosition(790 - prevBtn.getWidth(), 134);
		
		menuChildScene.attachChild(nextBtn);
		menuChildScene.attachChild(prevBtn);
	}

	private void createDifficultSelector() {
		IMenuItem easyBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(
				MENU_ID_EASY, 175, 70,resourceManager.difficultEasyTexture, vbom),
				1.2f, 1.0f);
		IMenuItem normalBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(
				MENU_ID_NORMAL, 175, 70, resourceManager.difficultNormalTexture, vbom),
				1.2f, 1.0f);
		IMenuItem hardBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(
				MENU_ID_HARD, 175, 70, resourceManager.difficultHardTexture, vbom),
				1.2f, 1.0f);
		IMenuItem backBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(
				MENU_ID_BACK, resourceManager.backButtonTexture, vbom), 1.2f,
				1.0f);

		easyBtn.setPosition(200 - easyBtn.getWidth() / 2, 300);
		normalBtn.setPosition(400 - hardBtn.getWidth() / 2, 300);
		hardBtn.setPosition(600 - normalBtn.getWidth() / 2, 300);
		backBtn.setPosition(30, camera.getHeight() - backBtn.getHeight() - 30);

		menuChildScene.addMenuItem(easyBtn);
		menuChildScene.addMenuItem(normalBtn);
		menuChildScene.addMenuItem(hardBtn);
		menuChildScene.addMenuItem(backBtn);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
