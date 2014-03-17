package com.towertest.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.modifier.IModifier;

import com.towertest.GameActivity;
import com.towertest.managers.ResourceManager;
import com.towertest.managers.SceneManager;
import com.towertest.managers.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements
IOnMenuItemClickListener {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int MENU_PLAY = 0;
	private static final int MENU_HELP = 1;
	private static final int MENU_BACK = 2;

	// ===========================================================
	// Fields
	// ===========================================================
	private MenuScene menuChildScene;
	private IMenuItem btnBack;

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
			SceneManager.getInstance().loadLevelSelectScene();
			return true;
		case MENU_HELP:
			return true;
		case MENU_BACK:
			return true;
		}
		return false;
	}

	@Override
	public void createScene() {
		menuChildScene = new MenuScene(camera);

		// Create background
		menuChildScene.attachChild(new Sprite(0, 0, GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT, ResourceManager
				.getInstance().backgroundTexture, vbom));
//		menuChildScene.attachChild(new Sprite(0, 0, (int) (270 * 1.5), GameActivity.CAMERA_HEIGHT,
//				ResourceManager.getInstance().treeTexture, vbom));
//
//		// Nature and human sprites
//		final Sprite natureSprite = new Sprite(-500, -500, (int) (167 * 1.5), GameActivity.CAMERA_HEIGHT,
//				ResourceManager.getInstance().natureTexture, vbom);
//		final Sprite humanSprite = new Sprite(-500, -500, (int) (167 * 1.5), GameActivity.CAMERA_HEIGHT,
//				ResourceManager.getInstance().humanTexture, vbom);
//
//		// Title Sprites
//		final Sprite titleSprite = new Sprite(380, 70, ResourceManager.getInstance().titleTexture, vbom);

		// Button
		final IMenuItem btnPlay = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_PLAY, resourceManager.btnPlayTexture,
						vbom), 1.2f, 1);

		final IMenuItem btnOption = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_HELP, resourceManager.btnOptionsTexture,
						vbom), 1.2f, 1);
		
//		btnPlay.setPosition(-500, -500);
//		btnOption.setPosition(-500, -500);
		
		// Grass Sprite
//		final Sprite grassSprite = new Sprite(-500, -500, GameActivity.CAMERA_WIDTH, 161 * GameActivity.CAMERA_WIDTH / 512, ResourceManager.getInstance().grassTexture, vbom);
//
//		natureSprite.registerEntityModifier(new MoveModifier(0.5f, -natureSprite
//				.getWidth(), 0, 0, 0, new IEntityModifierListener() {
//
//			@Override
//			public void onModifierStarted(IModifier<IEntity> pModifier,
//					IEntity pItem) {
//			}
//
//			@Override
//			public void onModifierFinished(IModifier<IEntity> pModifier,
//					IEntity pItem) {
//				humanSprite.registerEntityModifier(new MoveModifier(0.5f, camera
//						.getWidth(),
//						camera.getWidth() - humanSprite.getWidth(), 0, 0, new IEntityModifierListener() {
//
//					@Override
//					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
//					}
//
//					@Override
//					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
//						titleSprite.registerEntityModifier(new ScaleModifier(0.5f, 0f, 3f, new IEntityModifierListener() {
//							@Override
//							public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
//							}
//
//							@Override
//							public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
//								titleSprite.registerEntityModifier(new ScaleModifier(0.75f, 3f, 2.5f, new IEntityModifierListener() {
//
//									@Override
//									public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
//									}
//
//									@Override
//									public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
//										FadeInModifier modifier = new FadeInModifier(1f, new IEntityModifierListener() {
//											
//											@Override
//											public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
//												grassSprite.setPosition(0, camera.getHeight() - grassSprite.getHeight());
//												btnPlay.setPosition(camera.getWidth() / 2 - btnPlay.getWidth() / 2, 320);
//												btnOption.setPosition(camera.getWidth() / 2 - btnOption.getWidth() / 2, 400);
//											}
//											
//											@Override
//											public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
//												
//											}
//										});
//										grassSprite.registerEntityModifier(modifier);
//										btnPlay.registerEntityModifier(modifier);
//										btnOption.registerEntityModifier(modifier);
//										menuChildScene.attachChild(grassSprite);
//										menuChildScene.addMenuItem(btnPlay);
//										menuChildScene.addMenuItem(btnOption);
//									}
//								}));
//							}
//						}));
//						menuChildScene.attachChild(titleSprite);
//					}
//				}));
//				menuChildScene.attachChild(humanSprite);
//			}
//		}));
		
		btnPlay.setPosition(camera.getWidth() - btnPlay.getWidth(), 200);
		btnOption.setPosition(camera.getWidth() - btnPlay.getWidth(), 300);
		
		menuChildScene.addMenuItem(btnPlay);
		menuChildScene.addMenuItem(btnOption);
		
//		menuChildScene.attachChild(natureSprite);

//		menuChildScene.buildAnimations();
		menuChildScene.setOnMenuItemClickListener(this);
		
		setBackgroundEnabled(false);
		setChildScene(menuChildScene);
	}

	@Override
	public void onBackPressed() {
		disposeScene();
		ResourceManager.getInstance().unloadMainMenuResources();
		ResourceManager.getInstance().unloadFonts();
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
