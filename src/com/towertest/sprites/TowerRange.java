package com.towertest.sprites;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class TowerRange extends Sprite {
	private int zIndex = 2000; // show above the towers

	/**
	 * Create a new enemy
	 * 
	 * @param pX
	 * @param pY
	 * @param pTextureRegion
	 * @param tvbom
	 */
	public TowerRange(float pX, float pY, TextureRegion pTextureRegion, VertexBufferObjectManager tvbom) {
		super(pX, pY, pTextureRegion, tvbom);
		this.setZIndex(zIndex);
	}
	
	public TowerRange(float pX, float pY, TextureRegion pTextureRegion, VertexBufferObjectManager tvbom, float rangeMultiplier) {
		super(pX, pY, pTextureRegion.getWidth() * rangeMultiplier, pTextureRegion.getHeight() * rangeMultiplier, pTextureRegion, tvbom);
		this.setZIndex(zIndex);
	}
}