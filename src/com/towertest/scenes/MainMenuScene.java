package com.towertest.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.util.color.Color;

import android.util.Log;

import com.towertest.managers.SceneManager;
import com.towertest.managers.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements
		IOnMenuItemClickListener {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int MENU_PLAY = 0;
	private static final int MENU_OPT = 1;

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
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_PLAY:
			// Toast.makeText(getBaseContext(), "MENU_PLAY!",
			// Toast.LENGTH_LONG).show();
			Log.e("Jared", "MENU_PLAY");
			SceneManager.getInstance().loadGameScene(engine);
//			scene = createGameScene();
//			mainScene.setChildScene(scene);
			break;
		case MENU_OPT:
			// Toast.makeText(getBaseContext(), "MENU_OPT!",
			// Toast.LENGTH_LONG).show();
			Log.e("Jared", "MENU_OPT");
			break;
		}
		return false;
	}

	@Override
	public void createScene() {
		menuChildScene = new MenuScene(camera);
		menuChildScene.setBackground(new Background(Color.WHITE));

		final IMenuItem btnPlay = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_PLAY, resourceManager.btnPlayTexture,
						vbom), 1.1f, 1);

		final IMenuItem btnOption = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_OPT, resourceManager.btnOptionsTexture,
						vbom), 1.1f, 1);
		
		btnPlay.setPosition(camera.getCenterX(), camera.getCenterY() + 50);
		btnOption.setPosition(camera.getCenterX(), camera.getCenterY() - 50);
		
		menuChildScene.addMenuItem(btnPlay);
		menuChildScene.addMenuItem(btnOption);
		menuChildScene.buildAnimations();
		
		menuChildScene.setOnMenuItemClickListener(this);
		setBackgroundEnabled(false);
		
		setChildScene(menuChildScene);
	}

	@Override
	public void onBackPressed() {
		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		return null;
	}

	@Override
	public void disposeScene() {
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
