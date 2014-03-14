package com.towertest.sprites;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class LongRangeTower extends Tower {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	public static String texture = "tower.png";
	protected long cooldown = 1000; // in milliseconds | 1 sec = 1,000 millisec
	protected long credits = 100; // cost to build tower in credits
	protected int level = 1; // level of tower
	protected int maxLevel = 10; // level of tower
	public final int damage = 15; // Tower damage
	public String damageType = "normal";
	protected float cdMod = 0.1f;
	protected long lastFire = 0;
	// ===========================================================
	// Constructors
	// ===========================================================
	public LongRangeTower(TextureRegion b, float pX, float pY, float pWidth,
			float pHeight, TextureRegion pTextureRegion,
			TextureRegion hitAreaTextureGood, TextureRegion hitAreaTextureBad,
			Scene pScene, ArrayList<Tower> pArrayTower,
			VertexBufferObjectManager tvbom) {
		super(b, pX, pY, pWidth, pHeight, pTextureRegion, hitAreaTextureGood,
				hitAreaTextureBad, pScene, pArrayTower, tvbom, 5f);
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
