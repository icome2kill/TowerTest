package com.towertest.managers;

public class GamePreferencesManager {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final GamePreferencesManager INSTANCE = new GamePreferencesManager();
	// ===========================================================
	// Fields
	// ===========================================================
	
	public int selectedDifficult;
	public int selectedMap;
	public boolean soundOn = true;

	// ===========================================================
	// Constructors
	// ===========================================================
	private GamePreferencesManager() {};
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public static GamePreferencesManager getInstance() {
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
