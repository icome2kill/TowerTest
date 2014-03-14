package com.towertest;

import com.towertest.managers.ResourceManager;

public class Utils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public static int getColFromX(float pX) {
		return (int) Math.floor(pX / ResourceManager.getInstance().tmxTiledMap.getTileWidth());
	}

	public static int getRowFromY(float pY) {
		return (int) Math.floor(pY / ResourceManager.getInstance().tmxTiledMap.getTileHeight());
	}

	public static float getXFromCol(int pC) {
		return Math.round(pC * ResourceManager.getInstance().tmxTiledMap.getTileWidth());
	}

	public static float getYFromRow(int pR) {
		return Math.round(pR * ResourceManager.getInstance().tmxTiledMap.getTileHeight());
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
