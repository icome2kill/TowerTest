package com.towertest;

//TODO fix hand off of touch event from HUD to Scene.
//it breaks as soon as your move off the buildBasiTower sprite
import java.util.ArrayList;

import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

import android.util.Log;

import com.towertest.builders.TowerBuilder;
import com.towertest.scenes.GameScene;
import com.towertest.sprites.Tower;

/**
 * Used to build a tower when dragged off of the HUD implements
 * IOnAreaTouchListener
 * 
 * @author abinning
 * 
 */
public class BuildTowerTouchHandler implements IOnAreaTouchListener {
	// used to determine if they've moved far enough to start panning (o.w.,
	// they might want to tap on the tower instead)
	double distTraveled = 0;
	float lastX = 0;
	float lastY = 0;
	float firstX = 0;
	float firstY = 0;
	float startingOffsetX = 0;
	float startingOffsetY = 0;

	TouchEvent firstTouchEvent = null;
	/*-*
	 * Tells us if we can create a new tower, false = no, not allowed, true = yes, we can make a tower
	 */
	// boolean createNewTower;
	boolean showHitArea;
	boolean currentlyDragging = false;
	public static Tower tw;

	private GameScene scene;
	// Scene hud;
	// float touchX, touchY;
	ArrayList<? extends Tower> buildTower; // List of prototype towers
	ArrayList<Tower> arrayTower;
	BaseGameActivity myContext;

	/**
	 * Used to build a tower when dragged off of the HUD
	 * 
	 * @param bt
	 *            the buildTower button (tower type)
	 * @param scene
	 *            Scene
	 * @param creds
	 *            reference to credits
	 * @param al
	 *            array list to add new tower to
	 * @param btex
	 *            bullet TextureRegion for tower
	 * @param ttex
	 *            Tower TextureRegion
	 * @param vbom
	 *            VertexBufferObjectManager
	 * @param hagtex
	 *            TextureRegion for tower
	 * @param habtex
	 *            TextureRegion for tower
	 */
	public BuildTowerTouchHandler(ArrayList<? extends Tower> bt,
			GameScene scene, ArrayList<Tower> al) { // Scene
													// h,
		this.scene = scene;
		buildTower = bt;
		arrayTower = al;
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final ITouchArea pTouchArea, final float pTouchAreaLocalX,
			final float pTouchAreaLocalY) {

		if (pSceneTouchEvent.isActionUp()) {
			if (tw != null) {
				// TODO the problem is, "isActionUp" NEVER gets called!
				tw.setHitAreaShown(scene, false);
				tw.moveable = false;
				if (tw.hasPlaceError() || scene.getCredits() < tw.getCredits()) {
					// refund credits and remove tower, because they can't place
					// it
					// where it is
					tw.remove(false, scene);
				} else {
					// final float newX = TowerTest.sceneTransX(tw.getX()) -
					// tw.getXHandleOffset();
					// final float newY = TowerTest.sceneTransY(tw.getY()) -
					// tw.getYHandleOffset();
					tw.canPlace(true, scene, tw);

					// TODO add logic to not subtract credits if removed!!!!!!!

					// remove the credits, since we're placing it here
					scene.addCredits(-tw.getCredits());
					Log.i("TowerDrop", "getCol:" + tw.getCol());
					Log.i("TowerDrop", "getRow:" + tw.getRow());
				}
				// if location is good continue, else destroy tower and refund
				// cost
				tw = null;
				// createNewTower = true;// do this last, so we make sure the
				// tower gets finished before allowing more
			}
		} else if (pSceneTouchEvent.isActionMove()) {
			if (tw == null) {
				// This is the part that creates the tower when you hit the
				// "creation" tower
				// createNewTower = false;
				for (final Tower buildableTower : buildTower) {
					if (buildableTower.contains(pSceneTouchEvent.getX(),
							pSceneTouchEvent.getY())) {
						final float newX = scene.sceneTransX(pSceneTouchEvent
								.getX() + startingOffsetX)
								- buildableTower.getXHandleOffset();
						final float newY = scene.sceneTransY(pSceneTouchEvent
								.getY() + startingOffsetY)
								- buildableTower.getYHandleOffset();

						final Tower tower = new TowerBuilder(scene)
								.setX(newX)
								.setY(newY)
								.setDamage(buildableTower.getDamage())
								.setCooldown(buildableTower.getCD())
								.setCredit(buildableTower.getCredits())
								.setMaxlevel(buildableTower.getMaxLevel())
								.setRange(buildableTower.getRange())
								.setTowerTexture(buildableTower.getTowerTexture())
								.setBulletSpeed(buildableTower.getBulletSpeed())
								.setBulletTexture(
										buildableTower.getBulletTexture())
								.build();
						tower.setOnAreaTouchedListener(new IOnAreaTouchListener() {
							@Override
							public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
									ITouchArea pTouchArea, float pTouchAreaLocalX,
									float pTouchAreaLocalY) {
								return towerTouchEvent(pSceneTouchEvent, tower);
							}
						});
//
//						tw = new Tower(newX, newY,
//								buildableTower.getBulletTexture(),
//								buildableTower.getTowerTexture(),
//								GameScene.TILE_WIDTH, GameScene.TILE_HEIGHT,
//								scene, ResourceManager.getInstance().vbom,
//								buildableTower.getRange()) {
//							@Override
//							public boolean onAreaTouched(
//									TouchEvent pSceneTouchEvent,
//									float pTouchAreaLocalX,
//									float pTouchAreaLocalY) {
//								return towerTouchEvent(pSceneTouchEvent, this);
//							}
//						};
//
//						tw.setDamage(buildableTower.getDamage());
//						tw.setCooldown(buildableTower.getCD());
//						tw.setCredits(buildableTower.getCredits());
//						tw.setMaxLevel(buildableTower.getMaxLevel());
//						tw.setBulletSpeed(buildableTower.getBulletSpeed());

						tower.checkClearSpotAndPlace(scene, newX, newY);
						tower.setHitAreaShown(scene, true);
						arrayTower.add(tower); // add to array
						scene.registerTouchArea(tower);
						scene.attachChild(tower); // add it to the scene
						
						tw = tower;
						
						break;
					}
				}
			} else {
				if (tw.moveable) {
					// This moves it to it's new position whenever they move
					// their finger
					final float newX = scene.sceneTransX(pSceneTouchEvent
							.getX()) - tw.getXHandleOffset();
					final float newY = scene.sceneTransY(pSceneTouchEvent
							.getY()) - tw.getYHandleOffset();
					tw.checkClearSpotAndPlace(scene, newX, newY);
				}
			}
		}
		return true;
	}

	private boolean towerTouchEvent(TouchEvent pSceneTouchEvent, Tower thisTower) {
		// TODO add code for upgrades, better make a separate class for it,
		// perhaps contained within the Tower class
		
		if (pSceneTouchEvent.isActionDown()) {
			Log.d("Building Tower", "Tower touch event down");
			if (!currentlyDragging) {
				lastX = pSceneTouchEvent.getX();
				lastY = pSceneTouchEvent.getY();
				firstX = lastX;
				firstY = lastY;
				distTraveled = 0;
				showHitArea = true;
				currentlyDragging = true;
				firstTouchEvent = pSceneTouchEvent; // back it up
				return true;
			} else {
				Log.w("onSceneTouchEvent",
						"I had two down touch events witout an up!");
				return true;
			}
		} else if (pSceneTouchEvent.isActionMove()) {
			Log.d("Building Tower", "Tower touch event move");
			// distTraveled += Math.sqrt((Math.pow(lastX -
			// pSceneTouchEvent.getX(), 2)) + (Math.pow(lastY -
			// pSceneTouchEvent.getY(), 2))) * TowerTest.camera.getZoomFactor();
			distTraveled += Math.sqrt((Math.pow(
					lastX - pSceneTouchEvent.getX(), 2))
					+ (Math.pow(lastY - pSceneTouchEvent.getY(), 2)));
			// store x and y for next move event
			lastX = pSceneTouchEvent.getX();
			lastY = pSceneTouchEvent.getY();
			if (distTraveled < GameScene.TILE_HEIGHT) {
				// Log.e("TowerCreate","distTraveled:"+distTraveled+"<TOWER_HEIGHT:"+TowerTest.TOWER_HEIGHT);
				return true; // tell it we handled the touch event, because they
								// haven't gone far enough (should be true)
			} else {
				if (showHitArea) { // that means it's the first time we've ran
									// this, so..
					startingOffsetX = firstX - lastX;
					startingOffsetY = firstY - lastY;
					GameActivity.currentXoffset = lastX - firstX;
					GameActivity.currentYoffset = lastY - firstY;
				}
				showHitArea = false;
				return false; // pass it through if it's already too far
			}
		} else if (pSceneTouchEvent.isActionUp()) {
			GameActivity.currentXoffset = 0;
			GameActivity.currentYoffset = 0;

			scene.showTowerDetails(thisTower, true);
			if (showHitArea) {
				// this.setHitAreaShown(scene, !this.getHitAreaShown()); //
				// toggle hit area circle
				// TODO I have this temporarily disabled so we can quickly tap
				// to delete towers
				currentlyDragging = false;
				return true;
				// do upgrade
			} else {
				// NOT Upgrading Tower, they were panning around
				showHitArea = false;
				currentlyDragging = false;
				return false;
			}
		} else {
			showHitArea = false;
			return false;
		}
	}

}
