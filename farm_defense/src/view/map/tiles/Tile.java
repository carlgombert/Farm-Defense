package view.map.tiles;

import java.awt.image.BufferedImage;

public class Tile {
	
	public BufferedImage image;
	public boolean collision = false;
	public boolean store = false;
	
	public boolean isCollision() {
		return collision;
	}
	
	public boolean isStore() {
		return store;
	}
}
