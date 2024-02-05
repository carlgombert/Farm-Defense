package model.gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import controller.Game;
import controller.objectHandling.ID;
import model.GameObject;

public class Projectile extends GameObject{
		
	private Color color = new Color(148, 141, 62);
	private double angle;
	private int speed = 20;
	
	private LinkedList<Zombie> zombies;
	
	public Projectile(int x, int y, ID id, double angle) {
		super(x, y, id);
		this.angle = angle;
	}

	public void tick() 
	{
		zombies = new LinkedList<Zombie>();
		
		for(int i = 0; i < Game.handler.object.size(); i++) 
		{
			GameObject tempObject = Game.handler.object.get(i);
			if (tempObject.getId() == ID.Zombie) 
			{
				zombies.add((Zombie)tempObject);
			}
		}
		
		for (int j = 0; j < zombies.size(); j++)
		{
			Zombie tempZombie = zombies.get(j);
			
			if(this.getBounds().intersects(tempZombie.getBounds()))
			{
					tempZombie.setHealth(tempZombie.getHealth() - 2.5);
					Game.handler.removeObject(this);
			}
		}
		
		worldX += (int) (speed * Math.cos(angle));
		worldY += (int) (speed * Math.sin(angle));
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
		return new Rectangle(this.getScreenX()-10, this.getScreenY()-10, 20, 20);
	}

	@Override
	public Rectangle getSize() {
		return new Rectangle(worldX-10, worldY-10, 20, 20);
	}

}
