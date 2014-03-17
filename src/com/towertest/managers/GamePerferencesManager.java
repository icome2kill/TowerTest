package com.towertest.managers;

public class GamePerferencesManager {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final GamePerferencesManager INSTANCE = new GamePerferencesManager();
	// ===========================================================
	// Fields
	// ===========================================================
	
	public int selectedDifficult;
	public int selectedMap;
	public boolean soundOn = true;

	// ===========================================================
	// Constructors
	// ===========================================================
	private GamePerferencesManager() {};
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public static GamePerferencesManager getInstance() {
		return INSTANCE;
	}
	
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
