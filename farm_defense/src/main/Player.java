package main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Player extends GameObject{
	
	HashMap<Integer, BufferedImage[]> playerImages = new HashMap<Integer, BufferedImage[]>();
	
	public Image currImage;
	
	private int stepTimer = 0;
	private int step = 0;
	
	public Player(ID id) {
		super(400, 200, id);
		
		BufferedImage[] front = {Game.addImage(75, 75, "resources/player/front_right.png"), Game.addImage(75, 75, "resources/player/front_left.png")};
		BufferedImage[] back = {Game.addImage(75, 75, "resources/player/back_right.png"), Game.addImage(75, 75, "resources/player/back_left.png")};
		BufferedImage[] left = {Game.addImage(75, 75, "resources/player/left_right.png"), Game.addImage(75, 75, "resources/player/left_left.png")};
		BufferedImage[] right = {Game.addImage(75, 75, "resources/player/right_right.png"), Game.addImage(75, 75, "resources/player/right_left.png")};
		
		playerImages.put(0, front);
		playerImages.put(1, back);
		playerImages.put(2, left);
		playerImages.put(3, right);
		
		currImage = playerImages.get(0)[0];
		
		setScreenX((Game.WIDTH / 2) - (75/2));
		setScreenY((Game.HEIGHT / 2) - (75/2));
		
		worldX = 66*48;
		worldY = 36*48;
	}
	
	public void render(Graphics g) {
		g.drawImage(currImage, (int) Math.round(getScreenX()), (int) Math.round(getScreenY()), null);
		
	}
	
	public void tick() {
		worldX+= speedX;
		worldY+= speedY;
		currImage = playerImages.get(super.getDirection())[step];
		if(speedX != 0 || speedY != 0) {
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
	
	public Rectangle getBounds() {
		return new Rectangle(20 + getScreenX(), 20 + getScreenY(), 60, 60);
	}
}
	