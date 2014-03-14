package com.towertest.managers;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Typeface;
import android.util.Log;

import com.towertest.GameActivity;
import com.towertest.sprites.Enemy;
import com.towertest.sprites.Tower;

public class ResourceManager {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final ResourceManager INSTANCE = new ResourceManager();
	// ===========================================================
	// Fields
	// ===========================================================
	public Camera camera;
	public VertexBufferObjectManager vbom;
	public Engine engine;
	public GameActivity activity;

	private BuildableBitmapTextureAtlas gameTextureAtlas;
	public TextureRegion towerTexture;
	public TiledTextureRegion enemyTexture;
	public TextureRegion bulletTexture;
	public TextureRegion hitAreaGoodTexture;
	public TextureRegion hitAreaBadTexture;
	public TextureRegion texPause;
	public TextureRegion texPlay;
	public TMXTiledMap tmxTiledMap;
	public Sound fireSound;

	private BuildableBitmapTextureAtlas mainMenuTextureAtlas;
	public TextureRegion btnOptionsTexture;
	public TextureRegion btnAboutTexture;
	public TextureRegion btnPlayTexture;

	public Font font10;
	public Font font20;
	public Font font40;

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
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024);

		towerTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity, Tower.texture);
		bulletTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity, "towerRangeGood.png");
		hitAreaGoodTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity,
						"towerRangeGood.png");
		hitAreaBadTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity,
						"towerRangeBad.png");
		enemyTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				gameTextureAtlas, activity, Enemy.texture, 3, 1);

		texPause = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity, "pause.png");
		texPlay = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity, "play.png");

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
						}
					});
			// Load the Desert Map
			Log.i("Location:", "TMXMap Loading...");
			tmxTiledMap = tmxLoader.loadFromAsset("tmx/grid.tmx");
			Log.i("Location:", "TMXMap Loaded");
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}
	}

	public void unloadGameResource() {
		gameTextureAtlas.unload();
	}

	public void loadMainMenuResources() {
		loadMainMenuGraphics();
		loadMainMenuAudio();
		loadFonts();
	}

	private void loadMainMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mainMenuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024);
		
		btnPlayTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTextureAtlas, activity, "play.png");
		btnOptionsTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTextureAtlas, activity, "pause.png");
		
		try {
			mainMenuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
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
	}
	
	public void loadMapSelectResources() {
		
	}
	
	public void loadLevelSelectResources() {
		
	}

	public void loadFonts() {
		font10 = FontFactory.create(activity.getFontManager(),
				activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 10);
		font20 = FontFactory.create(activity.getFontManager(),
				activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 20);
		font40 = FontFactory.create(activity.getFontManager(),
				activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 40);

		font10.load();
		font20.load();
		font40.load();
	}

	public void unloadFonts() {
		font10.unload();
		font20.unload();
		font40.unload();
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
