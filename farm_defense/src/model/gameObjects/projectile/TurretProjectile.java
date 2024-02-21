package model.gameObjects.projectile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.LinkedList;

import controller.Game;
import controller.objectHandling.ID;
import model.GameObject;
import model.gameObjects.Zombie;
import util.ImageUtil;

public class TurretProjectile extends Projectile{
	
	private Color color = new Color(148, 141, 62);
	private int speed = 20;
	
	private boolean hitZombie = false; 
	// hitZombie becomes true after projectile makes contact with zombie. this is used to start displaying
	// blood animation
	
	private Image bloodAnimation = ImageUtil.addImage(16, 16, "resources/animations/blood.png");
	//image for blood animation
	
	private LinkedList<Zombie> zombies;
	
	private int animationCount = 0;
	// used to limit how long the animation stays on screen
	
	public TurretProjectile(int x, int y, ID id, double angle) {
		super(x, y, id, angle);

	}

	public void tick() 
	{
		if(!hitZombie) { // If zombie has not been hit, continue to check for collisions and move bullet
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
					hitZombie = true;
				}
				if(this.getBounds().intersects(Game.player.getBounds()))
				{
					// don't get caught in the line of fire :)
					Game.player.setHealth(Game.player.getHealth() - 5);
					hitZombie = true;
				}
			}
		
			worldX += (int) (speed * Math.cos(super.angle));
			worldY += (int) (speed * Math.sin(super.angle));
		}
		setScreenX(worldX - Game.player.getWorldX() + Game.player.getScreenX());
		setScreenY(worldY - Game.player.getWorldY() + Game.player.getScreenY());
	}

	public void render(Graphics g) {
		if(!hitZombie) {
			if(worldX > Game.player.getWorldX() - Game.WIDTH &&
					worldX < Game.player.getWorldX() + Game.WIDTH &&
					worldY > Game.player.getWorldY() - Game.HEIGHT &&
					worldY < Game.player.getWorldY() + Game.HEIGHT) {
				g.setColor(Color.darkGray);
				g.fillOval(getScreenX(), getScreenY(), 8, 8);
			}
			else {
				// If projectile leaves screen, remove it
				Game.handler.removeObject(this);
			}
		}
		
		if(hitZombie) {
			
			g.drawImage(bloodAnimation, screenX-8, screenY-8, null);
			animationCount++;
			
			if(animationCount > 50) {
				Game.handler.removeObject(this);
			}
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
