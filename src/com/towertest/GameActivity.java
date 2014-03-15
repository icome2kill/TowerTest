package com.towertest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.algorithm.path.astar.AStarPathFinder;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.towertest.managers.ResourceManager;
import com.towertest.managers.SceneManager;
import com.towertest.scenes.BaseScene;
import com.towertest.sprites.Enemy;
import com.towertest.sprites.Tower;

@SuppressWarnings("unused")
public class GameActivity extends SimpleBaseGameActivity {
	private ResourceManager resourceManager;
	
	public static int CAMERA_WIDTH = 800;
	public static int CAMERA_HEIGHT = 480;
	
	private Camera camera;
	/** used to offset the pan to adjust for panning from a tower */
	public static float currentXoffset = 0;
	/** used to offset the pan to adjust for panning from a tower */
	public static float currentYoffset = 0;
	// private Camera camera;

	// ========================================
	// Others
	// ========================================
	// for touches

	@Override
	public EngineOptions onCreateEngineOptions() {
		Log.i("Location:", "onCreateEngineOptions");
		// =================================================================================//
		// Setup Camera
		// ================================================================================//
		final DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		camera = new Camera(0, -50, CAMERA_WIDTH, CAMERA_HEIGHT);

		final EngineOptions mEngine = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(
						800, 480), camera);

		if (MultiTouch.isSupported(this)) {
			if (MultiTouch.isSupportedDistinct(this))
				Toast.makeText(this,
						"MultiTouch detected Pinch Zoom will work properly!",
						Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(
						this,
						"MultiTouch detected, but your device has problems distinguishing between fingers",
						Toast.LENGTH_LONG).show();
		} else
			Toast.makeText(
					this,
					"Sorry your device does NOT support MultiTouch! Use Zoom Buttons.",
					Toast.LENGTH_LONG).show();
		mEngine.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		return mEngine;
	}

	/**
	 * Load all game resources
	 */
	@Override
	protected void onCreateResources() {
		Log.i("Location:", "onCreateResources");
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		ResourceManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
		resourceManager = ResourceManager.getInstance();
	}

	/**
	 * Create Scene Create all scenes to use in game
	 */
	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		Log.i("Location:", "onCreateScene");
		SceneManager.getInstance().loadMainMenuScene();
		return SceneManager.getInstance().getCurrentScene();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		SceneManager.getInstance().getCurrentScene().onBackPressed();
	}

	
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//			return true;
//		} else {
//			return false;
//		}
//	}

	// =====================================
	// Pinch Zoom and Scroll stuff
	// =====================================
	// TODO establish limits
	// static float currentZoom = 1;

	/**
	 * Translates x coordinate from hud coordinates to scene coordinates (used
	 * for tower placement)
	 * 
	 * @param x
	 *            X coordinate to be translated
	 * @return translated X coordinate
	 */

	// public static float getZoom() {
	// return camera.getZoomFactor();
	// }
	//
	// private void scenePan(float pDistanceX, float pDistanceY) {
	// final float zoomFactor = TowerTest.camera.getZoomFactor();
	// TowerTest.camera.offsetCenter((-pDistanceX) / zoomFactor -
	// currentXoffset, (-pDistanceY) / zoomFactor - currentYoffset);
	// currentXoffset = 0;
	// currentYoffset = 0;
	// // Log.e("ScenePan", "currentXoffset:"+currentXoffset);
	// // Log.e("ScenePan", "currentYoffset:"+currentYoffset);
	//
	// // Log.e("ScenePan", "pDistanceX:"+pDistanceX);
	// // Log.e("ScenePan", "pDistanceY:"+pDistanceY);
	//
	// }

	// @Override
	// public void onScrollStarted(final ScrollDetector pScollDetector, final
	// int pPointerID, final float pDistanceX, final float pDistanceY) {
	// scenePan(pDistanceX, pDistanceY);
	// }
	//
	// @Override
	// public void onScroll(final ScrollDetector pScollDetector, final int
	// pPointerID, final float pDistanceX, final float pDistanceY) {
	// scenePan(pDistanceX, pDistanceY);
	// }
	//
	// @Override
	// public void onScrollFinished(final ScrollDetector pScollDetector, final
	// int pPointerID, final float pDistanceX, final float pDistanceY) {
	// scenePan(pDistanceX, pDistanceY);
	// }
	//
	// @Override
	// public void onPinchZoomStarted(final PinchZoomDetector
	// pPinchZoomDetector, final TouchEvent pTouchEvent) {
	// final float zoomFactor = TowerTest.camera.getZoomFactor();
	// mPinchZoomStartedCameraZoomFactor = zoomFactor;
	// currentXoffset = 0;
	// currentYoffset = 0;
	// }
	//
	// @Override
	// public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final
	// TouchEvent pTouchEvent, final float pZoomFactor) {
	// TowerTest.camera.setZoomFactor(Math.min(Math.max(TowerTest.MIN_ZOOM,
	// mPinchZoomStartedCameraZoomFactor * pZoomFactor), TowerTest.MAX_ZOOM));
	//
	// }
	//
	// @Override
	// public void onPinchZoomFinished(final PinchZoomDetector
	// pPinchZoomDetector, final TouchEvent pTouchEvent, final float
	// pZoomFactor) {
	// TowerTest.camera.setZoomFactor(Math.min(Math.max(TowerTest.MIN_ZOOM,
	// mPinchZoomStartedCameraZoomFactor * pZoomFactor), TowerTest.MAX_ZOOM));
	// }

	/*
	 * @Override protected void onResume() { super.onResume(); if(this.mEngine
	 * != null && !this.mEngine.isRunning()){ this.mEngine.start(); } }
	 * 
	 * @Override protected void onPause() { super.onPause(); if(this.mEngine !=
	 * null && this.mEngine.isRunning()){ this.mEngine.stop(); } };
	 */

	public static TextureRegion loadSprite(TextureManager tm, Context c,
			String strtex) {
		TextureRegion tr;
		BitmapTextureAtlas towerImage;
		towerImage = new BitmapTextureAtlas(tm, 512, 512);
		tr = BitmapTextureAtlasTextureRegionFactory.createFromAsset(towerImage,
				c, strtex, 0, 0);
		tm.loadTexture(towerImage);
		return tr;
	}

	// END OF CLASS
}
