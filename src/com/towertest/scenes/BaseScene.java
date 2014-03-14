/**
 * 
 */
package com.towertest.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


import com.towertest.GameActivity;
import com.towertest.managers.ResourceManager;
import com.towertest.managers.SceneManager.SceneType;

/**
 * @author jared meadows Simple extension of the scene class that allows you to pause it
 */
public abstract class BaseScene extends Scene {
	protected Camera camera;

	public boolean isPaused = false;

	protected ResourceManager resourceManager;
	protected Engine engine;
	protected GameActivity activity;
	protected VertexBufferObjectManager vbom;

	/**
	 * 
	 */
	public BaseScene() {
		this.resourceManager = ResourceManager.getInstance();
		this.engine = resourceManager.engine;
		this.activity = resourceManager.activity;
		this.vbom = resourceManager.vbom;
		this.camera = resourceManager.camera;
		createScene();
	}
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if (isPaused)
			return;
		super.onManagedUpdate(pSecondsElapsed);
	}

	public void setPaused(boolean p) {
		isPaused = p;
	}
	
	public abstract void createScene();
	public abstract void onBackPressed();
	public abstract SceneType getSceneType();
	public abstract void disposeScene();
}
