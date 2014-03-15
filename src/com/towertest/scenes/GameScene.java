package com.towertest.scenes;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import android.util.Log;

import com.towertest.BuildTowerTouchHandler;
import com.towertest.Utils;
import com.towertest.builders.EnemyBuilder;
import com.towertest.builders.TowerBuilder;
import com.towertest.hud.ProgressBar;
import com.towertest.logic.GameMap;
import com.towertest.logic.Level;
import com.towertest.logic.Wave;
import com.towertest.logic.Waypoint;
import com.towertest.managers.ResourceManager;
import com.towertest.managers.SceneManager;
import com.towertest.managers.SceneManager.SceneType;
import com.towertest.sprites.Enemy;
import com.towertest.sprites.Tower;

public class GameScene extends BaseScene implements IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================
	public static final int TILE_WIDTH = ResourceManager.getInstance().tmxTiledMap
			.getTileWidth();
	public static final int TILE_HEIGHT = ResourceManager.getInstance().tmxTiledMap
			.getTileHeight();

	public static final String TAG_TILED_PROPERTY_NAME_BUILDABLE = "Buildable";
	public static final String TAG_TILED_PROPERTY_NAME_PATH = "Path";

	public static final String TAG_TILED_PROPERTY_VALUE_TRUE = "True";
	public static final String TAG_TILED_PROPERTY_VALUE_FALSE = "False";

	// ===========================================================
	// Fields
	// ===========================================================
	private HUD hud;
	private Text fpsText;

	private ProgressBar waveProgress;
	private TMXLayer tmxLayer;

	private Waypoint lStarts[];
	private Waypoint lEnds[];
	private Wave[] waves;

	private Level currentLevel;
	private int rowMax;
	private int colMax;

	private FPSCounter fpsCounter;

	private Text creditText;
	private Text livesText;
	private long credits;
	private long lives;
	private final long initialCredits = 3000;
	private final long initialLives = 30;

	private ButtonSprite pauseButton;

	private ArrayList<Tower> arrayTower;
	private ArrayList<Enemy> arrayEn;

	private ArrayList<Tower> prototypeTowers;
	private Enemy[] enemyClone;

	private TMXTiledMap tmxTiledMap;

	private boolean allowDiagonal;

	private IUpdateHandler hudLoop;

	private IUpdateHandler loop;

	private GameMap map;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public TMXLayer getTmxLayer() {
		return tmxLayer;
	}

	public void setTmxLayer(TMXLayer tmxLayer) {
		this.tmxLayer = tmxLayer;
	}

	public TMXTiledMap getTmxTiledMap() {
		return tmxTiledMap;
	}

	public void setTmxTiledMap(TMXTiledMap mTMXTiledMap) {
		this.tmxTiledMap = mTMXTiledMap;
	}

	public Enemy[] getEnemyClone() {
		return enemyClone;
	}

	public void setEnemyClone(Enemy[] enemyClone) {
		this.enemyClone = enemyClone;
	}

	public int getColMax() {
		return colMax;
	}

	public void setColMax(int pColMax) {
		this.colMax = pColMax;
	}

	public int getRowMax() {
		return rowMax;
	}

	public void setRowMax(int pRowMax) {
		this.rowMax = pRowMax;
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Level currentLevel) {
		this.currentLevel = currentLevel;
	}

	public long getCredits() {
		return credits;
	}

	public void setCredits(long credits) {
		this.credits = credits;
	}

	public GameMap getMap() {
		return map;
	}

	public void setMap(GameMap map) {
		this.map = map;
	}

	public ArrayList<Enemy> getArrayEn() {
		return arrayEn;
	}

	public void setArrayEn(ArrayList<Enemy> arrayEn) {
		this.arrayEn = arrayEn;
	}

	public boolean isAllowDiagonal() {
		return allowDiagonal;
	}

	public void setAllowDiagonal(boolean allowDiagonal) {
		this.allowDiagonal = allowDiagonal;
	}

	public ArrayList<Tower> getArrayTower() {
		return arrayTower;
	}

	public void setArrayTower(ArrayList<Tower> arrayTower) {
		this.arrayTower = arrayTower;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void createScene() {
		setBackground(new Background(Color.BLUE));

		tmxTiledMap = ResourceManager.getInstance().tmxTiledMap;
		tmxLayer = tmxTiledMap.getTMXLayers().get(0);

		hud = new HUD();

		lStarts = new Waypoint[] { new Waypoint(0, 0) };
		lEnds = new Waypoint[] { new Waypoint(tmxTiledMap.getTileColumns(), 0) };

		map = new GameMap(lStarts, lEnds);

		enemyClone = new Enemy[lStarts.length];
		arrayEn = new ArrayList<Enemy>();
		arrayTower = new ArrayList<Tower>();

		EnemyBuilder enBuilder = new EnemyBuilder(this);

		for (int i = 0; i < enemyClone.length; i++) {
			enemyClone[i] = enBuilder
					.setX(Utils.getXFromCol(map.getStartLoc()[i].x))
					.setY(Utils.getXFromCol(map.getStartLoc()[i].y))
					.setHealth(1000)
					.setTexture(ResourceManager.getInstance().enemyTexture)
					.setSpeed(60f).build();

			enemyClone[i]
					.createPath(lEnds[0], activity, tmxLayer, getArrayEn());
		}

		waves = new Wave[] {
				new Wave(new int[] { 2 }, new Enemy[] { enemyClone[0] }, 2f),
				new Wave(new int[] { 3 }, new Enemy[] { enemyClone[0] }, 2f) };
		currentLevel = new Level(waves, map);

		camera.setHUD(hud);

		tmxLayer = tmxTiledMap.getTMXLayers().get(0);
		setColMax(tmxLayer.getTileColumns() - 1 + 1);
		setRowMax(tmxLayer.getTileRows() - 1 + 1);
		// tmxTileProperty =
		// this.mTMXTiledMap.getTMXTilePropertiesByGlobalTileID(0));
		attachChild(tmxLayer);

		fpsCounter = new FPSCounter();
		engine.registerUpdateHandler(fpsCounter);

		// Pause button
		pauseButton = new ButtonSprite(camera.getWidth() - 180, 20,
				ResourceManager.getInstance().texPause,
				ResourceManager.getInstance().texPlay, vbom,
				new OnClickListener() {
					@Override
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						togglePauseGame();
					}
				});

		hud.attachChild(pauseButton);
		hud.registerTouchArea(pauseButton);

		hudLoop = new IUpdateHandler() {
			@Override
			public void reset() {
			}

			@Override
			public void onUpdate(float pSecondsElapsed) {
				// =================HUD LOOP=======================
				fpsText.setText("FPS: "
						+ new DecimalFormat("#.##").format(fpsCounter.getFPS()));
				// code ends
			}
		};
		registerUpdateHandler(hudLoop);

		loop = new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				collision();
			}

			@Override
			public void reset() {
			}
		};
		registerUpdateHandler(loop);

		setTouchAreaBindingOnActionDownEnabled(true);
		setOnSceneTouchListener(this);

		hud.setTouchAreaBindingOnActionDownEnabled(true);

		// number of enemies remaining
		waveProgress = new ProgressBar(20, 70, 100, 10,
				currentLevel.getWaves().length, 0, vbom);

		waveProgress.setProgressColor(1.0f, 0.0f, 0.0f, 1.0f)
				.setFrameColor(0.4f, 0.4f, 0.4f, 1.0f)
				.setBackColor(0.0f, 0.0f, 0.0f, 0.2f);

		waveProgress.setProgress(0);

		hud.attachChild(waveProgress);

		fpsText = new Text(camera.getWidth() - 100, 20,
				ResourceManager.getInstance().font20, "FPS:",
				"FPS: xxx.xx".length(), ResourceManager.getInstance().vbom);
		creditText = new Text(20, 20, ResourceManager.getInstance().font40,
				"$", 12, ResourceManager.getInstance().vbom);

		livesText = new Text(20, 40 + creditText.getHeight()
				+ waveProgress.getHeight(),
				ResourceManager.getInstance().font40, "", 12,
				ResourceManager.getInstance().vbom);

		credits = initialCredits;
		addCredits(0); // initialize the value

		lives = initialLives;
		subtractLives(0); // initialize the value

		Rectangle infoArea = new Rectangle(0, 0, camera.getWidth(), 100, vbom);
		infoArea.setColor(Color.BLUE);

		// A tower button to build other towers xcoord,ycoord,xsize,ysize
		prototypeTowers = new ArrayList<Tower>();

		TowerBuilder towerBuilder = new TowerBuilder(this);

		Rectangle prototypeTowerArea = new Rectangle(camera.getWidth() - 56, 0,
				56, camera.getHeight(), vbom);
		prototypeTowerArea.setColor(Color.BLUE);

		prototypeTowers.add(towerBuilder.setX(0).setY(56).setRange(3).build());
		prototypeTowers.add(towerBuilder.setX(0).setY(112).setRange(5).build());

		for (Tower tower : prototypeTowers) {
			prototypeTowerArea.attachChild(tower);
			hud.registerTouchArea(tower);
		}

		hud.attachChild(prototypeTowerArea);

		// allows you to drag it
		final BuildTowerTouchHandler btth = new BuildTowerTouchHandler(
				prototypeTowers, this, getCredits(), getArrayTower(),
				ResourceManager.getInstance().hitAreaGoodTexture,
				ResourceManager.getInstance().hitAreaBadTexture,
				ResourceManager.getInstance().bulletTexture,
				ResourceManager.getInstance().towerTexture, currentLevel,
				getArrayEn(), ResourceManager.getInstance().activity,
				ResourceManager.getInstance().vbom);

		hud.setOnAreaTouchListener(btth);

		Log.d("GameScene", "Starting waves");
		startWaves();
	}

	@Override
	public void onBackPressed() {
		SceneManager.getInstance().loadMainMenuScene();
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.GAME_SCENE;
	}

	@Override
	public void disposeScene() {
		hud.detachSelf();
		hud = null;
		camera.setHUD(null);
		engine.unregisterUpdateHandler(enemyHandler);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void togglePauseGame() {
		setPaused(!isPaused);
		pauseButton.setCurrentTileIndex(isPaused ? 1 : 0);
		setPaused(isPaused);
	}

	public void addCredits(long enCredits) {
		setCredits(getCredits() + enCredits);
		creditText.setText("$" + getCredits());
		// update screen to reflect new score
	}

	public void subtractLives(long pLives) {
		lives -= pLives;
		if (lives < 1) {
			// they is dead bitches!
			loseGame();
			lives = 0;
		}
		livesText.setText(lives + " lives");
	}

	private void collision() {
		// Lets Loop our array of enemies
		// ***************************************************************
		// TODO WE SHOULD PROBABLY MULTITHREAD THIS LOOP FO' SHIZZLE!!!!!!
		// ***************************************************************
		if (getArrayEn().size() > 0) {
			for (int j = 0; j < getArrayEn().size(); j++) {// iterate through
															// the
															// enemies
				final Enemy enemy = getArrayEn().get(j);
				// Lets Loop our Towers
				for (int k = 0; k < getArrayTower().size(); k++) {// iterate
																	// through
																	// the
																	// towers
					final Tower tower = getArrayTower().get(k);
					// check if they are in range of the tower
					// TODO, add physics for collision
					if (tower.distanceTo(enemy) < tower.maxRange()) {
						tower.fire(enemy, this, getArrayEn(), activity);
					}
				}
			}
		}
	}

	// break this all out to a wave class, also use SpriteBatch
	TimerHandler enemyHandler;

	public void startWaves() {
		enemyHandler = new TimerHandler(
				currentLevel.getWaves()[currentLevel.getCurrentWaveNumber()]
						.getDelay(),
				true, new ITimerCallback() {
					Wave wave = currentLevel.getNextWave();

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						if (!isPaused) {
							Log.i("Wave Progress", "Wave number: "
									+ currentLevel.getCurrentWaveNumber()
									+ " Number of enemy: " + wave.getTotal());
							Enemy en = wave.spawn();
							if (en != null) {
								attachChild(en);
								en.startMoving();
								getArrayEn().add(en);
								currentLevel.increaseCurrentEnemyCount();
							} else {
								if (currentLevel.hasNextWave()) {
									wave = currentLevel.getNextWave();
								} else {
									engine.unregisterUpdateHandler(enemyHandler);
								}
							}
							waveProgress.setProgress(currentLevel
									.getCurrentWaveNumber());
						}
					}
				});
		engine.registerUpdateHandler(enemyHandler);
	}

	public float sceneTransX(float x) {
		// final float myZoom = camera.getZoomFactor();
		// final float myXOffset = camera.getCenterX() - TowerTest.CAMERA_WIDTH
		// / 2 / myZoom;
		// final float newX = x / myZoom + myXOffset;
		// return newX;
		return x + camera.getCenterX() - camera.getWidth() / 2;
	}

	/**
	 * Translates y coordinate from hud coordinates to scene coordinates (used
	 * for tower placement)
	 * 
	 * @param y
	 *            Y coordinate to be translated
	 * @return translated Y coordinate
	 */
	public float sceneTransY(float y) {
		// final float myZoom = camera.getZoomFactor();
		// final float myYOffset = camera.getCenterY() - TowerTest.CAMERA_HEIGHT
		// / 2 / myZoom;
		// final float newY = y / myZoom + myYOffset;
		// return newY;
		return y + camera.getCenterY() - camera.getHeight() / 2;
	}

	public float getPanX() {
		return camera.getCenterX();
	}

	public float getPanY() {
		return camera.getCenterY();
	}

	private void loseGame() {
		if (!isPaused) {
			togglePauseGame();
			// Toast.makeText(getBaseContext(), "LOSER!", Toast.LENGTH_LONG);
			Log.e("LOSER!", "Wrong Wrong Wrong, fingerpistols, you LOSE!");
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (BuildTowerTouchHandler.tw != null) {
			BuildTowerTouchHandler.tw.detachSelf();// this ensures that bad
			// towers get removed
			BuildTowerTouchHandler.tw = null;
		}
		return true;
	}
}
