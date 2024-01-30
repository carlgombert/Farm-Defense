package main;

import java.awt.Graphics;
import java.awt.Rectangle;

import util.MathUtil;

public class Turret extends GameObject{
	
	private int range;
	private int bullets;
	
	private int turnTimer = 100;
	private int turnCount = 0;
	
	private double angle;
	
	private boolean targeted = false;
	
	public Turret(int x, int y, ID id) {
		super(x, y, id);
	}

	public void tick() {
		turnCount++;
		if(turnCount > turnTimer) {
			lockToTarget();
			if(targeted) {
				shoot();
			}
			turnCount = 0;
		}
	}

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	// lockToTarget chooses a target and changes the turret angle to face target
	public void lockToTarget() {
		Zombie target = chooseTarget();
		if(!target.equals(null)) {
			angle = MathUtil.angleBetweenPoints(worldX, worldY, target.getWorldX(), target.getWorldY());
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
		// Fire bullets at angle
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
	
}
