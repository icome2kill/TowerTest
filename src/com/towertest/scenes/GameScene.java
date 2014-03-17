package com.towertest.scenes;

import java.util.ArrayList;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
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
import com.towertest.hud.GamePausedWindow;
import com.towertest.hud.TowerDetailsWindow;
import com.towertest.logic.GameMap;
import com.towertest.logic.Level;
import com.towertest.logic.Wave;
import com.towertest.logic.Waypoint;
import com.towertest.managers.GamePreferencesManager;
import com.towertest.managers.ResourceManager;
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
	// private Text enemyCountText;

	private long credits;
	private long lives;
	private long scores;

	private int currentMap;

	private final long initialCredits = 3000;
	private final long initialLives = 30;

	private ButtonSprite pauseButton;

	private ArrayList<Tower> arrayTower;
	private ArrayList<Tower> prototypeTowers;

	private ArrayList<Enemy> arrayEn;
	private Enemy[] enemyPrototype;

	private TMXTiledMap tmxTiledMap;

	private IUpdateHandler loop;
	private TimerHandler enemyHandler;

	private GameMap map;

	private GameOverWindow gameOverWindow;
	private TowerDetailsWindow towerDetailsWindow;
	private GamePausedWindow gamePausedWindow;

	private BuildTowerTouchHandler btth;

	private Rectangle infoArea;
	private Rectangle prototypeTowerArea;

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
		return enemyPrototype;
	}

	public void setEnemyClone(Enemy[] enemyClone) {
		this.enemyPrototype = enemyClone;
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

		loadMap(GamePreferencesManager.getInstance().selectedMap);
		loadLevel(GamePreferencesManager.getInstance().selectedDifficult);

		hud = new HUD();
		camera.setHUD(hud);

		// Pause button
		pauseButton = new ButtonSprite(8, camera.getHeight() - TILE_HEIGHT + 8,
				resourceManager.texPause, resourceManager.texPlay, vbom,
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
						if (tower.distanceTo(enemy) < tower.maxRange()) {
							tower.fire(enemy, GameScene.this, arrayEn, activity);
						}
					}
				}
				if (currentLevel.getCurrentEnemyCount() == currentLevel
						.getTotalEnemies() && arrayEn.size() == 0) {
					gameOverWindow.showResult(GameScene.this, camera, true);
					GameScene.this.unregisterUpdateHandler(this);
				}
			}

			@Override
			public void reset() {
			}
		};
		registerUpdateHandler(loop);
		setOnSceneTouchListener(this);
		hud.setTouchAreaBindingOnActionDownEnabled(true);

		creditText = new Text(20, 10, resourceManager.font20, "$ 0123456789",
				"$ 0123456789".length(), vbom);

		livesText = new Text(camera.getWidth() - 150, 10,
				resourceManager.font20, "Lives: 0123456789",
				"Lives 0123456789".length(), vbom);

		waveText = new Text(200, 10, resourceManager.font20, "Wave 1234567890",
				"Wave 1234567890".length(), vbom);

		scoreText = new Text(400, 10, resourceManager.font20,
				"Scores: 0123456789", "Score: 0123456789".length(), vbom);

		// enemyCountText = new Text(300, 10, resourceManager.font20,
		// "Enemy: 0123456789", "Enemy: 0123456789".length(), vbom);
		//
		// enemyCountText.setText("Enemy: 0/" + waves[0].getTotal());

		waveText.setText("Wave 1/" + waves.length);

		scores = 0;
		addScores(0);

		credits = initialCredits;
		addCredits(0); // initialize the value

		lives = initialLives;
		subtractLives(0); // initialize the value

		infoArea = new Rectangle(0, 0, camera.getWidth(), 40, vbom);
		infoArea.setColor(Color.TRANSPARENT);
		infoArea.attachChild(creditText);
		infoArea.attachChild(livesText);
		infoArea.attachChild(waveText);
		infoArea.attachChild(scoreText);
		// infoArea.attachChild(enemyCountText);

		hud.attachChild(infoArea);

		// A tower button to build other towers xcoord,ycoord,xsize,ysize
		prototypeTowers = new ArrayList<Tower>();

		TowerBuilder towerBuilder = new TowerBuilder(this);

		prototypeTowerArea = new Rectangle(camera.getWidth() - TILE_WIDTH,
				TILE_WIDTH, TILE_WIDTH, camera.getHeight() - TILE_WIDTH * 2,
				vbom);
		prototypeTowerArea.setColor(new Color(0.8f, 0.8f, 0.8f, 0.5f));

		prototypeTowers.add(towerBuilder.setX(0).setY(0).setRange(3)
				.setTowerTexture(resourceManager.towerTexture[0]).build());
		prototypeTowers.add(towerBuilder.setX(0).setY(TILE_HEIGHT * 1)
				.setRange(4).setTowerTexture(resourceManager.towerTexture[1])
				.build());
		prototypeTowers.add(towerBuilder.setX(0).setY(TILE_HEIGHT * 2)
				.setRange(1).setTowerTexture(resourceManager.towerTexture[2])
				.build());
//		prototypeTowers.add(towerBuilder.setX(0).setY(TILE_HEIGHT * 3)
//				.setRange(2).setTowerTexture(resourceManager.towerTexture[3])
//				.build());

		for (final Tower tower : prototypeTowers) {
			prototypeTowerArea.attachChild(tower);
			tower.setOnAreaTouchedListener(new IOnAreaTouchListener() {

				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						ITouchArea pTouchArea, float pTouchAreaLocalX,
						float pTouchAreaLocalY) {
					showTowerDetails(tower, false, false);
					return false;
				}
			});
			Text priceText = new Text(TILE_WIDTH / 2, TILE_HEIGHT / 2 + 10,
					ResourceManager.getInstance().font20, "0123456789",
					"0123456789".length(), vbom);
			priceText.setText(Long.toString(tower.getCredits()));
			tower.attachChild(priceText);
			hud.registerTouchArea(tower);
		}
		hud.attachChild(prototypeTowerArea);

		// allows you to drag it
		btth = new BuildTowerTouchHandler(prototypeTowers, this, arrayTower);
		hud.setOnAreaTouchListener(btth);

		gameOverWindow = new GameOverWindow(vbom);
		towerDetailsWindow = new TowerDetailsWindow(vbom);
		gamePausedWindow = new GamePausedWindow(vbom);

		startWaves();
	}

	private void loadLevel(int difficult) {
		lStarts = new Waypoint[] { new Waypoint(0, 1) };
		lEnds = new Waypoint[] { new Waypoint(tmxTiledMap.getTileColumns() - 1,
				6) };

		map = new GameMap(lStarts, lEnds);

		enemyPrototype = new Enemy[ResourceManager.ENEMY_RESOURCES.length];
		arrayEn = new ArrayList<Enemy>();
		arrayTower = new ArrayList<Tower>();

		EnemyBuilder enBuilder = new EnemyBuilder(this);

		for (int i = 0; i < enemyPrototype.length; i++) {
			enemyPrototype[i] = enBuilder
					.setX(Utils.getXFromCol(map.getStartLoc()[0].x))
					.setY(Utils.getXFromCol(map.getStartLoc()[0].y))
					.setHealth(500 * (i + 1))
					.setTexture(resourceManager.enemyTexture[i]).setSpeed(60f)
					.build();

			enemyPrototype[i].createPath(lEnds[0], activity, tmxLayer, arrayEn);
		}

//		waves = new Wave[] {
//				new Wave(new int[] { 2 }, new Enemy[] { enemyPrototype[0] }, 2f),
//				new Wave(new int[] { 2 }, new Enemy[] { enemyPrototype[1] }, 2f),
//				new Wave(new int[] { 2 }, new Enemy[] { enemyPrototype[2] }, 2f),
//				new Wave(new int[] { 2 }, new Enemy[] { enemyPrototype[3] }, 2f) };
		
		waves = new Wave[] { new Wave(new int [] { 1 }, new Enemy[] { enemyPrototype[0] }, 1f) };

		currentLevel = new Level(waves, map);
	}

	private void loadMap(int number) {
		currentMap = number;
		resourceManager.tmxTiledMap = resourceManager.tmxTiledMapArray[number];
		tmxTiledMap = resourceManager.tmxTiledMap;
		tmxLayer = tmxTiledMap.getTMXLayers().get(0);
		attachChild(tmxLayer);
	}

	@Override
	public void onBackPressed() {

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
		engine.clearUpdateHandlers();
		clearChildScene();
		clearEntityModifiers();
		clearTouchAreas();
		clearUpdateHandlers();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void togglePauseGame() {
		isPaused = !isPaused;
		pauseButton.setCurrentTileIndex(isPaused ? 1 : 0);
		if (isPaused) {
			// setOnSceneTouchListener(null);
			gamePausedWindow.show(this, camera);
		} else {
			// setOnSceneTouchListener(this);
			// registerTouchArea(prototypeTowerArea);
			// setOnAreaTouchListener(btth);
			gamePausedWindow.detachSelf();
		}
		hud.registerTouchArea(pauseButton);
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
							// enemyCountText.setText("Enemy "
							// + wave.getCurrentEnemyCount() + "/"
							// + wave.getTotal());
						}
					}
				});
		engine.registerUpdateHandler(enemyHandler);
	}

	public float sceneTransX(float x) {
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
		return y + camera.getCenterY() - camera.getHeight() / 2;
	}

	public void showTowerDetails(Tower tower, boolean showRange,
			boolean showRemove) {
		towerDetailsWindow.detachSelf();
		towerDetailsWindow.show(this, camera, tower, showRange, showRemove);
	}

	public float getPanX() {
		return camera.getCenterX();
	}

	public float getPanY() {
		return camera.getCenterY();
	}

	private void loseGame() {
		if (!isPaused) {
			// Toast.makeText(getBaseContext(), "LOSER!", Toast.LENGTH_LONG);
			gameOverWindow.showResult(this, camera, false);
			engine.unregisterUpdateHandler(loop);
			Log.e("LOSER!", "Wrong Wrong Wrong, fingerpistols, you LOSE!");
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		float x = pSceneTouchEvent.getX();
		float y = pSceneTouchEvent.getY();
		if (BuildTowerTouchHandler.tw != null) {
			BuildTowerTouchHandler.tw.detachSelf();// this ensures that bad
			BuildTowerTouchHandler.tw = null;
		}
		if (towerDetailsWindow != null && !towerDetailsWindow.contains(x, y)) {
			towerDetailsWindow.detachSelf();
		}
		return true;
	}

	public int getCurrentMap() {
		return currentMap;
	}
}
