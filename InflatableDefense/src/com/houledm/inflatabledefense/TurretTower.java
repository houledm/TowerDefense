package com.houledm.inflatabledefense;

import org.andengine.opengl.texture.region.ITiledTextureRegion;

public class TurretTower extends BaseAnimatedTower {
	
	public static final float SCOPE = 60.0f;
	private static final float TIME_BETWEEN_SHOTS = 0.46f;
	private static final int POWER = 1;
	public static final Integer COST = 15;
	public static final boolean HAS_BULLETS = false; 
	public static final int TOTAL_ANIMATION_TIME = 430;
	public static final int START_FRAME = 0;
	public static final int ANIMATION_COUNT = 4;

	public TurretTower(float pX, float pY, ITiledTextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion, SCOPE, TIME_BETWEEN_SHOTS, POWER, COST, HAS_BULLETS, START_FRAME, ANIMATION_COUNT);
	}
	
	@Override public void animate() {
		animate(new long[]{250,60,60,60}, startAnimationFrame, endAnimationFrame, true);
	}
	
	@Override public void upgrade() {
		super.upgrade();
		this.power *= 2;
	}
}
