package com.towertest.managers;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;
import android.util.Log;

import com.towertest.GameActivity;
import com.towertest.scenes.GameScene;
import com.towertest.sprites.Projectile;

public class ResourceManager {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final ResourceManager INSTANCE = new ResourceManager();

	public static int TILEID_BLOCKED = 100;

	public static final String[] ENEMY_RESOURCES = { "enemy1.png",
			"enemy2.png", "enemy3.png", "enemy4.png" };
	public static final String[] TOWER_RESOURCES = { "tower1.png",
			"tower2.png", "tower3.png", "tower4.png" };
	public static final String[] MAP_RESOURCES = { "map1", "map2", "map3" };

	public static int TILEID_PATH[] = new int[MAP_RESOURCES.length];
	public static int TILEID_UNPATHABLE[] = new int[MAP_RESOURCES.length];

	// ===========================================================
	// Fields
	// ===========================================================
	public Camera camera;
	public VertexBufferObjectManager vbom;
	public Engine engine;
	public GameActivity activity;

	private BuildableBitmapTextureAtlas gameTextureAtlas;
	public ITiledTextureRegion[] towerTexture;
	public ITiledTextureRegion[] enemyTexture;
	public ITextureRegion bulletTexture;
	public ITextureRegion hitAreaGoodTexture;
	public ITextureRegion hitAreaBadTexture;
	public ITextureRegion texPause;
	public ITextureRegion texPlay;
	public TMXTiledMap[] tmxTiledMapArray;
	public TMXTiledMap tmxTiledMap;
	public ITextureRegion towerRemoveButtonTexture;
	public Sound fireSound;

	public ITextureRegion resumeButtonTexture;
	public ITextureRegion lvlSelectButtonTexture;
	public ITextureRegion mainMenuButtonTexture;
	public ITextureRegion restartButtonTexture;

	private BuildableBitmapTextureAtlas mainMenuTextureAtlas;
	public ITextureRegion btnOptionsTexture;
	public ITextureRegion btnAboutTexture;
	public ITextureRegion btnPlayTexture;
	public ITextureRegion backgroundTexture;
	public ITextureRegion titleTexture;
	public ITextureRegion treeTexture;
	public ITextureRegion humanTexture;
	public ITextureRegion natureTexture;
	public ITextureRegion grassTexture;

	private BuildableBitmapTextureAtlas levelSelectTextureAtlas;
	public ITextureRegion[] mapTextures;
	public ITextureRegion difficultEasyTexture;
	public ITextureRegion difficultNormalTexture;
	public ITextureRegion difficultHardTexture;
	public ITextureRegion backButtonTexture;
	public ITextureRegion nextButtonTexture;
	public ITextureRegion prevButtonTexture;

	public Font font10;
	public Font font20;
	public Font font40;

	private int i = 0;

	// ===========================================================
	// Constructors
	// ===========================================================
	private ResourceManager() {
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public static ResourceManager getInstance() {
		return INSTANCE;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public static void prepareManager(Engine engine, GameActivity activity,
			Camera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}

	public void loadGameResource() {
		loadFonts();
		loadGameGraphics();
		loadGameAudio();
	}

	private void loadGameAudio() {
		try {
			SoundFactory.setAssetBasePath("mfx/");
			fireSound = SoundFactory.createSoundFromAsset(
					engine.getSoundManager(), activity, "tower.ogg");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024);

		towerTexture = new ITiledTextureRegion[TOWER_RESOURCES.length];

		for (int i = 0; i < towerTexture.length; i++) {
			towerTexture[i] = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(gameTextureAtlas, activity,
							TOWER_RESOURCES[i], 1, 1);
		}
		bulletTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity, Projectile.texture);

		hitAreaGoodTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity,
						"towerRangeGood.png");
		hitAreaBadTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity,
						"towerRangeBad.png");

		enemyTexture = new ITiledTextureRegion[ENEMY_RESOURCES.length];

		for (int i = 0; i < ENEMY_RESOURCES.length; i++) {
			enemyTexture[i] = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(gameTextureAtlas, activity,
							ENEMY_RESOURCES[i], 3, 1);
		}

		towerRemoveButtonTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity,
						"towerRemoveButton.png");

		texPause = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity, "pause.png");
		texPlay = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity, "play.png");

		resumeButtonTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity, "resume.png");
		lvlSelectButtonTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity, "lvl_select.png");
		restartButtonTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity, "restart.png");
		mainMenuButtonTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity, "main_menu.png");

		try {
			gameTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			gameTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}

		Log.i("Location:", "TMXMap block");
		try {
			final TMXLoader tmxLoader = new TMXLoader(
					ResourceManager.getInstance().activity.getAssets(),
					engine.getTextureManager(),
					TextureOptions.BILINEAR_PREMULTIPLYALPHA,
					ResourceManager.getInstance().vbom,
					new ITMXTilePropertiesListener() {
						@Override
						public void onTMXTileWithPropertiesCreated(
								final TMXTiledMap pTMXTiledMap,
								final TMXLayer pTMXLayer,
								final TMXTile pTMXTile,
								final TMXProperties<TMXTileProperty> pTMXTileProperties) {
							if (pTMXTileProperties != null) {
								if (pTMXTileProperties
										.containsTMXProperty(
												GameScene.TAG_TILED_PROPERTY_NAME_PATH,
												GameScene.TAG_TILED_PROPERTY_VALUE_TRUE)) {
									TILEID_PATH[i] = pTMXTile.getGlobalTileID();
								} else if (pTMXTileProperties
										.containsTMXProperty(
												GameScene.TAG_TILED_PROPERTY_NAME_PATH,
												GameScene.TAG_TILED_PROPERTY_VALUE_FALSE)
										&& !pTMXTileProperties
												.containsTMXProperty(
														GameScene.TAG_TILED_PROPERTY_NAME_BUILDABLE,
														GameScene.TAG_TILED_PROPERTY_VALUE_FALSE)) {
									TILEID_UNPATHABLE[i] = pTMXTile
											.getGlobalTileID();
								}
							}
						}
					});
			// Load the Desert Map
			Log.i("Location:", "TMXMap Loading...");
			tmxTiledMapArray = new TMXTiledMap[MAP_RESOURCES.length];
			for (i = 0; i < MAP_RESOURCES.length; i++) {
				tmxTiledMapArray[i] = tmxLoader.loadFromAsset("tmx/"
						+ MAP_RESOURCES[i] + ".tmx");
			}
			tmxTiledMap = tmxTiledMapArray[0];
			Log.i("Location:", "TMXMap Loaded");
		} catch (final TMXLoadException e) {
			Log.e("Resource Manager", "Load tmx map failed. Check this!");
			Debug.e(e);
		}
	}

	public void unloadGameResource() {
		gameTextureAtlas.unload();

		tmxTiledMap = null;
		texPlay = null;
		texPause = null;
		towerRemoveButtonTexture = null;
		bulletTexture = null;
		hitAreaGoodTexture = null;
		hitAreaBadTexture = null;
		resumeButtonTexture = null;
		lvlSelectButtonTexture = null;
		mainMenuButtonTexture = null;
		restartButtonTexture = null;

		for (int i = 0; i < ENEMY_RESOURCES.length; i++) {
			enemyTexture[i] = null;
		}
		for (int i = 0; i < TOWER_RESOURCES.length; i++) {
			towerTexture[i] = null;
		}
		for (int i = 0; i < MAP_RESOURCES.length; i++) {
			tmxTiledMapArray[i] = null;
		}
	}

	public void loadMainMenuResources() {
		loadMainMenuGraphics();
		loadMainMenuAudio();
		loadFonts();
	}

	private void loadMainMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		mainMenuTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024);

		btnPlayTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mainMenuTextureAtlas, activity, "play.png");
		btnOptionsTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mainMenuTextureAtlas, activity, "options.png");
		humanTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mainMenuTextureAtlas, activity, "human.png");
		natureTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mainMenuTextureAtlas, activity, "nature.png");
		treeTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mainMenuTextureAtlas, activity, "tree.png");
		titleTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mainMenuTextureAtlas, activity, "title.png");
		backgroundTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mainMenuTextureAtlas, activity, "bg.png");
		grassTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mainMenuTextureAtlas, activity, "grass.png");

		try {
			mainMenuTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			mainMenuTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Log.e("Resource Manager", "Error loadding main menu graphic");
			e.printStackTrace();
		}
	}

	private void loadMainMenuAudio() {

	}

	public void unloadMainMenuResources() {
		mainMenuTextureAtlas.unload();

		humanTexture = null;
		natureTexture = null;
		treeTexture = null;
		titleTexture = null;
		backgroundTexture = null;
		grassTexture = null;

		mainMenuTextureAtlas = null;
	}

	public void loadLevelSelectResources() {
		loadLevelSelectGraphics();
	}

	private void loadLevelSelectGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/level/");

		levelSelectTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024);

		mapTextures = new ITextureRegion[MAP_RESOURCES.length];
		for (int i = 0; i < mapTextures.length; i++) {
			mapTextures[i] = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(levelSelectTextureAtlas, activity,
							MAP_RESOURCES[i] + ".png");
		}

		difficultEasyTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(levelSelectTextureAtlas, activity, "easy.png");
		difficultNormalTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(levelSelectTextureAtlas, activity,
						"normal.png");
		difficultHardTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(levelSelectTextureAtlas, activity, "hard.png");
		backButtonTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(levelSelectTextureAtlas, activity, "back.png");

		backgroundTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(levelSelectTextureAtlas, activity,
						"background_resized.png");

		prevButtonTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(levelSelectTextureAtlas, activity,
						"previous.png");

		nextButtonTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(levelSelectTextureAtlas, activity, "next.png");

		try {
			levelSelectTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			levelSelectTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void unloadLevelSelectResources() {
		levelSelectTextureAtlas.unload();
		for (int i = 0; i < mapTextures.length; i++) {
			mapTextures[i] = null;
		}
		mapTextures = null;
		difficultEasyTexture = null;
		difficultNormalTexture = null;
		difficultHardTexture = null;
		levelSelectTextureAtlas = null;
	}

	public void loadFonts() {
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font10 = FontFactory.createStrokeFromAsset(activity.getFontManager(),
				mainFontTexture, activity.getAssets(), "font.ttf", 12f, true,
				Color.RED, 0.5f, Color.BLACK);
		font20 = FontFactory.createStrokeFromAsset(activity.getFontManager(),
				mainFontTexture, activity.getAssets(), "font.ttf", 26f, true,
				Color.RED, 0.5f, Color.BLACK);
		font40 = FontFactory.createStrokeFromAsset(activity.getFontManager(),
				mainFontTexture, activity.getAssets(), "font.ttf", 40f, true,
				Color.RED, 0.5f, Color.BLACK);

		font10.load();
		font20.load();
		font40.load();
	}

	public void unloadFonts() {
		font10.unload();
		font20.unload();
		font40.unload();

		font10 = null;
		font20 = null;
		font40 = null;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
