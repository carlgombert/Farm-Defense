package model;

import java.awt.Graphics;
import java.awt.Rectangle;

import controller.objectHandling.ID;

/**
 * The GameObject class serves as a superclass of all the in game objects
 */
public abstract class GameObject {
	
	// object identifier
	protected ID id;
	
	// boolean signifying if the object is colliding with a tile or not
	protected boolean XtileCollision;
	protected boolean YtileCollision;

	// world coordinates represent the position of the object on the broader map
	protected int worldX, worldY;
	protected int speedX, speedY;
	// screen coordinates represent where the object should be rendered
	protected int screenX, screenY;
	
	// the direction the object is facing
	protected int direction = 0;
	/*
	 * front: 0
	 * back: 1
	 * left: 2
	 * right: 3
	 */
	
	public GameObject(int x, int y, ID id) {
		this.worldX = x;
		this.worldY = y;
		this.id = id;
	}
	
	public int getWorldX() {
		return worldX;
	}

	public void setWorldX(int worldX) {
		this.worldX = worldX;
	}

	public int getWorldY() {
		return worldY;
	}

	public void setWorldY(int worldY) {
		this.worldY = worldY;
	}

	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract Rectangle getBounds();
	public abstract Rectangle getSize();
	
	
	public void setId(ID id) {
		this.id = id;
	}
	public ID getId() {
		return id;
	}
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}
	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
	public int getSpeedX() {
		return speedX;
	}
	public int getSpeedY() {
		return speedY;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getScreenX() {
		return screenX;
	}

	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}
	
	public boolean isXTileCollision() {
		return XtileCollision;
	}

	public void setXTileCollision(boolean XtileCollision) {
		this.XtileCollision = XtileCollision;
	}
	
	public boolean isYTileCollision() {
		return YtileCollision;
	}

	public void setYTileCollision(boolean YtileCollision) {
		this.YtileCollision = YtileCollision;
	}
}
