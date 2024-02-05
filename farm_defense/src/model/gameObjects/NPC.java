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
import view.map.TileManager;
import util.ImageUtil;
import util.MathUtil;
import util.TileUtil;

public class NPC extends GameObject
{
	private boolean talking = false;
	
	private String dialogue = "";
	private String currDialogue = "";
	private int currLetter = 0;
	
	public Image currImage;
	
	public NPC(int x, int y, ID id)
	{
		super(x, y, id);
		
		dialogue = "Hi! I am an NPC! Nice to meet you!";
		currImage = ImageUtil.addImage(75, 75, "resources/npc/cowboy_front.png");
	}

	public void tick() 
	{
		if (talking)
		{
			
		}
	}

	public void render(Graphics g) 
	{
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

	@Override
	public Rectangle getBounds() 
	{
		return new Rectangle(20 + getScreenX(), 20 + getScreenY(), 60, 60);
	}

	@Override
	public Rectangle getSize() 
	{
		return new Rectangle(10, 10, 50, 60);
	}
}
