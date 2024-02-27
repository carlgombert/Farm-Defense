package view.map.tile;

import java.awt.image.BufferedImage;

public class Tile {
	
	private BufferedImage image;
	private boolean collision = false;
	private boolean store = false;
	
	public void setCollision(boolean collision) {
		this.collision = collision;
	}
	
	public boolean isCollision() {
		return collision;
	}
	
	public void setStore(boolean store) {
		this.store = store;
	}
	
	public boolean isStore() {
		return store;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
}
