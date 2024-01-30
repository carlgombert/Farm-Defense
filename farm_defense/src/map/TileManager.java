package map;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import main.Game;


// the games map is built as a grid of a bunch of different tiles.
public class TileManager {
	Tile [] tile;
	int mapTileNum[][];
	
	public TileManager () {
		
		tile = new Tile [10];
		
		mapTileNum = new int[Game.mapCol][Game.mapRow];
		loadMap();
		
		getTileImage();
	}
	
	// sets map tile types to numbers
	public void getTileImage () {
		
		tile [0] = new Tile();
		tile [0].image = Game.addImage(48, 48, "resources/tiles/grass.png");
		
		tile [1] = new Tile();
		tile [1].image = Game.addImage(48, 48, "resources/tiles/water.png");
		
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
