package com.towertest.sprites;

import java.util.ArrayList;

import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.towertest.Utils;
import com.towertest.logic.Path;
import com.towertest.managers.ResourceManager;
import com.towertest.scenes.GameScene;

/**
 * Basic Tower class contains it's own projectiles and provides methods for
 * firing
 * 
 * @author Andrew Binning
 * @see Projectile
 * @see SplashTower
 */
public class Tower extends AnimatedSprite {
	// I am Tower class and have my own bullets //
	// TODO fire range, acquisition range, pattern/type.
	public static String texture = "tower_new.png";
	private int damage = 100; // Tower damage
	private String damageType = "normal";

	private long cooldown = 500; // in milliseconds | 1 sec = 1,000 millisec
	private long credits = 50; // cost to build tower in credits
	private int level = 1; // level of tower
	private int maxLevel = 10; // level of tower
	private float bulletSpeed;
	
	private IOnAreaTouchListener onAreaTouchedListener;

	public void setOnAreaTouchedListener(IOnAreaTouchListener onAreaTouchedListener) {
		this.onAreaTouchedListener = onAreaTouchedListener;
	}

	public float getBulletSpeed() {
		return bulletSpeed;
	}

	public void setBulletSpeed(float bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	private float cdMod = 0.5f;
	private long lastFire = 0;
	private int range; // As tile unit
	private static int total = 0; // total number of this type of tower

	public int getRange() {
		return range;
	}
	
	public ITextureRegion getBulletTexture() {
		return bulletTexture;
	}

	public void setBulletTexture(TextureRegion bulletTexture) {
		this.bulletTexture = bulletTexture;
	}

	private ITiledTextureRegion towerTexture;

	public ITiledTextureRegion getTowerTexture() {
		return towerTexture;
	}

	private ITextureRegion bulletTexture;
	private boolean placeError = false;
	private boolean hitAreaShown = false;
	private boolean hitAreaGoodShown = false;
	private boolean hitAreaBadShown = false;
	private int zIndex = 1000;
	public ITextureRegion hitAreaGoodTexture;
	public ITextureRegion hitAreaBadTexture;
	TowerRange towerRangeGood;
	TowerRange towerRangeBad;
	/**
	 * Tells us that we can move the tower, also tells us that the tower can
	 * shoot, false means it can do neither thing
	 */
	public boolean moveable = true;
	private Projectile spriteBullet;
	static Scene scene;
	static float lastCheckedX = 0;
	static float lastCheckedY = 0;

	// int speed = 500;
	ArrayList<Projectile> arrayBullets; // may change to spritebatch

	// Body range = PhysicsFactory.createCircularBody();

	// constructor
	/**
	 * Constructor for tower class
	 * 
	 * @param b
	 *            TextureRegion for bullet
	 * @param pX
	 *            x coordinate of tower to create
	 * @param pY
	 *            y coordinate of tower to create
	 * @param pWidth
	 *            width of tower
	 * @param pHeight
	 *            height of tower
	 * @param tvbom
	 *            VertexBufferObjectManager
	 */
	public Tower(float pX, float pY, ITextureRegion bulletTexture,
			ITiledTextureRegion towerTexture, float pWidth, float pHeight,
			Scene pScene, VertexBufferObjectManager tvbom, int range) {
		super(pX, pY, pWidth, pHeight, towerTexture, tvbom);
		this.bulletTexture = bulletTexture;
		this.towerTexture = towerTexture;
		scene = pScene;
		this.range = range;
		arrayBullets = new ArrayList<Projectile>(); // create a new ArrayList
		towerRangeGood = new TowerRange(0, 0,
				ResourceManager.getInstance().hitAreaGoodTexture,
				getVertexBufferObjectManager(), range * 2);
		towerRangeBad = new TowerRange(0, 0,
				ResourceManager.getInstance().hitAreaBadTexture,
				getVertexBufferObjectManager(), range * 2);
		towerRangeGood.setPosition(
				this.getWidth() / 2 - towerRangeGood.getWidth() / 2,
				this.getHeight() / 2 - towerRangeGood.getHeight() / 2);
		towerRangeBad.setPosition(
				this.getWidth() / 2 - towerRangeBad.getWidth() / 2,
				this.getHeight() / 2 - towerRangeBad.getHeight() / 2);

		this.setZIndex(zIndex); // used to determine the order stuff is drawn in
		total++;
	}

	/**
	 * Fires projectiles check cooldown in milli seconds with: <br>
	 * long elapsedTime = System.currentTimeMillis() - towerVar.getLastFire;
	 * 
	 * @param targetX
	 *            target attacking
	 * @param targetY
	 *            target attacking
	 * @param tx
	 *            location of projectile
	 * @param ty
	 *            location of projectile
	 * @return boolean True if tower fired (created bullet sprite), else false
	 */
	public boolean fire(Enemy target, GameScene scene,
			ArrayList<Enemy> arrayEn, BaseGameActivity myContext) {
		if (!scene.isPaused) {
			if (target.getHealth() > target.inboundDamage) {
				long elapsed = System.currentTimeMillis() - lastFire;
				// only fire if tower is off cool down
				if (elapsed > cooldown * cdMod && !moveable) { // not on
																// cooldown,
																// and
																// not actively
																// being
																// placed
					spriteBullet = new Projectile(this.getMidX(),
							this.getMidY(), 10f, 10f, getBulletTexture(),
							getVertexBufferObjectManager(), scene); // READY?!?
					spriteBullet.setSpeed(bulletSpeed);
					spriteBullet.setTarget(this, target); // AIM...
					spriteBullet.shoot(arrayEn, myContext); // FIIIIIRE!!!!
					arrayBullets.add(spriteBullet);
					lastFire = System.currentTimeMillis();
					// TODO check sound settings
//					ResourceManager.getInstance().fireSound.play();
					
					Sprite myBullet = this.getLastBulletSprite();
					scene.attachChild(myBullet);

//					long delay = (long) (cooldown / 1.1);
//					long[] ANIMATE = {delay };
//					animate(ANIMATE, false);
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * Get the cool down milliseconds
	 * 
	 * @return cool down in milliseconds
	 */
	public long getCD() {
		return cooldown;
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}

	public void setCredits(long credits) {
		this.credits = credits;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	/**
	 * Get the cool down Modifier as a float to represent a percentage if(
	 * elapsed > cooldown * cdMod)
	 * 
	 * @return cool down Modifier float
	 */
	public float getCDMod() {
		return cdMod;
	}

	/**
	 * Set the cool down Modifier as a float to represent a percentage if(
	 * elapsed > cooldown * cdMod)
	 */
	public void setCDMod(long cdm) {
		cdMod = cdm;
	}

	/**
	 * Loads the sound from mfx/
	 * 
	 * @param sm
	 *            SoundManager passed from engine
	 * @param act
	 *            SimpleBaseGameActivity Base class (this)
	 */

	public Sprite getLastBulletSprite() {
		return spriteBullet; // our main class uses this to attach to the scene
	}

	public ArrayList<Projectile> getArrayList() {
		return arrayBullets; // our main class uses this to check bullets etc
	}

	/**
	 * Get to cost to build this tower
	 * 
	 * @return build cost in credits
	 */
	public long getCredits() {
		return credits;
	}

	/**
	 * Get Current tower level
	 * 
	 * @return tower level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Upgrade tower one level
	 * 
	 * @return Returns false if tower already at max level
	 */
	public boolean upgradeLevel() {
		if (level == maxLevel)
			return false;
		else
			level++;
		return true;
	}

	/**
	 * Get the total number of these towers that have been built
	 * 
	 * @return number of this tower that has been built
	 */
	public int getTotal() {
		return total;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * Sell tower, return credit value
	 * 
	 * @return credit value of tower
	 */
	public long sell(GameScene scene) {
		remove(true, scene);
		return credits;
	}

	/**
	 * removes tower from scene and array, and updates counter
	 * 
	 * @param tower
	 * @param resetTile
	 */
	public void remove(final boolean resetTile, final GameScene scene) {
		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				scene.unregisterTouchArea(Tower.this);
				// scene.detachChild(tower);
				Tower.this.detachSelf();
				scene.getArrayTower().remove(Tower.this);
				if (resetTile) {
					Log.e("Tower", "removed " + Tower.this.getCol() + ","
							+ Tower.this.getRow());
				}
			}
		});
		total--;
		if (resetTile) { // we must re-calculate ALL paths, since there's no
							// telling how to know if the one they removed will
							// change paths :-\
			try {
				scene.getTmxLayer()
						.getTMXTileAt(getX(), getY())
						.setGlobalTileID(scene.getTmxTiledMap(),
								ResourceManager.TILEID_UNPATHABLE[scene.getCurrentMap()]);
				
				Path path;
				// first go through and update all the enemies in the ArrayList
				for (Enemy en : scene.getArrayEn()) {
					path = new Path(en, scene.getCurrentLevel().getMap()
							.getEndLoc()[0], scene.getTmxLayer(), scene
							.getCurrentLevel().getMap());
					en.path = path;
					en.stop();
					en.startMoving();
				}
				// now don't forget to update the enemyClone!
				for (Enemy en : scene.getEnemyClone()) {
					path = new Path(en, scene.getCurrentLevel().getMap()
							.getEndLoc()[0], scene.getTmxLayer(), scene
							.getCurrentLevel().getMap());
					en.path = path;
				}
			} catch (NullPointerException e) {
				// then they must be in the black void, so no need to reset the
				// tile
			}
		}
	}

	/**
	 * This gets the X attachment point (allows you to offset where you grab it
	 * at, 0,0 is the upper-left)
	 */
	public float getXHandleOffset() {
		return (this.getWidth() / 2) * 1.5f; // asjusted by 1.5 so it looks like
												// you're grabbing the center of
												// the turret
	}

	/**
	 * This gets the Y attachment point (allows you to offset where you grab it
	 * at, 0,0 is the upper-left)
	 */
	public float getYHandleOffset() {
		return this.getHeight() / 2; // default to the middle of the sprite
	}

	/**
	 * Checks to see if the tower can be placed here, and places it, also
	 * updates the tower if it should have a placement error
	 * 
	 * @param scene
	 * @param newX
	 *            x value where we're trying to place the tower
	 * @param newY
	 *            y value where we're trying to place the tower
	 * @param myContext
	 */
	public void checkClearSpotAndPlace(GameScene scene, float newX, float newY) {
		// try {
		final TMXTile tmxTile = scene.getTmxLayer().getTMXTileAt(
				newX + this.getXHandleOffset(), newY + this.getYHandleOffset());
		// Snaps tower to tile
		if (tmxTile == null) {
			this.setTowerPlaceError(scene, true);
			// Log.e("Jared","ERROR finding tile!!!");
			this.setPosition(newX, newY);
		} else {
			newX = tmxTile.getTileX()/* + this.getXHandleOffset() */;
			newY = tmxTile.getTileY()/* + this.getYHandleOffset() */;
			// TowerTest.tmxLayer.getTMXTileAt(newX, newY);
			// newX = tmxTile.getTileX();
			// newY = tmxTile.getTileY();
			this.setPosition(newX, newY);

			TMXProperties<TMXTileProperty> properties = tmxTile
					.getTMXTileProperties(ResourceManager.getInstance().tmxTiledMap);

			if (properties != null
					&& (properties.containsTMXProperty(
							GameScene.TAG_TILED_PROPERTY_NAME_PATH,
							GameScene.TAG_TILED_PROPERTY_VALUE_TRUE) || properties
							.containsTMXProperty(
									GameScene.TAG_TILED_PROPERTY_NAME_BUILDABLE,
									GameScene.TAG_TILED_PROPERTY_VALUE_FALSE))) {
				// set the circle to red (it has an error)
				this.setTowerPlaceError(scene, true);
				// Log.e("Jared","CAN NOT PLACE ON CACTI "+newX+","+newY);
			} else {
				if ((newX != lastCheckedX) || (newY != lastCheckedY)) { // only
																		// check
																		// it if
																		// we
																		// haven't
																		// checked
																		// it
																		// yet
					if (this.canPlace(false, scene, this)) {
						// set the circle to green
						this.setTowerPlaceError(scene, false);
						// Log.e("Jared","Can Place Here");
					} else {
						this.setTowerPlaceError(scene, true);
						// Log.e("Jared","CAN NOT PLACE HERE");
					}
				}
			}
		}
		lastCheckedX = newX;
		lastCheckedY = newY;
		/*
		 * } catch (Exception e) { // this happens when it's drug off the map
		 * this.setTowerPlaceError(scene, true);
		 * Log.e("Jared","ERROR returning TMXTiledMap Properties!!!");
		 * this.setPosition(newX, newY); }
		 */
	}

	/**
	 * Checks to see if a tower can be placed there. Checks:
	 * 
	 * @param newX
	 * @param newY
	 * @param assignPaths
	 * @param tw
	 * @return
	 */
	public boolean canPlace(boolean assignPaths, GameScene scene, Tower tw) {
		float newX = tw.getX();
		float newY = tw.getY();
		final TMXTile tmxTile = scene.getTmxLayer().getTMXTileAt(newX, newY);
		if (tmxTile != null) {
			int backupTileID = tmxTile.getGlobalTileID();

			tmxTile.setGlobalTileID(scene.getTmxTiledMap(),
					ResourceManager.TILEID_BLOCKED);
			// crazy loop action
			boolean towerNotAllowed = false;
			Path[] tempPaths = new Path[scene.getArrayEn().size()
					+ scene.getEnemyClone().length];
			boolean[] needsNewPath = new boolean[scene.getArrayEn().size()
					+ scene.getEnemyClone().length];
			if (assignPaths) {
				// check all the enemy paths to see if they can find a new path
				for (int i = 0; i < scene.getArrayEn().size(); i++) {
					Enemy enemy = scene.getArrayEn().get(i);
					Log.d("Tower", "Checking path");
					// if the tower is on this enemy's path, then, check if the
					// enemy can find a new one
					if (enemy.path != null) {
						if (enemy.path.checkRemainingPath(
								Utils.getColFromX(newX),
								Utils.getRowFromY(newY))) {
							// only then, should we check pathfinding!
							tempPaths[i] = new Path(enemy, scene
									.getCurrentLevel().getMap().getEndLoc()[0],
									scene.getTmxLayer(), scene
											.getCurrentLevel().getMap());
							if (tempPaths[i].rcPath == null) {
								// they can't put it here!
								towerNotAllowed = true;
								break;
							}
							needsNewPath[i] = true;
						} else {
							needsNewPath[i] = false;
						}
					}
				}
			}
			// also, check the starting points!
			if (!towerNotAllowed) {
				for (int i = 0; i < scene.getEnemyClone().length; i++) {
					if (scene.getEnemyClone()[i].path == null) {
						towerNotAllowed = true;
						Log.e("Jared",
								"Warning, a starting point doesn't have a path!!!");
					} else {
						if (scene.getEnemyClone()[i].path.rcPath.contains(
								Utils.getColFromX(newX),
								Utils.getColFromX(newY))) {
							tempPaths[scene.getArrayEn().size() + i] = new Path(
									scene.getEnemyClone()[i], scene
											.getCurrentLevel().getMap()
											.getEndLoc()[0],
									scene.getTmxLayer(), scene
											.getCurrentLevel().getMap());
							if (tempPaths[scene.getArrayEn().size() + i].rcPath == null) {
								// they can't put it here!
								towerNotAllowed = true;
							}
							needsNewPath[scene.getArrayEn().size() + i] = true;
						}
					}
				}
			}
			// now that we have all the paths, if they were all good, assign
			// them to their enemies, otherwise remove the tower
			if (!assignPaths) {
				tmxTile.setGlobalTileID(scene.getTmxTiledMap(), backupTileID);
				return !towerNotAllowed;
			} else {
				if (towerNotAllowed) { // remove it, because one enemy had a
										// problem with it
					tw.remove(false, scene);
					tmxTile.setGlobalTileID(scene.getTmxTiledMap(),
							backupTileID);
					return false;
				} else {
					// go through and assign the paths, since nobody had a
					// problem with it
					for (int i = 0; i < scene.getArrayEn().size(); i++) {
						if (needsNewPath[i]) {
							Enemy enemy = scene.getArrayEn().get(i);
							enemy.path = tempPaths[i];
							enemy.stop();
							enemy.startMoving();
						}
					}
					for (int i = 0; i < scene.getEnemyClone().length; i++) {
						if (needsNewPath[scene.getArrayEn().size() + i]) {
							scene.getEnemyClone()[i].path = tempPaths[scene
									.getArrayEn().size() + i];
						}
					}
					return true;
				}
			}
		} else { // the tile is null (the tower is in the black void), so return
					// false
			return false;
		}
	}

	/**
	 * This tells the tower if it is allowed to be placed where it is, when it
	 * is being placed
	 */
	public void setTowerPlaceError(Scene scene, boolean towerPlaceError) {
		placeError = towerPlaceError;
		if (hitAreaShown) {
			// update the hit area to reflect this
			setHitAreaShown(scene, hitAreaShown);
		}
		// update graphics
	}

	public boolean getHitAreaShown() {
		return hitAreaShown;
	}

	/**
	 * Enables or disables the display of the "hit area", also updates color if
	 * necessary
	 */
	public void setHitAreaShown(Scene scene, boolean showHitArea) {
		if (moveable) {
			towerRangeGood.setPosition(
					this.getWidth() / 2 - towerRangeGood.getWidth() / 2,
					this.getHeight() / 2 - towerRangeGood.getHeight() / 2);
			towerRangeBad.setPosition(
					this.getWidth() / 2 - towerRangeBad.getWidth() / 2,
					this.getHeight() / 2 - towerRangeBad.getHeight() / 2);
		} else {
			towerRangeGood.setPosition(
					this.getX() + this.getWidth() / 2
							- towerRangeGood.getWidth() / 2,
					this.getY() + this.getHeight() / 2
							- towerRangeGood.getHeight() / 2);
			towerRangeBad.setPosition(
					this.getX() + this.getWidth() / 2
							- towerRangeBad.getWidth() / 2,
					this.getY() + this.getHeight() / 2
							- towerRangeBad.getHeight() / 2);
		}

		if (showHitArea) {
			// we attach it to this sprite, that way it's tied to it!
			if (placeError) {
				if (!hitAreaBadShown) {
					if (this.moveable) {
						this.attachChild(towerRangeBad);
					} else {
						scene.attachChild(towerRangeBad);
					}
					hitAreaBadShown = true;
				}
				if (hitAreaGoodShown) {
					if (this.moveable) {
						this.detachChild(towerRangeGood);
					} else {
						scene.detachChild(towerRangeGood);
					}
					hitAreaGoodShown = false;
				}
			} else {
				if (hitAreaBadShown) {
					if (this.moveable) {
						this.detachChild(towerRangeBad);
					} else {
						scene.detachChild(towerRangeBad);
					}
					hitAreaBadShown = false;
				}
				if (!hitAreaGoodShown) {
					if (this.moveable) {
						this.attachChild(towerRangeGood);
					} else {
						scene.attachChild(towerRangeGood);
					}
					hitAreaGoodShown = true;
				}
			}
		} else {
			// detach both!
			if (hitAreaGoodShown) {
				if (this.moveable) {
					this.detachChild(towerRangeGood);
				} else {
					scene.detachChild(towerRangeGood);
				}
				hitAreaGoodShown = false;
			}
			if (hitAreaBadShown) {
				if (this.moveable) {
					this.detachChild(towerRangeBad);
				} else {
					scene.detachChild(towerRangeBad);
				}
				hitAreaBadShown = false;
			}
		}
		hitAreaShown = showHitArea;
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (onAreaTouchedListener != null) {
			return onAreaTouchedListener.onAreaTouched(pSceneTouchEvent, this, pTouchAreaLocalX, pTouchAreaLocalY);			
		}
		return super
				.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}



	/** tells us if the tower can be placed where it is */
	public boolean hasPlaceError() {
		return this.placeError;
	}

	/**
	 * function for determining the distance to another sprite
	 * 
	 * @param s
	 *            sprite you want the distance to
	 * @return the distance to said sprite
	 */
	public double distanceTo(Enemy s) {
		return Math.sqrt(Math.pow(this.getMidX() - s.getMidX(), 2)
				+ Math.pow(this.getMidY() - s.getMidY(), 2));
	}

	/**
	 * Gives you the range of the tower, based on the size of the towerRangeGood
	 * circle
	 * 
	 * @return half the height of the towerRangeGood circle
	 */
	public float maxRange() {
		return this.towerRangeGood.getHeight() / 2.f;
	}

	public Vector2 getPosition() {
		return new Vector2(this.getX(), this.getY());
	}

	public float getMidX() {
		return this.getX() + this.getWidth() / 2;
	}

	public float getMidY() {
		return this.getY() + this.getHeight() / 2;
	}

	public void removeBullet(Projectile b) {
		arrayBullets.remove(b);
	}

	/**
	 * returns which column the tower is in (between 0 for the first column, and
	 * 14 for the last column)
	 */
	public int getCol() {
		return Utils.getColFromX(this.getX());
	}

	/**
	 * returns which row the tower is in (between 0 for the first row, and 6 for
	 * the last row)
	 */
	public int getRow() {
		return Utils.getRowFromY(this.getY());
	}

	public String getDamageType() {
		return damageType;
	}

	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}
}