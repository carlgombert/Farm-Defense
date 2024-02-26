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
	
	public static void addLight(int worldX, int worldY) {
		lightPositions.add(new int[]{worldX%48, worldY%48});
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
	
	public static int getLightDistance(int x, int y) {
		int distance = Game.mapCol;
		for(int i = 0; i < lightPositions.size(); i++) {
			distance = Math.min(
					(int) MathUtil.Distance(x, y, lightPositions.get(i)[0], lightPositions.get(i)[1]),
					distance);
		}
		return distance;
	}
}
