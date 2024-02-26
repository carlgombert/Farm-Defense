package view.map.tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import controller.Game;
import model.Sound;
import util.ImageUtil;
import util.MathUtil;
import view.map.LightManager;


// the games map is built as a grid of a bunch of different tiles.
public class TileManager {
	public static Tile[] tile;
	public static int mapTileNum[][];
	
	public TileManager () {
		
		tile = new Tile [30];
		
		mapTileNum = new int[Game.mapCol][Game.mapRow];
		loadMap();
		
		getTileImage();
	}
	
	// sets map tile types to numbers
	public void getTileImage () {
		
		// we now have 3 different grass tiles for variation
		tile [0] = new Tile();
		tile [0].image = ImageUtil.addImage(48, 48, "resources/tiles/grass_1.png");
		tile [14] = new Tile();
		tile [14].image = ImageUtil.addImage(48, 48, "resources/tiles/grass_2.png");
		tile [15] = new Tile();
		tile [15].image = ImageUtil.addImage(48, 48, "resources/tiles/grass_3.png");
		
		tile [1] = new Tile();
		tile [1].image = ImageUtil.addImage(48, 48, "resources/tiles/water.png");
		tile [1].collision = true;
		
		tile [2] = new Tile();
		tile [2].image = ImageUtil.addImage(48, 48, "resources/tiles/bottom_left_water.png");
		tile [2].collision = true;
		
		tile [3] = new Tile();
		tile [3].image = ImageUtil.addImage(48, 48, "resources/tiles/bottom_right_water.png");
		tile [3].collision = true;
		
		tile [4] = new Tile();
		tile [4].image = ImageUtil.addImage(48, 48, "resources/tiles/bottom_water.png");
		tile [4].collision = true;
		
		tile [5] = new Tile();
		tile [5].image = ImageUtil.addImage(48, 48, "resources/tiles/left_water.png");
		tile [5].collision = true;
		
		tile [6] = new Tile();
		tile [6].image = ImageUtil.addImage(48, 48, "resources/tiles/right_water.png");
		tile [6].collision = true;
		
		tile [7] = new Tile();
		tile [7].image = ImageUtil.addImage(48, 48, "resources/tiles/top_left_water.png");
		tile [7].collision = true;
		
		tile [8] = new Tile();
		tile [8].image = ImageUtil.addImage(48, 48, "resources/tiles/top_right_water.png");
		tile [8].collision = true;
		
		tile [9] = new Tile();
		tile [9].image = ImageUtil.addImage(48, 48, "resources/tiles/top_water.png");
		tile [9].collision = true;
		
		tile [10] = new Tile();
		tile [10].image = ImageUtil.addImage(48, 48, "resources/tiles/bottom_left_corner_water.png");
		tile [10].collision = true;
		
		tile [11] = new Tile();
		tile [11].image = ImageUtil.addImage(48, 48, "resources/tiles/bottom_right_corner_water.png");
		tile [11].collision = true;
		
		tile [12] = new Tile();
		tile [12].image = ImageUtil.addImage(48, 48, "resources/tiles/brick.png");
		tile [12].collision = true;
		
		tile [13] = new Tile();
		tile [13].image = ImageUtil.addImage(48, 48, "resources/tiles/gravestone.png");
		
		tile [16] = new Tile();
		tile [16].image = ImageUtil.addImage(48, 48, "resources/tiles/store/wall.png");
		tile [16].collision = true;
		
		tile [17] = new Tile();
		tile [17].image = ImageUtil.addImage(48, 48, "resources/tiles/store/roof.png");
		tile [17].collision = true;
		
		tile [18] = new Tile();
		tile [18].image = ImageUtil.addImage(48, 48, "resources/tiles/store/window_bottom_left.png");
		tile [18].collision = true;
		tile [18].store = true;
		
		tile [19] = new Tile();
		tile [19].image = ImageUtil.addImage(48, 48, "resources/tiles/store/window_bottom_right.png");
		tile [19].collision = true;
		tile [19].store = true;
		
		tile [20] = new Tile();
		tile [20].image = ImageUtil.addImage(48, 48, "resources/tiles/store/window_top_left.png");
		tile [20].collision = true;
		
		tile [21] = new Tile();
		tile [21].image = ImageUtil.addImage(48, 48, "resources/tiles/store/window_top_right.png");
		tile [21].collision = true;
		
	}
	
	//loads in map as txt files and reads in numbers as their specified tile type
	public void loadMap() {
		try {
			URL url = getClass().getClassLoader().getResource("resources/maps/map1.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			int col = 0;
			int row = 0;
			while (col < Game.mapCol && row < Game.mapRow) {
				String line = br.readLine ();
				while (col < Game.mapCol) {
					String numbers[] = line.split("\t");
					int num = Integer.parseInt (numbers[col]);
					if(num == 0) {
						// janky way to generate random grass tile
						num = (new int[]{0, 14, 15})[MathUtil.randomNumber(0, 2)];
					}
					mapTileNum[col][row] = num;
					col++;
				}
				if (col == Game.mapCol) {
					col = 0;
					row++;
				}
			}
			br.close () ;
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public int[][] getMap()
	{
		return mapTileNum;
	}
	
	public void render (Graphics g) {
		int col = 0;
		int row = 0;
		while (col < Game.mapCol && row < Game.mapRow) {
			int tileNum = mapTileNum[col][row];
			int[][] buildingMap = Game.buildingManager.getBuildingMap();
			
			int worldX = col * 48;
			int worldY = row * 48;
			
			int screenX = worldX - Game.player.getWorldX() + Game.player.getScreenX();
			int screenY = worldY - Game.player.getWorldY() + Game.player.getScreenY();
			
			// if the tile is outside of the players view, do not render.
			if(worldX > Game.player.getWorldX() - Game.WIDTH &&
					worldX < Game.player.getWorldX() + Game.WIDTH &&
					worldY > Game.player.getWorldY() - Game.HEIGHT &&
					worldY < Game.player.getWorldY() + Game.HEIGHT/* &&
					buildingMap[col][row] == 0*/) 
			{
				g.drawImage(tile[tileNum].image, screenX, screenY, 48, 48, null);
			}
			
			col++;
			
			if (col == Game.mapCol) {
				col = 0;
				row++;
			}
		}
	}
	
	// filter to put over map. the map gets darker further from the player
	public void renderNightFade (Graphics g) {
		int col = 0;
		int row = 0;
		while (col < Game.mapCol && row < Game.mapRow) {
			int tileNum = mapTileNum[col][row];
			
			int worldX = col * 48;
			int worldY = row * 48;
			
			int screenX = worldX - Game.player.getWorldX() + Game.player.getScreenX();
			int screenY = worldY - Game.player.getWorldY() + Game.player.getScreenY();
			
			// if the tile is outside of the players view, do not render.
			if(worldX > Game.player.getWorldX() - Game.WIDTH &&
					worldX < Game.player.getWorldX() + Game.WIDTH &&
					worldY > Game.player.getWorldY() - Game.HEIGHT &&
					worldY < Game.player.getWorldY() + Game.HEIGHT) {
				// set color to yellow with %50 opacity. this will create a warm glow around the player
				Color color = new Color(225, 202, 0, 127);
				g.setColor(color);
				// overlay the yellow over the current tile
				g.fillRect(screenX, screenY, 48, 48);
				//create opacity for the dark brown tile that will get stronger the greater the distance the tile is
				//from the player
				int alpha = (int) MathUtil.Distance(worldX, worldY, Game.player.getWorldX(), Game.player.getWorldY()); 
				//make sure alpha doesn't exceed limit
				alpha = MathUtil.clamp((int)(alpha * 0.75), 0, 253);
				color = new Color(27, 5, 0, alpha);
				g.setColor(color);
				//overlay dark brown over current tile
				g.fillRect(screenX, screenY, 48, 48);
			}
			
			col++;
			
			if (col == Game.mapCol) {
				col = 0;
				row++;
			}
		}
	}
	
	// filter to put over map. the map is darker than normal
	public void renderNightConstant (Graphics g) {
		int col = 0;
		int row = 0;
		while (col < Game.mapCol && row < Game.mapRow) {
			int tileNum = mapTileNum[col][row];
			
			int worldX = col * 48;
			int worldY = row * 48;
			
			int screenX = worldX - Game.player.getWorldX() + Game.player.getScreenX();
			int screenY = worldY - Game.player.getWorldY() + Game.player.getScreenY();
			
			// if the tile is outside of the players view, do not render.
			if(worldX > Game.player.getWorldX() - Game.WIDTH &&
					worldX < Game.player.getWorldX() + Game.WIDTH &&
					worldY > Game.player.getWorldY() - Game.HEIGHT &&
					worldY < Game.player.getWorldY() + Game.HEIGHT) {
				
				//set color to a dark brown with an opacity of %50
				int alpha = LightManager.getLightDistance(col, row) * 50; 
				//make sure alpha doesn't exceed limit
				alpha = MathUtil.clamp((int)(alpha * 0.75), 0, 230);
				
				Color color = new Color(27, 5, 0, alpha);
				g.setColor(color);
				//pverlay the current tile with the color
				g.fillRect(screenX, screenY, 48, 48);
			}
			
			col++;
			
			if (col == Game.mapCol) {
				col = 0;
				row++;
			}
		}
	}
}
