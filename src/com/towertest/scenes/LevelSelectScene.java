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
import org.andengine.util.color.Color;

import com.towertest.GameActivity;
import com.towertest.managers.GamePreferencesManager;
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
			GamePreferencesManager.getInstance().selectedDifficult = 0;
			SceneManager.getInstance().loadGameScene(
					ResourceManager.getInstance().engine);
			return true;
		case MENU_ID_NORMAL:
			GamePreferencesManager.getInstance().selectedDifficult = 1;
			SceneManager.getInstance().loadGameScene(
					ResourceManager.getInstance().engine);
			return true;
		case MENU_ID_HARD:
			GamePreferencesManager.getInstance().selectedDifficult = 2;
			SceneManager.getInstance().loadGameScene(
					ResourceManager.getInstance().engine);
			return true;
		case MENU_ID_BACK:
			SceneManager.getInstance().loadMainMenuScene();
			return true;
		case MENU_ID_PREV:
			if (GamePreferencesManager.getInstance().selectedMap == 0) {
				// At first map, move to last map.
				GamePreferencesManager.getInstance().selectedMap = ResourceManager.MAP_RESOURCES.length - 1;
				mapSelector.registerEntityModifier(new MoveByModifier(0.5f,
						-GameActivity.CAMERA_WIDTH * 2, 0));
			} else {
				mapSelector.registerEntityModifier(new MoveByModifier(0.5f,
						GameActivity.CAMERA_WIDTH, 0));
				GamePreferencesManager.getInstance().selectedMap -= 1;
			}
			return true;
		case MENU_ID_NEXT:
			if (GamePreferencesManager.getInstance().selectedMap == ResourceManager.MAP_RESOURCES.length - 1) {
				// At last map. Move to first map
				GamePreferencesManager.getInstance().selectedMap = 0;
				mapSelector.registerEntityModifier(new MoveByModifier(0.5f,
						GameActivity.CAMERA_WIDTH * 2, 0));
			}
			else {
				mapSelector
				.registerEntityModifier(new MoveByModifier(0.5f, -GameActivity.CAMERA_WIDTH, 0));
				GamePreferencesManager.getInstance().selectedMap += 1;
			}
			return true;
		}
		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	private void createBackground() {
		menuChildScene.attachChild(new Sprite(0, 0, GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT,
				resourceManager.backgroundTexture, vbom));
		
//		Rectangle background = new Rectangle(0, 0, GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT, vbom);
//		background.setColor(Color.WHITE);
//		menuChildScene.attachChild(background);
		
		menuChildScene.setBackgroundEnabled(false);
	}

	private void createMapSelector() {
		GamePreferencesManager.getInstance().selectedMap = 0;
		mapSelector = new Rectangle(0, 100, GameActivity.CAMERA_WIDTH * 3, GameActivity.CAMERA_HEIGHT / 2, vbom);
		mapSelector.setAlpha(0f);
		for (int i = 0; i < resourceManager.mapTextures.length; i++) {
			Sprite mapSprite = new Sprite(0, 0, resourceManager.mapTextures[i], vbom);
			mapSprite.setPosition(
					i * GameActivity.CAMERA_WIDTH + camera.getWidth() / 2 - mapSprite.getWidth() / 2,
					0);
			mapSelector.attachChild(mapSprite);
		}
		menuChildScene.attachChild(mapSelector);

		IMenuItem nextBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(
				MENU_ID_NEXT, resourceManager.nextButtonTexture, vbom), 1.2f,
				1.0f);

		IMenuItem prevBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(
				MENU_ID_PREV, resourceManager.prevButtonTexture, vbom), 1.2f,
				1.0f);

		prevBtn.setPosition(30, 234);
		nextBtn.setPosition(GameActivity.CAMERA_WIDTH - prevBtn.getWidth() - 30, 234);

		menuChildScene.addMenuItem(nextBtn);
		menuChildScene.addMenuItem(prevBtn);
	}

	private void createDifficultSelector() {
		IMenuItem easyBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(
				MENU_ID_EASY, 226, 87, resourceManager.difficultEasyTexture,
				vbom), 1.2f, 1.0f);
		IMenuItem normalBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(
				MENU_ID_NORMAL, 226, 87,
				resourceManager.difficultNormalTexture, vbom), 1.2f, 1.0f);
		IMenuItem hardBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(
				MENU_ID_HARD, 226, 87, resourceManager.difficultHardTexture,
				vbom), 1.2f, 1.0f);
		IMenuItem backBtn = new ScaleMenuItemDecorator(new SpriteMenuItem(
				MENU_ID_BACK, resourceManager.backButtonTexture, vbom), 1.2f,
				1.0f);

		easyBtn.setPosition(GameActivity.CAMERA_WIDTH / 4 - easyBtn.getWidth() / 2, 450);
		normalBtn.setPosition(GameActivity.CAMERA_WIDTH / 2 - hardBtn.getWidth() / 2, 450);
		hardBtn.setPosition(GameActivity.CAMERA_WIDTH * 3/ 4 - normalBtn.getWidth() / 2, 450);
		backBtn.setPosition(30, 30);

		menuChildScene.addMenuItem(easyBtn);
		menuChildScene.addMenuItem(normalBtn);
		menuChildScene.addMenuItem(hardBtn);
		menuChildScene.addMenuItem(backBtn);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
