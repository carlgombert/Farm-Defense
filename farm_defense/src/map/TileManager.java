package map;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import main.Game;
import util.ImageUtil;


// the games map is built as a grid of a bunch of different tiles.
public class TileManager {
	public static Tile[] tile;
	public static int mapTileNum[][];
	
	public TileManager () {
		
		tile = new Tile [20];
		
		mapTileNum = new int[Game.mapCol][Game.mapRow];
		loadMap();
		
		getTileImage();
	}
	
	// sets map tile types to numbers
	public void getTileImage () {
		
		tile [0] = new Tile();
		tile [0].image = ImageUtil.addImage(48, 48, "resources/tiles/grass.png");
		
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
		
	}
	
	//loads in map as txt files and reads in numbers as their specified tile type
	public void loadMap() {
		try {
			File file = new File("resources/maps/map1.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			int col = 0;
			int row = 0;
			while (col < Game.mapCol && row < Game.mapRow) {
				String line = br.readLine ();
				while (col < Game.mapCol) {
					String numbers[] = line.split("\t");
					int num = Integer.parseInt (numbers[col]);
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
	
	public void render (Graphics g) {
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
				g.drawImage(tile[tileNum].image, screenX, screenY, 48, 48, null);
			}
			
			col++;
			
			if (col == Game.mapCol) {
				col = 0;
				row++;
			}
		}
	}
}
