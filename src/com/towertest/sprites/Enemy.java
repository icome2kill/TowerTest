package com.towertest.sprites;

import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;

import android.util.Log;

import com.towertest.Utils;
import com.towertest.hud.ProgressBar;
import com.towertest.logic.GameMap;
import com.towertest.logic.Path;
import com.towertest.logic.Waypoint;
import com.towertest.managers.ResourceManager;
import com.towertest.managers.SceneManager;
import com.towertest.managers.SceneManager.SceneType;
import com.towertest.scenes.BaseScene;
import com.towertest.scenes.GameScene;

public class Enemy extends AnimatedSprite {
	public final static String TEXTURE = "knight.png";
	private static final double TOLERANCE = 0.000001;
	
	public float speed; // movement speed (distance to move per ?)
	public Path path;
	/**
	 * used to verify that the target hasn't died yet, makes sure that they
	 * don't get duplicate kill credit for more than one bullet hitting target
	 * and striking killing blow
	 */
	public boolean isAlive = true;
	// static (only set once) area
	/**
	 * Keeps track of damage that is coming at the target, so the towers don't
	 * overkill like crazy
	 */
	public int inboundDamage = 0;

	private int health;
	private int credits;
	private int score;
	private ProgressBar healthBar;
	private PathModifier trajectory;
	private static GameMap map;

	/**
	 * Create new enemy
	 * 
	 * @param pX
	 *            X Coordinate
	 * @param pY
	 *            Y Coordinate
	 * @param pWidth
	 * @param pHeight
	 * @param iTextureRegion
	 * @param tvbom
	 * @param map
	 * @see GameMap
	 */
	public Enemy(float pX, float pY, float pWidth, float pHeight, int health,
			ITiledTextureRegion iTextureRegion,
			VertexBufferObjectManager tvbom, GameMap map) {
		super(pX, pY, pWidth, pHeight, iTextureRegion, tvbom);
		this.health = health;
		healthBar = new ProgressBar(0, 0, GameScene.TILE_WIDTH, 10, health,
				health, tvbom);
		healthBar.setProgressColor(1.0f, 0.0f, 0.0f, 1.0f)
				.setFrameColor(0.4f, 0.4f, 0.4f, 1.0f)
				.setBackColor(0.0f, 0.0f, 0.0f, 0.2f);
		Enemy.map = map;
	}

	/**
	 * Create a new enemy with a set Path list of waypoints
	 * 
	 * @param p
	 *            Path of waypoints
	 * @param b
	 * @param pX
	 *            Xcoord location
	 * @param pY
	 *            Ycoord location
	 * @param iTextureRegion
	 * @param tvbom
	 */
	public Enemy(float pX, float pY, float pWidth, float pHeight,
			ITiledTextureRegion iTextureRegion, VertexBufferObjectManager tvbom) {
		// used by the clone function
		super(pX, pY, pWidth, pHeight, iTextureRegion, tvbom);
	}

	public void createPath(Waypoint pEnd, BaseGameActivity myContext,
			TMXLayer pTmxlayer, ArrayList<Enemy> arrayEn) {
		Log.d("Enemy", "Creating path");
		path = new Path(this, pEnd, pTmxlayer, map);
		if (path == null) {
			Log.d("Enemy", "Path is null");
		}
	}

	public void stop() {
		unregisterEntityModifier(trajectory);
	}

	/**
	 * Deal damage to the enemy, modified by type <br>
	 * 
	 * @param amount
	 *            amount of damage to subtract from helth
	 * @param type
	 *            used to modify the amount of damage based on armor.
	 * @return Less than 1 if enemy died
	 */
	public int takeDamage(int amount, String type) {
		health -= amount;
		// update health bar
		this.healthBar.setProgress(health);
		return health;
	}

	/**
	 * Get enemy current health
	 * 
	 * @return Health of enemy
	 */
	public int getHealth() {
		return health;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	/**
	 * Get enemy Credit value if killed
	 * 
	 * @return Credit value of enemy
	 */
	public int getCredits() {
		return credits;
	}

	public float getMidX() {
		return getX() + getWidth() / 2;
	}

	public float getMidY() {
		return getY() + getHeight() / 2;
	}

	public void startMoving() {
		if (path != null) {
			// convert our type of path we have to their type of path
			final org.andengine.entity.modifier.PathModifier.Path tempPath = path
					.getEntityPath();
			if (tempPath != null) {
				// now find the total length of the path
				final float dist = tempPath.getLength();

				// D=r*t
				// therefore t = D/r
				trajectory = new PathModifier(dist / speed, tempPath);
				trajectory
						.addModifierListener(new IModifierListener<IEntity>() {
							@Override
							public void onModifierStarted(
									IModifier<IEntity> pModifier, IEntity pItem) {
								// Do stuff here if you want to
								long ANIMATE[] = { 300, 300, 300, 300, 300, 300 };
								Enemy.this.animate(ANIMATE);
							}

							@Override
							public void onModifierFinished(
									IModifier<IEntity> pModifier, IEntity pItem) {
								ResourceManager.getInstance().engine
										.runOnUpdateThread(new Runnable() {
											@Override
											public void run() {
												// scene.detachChild(Enemy.this);
												Enemy.this.detachSelf();
												BaseScene scene = SceneManager
														.getInstance()
														.getCurrentScene();
												if (scene.getSceneType() == SceneType.GAME_SCENE) {
													GameScene gameScene = (GameScene) scene;
													gameScene.getArrayEn()
															.remove(Enemy.this);
													// subtract a life
													gameScene.subtractLives(1);
												}
											}
										}); // enemy takes damage
							}
						});
				registerEntityModifier(trajectory);
			}
		}
	}

	/**
	 * returns which column the enemy is in (between 0 for the first column, and
	 * 14 for the last column)
	 */
	public int getCol() {
		return Utils.getColFromX(getX());
	}

	/**
	 * returns which row the enemy is in (between 0 for the first row, and 6 for
	 * the last row)
	 */
	public int getRow() {
		return Utils.getRowFromY(getY());
	}

	@Override
	public Enemy clone() {
		// no need to use the other constructor, since those are already set
		final Enemy returnEnemy = new Enemy(getX(), getY(), getWidth(),
				getHeight(), getTiledTextureRegion(),
				getVertexBufferObjectManager());
		returnEnemy.health = this.health;
		returnEnemy.speed = this.speed;
		returnEnemy.credits = this.credits;
		returnEnemy.score = this.score;
		returnEnemy.path = path.clone(returnEnemy);
		returnEnemy.healthBar = healthBar.clone(returnEnemy);
		return returnEnemy;
	}

	/**
	 * Gets the distance to the next point in the Enemy's path
	 * 
	 * @return the second point of the link, to get the link reference .get(x)
	 *         and .get(x-1)
	 */
	public int getCurrentLink() {
		// first find which two points the Enemy is between
		double myError;
		int curLink = this.path.xyPath.size() - 1; // default to the last link
		if (this.path.xyPath.size() > 2) {
			for (int i = 1; i < this.path.xyPath.size(); i++) {
				if (((this.path.xyPath.get(i - 1).y <= this.getY()) && (this
						.getY() <= this.path.xyPath.get(i).y))
						|| ((this.path.xyPath.get(i).y <= this.getY()) && (this
								.getY() <= this.path.xyPath.get(i - 1).y))) { // getY()
																				// is
																				// between
																				// y1
																				// and
																				// y2
					if (((this.path.xyPath.get(i - 1).x <= this.getX()) && (this
							.getX() <= this.path.xyPath.get(i).x))
							|| ((this.path.xyPath.get(i).x <= this.getX()) && (this
									.getX() <= this.path.xyPath.get(i - 1).x))) { // getX()
																					// is
																					// between
																					// x1
																					// and
																					// x2
						if ((Math.abs(this.path.xyPath.get(i).x
								- this.path.xyPath.get(i - 1).x)) < TOLERANCE) { // vertical
																					// line,
																					// therefore
							if (Math.abs(this.getX()
									- this.path.xyPath.get(i - 1).x) < TOLERANCE) { // just
																					// see
																					// if
																					// the
																					// X
																					// value
																					// matches
								curLink = i;
								i = this.path.xyPath.size() - 1;
							}
						} else {
							double m = (this.path.xyPath.get(i).y - this.path.xyPath
									.get(i - 1).y)
									/ (this.path.xyPath.get(i).x - this.path.xyPath
											.get(i - 1).x);
							double b = this.path.xyPath.get(i - 1).y - m
									* this.path.xyPath.get(i - 1).x; // b = y-mx
							// now check to see if our point is on y=mx+b
							myError = (m * this.getX() + b) - this.getY(); // should
																			// be
																			// zero
																			// if
																			// we
																			// are
																			// on
																			// this
																			// line
							if (Math.abs(myError) < TOLERANCE) {
								curLink = i;
								i = this.path.xyPath.size() - 1;
							}
						}
					}
				}
				// if it's on this line
				// check if it's between myPoint.x,myPoint.y and
				// this.path.xyPath.get(i).x,this.path.xyPath.get(i).y
			}
			// now we have which link we're on
			return curLink;
		} else {
			return -1;
		}
	}
}
