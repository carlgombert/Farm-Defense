package model.gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import controller.Game;
import controller.objectHandling.ID;
import model.GameObject;

public class Projectile extends GameObject{
		
	private Color color = new Color(148, 141, 62);
	private double angle;
	private int speed = 20;
	
	public Projectile(int x, int y, ID id, double angle) {
		super(x, y, id);
		this.angle = angle;
	}

	public void tick() {
		worldX += (int) Math.round(speed * Math.cos(angle));
		worldY += (int) Math.round(speed * Math.sin(angle));
		setScreenX(worldX - Game.player.getWorldX() + Game.player.getScreenX());
		setScreenY(worldY - Game.player.getWorldY() + Game.player.getScreenY());
	}

	public void render(Graphics g) {
		if(worldX > Game.player.getWorldX() - Game.WIDTH &&
				worldX < Game.player.getWorldX() + Game.WIDTH &&
				worldY > Game.player.getWorldY() - Game.HEIGHT &&
				worldY < Game.player.getWorldY() + Game.HEIGHT) {
			g.setColor(Color.darkGray);
			g.fillOval(getScreenX(), getScreenY(), 8, 8);
		}
		else {
			Game.handler.removeObject(this);
		}
		
	}

	public Rectangle getBounds() {
		return new Rectangle(worldX-10, worldY-10, 20, 20);
	}

	@Override
	public Rectangle getSize() {
		return new Rectangle(worldX-10, worldY-10, 20, 20);
	}

}
