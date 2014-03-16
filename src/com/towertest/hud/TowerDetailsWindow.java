package com.towertest.hud;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.towertest.managers.ResourceManager;
import com.towertest.scenes.GameScene;
import com.towertest.sprites.Tower;

public class TowerDetailsWindow extends Rectangle {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private Text damageText;
	private Text rangeText;
	private Text speedText;

	private ButtonSprite removeButton;
	private Tower currentTower;
	private Scene currentScene;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public TowerDetailsWindow(VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, 800, 40, pVertexBufferObjectManager);
		
		damageText = new Text(10, 0, ResourceManager.getInstance().font20, "Damage: 0123456789", pVertexBufferObjectManager);
		rangeText = new Text(210, 0, ResourceManager.getInstance().font20, "Range: 0123456789", pVertexBufferObjectManager);
		speedText = new Text(410, 0, ResourceManager.getInstance().font20, "Speed: 0123456789", pVertexBufferObjectManager);
		
		removeButton = new ButtonSprite(610, 0, ResourceManager.getInstance().towerRemoveButtonTexture, pVertexBufferObjectManager);
		
		attachChild(damageText);
		attachChild(rangeText);
		attachChild(speedText);
		attachChild(removeButton);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public void show(final Scene scene, Camera camera, final Tower tower, boolean showRange) {
		currentTower = tower;
		currentScene = scene;
		
		setPosition(40, camera.getHeight()-90);
		damageText.setText("Damage: " + tower.getDamage());
		rangeText.setText("Range: " + tower.getRange());
		speedText.setText("Speed: " + tower.getCD());
		
		removeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				detachSelf();
				tower.remove(true, (GameScene) scene);
			}
		});
		
		tower.setHitAreaShown(scene, showRange);
		
		scene.registerTouchArea(removeButton);
		scene.attachChild(this);
	}

	@Override
	public boolean detachSelf() {
		if (currentTower != null) {
			currentTower.setHitAreaShown(currentScene, false);
		}
		return super.detachSelf();
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
