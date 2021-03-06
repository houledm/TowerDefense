package com.example.towerdefense;

import java.util.HashMap;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.util.Log;

public class TowerTile {
	
	static HashMap<BaseTextureRegion, Class<?>> towerMap;
  
  private Class<?> T;
	private Sprite sprite;
	private Rectangle frame;
	
	private boolean isTouched;
	private boolean isMoved;
	
	public static void initializeMap() {
		ResourceManager resourceManager = ResourceManager.getInstance();
    HashMap<BaseTextureRegion, Class<?>> aMap = new HashMap<BaseTextureRegion, Class<?>>();
    aMap.put(resourceManager.getTurretTowerRegion(), TurretTower.class);
    aMap.put(resourceManager.getDartTowerRegion(), DartTower.class);
    aMap.put(resourceManager.getFlameTowerRegion(), FlameTower.class);
    aMap.put(resourceManager.getIceTowerRegion(), IceTower.class);
    aMap.put(resourceManager.getSpikeTowerRegion(), SpikeTower.class);
    towerMap = aMap;	
	}
	
	
	//*******************Getters and Setters*********************//
	public Rectangle getFrame() {
		return frame;
	}
	public Sprite getSprite() {
		return sprite;
	}
	public boolean getOnTouched() {
		Log.i("isTouched", isTouched+"");
		if (!isTouched) return isTouched;
		isTouched = false;
		return true;
	}
	public boolean getOnMoved() {
		if (!isMoved) return isMoved;
		isMoved = false;
		return true;
	}
	public void returnOnTouched() {
		isTouched = false;
	}
	public void returnOnMoved() {
		isMoved = false;
	}
	public Class<?> getTowerClass() {
		return T;
	}
	
	
	//***************Public Methods************************//
	
	/**
	 * This initializes each of the Tower buttons within the HUD
	 * @param region
	 * @param number
	 */
	public TowerTile(BaseTextureRegion region) {
		T = towerMap.get(region);
		isTouched = false;
		TowerDefenseActivity activity = TowerDefenseActivity.getSharedInstance();
		ZoomCamera camera = activity.getCamera();
		VertexBufferObjectManager vbom = activity.getVertexBufferObjectManager();
		TMXTiledMap map = GameScene.getSharedInstance().getTMXTiledMap();
		
		Float height = camera.getHeight() - map.getTileRows()*map.getTileHeight();
		frame = new Rectangle(0.0f,0.0f, height, height, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown())
					isTouched = true;
				else if (pSceneTouchEvent.isActionMove()) {
					isMoved = true;
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		frame.setColor(Color.WHITE);
		sprite = new Sprite(0,0, region, vbom);
	}
}
