package view.map.tile;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import controller.Game;
import util.ImageUtil;
import util.MathUtil;
import view.map.LightManager;


// the games map is built as a grid of a bunch of different tiles.
public class TileManager {
	public static Tile[] tile;
	public static int mapTileNum[][];
	
	public TileManager () {
		
		tile = new Tile [30];
		
		mapTileNum = new int[Game.MAP_COL][Game.MAP_ROW];
		loadMap();
		
		getTileImage();
	}
	
	// sets map tile types to numbers
	public void getTileImage () {
		
		// we now have 3 different grass tiles for variation
		tile [0] = new Tile();
		tile [0].setImage(ImageUtil.addImage(48, 48, "resources/tiles/grass_1.png"));
		tile [14] = new Tile();
		tile [14].setImage(ImageUtil.addImage(48, 48, "resources/tiles/grass_2.png"));
		tile [15] = new Tile();
		tile [15].setImage(ImageUtil.addImage(48, 48, "resources/tiles/grass_3.png"));
		
		tile [1] = new Tile();
		tile [1].setImage(ImageUtil.addImage(48, 48, "resources/tiles/water.png"));
		tile [1].setCollision(true);
		
		tile [2] = new Tile();
		tile [2].setImage(ImageUtil.addImage(48, 48, "resources/tiles/bottom_left_water.png"));
		tile [2].setCollision(true);
		
		tile [3] = new Tile();
		tile [3].setImage(ImageUtil.addImage(48, 48, "resources/tiles/bottom_right_water.png"));
		tile [3].setCollision(true);
		
		tile [4] = new Tile();
		tile [4].setImage(ImageUtil.addImage(48, 48, "resources/tiles/bottom_water.png"));
		tile [4].setCollision(true);
		
		tile [5] = new Tile();
		tile [5].setImage(ImageUtil.addImage(48, 48, "resources/tiles/left_water.png"));
		tile [5].setCollision(true);
		
		tile [6] = new Tile();
		tile [6].setImage(ImageUtil.addImage(48, 48, "resources/tiles/right_water.png"));
		tile [6].setCollision(true);
		
		tile [7] = new Tile();
		tile [7].setImage(ImageUtil.addImage(48, 48, "resources/tiles/top_left_water.png"));
		tile [7].setCollision(true);
		
		tile [8] = new Tile();
		tile [8].setImage(ImageUtil.addImage(48, 48, "resources/tiles/top_right_water.png"));
		tile [8].setCollision(true);
		
		tile [9] = new Tile();
		tile [9].setImage(ImageUtil.addImage(48, 48, "resources/tiles/top_water.png"));
		tile [9].setCollision(true);
		
		tile [10] = new Tile();
		tile [10].setImage(ImageUtil.addImage(48, 48, "resources/tiles/bottom_left_corner_water.png"));
		tile [10].setCollision(true);
		
		tile [11] = new Tile();
		tile [11].setImage(ImageUtil.addImage(48, 48, "resources/tiles/bottom_right_corner_water.png"));
		tile [11].setCollision(true);
		
		tile [12] = new Tile();
		tile [12].setImage(ImageUtil.addImage(48, 48, "resources/tiles/brick.png"));
		tile [12].setCollision(true);
		
		tile [13] = new Tile();
		tile [13].setImage(ImageUtil.addImage(48, 48, "resources/tiles/gravestone.png"));
		
		tile [16] = new Tile();
		tile [16].setImage(ImageUtil.addImage(48, 48, "resources/tiles/store/wall.png"));
		tile [16].setCollision(true);
		
		tile [17] = new Tile();
		tile [17].setImage(ImageUtil.addImage(48, 48, "resources/tiles/store/roof.png"));
		tile [17].setCollision(true);
		
		tile [18] = new Tile();
		tile [18].setImage(ImageUtil.addImage(48, 48, "resources/tiles/store/window_bottom_left.png"));
		tile [18].setCollision(true);
		tile [18].setStore(true);
		
		tile [19] = new Tile();
		tile [19].setImage(ImageUtil.addImage(48, 48, "resources/tiles/store/window_bottom_right.png"));
		tile [19].setCollision(true);
		tile [19].setStore(true);
		
		tile [20] = new Tile();
		tile [20].setImage(ImageUtil.addImage(48, 48, "resources/tiles/store/window_top_left.png"));
		tile [20].setCollision(true);
		
		tile [21] = new Tile();
		tile [21].setImage(ImageUtil.addImage(48, 48, "resources/tiles/store/window_top_right.png"));
		tile [21].setCollision(true);
		
	}
	
	//loads in map as txt files and reads in numbers as their specified tile type
	public void loadMap() {
		try {
			URL url = getClass().getClassLoader().getResource("resources/maps/map1.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			int col = 0;
			int row = 0;
			while (col < Game.MAP_COL && row < Game.MAP_ROW) {
				String line = br.readLine ();
				while (col < Game.MAP_COL) {
					String numbers[] = line.split("\t");
					int num = Integer.parseInt (numbers[col]);
					if(num == 0) {
						// janky way to generate random grass tile
						num = (new int[]{0, 14, 15})[MathUtil.randomNumber(0, 2)];
					}
					mapTileNum[col][row] = num;
					col++;
				}
				if (col == Game.MAP_COL) {
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
		while (col < Game.MAP_COL && row < Game.MAP_ROW) {
			int tileNum = mapTileNum[col][row];
			
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
				g.drawImage(tile[tileNum].getImage(), screenX, screenY, 48, 48, null);
			}
			
			col++;
			
			if (col == Game.MAP_COL) {
				col = 0;
				row++;
			}
		}
	}
	
	// filter to put over map at night
	public void renderNight (Graphics g) {
		int col = 0;
		int row = 0;
		while (col < Game.MAP_COL && row < Game.MAP_ROW) {
			
			int worldX = col * 48;
			int worldY = row * 48;
			
			int screenX = worldX - Game.player.getWorldX() + Game.player.getScreenX();
			int screenY = worldY - Game.player.getWorldY() + Game.player.getScreenY();
			
			// if the tile is outside of the players view, do not render.
			if(worldX > Game.player.getWorldX() - Game.WIDTH &&
					worldX < Game.player.getWorldX() + Game.WIDTH &&
					worldY > Game.player.getWorldY() - Game.HEIGHT &&
					worldY < Game.player.getWorldY() + Game.HEIGHT) {
				
				//get an opacity value for the tile relative to the distance from light
				int alpha = LightManager.getLightDistance(col, row) * 50; 
				
				//make sure opacity doesn't exceed limit
				alpha = MathUtil.clamp((int)(alpha * 0.75), 0, 230);
				
				//create dark brown color with opacity
				Color color = new Color(27, 5, 0, alpha);
				g.setColor(color);
				
				//overlay the current tile with the color
				g.fillRect(screenX, screenY, 48, 48);
			}
			
			col++;
			
			if (col == Game.MAP_COL) {
				col = 0;
				row++;
			}
		}
	}
}
