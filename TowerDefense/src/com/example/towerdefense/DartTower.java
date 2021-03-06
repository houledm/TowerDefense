package com.example.towerdefense;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.modifier.IModifier;

import android.util.Log;

public class DartTower extends Tower{
	
	private static final float SCOPE = 100.0f;
	private static final float TIME_BETWEEN_SHOTS = 1.0f;
	private static final int POWER = 3;
	public static final Integer COST = 5;
	public static final boolean HAS_BULLETS = true;
	
	private DartBulletPool bulletPool;

	public DartTower(float pX, float pY,TextureRegion pTextureRegion) {
		super(pX, pY, SCOPE, TIME_BETWEEN_SHOTS, POWER, COST, HAS_BULLETS, pTextureRegion);
		bulletPool = new DartBulletPool(ResourceManager.getInstance().getDartBulletRegion());
	}
	
	@Override
	public void fireBullets (final Enemy e) {
		
		final DartBullet b = bulletPool.obtainPoolItem();
		b.setPosition((this.getX() + this.getWidthScaled()/2) - (b.getWidthScaled() + this.getWidthScaled()*3/2),
				(this.getY() + this.getHeightScaled()/2) - (b.getHeightScaled()/2 + this.getHeightScaled()/2));
		b.setRotation(this.getRotation());
		MoveModifier mmodify = new MoveModifier(DartBullet.SPEED, b.getX(), e.getX() - b.getWidthScaled()*2, b.getY(), e.getYReal() - e.getHeightScaled(),
				new IEntityModifier.IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
						
					}

					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier,
							IEntity pItem) {
						if (!e.isDead()) {
							Log.i("Shoot", "SHOOT "+e.getIndex());
							e.hit(POWER);
							checkForDeadEnemies(e);
						}
						bulletPool.recyclePoolItem(b);
						
					}
			
		});
		b.registerEntityModifier(mmodify);
		mmodify.setAutoUnregisterWhenFinished(true);
		
		Log.i("Dart Away", "Firing Dart");
		
		GameScene.getSharedInstance().attachChild(b);
	}
	
	public void random() {
		
	}

}
