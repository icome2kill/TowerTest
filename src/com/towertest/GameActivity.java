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
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
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

	public static int CAMERA_WIDTH = 960;
	public static int CAMERA_HEIGHT = 576;

	private Camera camera;
	/** used to offset the pan to adjust for panning from a tower */
	public static float currentXoffset = 0;
	/** used to offset the pan to adjust for panning from a tower */
	public static float currentYoffset = 0;

	@Override
	public EngineOptions onCreateEngineOptions() {
		Log.i("Location:", "onCreateEngineOptions");
		// =================================================================================//
		// Setup Camera
		// ================================================================================//
		final DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		final EngineOptions mEngine = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				camera);

		// if (MultiTouch.isSupported(this)) {
		// if (MultiTouch.isSupportedDistinct(this))
		// Toast.makeText(this,
		// "MultiTouch detected Pinch Zoom will work properly!",
		// Toast.LENGTH_SHORT).show();
		// else
		// Toast.makeText(
		// this,
		// "MultiTouch detected, but your device has problems distinguishing between fingers",
		// Toast.LENGTH_LONG).show();
		// } else
		// Toast.makeText(
		// this,
		// "Sorry your device does NOT support MultiTouch! Use Zoom Buttons.",
		// Toast.LENGTH_LONG).show();
		mEngine.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		return mEngine;
	}

	/**
	 * Load all game resources
	 */
	@Override
	protected void onCreateResources() {
		Log.i("Location:", "onCreateResources");
		ResourceManager.prepareManager(mEngine, this, camera,
				getVertexBufferObjectManager());
		resourceManager = ResourceManager.getInstance();
		resourceManager.loadMusicResources();
		if (resourceManager.bgMusic != null) {
			resourceManager.bgMusic.play();
		}
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
	// END OF CLASS
}
