package com.towertest.managers;

import org.andengine.engine.Engine;

import com.towertest.scenes.BaseScene;
import com.towertest.scenes.GameScene;
import com.towertest.scenes.LevelSelectScene;
import com.towertest.scenes.MainMenuScene;
import com.towertest.scenes.MapSelectScene;

public class SceneManager {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final SceneManager INSTANCE = new SceneManager();
	// ===========================================================
	// Fields
	// ===========================================================

	private BaseScene menuScene;
	private BaseScene gameScene;
	private BaseScene levelSelectScene;
	private BaseScene mapSelectScene;

	private BaseScene currentScene = null;
	private SceneType currentSceneType = null;

	private Engine engine = ResourceManager.getInstance().engine;

	// ===========================================================
	// Constructors
	// ===========================================================
	private SceneManager() {
	};

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public static SceneManager getInstance() {
		return INSTANCE;
	}

	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}

	public void setSceneType(SceneType sceneType) {
		switch (sceneType) {
		case MENU_SCENE:
			setScene(menuScene);
			break;
		case GAME_SCENE:
			setScene(gameScene);
			break;
		case LELEL_SELECT_SCENE:
			setScene(levelSelectScene);
			break;
		case MAP_SELECT_SCENE:
			setScene(mapSelectScene);
			break;
		}
	}

	public BaseScene getCurrentScene() {
		return currentScene;
	}

	public SceneType getCurrentSceneType() {
		return currentSceneType;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public void loadGameScene(final Engine engine) {
		disposeCurrentScene();
		ResourceManager.getInstance().loadGameResource();
		gameScene = new GameScene();
		setScene(gameScene);
	}

	public void loadMainMenuScene() {
		disposeCurrentScene();
		ResourceManager.getInstance().loadMainMenuResources();
		menuScene = new MainMenuScene();
		setScene(menuScene);
	}

	public void loadMapSelectScene() {
		disposeCurrentScene();
		ResourceManager.getInstance().loadMapSelectResources();
		mapSelectScene = new MapSelectScene();
		setScene(mapSelectScene);
	}
	
	public void loadLevelSelectScene() {
		disposeCurrentScene();
		ResourceManager.getInstance().loadLevelSelectResources();
		levelSelectScene = new LevelSelectScene();
		setScene(levelSelectScene);
	}

	private void disposeCurrentScene() {
		if (currentSceneType != null) {
			switch (currentSceneType) {
			case MENU_SCENE:
				ResourceManager.getInstance().unloadMainMenuResources();
				break;
			case GAME_SCENE:
				ResourceManager.getInstance().unloadGameResource();
				break;
			case MAP_SELECT_SCENE:
				break;
			case LELEL_SELECT_SCENE:
				break;
			}
		}
		if (currentScene != null) {
			currentScene.disposeScene();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public static enum SceneType {
		MENU_SCENE, LELEL_SELECT_SCENE, MAP_SELECT_SCENE, GAME_SCENE
	}
}
