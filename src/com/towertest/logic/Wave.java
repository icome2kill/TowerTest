package com.towertest.logic;

import com.towertest.sprites.Enemy;


/**
 * Class represent wave of enemy
 * Each wave will have a protoptype enemy as well as their number
 * @author Nguyen
 *
 */
public class Wave {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	// Use 2 array instead of a map for performance
	private int[] numberOfEnemies;
	private Enemy[] enemies;
	private int count;
	private int index;
	private int currentEnemyCount;
	
	private float delay; // Delay between enemy spawn
	// ===========================================================
	// Constructors
	// ===========================================================

	public Wave(int[] numberOfEnemies, Enemy[] enemies, float delay) {
		super();
		this.numberOfEnemies = numberOfEnemies;
		this.enemies = enemies;
		this.delay = delay;
		
		index = 0;
		count = 0;
		currentEnemyCount = 0;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public int[] getNumberOfEnemies() {
		return numberOfEnemies;
	}

	public Enemy[] getEnemies() {
		return enemies;
	}
	
	public float getDelay() {
		return delay;
	}
	
	public int getTotal() {
		int total = 0;
		for (int i = 0; i < numberOfEnemies.length; i++) {
			total += numberOfEnemies[i];
		}
		return total;
	}
	
	public Enemy spawn() {
		// Determine current enemy type
		if (index == enemies.length) {
			return null;
		}
		Enemy enemy = enemies[index].clone();
		if (++count == numberOfEnemies[index]) {
			count = 0;
			index++;
		}
		currentEnemyCount++;
		return enemy;
	}
	
	public int getCurrentEnemyCount() {
		return currentEnemyCount;
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
