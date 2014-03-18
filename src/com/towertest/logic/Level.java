package com.towertest.logic;

public class Level {
	private GameMap map;

	private Wave[] waves;

	private int currentWaveNumber;
	private int currentEnemyCount;
	private int currentDelayBetweenWaves;
	
	/**
	 * Constructor intializes variables
	 * 
	 * @param waves
	 */
	public Level(Wave[] waves, GameMap map) {
		this.map = map;
		this.waves = waves;
		
		currentWaveNumber = 0;
		currentEnemyCount = 0;
	}

	/**
	 * Load a TMX map from file name
	 * 
	 * @param sMap File name
	 */
	public static void loadMap(String sMap) {

	}

	/**
	 * Load the level from a file
	 * 
	 * @return true is successful
	 */
	public boolean loadLevel() {
		// read file for: tmx file name, default waypoints, enemy list or modifiers
		return false;
	}
	
	public int getTotalEnemies() {
		int total = 0;
		for(int i = 0; i < waves.length; i++) {
			total += waves[i].getTotal();
		}
		return total;
	}
	
	public Wave[] getWaves() {
		return waves;
	}
	
	public Wave getNextWave() {
		return waves[currentWaveNumber++];
	}

	public int getCurrentWaveNumber() {
		return currentWaveNumber;
	}

	public int getCurrentEnemyCount() {
		return currentEnemyCount;
	}

	public void setCurrentEnemyCount(int currentEnemyCount) {
		this.currentEnemyCount = currentEnemyCount;
	}

	public int getCurrentDelayBetweenWaves() {
		currentDelayBetweenWaves = (int) (waves[currentWaveNumber].getDelay() * waves[currentWaveNumber].getTotal() * 2);
		return currentDelayBetweenWaves;
	}
	
	public boolean hasNextWave() {
		return currentWaveNumber < waves.length;
	}
	
	public void increaseCurrentEnemyCount() {
		currentEnemyCount++;
	}
	
	public GameMap getMap() {
		return map;
	}
}