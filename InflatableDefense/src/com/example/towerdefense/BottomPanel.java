package com.example.towerdefense;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.opengl.font.Font;
import org.andengine.util.color.Color;

public class BottomPanel extends HUD{
	
	private List<TowerTile> tiles;
	private float distanceFromMapToScene;
	private ZoomCamera mCamera;
	private Text money;
	private String moneyText;
	private Text wave;
	private String waveText;
	private Text life;
	private String lifeText;
	private Text towerTitle;
	private String towerTitleText;
	private Text towerCost;
	private String towerCostText;
	private Text towerUpgradeCost;
	private String towerUpgradeCostText;
	private Text towerDeleteCost;
	private String towerDeleteCostText;
	
	public BottomPanel(ZoomCamera camera, TMXTiledMap map) {
		super();
		
		tiles = new ArrayList<TowerTile>();
		
		this.mCamera = camera;
		
		distanceFromMapToScene = camera.getBoundsHeight() - (map.getTileRows()*map.getTileHeight());
		this.setPosition(0.0f, camera.getHeight()/3 - distanceFromMapToScene/2);

		camera.setHUD(this);
		
		ResourceManager resourceManager = ResourceManager.getInstance();
		GameScene scene = GameScene.getSharedInstance();
		
		Font font = resourceManager.getFont();
		
		moneyText = resourceManager.getActivity().getString(R.string.money_text);
		money = new Text(0, 0, font, "Money: $1234567890",
				resourceManager.getVbom());
		money.setText(moneyText.concat(scene.getMoney()+""));
		money.setScale(0.7f);
		money.setPosition(mCamera.getBoundsWidth()/2 + money.getWidthScaled()*3/4, mCamera.getBoundsHeight()*3/4 - money.getHeightScaled()*1.3f);
		this.attachChild(money);
		
		lifeText = resourceManager.getActivity().getString(R.string.life_text);
		life = new Text(0, 0, font, "Lives: 123",
				resourceManager.getVbom());
		life.setText(lifeText+" "+scene.getLives());
		life.setScale(0.7f);
		life.setPosition(mCamera.getBoundsWidth()*2/3 + life.getWidthScaled(), -1* mCamera.getBoundsHeight() + life.getHeightScaled()*3.5f);
		life.setColor(Color.WHITE);
		this.attachChild(life);
		
		waveText = resourceManager.getActivity().getString(R.string.wave_text);
		wave = new Text(0, 0, font, "Wave: 123",
				resourceManager.getVbom());
		wave.setText(waveText+" "+scene.getWaveCount());
		wave.setScale(0.7f);
		wave.setPosition(life);
		wave.setX(this.getX()+mCamera.getBoundsWidth()/3 + wave.getWidthScaled()/2);
		this.attachChild(wave);
		
		towerTitleText = "Turret Tower";
		towerTitle = new Text(0,0,font,"Whachamausit Amazing Tower", resourceManager.getVbom());
		towerTitle.setScale(0.7f);
		towerTitle.setPosition(money);
		towerTitle.setX(towerTitle.getX() - money.getWidthScaled() * 1.4f);
		
		towerCostText = "$10";
		towerCost = new Text(0,0,font,"Cost: $123456789",resourceManager.getVbom());
		towerCost.setPosition(towerTitle);
		towerCost.setX(towerCost.getX() + towerTitle.getWidthScaled()/4);
		towerCost.setY(towerCost.getY() + money.getHeightScaled()*2/3 + money.getHeightScaled()/4);
		
		towerUpgradeCostText = "Upgrade: N/A";
		towerUpgradeCost = new Text(0,0,font,"Upgrade: $123456789", resourceManager.getVbom());
		towerUpgradeCost.setScale(0.7f);
		towerUpgradeCost.setPosition(towerTitle);
		
		towerDeleteCostText = "Remove: $10";
		towerDeleteCost = new Text(0,0,font,"Remove: $123456789", resourceManager.getVbom());
		towerDeleteCost.setScale(0.7f);
		towerDeleteCost.setPosition(towerUpgradeCost);
		towerDeleteCost.setY(towerCost.getY());
		
	}
	
	public void placeTowerAccess(TowerTile tile, int count) {
		tiles.add(tile);
		
		tile.returnOnTouched();
		
		if (count > 1) {
			Line line = new Line(0,0,0,0,TowerDefenseActivity.getSharedInstance().getVertexBufferObjectManager());
			line.setColor(Color.BLACK);
			line.setLineWidth(10.0f);
			line.setPosition(((count-1)*tile.getFrame().getWidthScaled()) + line.getLineWidth()/4.9f, this.getY() + tile.getFrame().getHeightScaled(),
					((count-1)*tile.getFrame().getWidthScaled()) + line.getLineWidth()/4.9f, this.getY());
			this.attachChild(line);
		}
		
		tile.getFrame().setPosition((count-1)*tile.getFrame().getWidthScaled(), this.getY());
		tile.getSprite().setPosition(tile.getFrame());
		
		this.attachChild(tile.getFrame());
		this.attachChild(tile.getSprite());
		
		this.registerTouchArea(tile.getFrame());
	}
	
	public void placeStartButton(Sprite button) {
		this.attachChild(button);
		this.registerTouchArea(button);
		button.setPosition(mCamera.getBoundsWidth() - button.getWidthScaled()*1.55f, this.getY() - button.getHeightScaled() /1.8f);
	}
	
	public void attachTowerTextDescription(Class<?> T) {
		if (T == TurretTower.class) {
			towerTitleText = "Turret Tower";
			towerCostText = "$"+TurretTower.COST;
		}
		else if (T == IceTower.class) {
			towerTitleText = "Freeze Tower";
			towerCostText = "$"+IceTower.COST;
		}
		else if (T == DartTower.class) {
			towerTitleText = "Dart Tower";
			towerCostText = "$"+DartTower.COST;
		}
		else if (T == FlameTower.class) {
			towerTitleText = "Flame Tower";
			towerCostText = "$"+FlameTower.COST;
		}
		else return;
		
		towerTitle.setText(towerTitleText);
		towerCost.setText(towerCostText);
		this.attachChild(towerTitle);
		this.attachChild(towerCost);
	}
	public void detachTowerTextDescription() {
		towerTitle.detachSelf();
		towerCost.detachSelf();
	}
	public void attachTowerUpgradeDeleteText(ITower tower) {
		towerDeleteCostText = "Remove: $"+(int)(tower.getCost()*0.80);
		towerDeleteCost.setText(towerDeleteCostText);
		
		towerUpgradeCost.setText(towerUpgradeCostText);
		
		this.attachChild(towerDeleteCost);
		this.attachChild(towerUpgradeCost);
	}
	public void detachTowerUpgradeDeleteText() {
		towerUpgradeCost.detachSelf();
		towerDeleteCost.detachSelf();
	}
	
	public List<TowerTile> getTiles() {
		return tiles;
	}
	public void setMoneyText(Integer text) {
		money.setText(moneyText+text);
	}
	public void setWaveText(Integer text) {
		wave.setText(waveText+" "+text);
	}
	public void setLifeText(Integer text) {
		life.setText(lifeText+" "+text);
	}

}
