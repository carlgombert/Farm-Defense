package main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import util.ImageUtil;

public class Zombie extends GameObject{
	
	HashMap<Integer, BufferedImage[]> zombieImages = new HashMap<Integer, BufferedImage[]>();
	
	public Image currImage;
	
	private int stepTimer = 0;
	private int step = 0;
	
	private int speed = 1;

	public Zombie(int x, int y, ID id) {
		super(x, y, id);
		
		BufferedImage[] front = {ImageUtil.addImage(75, 75, "res/baby/front_right.png"), ImageUtil.addImage(75, 75, "res/baby/front_left.png")};
		BufferedImage[] back = {ImageUtil.addImage(75, 75, "res/baby/back_right.png"), ImageUtil.addImage(75, 75, "res/baby/back_left.png")};
		BufferedImage[] left = {ImageUtil.addImage(75, 75, "res/baby/left_right.png"), ImageUtil.addImage(75, 75, "res/baby/left_left.png")};
		BufferedImage[] right = {ImageUtil.addImage(75, 75, "res/baby/right_right.png"), ImageUtil.addImage(75, 75, "res/baby/right_left.png")};
		
		
		zombieImages.put(0, front);
		zombieImages.put(1, back);
		zombieImages.put(2, left);
		zombieImages.put(3, right);
		
		currImage = zombieImages.get(0)[0];
		
	}

	public void tick() {
		// TODO add zombie functionaility
		
	}

	public void render(Graphics g) {
		// TODO add to method so image is only drawn if the zombie is on the players screen
		g.drawImage(currImage, (int) Math.round(getScreenX()), (int) Math.round(getScreenY()), null);
		
	}
	
	private void collision() {
		for(int i = 0; i < Game.handler.object.size(); i++) {
			GameObject tempObject = Game.handler.object.get(i);
			
			if(getBounds().intersects(tempObject.getBounds())){
				
			}
		}
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(getScreenX() + 20 , getScreenY() + 20, 60, 60);
	}

	@Override
	public Rectangle getSize() {
		return new Rectangle(20, 20, 60, 60);
	}

}
