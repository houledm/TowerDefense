package com.example.towerdefense;

import java.util.concurrent.CopyOnWriteArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.modifier.IModifier;

import android.util.Log;

public class BaseAnimatedTower extends AnimatedSprite implements ITower {
	
	private RectangularShape entity;
	
	public float scope;
	private float timeBetweenShots;
	private int power;
	public Integer cost;
	
	private Rectangle sight;
	
	private Enemy lockedOn;
	
	protected CopyOnWriteArrayList<Enemy> queue;
	
	private boolean shooting;
	
	private int animationTime;
	private long startTime;
	
	public BaseAnimatedTower(float pX, float pY, ITiledTextureRegion pTextureRegion, float scope, float time, int power, int cost, int animationTime) {
		super(pX, pY, pTextureRegion, ResourceManager.getInstance().getVbom());
		this.scope = scope;
		this.timeBetweenShots = time;
		this.power = power;
		this.cost = cost;
		this.animationTime = animationTime;
		
		sight = new Rectangle(0,0,
				scope*2, scope*2, ResourceManager.getInstance().getVbom());
		sight.setPosition(this.getX() - sight.getWidthScaled()*10, this.getY());
		
		entity = this;
		entity.setZIndex(1);
		
		queue = new CopyOnWriteArrayList<Enemy>();
		
		shooting = false;
	}
	
	public void animate(){}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x,y);
		sight.setPosition((x + this.getWidthScaled()/2) - (sight.getWidthScaled()/2 - this.getWidthScaled() /2), 
				(y + this.getHeightScaled()/2) - (sight.getHeightScaled()/2 - this.getHeightScaled()/2));
		SubMenuManager.getReticle(this).setPosition(x-this.getWidthScaled()/3,y-this.getWidthScaled()/3);
		
	}

	@Override
	public boolean inSights(float x, float y) {
		return sight.contains(x, y);
	}

	@Override
	public boolean lockedOnInSight() {
		return inSights(lockedOn.getXReal(), lockedOn.getYReal());
	}
	
	@Override
	public void onImpact(Enemy enemy) {
		if (this.isAnimationRunning()) return;
		startTime = System.currentTimeMillis();
		animate();
	}
	
	@Override
	public void onEnemyOutOfRange(Enemy e){
		if (queue.contains(e)) queue.remove(e);
	}

	@Override
	public void onIdleInWave() {
		this.clearQueue();
		if (System.currentTimeMillis() - startTime >= animationTime) {
			this.setCurrentTileIndex(0);
			this.stopAnimation();
		}
	}

	@Override
	public void onWaveEnd() {
		this.clearQueue();
		this.setCurrentTileIndex(0);
		this.stopAnimation();
	}

	@Override
	public void hitEnemy(Enemy e) {
		e.hit(power);
	}

	@Override
	public Rectangle getSight() {
		return sight;
	}

	@Override
	public float getRadius() {
		return scope;
	}

	@Override
	public void setLockedOn(Enemy e) {
		lockedOn = e;
	}

	@Override
	public Enemy getLockedOn() {
		try {return queue.get(0);}
		catch(Exception e) {return null;}
	}

	@Override
	public Integer getCost() {
		return cost;
	}

	@Override
	public void shoot(final Enemy e) {		
		if (shooting) return;
		
		//animate();
		
		shooting = true;
		DelayModifier modifier = new DelayModifier(timeBetweenShots, new IEntityModifierListener() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				hitEnemy(e);
				checkForDeadEnemies(e);
			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {shooting = false;}
			
		});
		this.registerEntityModifier(modifier);
		modifier.setAutoUnregisterWhenFinished(true);
	}

	@Override
	public void checkForDeadEnemies(Enemy e) {
		synchronized(LOCK) {
			if (e.isDead() && e.getUserData()!="dead") {
				GameScene scene = GameScene.getSharedInstance();
				Log.i("Dead Enemy", "Enemy "+e.getIndex()+" is dead");
				scene.addAmount(e.getWorth());
				e.destroy();
				scene.incrementDeadCount();
				scene.seeIfWaveFinished();
			}
		}
	}

	@Override
	public RectangularShape getEntity() {
		return entity;
	}

	@Override
	public CopyOnWriteArrayList<Enemy> getQueue() {
		return this.queue;
	}
	
	@Override
	public void clearQueue() {
		queue.clear();
	}

	@Override
	public void addEnemyToQueue(Enemy enemy) {
		if (!queue.contains(enemy)) queue.add(enemy);
	}

	@Override
	public void removeEnemyFromQueue(Enemy enemy) {
		this.queue.remove(enemy);
	}
	
}
