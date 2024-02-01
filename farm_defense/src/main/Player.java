package main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import map.TileManager;
import util.ImageUtil;
import util.MathUtil;
import util.TileUtil;

public class Player extends GameObject{
	
	HashMap<Integer, BufferedImage[]> playerImages = new HashMap<Integer, BufferedImage[]>();
	
	public Image currImage;
	
	private int stepTimer = 0;
	private int step = 0;
	
	// variables used when get hit by a zombie
	private boolean hitByZombie = false;
	private double angleFromZombie;
	private int hitTimer = 0;
	
	public Player(ID id) {
		super(400, 200, id);
		
		BufferedImage[] front = {ImageUtil.addImage(75, 75, "resources/player/front_right.png"), ImageUtil.addImage(75, 75, "resources/player/front_left.png")};
		BufferedImage[] back = {ImageUtil.addImage(75, 75, "resources/player/back_right.png"), ImageUtil.addImage(75, 75, "resources/player/back_left.png")};
		BufferedImage[] left = {ImageUtil.addImage(75, 75, "resources/player/left_right.png"), ImageUtil.addImage(75, 75, "resources/player/left_left.png")};
		BufferedImage[] right = {ImageUtil.addImage(75, 75, "resources/player/right_right.png"), ImageUtil.addImage(75, 75, "resources/player/right_left.png")};
		
		playerImages.put(0, front);
		playerImages.put(1, back);
		playerImages.put(2, left);
		playerImages.put(3, right);
		
		currImage = playerImages.get(0)[0];
		
		setScreenX((Game.WIDTH / 2) - (75/2));
		setScreenY((Game.HEIGHT / 2) - (75/2));
		
		worldX = 20*48;
		worldY = 20*48;
	}
	
	public void render(Graphics g) {
		g.drawImage(currImage, (int) Math.round(getScreenX()), (int) Math.round(getScreenY()), null);
		
	}
	
	public void tick() {
		tileCollision = false;
		TileUtil.checkTileCollision(this);
		if(!tileCollision && !hitByZombie) {
			worldX += speedX;
			worldY += speedY;
        }
		else if (hitByZombie) // when player gets hit by zombie, 
		{
			if (hitTimer < 25)
			{
				worldX += (Math.round(Math.cos(angleFromZombie)) * 3);
				worldY += (Math.round(Math.sin(angleFromZombie)) * 3);
				hitTimer++;
			}
			else
			{
				hitByZombie = false;
				hitTimer = 0;
			}
		}
		
		currImage = playerImages.get(super.getDirection())[step];
		if((speedX != 0 || speedY != 0) && !tileCollision) {
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
			if(speedY < 0) {super.setDirection(1);}
			else if(speedY > 0) {super.setDirection(0);}
			else if(speedX < 0) {super.setDirection(2);}
			else if(speedX > 0) {super.setDirection(3);}
		}
		
	}
	
	private void collision() {
		for(int i = 0; i < Game.handler.object.size(); i++) {
			GameObject tempObject = Game.handler.object.get(i);
			
			if(getBounds().intersects(tempObject.getBounds())){
				if(tempObject.getId() == ID.Zombie) 
				{
					// Zombie collision
				}
			}
		}
	}
	
	public void zombieCollision(GameObject zombie)
	{
		// calculates the angle from the zombie, which will help calculate the direction the player gets hit
		angleFromZombie = MathUtil.angleBetweenPoints(zombie.getWorldX(), zombie.getWorldY(), this.getWorldX(), this.getWorldY());
		
		// puts player into "hit by zombie" mode, which renders him unable to move and will bounce him back
		hitByZombie = true;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(20 + getScreenX(), 20 + getScreenY(), 60, 60);
	}

	public Rectangle getSize() {
		return new Rectangle(10, 10, 50, 60);
	}
}
	