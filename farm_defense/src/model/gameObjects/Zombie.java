package model.gameObjects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import controller.Game;
import controller.objectHandling.ID;
import model.GameObject;
import util.ImageUtil;
import util.MathUtil;
import util.TileUtil;


public class Zombie extends GameObject{
	
	HashMap<Integer, BufferedImage[]> zombieImages = new HashMap<Integer, BufferedImage[]>();
	
	public Image currImage;
	
	private double health = 10.0;
	
	private int stepTimer = 0;
	private int step = 0;
	
	private double targetAngle;
	
	private int speed = 1;
	private double doubleSpeedX; // double versions of speedx & y
	private double doubleSpeedY;

	public Zombie(int x, int y, ID id) {
		super(x, y, id);
		
		BufferedImage[] front = {ImageUtil.addImage(75, 75, "resources/zombie/front_right.png"), ImageUtil.addImage(75, 75, "resources/zombie/front_left.png")};
		BufferedImage[] back = {ImageUtil.addImage(75, 75, "resources/zombie/back_right.png"), ImageUtil.addImage(75, 75, "resources/zombie/back_left.png")};
		BufferedImage[] left = {ImageUtil.addImage(75, 75, "resources/zombie/left_right.png"), ImageUtil.addImage(75, 75, "resources/zombie/left_left.png")};
		BufferedImage[] right = {ImageUtil.addImage(75, 75, "resources/zombie/right_right.png"), ImageUtil.addImage(75, 75, "resources/zombie/right_left.png")};
		
		
		zombieImages.put(0, front);
		zombieImages.put(1, back);
		zombieImages.put(2, left);
		zombieImages.put(3, right);
		
		currImage = zombieImages.get(0)[0];
		
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
		TileUtil.checkTileCollision(this);
		
		// if the zombie is in the same x plane as the character, the angle between
		// points function will try to divide by zero
		if (this.getWorldX() == Game.player.getWorldX() && !YtileCollision)
		{
			doubleSpeedX = 0;
			doubleSpeedY = speed * Math.signum(Game.player.getWorldY() - this.getWorldY());
			worldY += speedY;
		}
		else // finds closest angle to a player, will probably add another 
		{
			targetAngle = MathUtil.angleBetweenPoints(this.getWorldX(), this.getWorldY(), Game.player.getWorldX(), Game.player.getWorldY());
					
			doubleSpeedX = ((double)speed * Math.cos(targetAngle));
			doubleSpeedY = ((double)speed * Math.sin(targetAngle));
						
			speedX = (int)Math.round(doubleSpeedX);
			speedY = (int)Math.round(doubleSpeedY);
			
			if (!XtileCollision) worldX += speedX;
			if (!YtileCollision) worldY += speedY;
		}
		
		if ((speedX != 0 || speedY != 0)/* && !tileCollision*/) 
		{
			stepTimer++;
			if(stepTimer > 10) {
				if(step == 0) {
					step = 1;
				}
				else {
					step = 0;
				}
				stepTimer = 0;
			}
			if(speedY <= -0.01) {super.setDirection(1);}
			else if(speedY >= 0.01) {super.setDirection(0);}
			else if(speedX < 0) {super.setDirection(2);}
			else if(speedX > 0) {super.setDirection(3);}
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
			
			//show hitbox
			//g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
		}
		
	}
	
	private void collision() {
		for(int i = 0; i < Game.handler.object.size(); i++) {
			GameObject tempObject = Game.handler.object.get(i);
			
			if(getBounds().intersects(tempObject.getBounds())){
				
			}
		}
	}
	
	public double getHealth()
	{
		return health;
	}
	
	public void setHealth(double h)
	{
		health = h;
	}
	
	public int getSpeedX() {
		return speedX;
	}
	public int getSpeedY() {
		return speedY;
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
