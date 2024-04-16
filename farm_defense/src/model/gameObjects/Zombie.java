package model.gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import controller.Game;
import controller.objectHandling.ID;
import model.GameObject;
import model.Sound;
import util.ImageUtil;
import util.MathUtil;
import util.TileUtil;
import view.map.farming.FarmingManager;

/**
 * The Zombie class represents the main enemy in the game. The zombie will move in the direction
 * of a target and do damage when they touch. the zombie will be removed when it takes a certain
 * amount of damage
 */
public class Zombie extends GameObject{
	
	HashMap<Integer, BufferedImage[]> zombieImages = new HashMap<Integer, BufferedImage[]>();
	
	public Image currImage;
	
	private double health = 10.0;
	
	private int stepTimer = 0;
	private int step = 0;
	
	private int eatTimer = 0;
	
	// sound timer holds the number of ticks until the zombie will make a random noise
	private int soundTimer;
	
	private double targetAngle;
	
	private double speed = 1;
	private double doubleSpeedX; // double versions of speedx & y
	private double doubleSpeedY;
	
	private boolean cropSeeking = false;
	private boolean cropEating = false;
	
	private boolean destroyingBuilding = false;
	private int destroyBuildingCol;
	private int destroyBuildingRow;
	private long destroyBuildingCounter = 0;

	public Zombie(int x, int y, ID id) {
		super(x, y, id);
		
		soundTimer = MathUtil.randomNumber(100, 5000);
		
		BufferedImage[] front = {ImageUtil.addImage(75, 75, "resources/zombie/front_1.png"), ImageUtil.addImage(75, 75, "resources/zombie/front_1.png"), ImageUtil.addImage(75, 75, "resources/zombie/front_2.png"), ImageUtil.addImage(75, 75, "resources/zombie/front_2.png")};
		BufferedImage[] back = {ImageUtil.addImage(75, 75, "resources/zombie/back_1.png"), ImageUtil.addImage(75, 75, "resources/zombie/back_1.png"), ImageUtil.addImage(75, 75, "resources/zombie/back_2.png"), ImageUtil.addImage(75, 75, "resources/zombie/back_2.png")};
		BufferedImage[] left = {ImageUtil.addImage(75, 75, "resources/zombie/left_1.png"), ImageUtil.addImage(75, 75, "resources/zombie/left_2.png"), ImageUtil.addImage(75, 75, "resources/zombie/left_3.png"), ImageUtil.addImage(75, 75, "resources/zombie/left_4.png")};
		BufferedImage[] right = {ImageUtil.addImage(75, 75, "resources/zombie/right_1.png"), ImageUtil.addImage(75, 75, "resources/zombie/right_2.png"), ImageUtil.addImage(75, 75, "resources/zombie/right_3.png"), ImageUtil.addImage(75, 75, "resources/zombie/right_4.png")};
		
		
		zombieImages.put(0, front);
		zombieImages.put(1, back);
		zombieImages.put(2, left);
		zombieImages.put(3, right);
		
		currImage = zombieImages.get(0)[0];
		
		speed = 1 + Game.nightCount/10;
		health = 10 + 2*Game.nightCount;
		
		if(MathUtil.randomNumber(0, 100) < 50) {
			cropSeeking = true;
		}
		
	}

	public void tick() 
	{
		// if the zombie's health is below zero, kill it
		if (health <= 0)
		{
			Game.handler.removeObject(this);
		}
		
		XtileCollision = false;
		YtileCollision = false;
		
		soundTimer--;
		
		if(soundTimer == 0) {
			soundTimer = MathUtil.randomNumber(100, 5000);
			Sound.zombieSound();
		}
		
		int targetX = Game.player.getWorldX();
		int targetY = Game.player.getWorldY();
		
		if(cropSeeking && !cropEating) {
			Rectangle targetCrop = FarmingManager.closestCrop(worldX, worldY);
			if(targetCrop != null) {
				targetX = targetCrop.x-24;
				targetY = targetCrop.y-24;
			}
			else {
				cropSeeking = false;
				cropEating = false;
			}
			if(MathUtil.Distance(worldX, worldY, targetX, targetY) == 0) {
				cropSeeking = false;
				cropEating = true;
			}
		}
		
		if(cropEating && !cropSeeking) {
			Rectangle targetCrop = FarmingManager.closestCrop(worldX, worldY);
			if(targetCrop == null) {
				cropSeeking = true;
				cropEating = false;
			}
			else if(MathUtil.Distance(worldX, worldY, targetCrop.x-24, targetCrop.y-24) != 0) {
				cropSeeking = true;
				cropEating = false;
			}
			else {
				eatTimer++;
				if(eatTimer > 15) {
					FarmingManager.attackCrop(worldX+24, worldY+24);
					eatTimer = 0;
				}
			}
		}
		
		// if the zombie is in the same x plane as the character, the angle between
		// points function will try to divide by zero
		if(!cropEating) 
		{
			
			targetAngle = MathUtil.angleBetweenPoints(this.getWorldX(), this.getWorldY(), targetX, targetY);
			
			if((speed * Math.sin(targetAngle)) <= -0.49) {super.setDirection(1);}
			else if((speed * Math.sin(targetAngle)) >= 0.49) {super.setDirection(0);}
			else if((speed * Math.cos(targetAngle)) < 0) {super.setDirection(2);}
			else if((speed * Math.cos(targetAngle)) > 0) {super.setDirection(3);}
			
			targetAngle = zombieCollsion(targetAngle);
					
			doubleSpeedX = (speed * Math.cos(targetAngle));
			doubleSpeedY = (speed * Math.sin(targetAngle));
			
			speedX = (int)Math.round(doubleSpeedX);
			speedY = (int)Math.round(doubleSpeedY);
		}
		else 
		{
			speedX = 0;
			speedY = 0;
		}
		
		TileUtil.checkTileCollision(this);
		Game.buildingManager.checkBuildingCollision(this);
		
		if (!XtileCollision && !destroyingBuilding && !cropEating)
			{
				worldX += speedX;
			}
		if (!YtileCollision && !destroyingBuilding && !cropEating) 
			{
				worldY += speedY;
			}
		
		if (destroyingBuilding)
		{
			if (destroyBuildingCounter == 0) destroyBuildingCounter = System.currentTimeMillis();
			
			if (destroyBuildingCounter + 2000 < System.currentTimeMillis())
			{
				Game.buildingManager.damageBuilding(destroyBuildingCol, destroyBuildingRow, 10);
				destroyBuildingCounter = System.currentTimeMillis();
			}
		}
		else
		{
			destroyBuildingCounter = 0;
		}
		
		if ((speedX != 0 || speedY != 0) && !destroyingBuilding) 
		{
			stepTimer++;
			if(stepTimer > 10) {
				if(step < 3) {
					step++;
				}
				else {
					step = 0;
				}
				stepTimer = 0;
			}
		}
		
		currImage = zombieImages.get(super.getDirection())[step];
		
		// check to see if the zombie has collided with the player
		if(this.getBounds().intersects(Game.player.getBounds()))
		{
			Game.player.zombieCollision(this);
		}
	}

	public void render(Graphics g) {
		// only draws the zombie if it is on the players screen
		if(worldX > Game.player.getWorldX() - Game.WIDTH &&
				worldX < Game.player.getWorldX() + Game.WIDTH &&
				worldY > Game.player.getWorldY() - Game.HEIGHT &&
				worldY < Game.player.getWorldY() + Game.HEIGHT) 
		{
			setScreenX(worldX - Game.player.getWorldX() + Game.player.getScreenX());
			setScreenY(worldY - Game.player.getWorldY() + Game.player.getScreenY());
			
			g.drawImage(currImage, (int) Math.round(getScreenX()), (int) Math.round(getScreenY()), null);
			
			
		}
		
	}
	
	public Double zombieCollsion(double angle) {
		for(int i = 0; i < Game.handler.getObjects().size(); i++) {
			if(Game.handler.getObjects().get(i).getId() == ID.Zombie) {
				Zombie zom = (Zombie) Game.handler.getObjects().get(i);
				if(zom.getWorldX() != this.getWorldX() && zom.getWorldY() != this.getWorldY()) {
					if(this.getBounds().intersects(zom.getBounds())) {
						Rectangle intersection = this.getBounds().intersection(zom.getBounds());
						Double angle2 = MathUtil.angleBetweenPoints(this.getBounds().getCenterX(), this.getBounds().getCenterY(), intersection.getCenterX(), intersection.getCenterY());
						angle = angle2+Math.PI;
					}
				}
			}
		}
		return angle;
	}
	
	public double getHealth() {
		return health;
	}
	
	public void setHealth(double h) {
		health = h;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public int getSpeedX() {
		return speedX;
	}
	
	public int getSpeedY() {
		return speedY;
	}
	
	public void setDestroyingBuilding(boolean b, int col, int row)
	{
		destroyingBuilding = b;
		
		
		if (destroyingBuilding)
		{
			destroyBuildingCol = col;
			destroyBuildingRow = row;
		}
	}
	
	public boolean getDestroyingBuilding()
	{
		return destroyingBuilding;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(getScreenX() + 20 , getScreenY() + 20, 33, 30);
	}

	@Override
	public Rectangle getSize() {
		return new Rectangle(10, 10, 50, 60);
	}

}
