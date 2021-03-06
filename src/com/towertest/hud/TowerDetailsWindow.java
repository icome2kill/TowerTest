package com.towertest.hud;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import com.towertest.GameActivity;
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
	public TowerDetailsWindow(
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, GameActivity.CAMERA_WIDTH, GameScene.TILE_HEIGHT,
				pVertexBufferObjectManager);

		damageText = new Text(10, GameScene.TILE_HEIGHT / 2
				- ResourceManager.getInstance().font20.getLineHeight() / 2,
				ResourceManager.getInstance().font20, "Damage: 0123456789",
				pVertexBufferObjectManager);
		rangeText = new Text(260, GameScene.TILE_HEIGHT / 2
				- ResourceManager.getInstance().font20.getLineHeight() / 2,
				ResourceManager.getInstance().font20, "Range: 0123456789",
				pVertexBufferObjectManager);
		speedText = new Text(460, GameScene.TILE_HEIGHT / 2
				- ResourceManager.getInstance().font20.getLineHeight() / 2,
				ResourceManager.getInstance().font20, "Speed: 0123456789",
				pVertexBufferObjectManager);

		removeButton = new ButtonSprite(750, 12,
				ResourceManager.getInstance().towerRemoveButtonTexture,
				pVertexBufferObjectManager);
		setColor(new Color(Color.TRANSPARENT));

		attachChild(damageText);
		attachChild(rangeText);
		attachChild(speedText);
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
	public void show(final GameScene scene, Camera camera, final Tower tower,
			boolean showRange, boolean showRemove) {
		currentTower = tower;
		currentScene = scene;

		setPosition(GameScene.TILE_WIDTH, camera.getHeight()
				- GameScene.TILE_HEIGHT);
		damageText.setText("Damage: " + tower.getDamage());
		rangeText.setText("Range: " + tower.getRange());
		speedText.setText("Speed: " + tower.getCD());

		removeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (scene.getArrayTower().contains(tower))
					tower.remove(true, (GameScene) scene);
				detachSelf();
			}
		});

		detachChild(removeButton);
		if (showRemove) {
			attachChild(removeButton);
		}

		tower.setHitAreaShown(scene, showRange);

		camera.getHUD().registerTouchArea(removeButton);
		camera.getHUD().attachChild(this);
	}

	@Override
	public boolean detachSelf() {
		if (currentScene != null) {
			ResourceManager.getInstance().camera.getHUD().unregisterTouchArea(
					removeButton);
		}
		if (currentTower != null) {
			currentTower.setHitAreaShown(currentScene, false);
		}
		return super.detachSelf();
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
