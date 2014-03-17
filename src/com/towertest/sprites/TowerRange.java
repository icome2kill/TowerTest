package com.towertest.sprites;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.towertest.Utils;

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
	public TowerRange(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager tvbom) {
		super(pX, pY, pTextureRegion, tvbom);
		this.setZIndex(zIndex);
	}

	public TowerRange(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager tvbom, int range) {
		super(pX, pY, Utils.getXFromCol(range + 1), Utils
				.getXFromCol(range + 1), pTextureRegion, tvbom);
		this.setZIndex(zIndex);
	}
}