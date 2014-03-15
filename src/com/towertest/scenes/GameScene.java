package com.towertest.scenes;

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
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import android.util.Log;

import com.towertest.BuildTowerTouchHandler;
import com.towertest.Utils;
import com.towertest.builders.EnemyBuilder;
import com.towertest.builders.TowerBuilder;
import com.towertest.hud.GameOverWindow;
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

	private TMXLayer tmxLayer;

	private Waypoint lStarts[];
	private Waypoint lEnds[];
	private Wave[] waves;

	private Level currentLevel;

	private Text creditText;
	private Text livesText;
	private Text waveText;
	private Text scoreText;
	private Text enemyCountText;

	private long credits;
	private long lives;
	private long scores;

	private final long initialCredits = 3000;
	private final long initialLives = 30;

	private ButtonSprite pauseButton;

	private ArrayList<Tower> arrayTower;
	private ArrayList<Tower> prototypeTowers;

	private ArrayList<Enemy> arrayEn;
	private Enemy[] enemyClone;

	private TMXTiledMap tmxTiledMap;

	private IUpdateHandler loop;

	private GameMap map;

	private GameOverWindow gameOverWindow;

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

	public ArrayList<Tower> getArrayTower() {
		return arrayTower;
	}

	public void setArrayTower(ArrayList<Tower> arrayTower) {
		this.arrayTower = arrayTower;
	}

	public long getScores() {
		return scores;
	}

	public void addScores(long scores) {
		this.scores += scores;
		scoreText.setText("Score: " + this.scores);
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
		// tmxTileProperty =
		// this.mTMXTiledMap.getTMXTilePropertiesByGlobalTileID(0));
		attachChild(tmxLayer);

		// Pause button
		pauseButton = new ButtonSprite(0, camera.getHeight() - 40,
				ResourceManager.getInstance().texPause,
				ResourceManager.getInstance().texPlay, vbom,
				new OnClickListener() {
					@Override
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						togglePauseGame();
					}
				});

		pauseButton.setWidth(40f);
		pauseButton.setHeight(40f);

		hud.attachChild(pauseButton);
		hud.registerTouchArea(pauseButton);

		loop = new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				for (Enemy enemy : arrayEn) {
					for (Tower tower : arrayTower) {// iterate
						// TODO, add physics for collision
						if (tower.distanceTo(enemy) < tower.maxRange()) {
							tower.fire(enemy, GameScene.this, arrayEn, activity);
						}
					}
				}
				if (currentLevel.getCurrentEnemyCount() == currentLevel.getTotalEnemies() && arrayEn.size() == 0) {
					gameOverWindow.showResult(GameScene.this, camera, true);
					GameScene.this.unregisterUpdateHandler(this);
				}
			}

			@Override
			public void reset() {
			}
		};
		registerUpdateHandler(loop);

		setTouchAreaBindingOnActionDownEnabled(true);
		setOnSceneTouchListener(this);

		hud.setTouchAreaBindingOnActionDownEnabled(true);

		creditText = new Text(20, 0, ResourceManager.getInstance().font20,
				"$ 0123456789", "$ 0123456789".length(), vbom);

		livesText = new Text(200, 0, ResourceManager.getInstance().font20,
				"Lives 0123456789", "Lives 0123456789".length(), vbom);

		waveText = new Text(300, 0, ResourceManager.getInstance().font20,
				"Wave 1234567890", "Wave 1234567890".length(), vbom);

		scoreText = new Text(650, 0, ResourceManager.getInstance().font20,
				"Scores: 0123456789", "Score: 0123456789".length(), vbom);

		enemyCountText = new Text(300, 25,
				ResourceManager.getInstance().font20, "Enemy: 0123456789",
				"Enemy: 0123456789".length(), vbom);

		enemyCountText.setText("Enemy: 0/" + waves[0].getTotal());

		waveText.setText("Wave 1/" + waves.length);

		scores = 0;
		addScores(0);

		credits = initialCredits;
		addCredits(0); // initialize the value

		lives = initialLives;
		subtractLives(0); // initialize the value

		Rectangle infoArea = new Rectangle(0, 0, camera.getWidth(), 40, vbom);
		infoArea.setColor(Color.BLUE);
		infoArea.attachChild(creditText);
		infoArea.attachChild(livesText);
		infoArea.attachChild(waveText);
		infoArea.attachChild(scoreText);
		infoArea.attachChild(enemyCountText);

		hud.attachChild(infoArea);

		// A tower button to build other towers xcoord,ycoord,xsize,ysize
		prototypeTowers = new ArrayList<Tower>();

		TowerBuilder towerBuilder = new TowerBuilder(this);

		Rectangle prototypeTowerArea = new Rectangle(camera.getWidth() - 56, 0,
				56, camera.getHeight(), vbom);
		prototypeTowerArea.setColor(new Color(0.8f, 0.8f, 0.8f, 0.5f));

		prototypeTowers.add(towerBuilder.setX(0).setY(56).setRange(3).build());
		prototypeTowers.add(towerBuilder.setX(0).setY(112).setRange(5).build());

		for (Tower tower : prototypeTowers) {
			prototypeTowerArea.attachChild(tower);
			hud.registerTouchArea(tower);
		}

		hud.attachChild(prototypeTowerArea);

		// allows you to drag it
		final BuildTowerTouchHandler btth = new BuildTowerTouchHandler(
				prototypeTowers, this, arrayTower);

		hud.setOnAreaTouchListener(btth);

		gameOverWindow = new GameOverWindow(vbom);

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
		unregisterUpdateHandler(loop);
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
							waveText.setText("Wave "
									+ currentLevel.getCurrentWaveNumber() + "/"
									+ currentLevel.getWaves().length);
							enemyCountText.setText("Enemy "
									+ wave.getCurrentEnemyCount() + "/"
									+ wave.getTotal());
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
			gameOverWindow.showResult(this, camera, false);
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
