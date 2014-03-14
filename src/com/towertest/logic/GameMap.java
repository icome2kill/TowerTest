package com.towertest.logic;

public class GameMap {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	/** Locations where Enemies should enter the map */
	private Waypoint[] startLoc;
	/** Locations where the Enemies should want to leave */
	private Waypoint[] endLoc;
	// ===========================================================
	// Constructors
	// ===========================================================
	public GameMap(Waypoint[] startLoc, Waypoint[] endLoc) {
		super();
		this.setStartLoc(startLoc);
		this.setEndLoc(endLoc);
	}
	public Waypoint[] getEndLoc() {
		return endLoc;
	}
	public void setEndLoc(Waypoint[] endLoc) {
		this.endLoc = endLoc;
	}
	public Waypoint[] getStartLoc() {
		return startLoc;
	}
	public void setStartLoc(Waypoint[] startLoc) {
		this.startLoc = startLoc;
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
