package model.gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import controller.Game;
import controller.objectHandling.ID;
import model.GameObject;
import util.MathUtil;

public class Turret extends GameObject{
	
	private int range;
	private int bullets;
	
	private int turnTimer = 100;
	private int turnCount = 0;
	
	private double angle;
	
	private boolean targeted = false;
	
	private boolean shooting = false;
	private int shootTimer = 0;
	private int shootCount = 0;
	
	public Turret(int x, int y, ID id) {
		super(x, y, id);
		range = 300;
		bullets = 5;
	}

	public void tick() {
		if(shooting) {
			shootTimer++;
			if(shootTimer % 5 == 0) {
				shoot();
				shootCount += 1;
			}
			if(shootCount > bullets) {
				shooting = false;
				targeted = false;
				shootCount = 0;
				shootTimer = 0;
			}
		}
		else {
			turnCount++;
			if(turnCount > turnTimer) {
				lockToTarget();
				if(targeted) {
					shooting = true;
				}
				turnCount = 0;
			}
		}
		setScreenX(worldX - Game.player.getWorldX() + Game.player.getScreenX());
		setScreenY(worldY - Game.player.getWorldY() + Game.player.getScreenY());
	}

	public void render(Graphics g) {
		g.setColor(Color.darkGray);
		g.fillOval(screenX, screenY, 80, 80);
		
	}
	
	// lockToTarget chooses a target and changes the turret angle to face target
	public void lockToTarget() {
		Zombie target = chooseTarget();
		if(target != null) {
			int targetX = target.getWorldX() + ((target.getSize().x + target.getSize().width)/2);
			int targetY = target.getWorldY() + ((target.getSize().y + target.getSize().height)/2);
			angle = MathUtil.angleBetweenPoints(worldX+40, worldY+40, targetX, targetY);
			targeted = true;
		}
	}

	// chooseTarget looks for any zombies within range
	public Zombie chooseTarget() {
		for(int i =0; i < Game.handler.object.size(); i++) {
			GameObject tempObject = Game.handler.object.get(i);
			
			if(tempObject.getId() == ID.Zombie) {
				// if the distance between position and zombie is within range
				if(MathUtil.Distance(worldX, worldY, tempObject.getWorldX(), tempObject.getWorldY()) <= range) {
					return (Zombie) tempObject;
				}
			}
		}
		return null;
	}
	
	public void shoot() {
		Game.handler.addObject(new Projectile(worldX+40, worldY+40, ID.Projectile, angle));
	}
	
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getBullets() {
		return bullets;
	}

	public void setBullets(int bullets) {
		this.bullets = bullets;
	}

	public Rectangle getSize() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
