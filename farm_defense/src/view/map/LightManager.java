package view.map;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import controller.Game;
import util.ImageUtil;
import util.MathUtil;

public class LightManager {
	private static ArrayList<int[]> lightPositions = new ArrayList<int[]>();
	private static BufferedImage torch = ImageUtil.addImage(48, 48, "resources/tiles/torch.png");
	
	/**
    * add a light to the map
    *
    * @param  x x coordinate of light position
    * @param  y y coordinate of light position
    */
	public static void addLight(int worldX, int worldY) {
		lightPositions.add(new int[]{(worldX-worldX%48)/48, (worldY-worldY%48)/48});
	}
	
	public static void render(Graphics g) {
		for(int i = 0; i < lightPositions.size(); i++) {
			
			int worldX = lightPositions.get(i)[0] * 48;
			int worldY = lightPositions.get(i)[1] * 48;
			
			int screenX = worldX - Game.player.getWorldX() + Game.player.getScreenX();
			int screenY = worldY - Game.player.getWorldY() + Game.player.getScreenY();
			
			if(worldX > Game.player.getWorldX() - Game.WIDTH &&
					worldX < Game.player.getWorldX() + Game.WIDTH &&
					worldY > Game.player.getWorldY() - Game.HEIGHT &&
					worldY < Game.player.getWorldY() + Game.HEIGHT) 
			{
				g.drawImage(torch, screenX, screenY, 48, 48, null);
			}
		}
	}
	
	/**
    * find the closest light to a given point
    *
    * @param  x x coordinate of given point (given as tile coordinate)
    * @param  y y coordinate of given point (given as tile coordinate)
    * @return         returns the distance of the closest light
    */
	public static int getLightDistance(int x, int y) {
		int distance = Game.MAP_COL;
		for(int i = 0; i < lightPositions.size(); i++) {
			distance = Math.min(
					(int) MathUtil.Distance(x, y, lightPositions.get(i)[0], lightPositions.get(i)[1]),
					distance);
		}
		if(Game.player.getWeaponState() == Game.player.stateTorch()) {
			distance = Math.min(
					(int) (MathUtil.Distance(x, y, Game.player.getWorldX()/48+1, Game.player.getWorldY()/48+1)*1.5),
					distance);
		}
		return distance;
	}
}
