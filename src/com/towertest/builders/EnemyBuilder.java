package com.towertest.builders;

import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.towertest.managers.ResourceManager;
import com.towertest.scenes.GameScene;
import com.towertest.sprites.Enemy;

public class EnemyBuilder {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	private float speed;
	private int health;
	private ITiledTextureRegion texture;
	private int score;
	private int credit; // Credit for killing an enemy
	
	private float x;
	private float y;
	
	private GameScene scene;
	// ===========================================================
	// Constructors
	// ===========================================================
	public EnemyBuilder(GameScene scene) {
		this.scene = scene;
		
		// Default
		x = 0;
		y = 0;
		speed = 50f;
		health = 500;
		score = 10;
		credit = score;
		texture = ResourceManager.getInstance().enemyTexture;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public EnemyBuilder setSpeed(float speed) {
		this.speed = speed;
		return this;
	}

	public EnemyBuilder setHealth(int health) {
		this.health = health;
		return this;
	}

	public EnemyBuilder setTexture(ITiledTextureRegion texture) {
		this.texture = texture;
		return this;
	}

	public EnemyBuilder setX(float x) {
		this.x = x;
		return this;
	}

	public EnemyBuilder setY(float y) {
		this.y = y;
		return this;
	}
	
	public EnemyBuilder setScore(int score) {
		this.score = score;
		return this;
	}
	
	public EnemyBuilder setCredit(int credit) {
		this.credit = credit;
		return this;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public Enemy build() {
		Enemy enemy = new Enemy(x, y, GameScene.TILE_WIDTH, GameScene.TILE_HEIGHT, health, texture, ResourceManager.getInstance().vbom, scene.getMap());
		enemy.speed = speed;
		enemy.setScore(score);
		enemy.setCredits(credit);
		return enemy;
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
