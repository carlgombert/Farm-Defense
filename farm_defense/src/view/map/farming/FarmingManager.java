package view.map.farming;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;

import controller.Game;
import model.GameObject;
import util.ImageUtil;
import util.TxtFileUtil;

public class FarmingManager 
{
	public static Crop[] crop;
	public static int mapFarmland[][]; // map of all farmland in the map - 0: nothing, 1: empty farmland, any other number: crop (# is crop id)
	public static int mapCropStage[][]; // map of the current stage of the crop in that tile - 0: no crop, 1: stage 1, etc.
	
	public static int mouseX;
	public static int mouseY;
	public static int mouseWorldX;
	public static int mouseWorldY;
	
	public BufferedImage farmlandImage;
	
	public String[] lines;
	
	public FarmingManager()
	{
		crop = new Crop[40];
		mapFarmland = new int[Game.mapCol][Game.mapRow];
		mapCropStage = new int[Game.mapCol][Game.mapRow];
		
		farmlandImage = ImageUtil.addImage(48, 48, "resources/farming/farmland.png");
		
		// resets all farmland on the map and all crop stages to 0
		resetFarmingMap();
		
		loadFarmlandMap();
		loadCropStageMap();
		getCropImage();
	}
	
	public void getCropImage()
	{
		// crop image numbers will be the same as the crop id in the inventory
		crop [30] = new Crop();
		crop [30].image1 = ImageUtil.addImage(24, 24, "resources/farming/carrot_stage1.png");
		crop [30].image2 = ImageUtil.addImage(24, 24, "resources/farming/carrot_stage2.png");
		crop [30].image3 = ImageUtil.addImage(24, 24, "resources/farming/carrot_stage3.png");
	}
	
	// loads the map of farmland/crops from the farmlandmap.txt file to mapFarmland[][]
	public void loadFarmlandMap()
	{
		try {
			URL url = TxtFileUtil.readFile("resources/maps/farmlandmap.txt");
			BufferedReader br = TxtFileUtil.readURL(url);
			int col = 0;
			int row = 0;
			while (col < Game.mapCol && row < Game.mapRow) {
				String line = br.readLine();
				while (col < Game.mapCol) {
					String numbers[] = line.split("\t");
					int num = Integer.parseInt (numbers[col]);
					mapFarmland[col][row] = num;
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
			System.out.println("error occured loading map");
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
			while (col < Game.mapCol && row < Game.mapRow) {
				String line = br.readLine ();
				while (col < Game.mapCol) {
					String numbers[] = line.split("\t");
					int num = Integer.parseInt (numbers[col]);
					mapCropStage[col][row] = num;
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
	
	public void render (Graphics g) 
	{
		int col = 0;
		int row = 0;
		while (col < Game.mapCol && row < Game.mapRow) {
			int tileNum = mapFarmland[col][row];
			
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
			
			if (col == Game.mapCol) {
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
		if (mapFarmland[col][row] == 0) changeNumber = 1;
		else changeNumber = 0;
		
		try 
		{
			URL url = TxtFileUtil.readFile("resources/maps/farmlandmap.txt");
			BufferedReader br = TxtFileUtil.readURL(url);
			
			String line = br.readLine();;
			lines = new String[45]; // string array of every line in the farmlandmap.txt file
			
			int arrayNum = 0;
			
			while (line != null)
			{
				lines[arrayNum] = line;
				arrayNum++;
				line = br.readLine();
			}
	
			String firstHalf = "";
			String secondHalf = "";
			
			// rewrites the number in the lines[] string list to whatever was decided
			for (int i = 0; i < col; i++) firstHalf += mapFarmland[i][row] + "\t";
			for (int i = col + 1; i < 40; i++) secondHalf += mapFarmland[i][row] + "\t";
			
			String newString = firstHalf + changeNumber + "\t" + secondHalf;
			
			lines[row] = newString;
			
			// once the tiles are all fully calculated, rewrite the farmlandmap.txt file
			String newFile = "";
			
			for(int i = 0; i < 40; i++)
			{
				newFile += lines[i];
				if (i != 39) newFile += "\n";
			}
			
			FileWriter writer = new FileWriter(new File(url.getFile()));
			
			writer.write(newFile);
			
			br.close();
			writer.close();
			
			// if there was a fully grown crop on that tile, give them 5 grown crops
			if (mapCropStage[col][row] == 3)
			{
				Game.inventory.addItem(mapFarmland[col][row] + 10, 5);
				setPlantStage(col, row, 0);
			}
			
			// reloads the farming map once the file is rewritten
			Game.farmingManager.loadFarmlandMap();
 		} catch (Exception e) 
		{
			System.out.println(e);
			System.out.println("the issue seems to be with tilling??");
		}
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

			try 
			{
				URL url1 = TxtFileUtil.readFile("resources/maps/farmlandmap.txt");
				BufferedReader br1 = TxtFileUtil.readURL(url1);
				
				String line = br1.readLine();;
				lines = new String[45]; // string array of every line in the farmlandmap.txt file
				
				int arrayNum = 0;
				
				while (line != null)
				{
					lines[arrayNum] = line;
					arrayNum++;
					line = br1.readLine();
				}
		
				String firstHalf = "";
				String secondHalf = "";
				
				// rewrites the number in the lines[] string list to the id of the seed
				for (int i = 0; i < col; i++) firstHalf += mapFarmland[i][row] + "\t";
				for (int i = col + 1; i < 40; i++) secondHalf += mapFarmland[i][row] + "\t";
				
				String newString = firstHalf + seedID + "\t" + secondHalf;
				
				lines[row] = newString;
				
				// once the tiles are all fully calculated, rewrite the farmlandmap.txt file
				String newFile = "";
				
				for(int i = 0; i < 40; i++)
				{
					newFile += lines[i];
					if (i != 39) newFile += "\n";
				}
				
				FileWriter writer1 = new FileWriter(new File(url1.getFile()));
				
				writer1.write(newFile);
				
				br1.close();
				writer1.close();
				
				// sets plant stage in this tile to 1 (plant is now growing in this tile)
				setPlantStage(col, row, 1);
				
				// reloads the farmlandmap once the file is rewritten
				Game.farmingManager.loadFarmlandMap();
				
				// subtract 1 from the num of buildings in the player's inventory
				Game.inventory.minusItem(1);
	 		} catch (Exception e) 
			{
				System.out.println(e);
			}
		}
	}
	
	// sets the current growing stage of the specified plant tile
	public void setPlantStage(int col, int row, int stage)
	{
		if (stage > 3 || stage < 0) return; // dont even bother running code if its an invalid stage
		
		try 
		{	
			URL url1 = TxtFileUtil.readFile("resources/maps/cropstagemap.txt");
			BufferedReader br1 = TxtFileUtil.readURL(url1);
			
			String line = br1.readLine();;
			lines = new String[45]; // string array of every line in the cropstagemap.txt file
			
			int arrayNum = 0;
			
			while (line != null)
			{
				lines[arrayNum] = line;
				arrayNum++;
				line = br1.readLine();
			}
	
			String firstHalf = "";
			String secondHalf = "";
			
			// rewrites the number in the lines[] string list to the specified crop stage
			for (int i = 0; i < col; i++) firstHalf += mapCropStage[i][row] + "\t";
			for (int i = col + 1; i < 40; i++) secondHalf += mapCropStage[i][row] + "\t";
			
			String newString = firstHalf + stage + "\t" + secondHalf;
			
			lines[row] = newString;
			
			// once the tiles are all fully calculated, rewrite the cropstagemap.txt file
			String newFile = "";
			
			for(int i = 0; i < 40; i++)
			{
				newFile += lines[i];
				if (i != 39) newFile += "\n";
			}
			
			FileWriter writer1 = new FileWriter(new File(url1.getFile()));
			
			writer1.write(newFile);
			
			br1.close();
			writer1.close();
			
			// reloads the crop stage map once the file is rewritten
			Game.farmingManager.loadCropStageMap();
 		} catch (Exception e) 
		{
			System.out.println(e);
		}
	}
	
	// this function advances the crop stage of all crops on the map
	// probably not how we want to do this in the future if we want crops to grow at different rates
	public void advanceAllStages()
	{
		try
		{
			URL url = TxtFileUtil.readFile("resources/maps/cropstagemap.txt");
			BufferedReader br = TxtFileUtil.readURL(url);
			
			String line = br.readLine();
			lines = new String[45]; // string array of every line in the cropstagemap.txt file
			
			String newLine;
			
			// increases all numbers in the lines[] string array that isn't a 0 or 3
			for (int i = 0; i < 40; i++)
			{
				newLine = "";
				
				for (int j = 0; j < 40; j++)
				{
					if (mapCropStage[j][i] != 0 && mapCropStage[j][i] < 3) newLine += (mapCropStage[j][i] + 1) + "\t";
					else newLine += mapCropStage[j][i] + "\t";
				}
				
				lines[i] = newLine;
			}
			
			// rewrites cropstagemap.txt file with new crop stages
			String newFile = "";
			
			for (int i = 0; i < 40; i++)
			{
				newFile += lines[i];
				if (i != 39) newFile += "\n";
			}
			
			FileWriter writer = new FileWriter(new File(url.getFile()));
			
			writer.write(newFile);
			writer.close();
			
			// reload crop stage map once done
			Game.farmingManager.loadCropStageMap();
		} catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	public int getPlantStage(int col, int row)
	{
		return mapCropStage[col][row];
	}
	
	// resets both farmlandmap.txt and cropstagemap.txt
	public void resetFarmingMap()
	{
		try
		{
			URL url1 = TxtFileUtil.readFile("resources/maps/farmlandmap.txt");
			URL url2 = TxtFileUtil.readFile("resources/maps/cropstagemap.txt");

			
			File file = new File(url1.getFile()).getCanonicalFile();
			
			FileWriter fw = new FileWriter(file);
			

			BufferedWriter writer1 = new BufferedWriter(fw);
			
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(new File(url2.getFile())));
			
			String blankLine = "";
			String blankFile = "";
			
			for (int i = 0; i < 40; i++)
			{
				blankLine += '0';
				if (i != 39) blankLine += '\t';
			}
			
			for (int i = 0; i < 40; i++)
			{
				blankFile += blankLine;
				if (i != 39) blankFile += '\n';
			}
			
			writer1.write(blankFile);
			writer2.write(blankFile);
			
			writer1.close();
			writer2.close();
		} catch (Exception e) 
		{
			System.out.println(e);
		}
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
