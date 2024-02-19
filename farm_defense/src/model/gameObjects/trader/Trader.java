package model.gameObjects.trader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import controller.Game;
import controller.objectHandling.ID;
import model.GameObject;

public class Trader extends GameObject{
	public ArrayList<Item> items = new ArrayList<Item>();
	
	public Trader(int x, int y, ID id) {
		super(x, y, id);
		
		items.add(new Item("test1", 50));
		items.add(new Item("test2", 50));
		items.add(new Item("test3", 50));
	}

	public void tick() {
		setScreenX(worldX - Game.player.getWorldX() + Game.player.getScreenX());
		setScreenY(worldY - Game.player.getWorldY() + Game.player.getScreenY());
	}

	public void render(Graphics g) {
		g.setColor(Color.orange);
		g.drawRect(screenX, screenY, 50, 60);
	}

	
	public Rectangle getBounds() {
		return null;
	}

	
	public Rectangle getSize() {
		return null;
	}
}
