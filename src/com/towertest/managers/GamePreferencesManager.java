package com.towertest.managers;

public class GamePreferencesManager {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final GamePreferencesManager INSTANCE = new GamePreferencesManager();
	// ===========================================================
	// Fields
	// ===========================================================
	
	public int selectedDifficult = 0;
	public int selectedMap = 0;
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
