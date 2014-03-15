package com.towertest.builders;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;

import com.towertest.managers.ResourceManager;
import com.towertest.scenes.GameScene;
import com.towertest.sprites.Tower;

public class TowerBuilder {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private Tower tower;
	private float x;
	private float y;

	private int damage;
	private long cd;
	private long credit;
	private int range;
	private String damageType;

	private int level;
	private int maxLevel;

	private float bulletSpeed;

	private ITextureRegion bulletTexture;
	private ITiledTextureRegion towerTexture;

	private GameScene scene;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TowerBuilder(GameScene scene) {
		this.scene = scene;

		// Default values
		damage = 10;
		cd = 500;
		credit = 50;
		level = 1;
		maxLevel = 3;
		damageType = "normal";
		bulletSpeed = 100f;

		bulletTexture = ResourceManager.getInstance().bulletTexture;
		towerTexture = ResourceManager.getInstance().towerTexture;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public TowerBuilder setX(float x) {
		this.x = x;
		return this;
	}

	public TowerBuilder setY(float y) {
		this.y = y;
		return this;
	}

	public TowerBuilder damageType(String type) {
		this.damageType = type;
		return this;
	}

	public TowerBuilder setDamage(int damage) {
		this.damage = damage;
		return this;
	}

	public TowerBuilder setCooldown(long cd) {
		this.cd = cd;
		return this;
	}

	public TowerBuilder setCredit(long credit) {
		this.credit = credit;
		return this;
	}

	public TowerBuilder setRange(int range) {
		this.range = range;
		return this;
	}

	public TowerBuilder setMaxlevel(int level) {
		maxLevel = level;
		return this;
	}

	public TowerBuilder setBulletTexture(TextureRegion texture) {
		this.bulletTexture = texture;
		return this;
	}

	public TowerBuilder setBulletSpeed(int speed) {
		this.bulletSpeed = speed;
		return this;
	}

	public TowerBuilder setTowerTexture(ITiledTextureRegion texture) {
		this.towerTexture = texture;
		return this;
	}

	public Tower build() {
		tower = new Tower(x, y, bulletTexture, towerTexture,
				GameScene.TILE_WIDTH, GameScene.TILE_HEIGHT, scene,
				ResourceManager.getInstance().vbom, range);
		tower.setCooldown(cd);
		tower.setDamage(damage);
		tower.setCredits(credit);
		tower.setLevel(level);
		tower.setMaxLevel(maxLevel);
		tower.setDamageType(damageType);
		tower.setBulletSpeed(bulletSpeed);
		return tower;
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
