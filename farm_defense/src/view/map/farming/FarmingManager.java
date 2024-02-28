package view.map.farming;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.net.URL;

import controller.Game;
import util.ImageUtil;
import util.TxtFileUtil;

public class FarmingManager 
{
	private static Crop[] crop;
	private static int mapFarmland[][]; // map of all farmland in the map - 0: nothing, 1: empty farmland, any other number: crop (# is crop id)
	private static int mapCropStage[][]; // map of the current stage of the crop in that tile - 0: no crop, 1: stage 1, etc.
	
	private static int mouseX;
	private static int mouseY;
	private static int mouseWorldX;
	private static int mouseWorldY;
	
	private BufferedImage farmlandImage;
	
	private int numCrops = 0;
	
	public FarmingManager()
	{
		crop = new Crop[40];
		mapFarmland = new int[Game.MAP_COL][Game.MAP_ROW];
		mapCropStage = new int[Game.MAP_COL][Game.MAP_ROW];
		
		farmlandImage = ImageUtil.addImage(48, 48, "resources/farming/farmland.png");
		
		loadFarmlandMap();
		loadCropStageMap();
		getCropImage();
		
		Game.player.setNoCrops(true);
	}
	
	public void getCropImage()
	{
		// crop image numbers will be the same as the crop id in the inventory
		crop [30] = new Crop();
		crop [30].setImage1(ImageUtil.addImage(24, 24, "resources/farming/carrot_stage1.png"));
		crop [30].setImage2(ImageUtil.addImage(24, 24, "resources/farming/carrot_stage2.png"));
		crop [30].setImage3(ImageUtil.addImage(24, 24, "resources/farming/carrot_stage3.png"));
	}
	
	// loads the map of farmland/crops from the farmlandmap.txt file to mapFarmland[][]
	public void loadFarmlandMap()
	{
		try {
			URL url = TxtFileUtil.readFile("resources/maps/farmlandmap.txt");
			BufferedReader br = TxtFileUtil.readURL(url);
			int col = 0;
			int row = 0;
			while (col < Game.MAP_COL && row < Game.MAP_ROW) {
				String line = br.readLine();
				while (col < Game.MAP_COL) {
					String numbers[] = line.split("\t");
					int num = Integer.parseInt (numbers[col]);
					mapFarmland[col][row] = num;
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
	
	// loads the map of crop stage #s from the cropstagemap.txt file to mapCropStage[][]
	public void loadCropStageMap()
	{
		try {
			URL url = TxtFileUtil.readFile("resources/maps/cropstagemap.txt");
			BufferedReader br = TxtFileUtil.readURL(url);
			int col = 0;
			int row = 0;
			while (col < Game.MAP_COL && row < Game.MAP_ROW) {
				String line = br.readLine ();
				while (col < Game.MAP_COL) {
					String numbers[] = line.split("\t");
					int num = Integer.parseInt (numbers[col]);
					mapCropStage[col][row] = num;
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
	
	
	
	public void render (Graphics g) 
	{
		int col = 0;
		int row = 0;
		while (col < Game.MAP_COL && row < Game.MAP_ROW) {
			
			int worldX = col * 48;
			int worldY = row * 48;
			
			int screenX = worldX - Game.player.getWorldX() + Game.player.getScreenX();
			int screenY = worldY - Game.player.getWorldY() + Game.player.getScreenY();
			
			// if outside of the players view, do not render.
			if(worldX > Game.player.getWorldX() - Game.WIDTH &&
					worldX < Game.player.getWorldX() + Game.WIDTH &&
					worldY > Game.player.getWorldY() - Game.HEIGHT &&
					worldY < Game.player.getWorldY() + Game.HEIGHT &&
					mapFarmland[col][row] != 0) 
			{
				// draw farmland tile
				g.drawImage(farmlandImage, screenX, screenY, 48, 48, null);
				
				// draw crop if there is any on the farmland
				if (mapFarmland[col][row] != 1) g.drawImage(crop[mapFarmland[col][row]].getCurrentStage(mapCropStage[col][row]), screenX, screenY, 48, 48, null);
			}
			
			col++;
			
			if (col == Game.MAP_COL) {
				col = 0;
				row++;
			}
		}
	}
	
	// creates or deletes a farmland tile
	public void tillFarmland()
	{
		// reverts the screen mouse coordinates to world coordinates 
		mouseWorldX = mouseX + Game.player.getWorldX() - Game.player.getScreenX();
		mouseWorldY = mouseY + Game.player.getWorldY() - Game.player.getScreenY();
		
		// grabs the col and row of the tile that the mouse is hovering over
		int col = mouseWorldX / 48;
		int row = mouseWorldY / 48;
		
		int changeNumber; // represents the number to change the number in the .txt file to
		
		// determines if a farmland tile should be added or removed based on if there is one already there
		if (mapFarmland[col][row] == 0 && Game.buildingManager.getBuildingMap()[col][row] == 0) changeNumber = 1;
		else changeNumber = 0;
		
		if(mapCropStage[col][row] >= 1){ // some crop has been removed, therefor decrease crop count
			numCrops--;
			
			if(numCrops <= 0) {
				Game.player.setNoCrops(true);
			}
		}
		
		//if tile has fully grown crop, give crop to player
		if (mapCropStage[col][row] == 3)
		{
			Game.inventory.addItem(mapFarmland[col][row] + 10, 1);
			setPlantStage(col, row, 0);
			
		}
		
		mapFarmland[col][row] = changeNumber;
	}
	
	// plants the selected seed on a specified farmland tile 
	public void plantSeed()
	{
		// reverts the screen mouse coordinates to world coordinates 
		mouseWorldX = mouseX + Game.player.getWorldX() - Game.player.getScreenX();
		mouseWorldY = mouseY + Game.player.getWorldY() - Game.player.getScreenY();
		
		// grabs the col and row of the tile that the mouse is hovering over
		int col = mouseWorldX / 48;
		int row = mouseWorldY / 48;
		
		//int charIndex = col + col;
		
		// only plant if the player is planting on a farmland tile
		if (mapFarmland[col][row] == 1)
		{
			// grabs number to place in .txt file (same number as crop id in inventory)
			int seedID = Game.inventory.getCurrentID();
			
			mapFarmland[col][row] = seedID;
			
			// sets plant stage in this tile to 1 (plant is now growing in this tile)
			setPlantStage(col, row, 1);
			
			// subtract 1 from the num of buildings in the player's inventory
			Game.inventory.minusItem(1);
			
			// since a crop is added make sure the game knows the player has crops planted
			if(Game.player.isNoCrops()) {
				Game.player.setNoCrops(false);
			}
			
			numCrops++;
			
		}
	}
	
	// sets the current growing stage of the specified plant tile
	public void setPlantStage(int col, int row, int stage)
	{
		if (stage > 3 || stage < 0) return; // dont even bother running code if its an invalid stage
		
		mapCropStage[col][row] = stage;
	}
	
	// this function advances the crop stage of all crops on the map
	// probably not how we want to do this in the future if we want crops to grow at different rates
	public void advanceAllStages()
	{
		for (int i = 0; i < 40; i++)
		{
			for (int j = 0; j < 40; j++)
			{
				if (mapCropStage[j][i] != 0 && mapCropStage[j][i] < 3) {
					mapCropStage[j][i] =  mapCropStage[j][i] + 1;
				}
			}
		}
	}
	
	public int[][] getFarmland()
	{
		return mapFarmland;
	}
	
	public int getPlantStage(int col, int row)
	{
		return mapCropStage[col][row];
	}
	
	public void setMouseX(int x)
	{
		mouseX = x;
	}
	
	public void setMouseY(int y)
	{
		mouseY = y;
	}
}
