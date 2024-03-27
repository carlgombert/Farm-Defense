package view.map.building;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;

import controller.Game;
import controller.objectHandling.ID;
import model.GameObject;
import model.gameObjects.Zombie;
import util.ImageUtil;

public class BuildingManager 
{
	private static Building[] building;
	private static int mapBuildingNum[][];
	private static int mapRotationNum[][];
	private static int mapHealthNum[][];
	
	private static int mouseX;
	private static int mouseY;
	private static int mouseWorldX;
	private static int mouseWorldY;
	
	public String[] lines;
	
	/*    
	 *    This code needs to be changed so that the player can only build a certain
	 *    distance from their character
	 *    
	 *    Also when more buildings are added we need to change the way the .txt
	 *    files are edited to something similar to the way they are edited in
	 *    the FarmingManager
	 */
	public BuildingManager()
	{
		building = new Building[20];
		mapBuildingNum = new int[Game.MAP_COL][Game.MAP_ROW];
		mapRotationNum = new int[Game.MAP_COL][Game.MAP_ROW];
		mapHealthNum = new int[Game.MAP_COL][Game.MAP_ROW];
		
		// resets the building map, deleting this will save the player's buildings across games
		resetBuildingMap();
		
		loadBuildingMap(); // building map keeps track of the buildings on the map
		loadBuildingRotation(); // keeps tracking the rotation of each building on the map
		getBuildingImage();
	}
	
	/*  
	 *  creates new building based on the id of the building seen in the .txt file
	 *
	 *  different images (image0, image1, etc) for the same building corresponds to different
	 *  rotations of the building
	 *
	 *  setConnections sets the amount of other buildings that this building is connected to
	 *  useful for other calculations in the future
	 */
	public void getBuildingImage()
	{
		// wood buildings
		building [1] = new Building();
		building [1].setImage0(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_corner_1_0.png"));
		building [1].setImage1(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_corner_1_1.png"));
		building [1].setImage2(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_corner_1_2.png"));
		building [1].setImage3(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_corner_1_3.png"));
		building [1].setConnections(1);
		
		building [2] = new Building();
		building [2].setImage0(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_corner_2_0.png"));
		building [2].setImage1(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_corner_2_1.png"));
		building [2].setImage2(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_corner_2_2.png"));
		building [2].setImage3(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_corner_2_3.png"));
		building [2].setConnections(2);
		
		building [3] = new Building();
		building [3].setImage0(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_corner_3_0.png"));
		building [3].setImage1(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_corner_3_1.png"));
		building [3].setImage2(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_corner_3_2.png"));
		building [3].setImage3(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_corner_3_3.png"));
		building [3].setConnections(3);
		
		building [4] = new Building();
		building [4].setImage0(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_corner_4.png"));
		building [4].setConnections(4);
		
		building [5] = new Building();
		building [5].setImage0(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_corner_0.png"));
		building [5].setConnections(0);
		
		building [6] = new Building();
		building [6].setImage0(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_side_0.png"));
		building [6].setImage1(ImageUtil.addImage(48, 48, "resources/buildings/wood/wall_side_1.png"));
		building [6].setConnections(2);
		
		// stone buildings
		building [11] = new Building();
		building [11].setImage0(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_corner_1_0.png"));
		building [11].setImage1(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_corner_1_1.png"));
		building [11].setImage2(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_corner_1_2.png"));
		building [11].setImage3(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_corner_1_3.png"));
		building [11].setConnections(1);
		
		building [12] = new Building();
		building [12].setImage0(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_corner_2_0.png"));
		building [12].setImage1(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_corner_2_1.png"));
		building [12].setImage2(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_corner_2_2.png"));
		building [12].setImage3(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_corner_2_3.png"));
		building [12].setConnections(2);
		
		building [13] = new Building();
		building [13].setImage0(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_corner_3_0.png"));
		building [13].setImage1(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_corner_3_1.png"));
		building [13].setImage2(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_corner_3_2.png"));
		building [13].setImage3(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_corner_3_3.png"));
		building [13].setConnections(3);
		
		building [14] = new Building();
		building [14].setImage0(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_corner_4.png"));
		building [14].setConnections(4);
		
		building [15] = new Building();
		building [15].setImage0(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_corner_0.png"));
		building [15].setConnections(0);
		
		building [16] = new Building();
		building [16].setImage0(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_side_0.png"));
		building [16].setImage1(ImageUtil.addImage(48, 48, "resources/buildings/stone/wall_stone_side_1.png"));
		building [16].setConnections(2);
	}
	
	// loads mapBuildingNum[][] with the building numbers from the buildingmap.txt file
	public void loadBuildingMap() 
	{
		try {
			URL url = getClass().getClassLoader().getResource("resources/maps/buildingmap.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			int col = 0;
			int row = 0;
			while (col < Game.MAP_COL && row < Game.MAP_ROW) {
				String line = br.readLine ();
				while (col < Game.MAP_COL) {
					String numbers[] = line.split("\t");
					int num = Integer.parseInt (numbers[col]);
					mapBuildingNum[col][row] = num;
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
	
	// loads mapRotationNum[][] with the rotation numbers from the buildingrotationmap.txt file
	public void loadBuildingRotation()
	{
		try 
		{
			URL url = getClass().getClassLoader().getResource("resources/maps/buildingrotationmap.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			int col = 0;
			int row = 0;
			while (col < Game.MAP_COL && row < Game.MAP_ROW) {
				String line = br.readLine();
				while (col < Game.MAP_COL) {
					String numbers[] = line.split("\t");
					int num = Integer.parseInt (numbers[col]);
					mapRotationNum[col][row] = num;
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
	
	public void loadBuildingHealth()
	{
		try 
		{
			URL url = getClass().getClassLoader().getResource("resources/maps/buildinghealthmap.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			int col = 0;
			int row = 0;
			while (col < Game.MAP_COL && row < Game.MAP_ROW) {
				String line = br.readLine();
				while (col < Game.MAP_COL) {
					String numbers[] = line.split("\t");
					int num = Integer.parseInt (numbers[col]);
					mapHealthNum[col][row] = num;
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
	
	public void render (Graphics g) {
		int col = 0;
		int row = 0;
		while (col < Game.MAP_COL && row < Game.MAP_ROW) {
			int tileNum = mapBuildingNum[col][row];
			
			int worldX = col * 48;
			int worldY = row * 48;
			
			int screenX = worldX - Game.player.getWorldX() + Game.player.getScreenX();
			int screenY = worldY - Game.player.getWorldY() + Game.player.getScreenY();
			
			// if the building is outside of the players view, do not render.
			if(worldX > Game.player.getWorldX() - Game.WIDTH &&
					worldX < Game.player.getWorldX() + Game.WIDTH &&
					worldY > Game.player.getWorldY() - Game.HEIGHT &&
					worldY < Game.player.getWorldY() + Game.HEIGHT &&
					mapBuildingNum[col][row] != 0) 
			{
				// chooses which picture to render based on the rotation of the building
				int rotation = mapRotationNum[col][row];
				BufferedImage image = null;
				
				if (rotation == 0) image = building[tileNum].getImage0();
				if (rotation == 1) image = building[tileNum].getImage1();
				if (rotation == 2) image = building[tileNum].getImage2();
				if (rotation == 3) image = building[tileNum].getImage3();
				
				g.drawImage(image, screenX, screenY, 48, 48, null);
				
				int buildingMaxHealth = mapHealthNum[col][row];
				
				switch (getBuildingID(mapBuildingNum[col][row]))
				{
					case 20:
						buildingMaxHealth = 30;
						break;
					case 21:
						buildingMaxHealth = 50;
						break;
				}
				
				if (mapHealthNum[col][row] > 0 && mapHealthNum[col][row] != buildingMaxHealth)
				{	
					g.setColor(Color.red);
					g.fillRect(screenX + (Game.TILE_SIZE / 8), screenY + (int)(Game.TILE_SIZE * (3.0/4.0)), (int)(Game.TILE_SIZE * (3.0/4.0)), 4);
					g.setColor(Color.green);
					g.fillRect(screenX + (Game.TILE_SIZE / 8), screenY + (int)(Game.TILE_SIZE * (3.0/4.0)), (int)((mapHealthNum[col][row] / (double)buildingMaxHealth) * (Game.TILE_SIZE * (3.0/4.0))), 4);
				}
			}
			
			col++;
			
			if (col == Game.MAP_COL) {
				col = 0;
				row++;
			}
		}
	}
	
	// creates a building based on where the mouse is on the screen
	public void createBuilding(int id)
	{
		// reverts the screen mouse coordinates to world coordinates 
		mouseWorldX = mouseX + Game.player.getWorldX() - Game.player.getScreenX();
		mouseWorldY = mouseY + Game.player.getWorldY() - Game.player.getScreenY();
		
		// grabs the col and row of the tile that the mouse is hovering over
		int col = mouseWorldX / 48;
		int row = mouseWorldY / 48;
		
		// checks to make sure the tile that the player is trying to build on is empty (no building, no farmland)
		if (mapBuildingNum[col][row] == 0 && Game.farmingManager.getFarmland()[col][row] == 0)
		{
			// set the building's health
			switch (Game.inventory.getCurrentID())
			{
				case 20:
					mapHealthNum[col][row] = 30;
					break;
				case 21:
					mapHealthNum[col][row] = 50;
					break;
			}
			
			// subtract 1 from the num of buildings in the player's inventory
			Game.inventory.minusItem(1);
				
			// calculates what the building should be based on what the buildings are around it
			recalculateTile(col, row, id);
		}
	}
	
	public void removeBuilding() {
		// reverts the screen mouse coordinates to world coordinates 
		mouseWorldX = mouseX + Game.player.getWorldX() - Game.player.getScreenX();
		mouseWorldY = mouseY + Game.player.getWorldY() - Game.player.getScreenY();
		
		// grabs the col and row of the tile that the mouse is hovering over
		int col = mouseWorldX / 48;
		int row = mouseWorldY / 48;
		
		// checks to make sure the tile that the player is trying to remove has a building
		if (mapBuildingNum[col][row] != 0)
		{
			mapBuildingNum[col][row] = 0;
			
			int id = Game.inventory.getCurrentID();
			int fullHealth = 100;
			switch (id)
			{
				case 20:
					fullHealth = 30;
					break;
				case 21:
					fullHealth = 50;
					break;
			}
			
			// if wall is undamaged, add back to player inventory
			if(mapHealthNum[col][row] == fullHealth) { 
				Game.inventory.addItem(id, 1);
			}
			
			recalculateTile(col, row, 0);
		}
	}
	
	// calculates what building should be on the tile based on the buildings around it
	//
	// this function will call recacalulateTile() on all of the tiles around it to 
	// recalculate them because there will now be a new building attached to it
	//
	// as of now, it calls recalculate on every tile regardless of whether or not it needs
	// to be updated. this can be fixed later if needed for optimization
	public void recalculateTile(int col, int row, int buildingID)
	{
		// current building number in the mapBuildingNum array
		int buildingNum = mapBuildingNum[col][row];
		
		// integers representing if there is a building next to the current building in the
		// specified direction. 0 = false; 1 = true
		// these are integers instead of booleans because (left - right == 0) is used to 
		// figure out if there is a building to the left and the right, and vice versa
		int left = 0;
		int right = 0;
		int up = 0;
		int down = 0;
		
		// sets variables to 1 if there is a building next to the current building in the 
		// specified direction
		if (mapBuildingNum[col-1][row] != 0) left = 1;
		if (mapBuildingNum[col+1][row] != 0) right = 1;
		if (mapBuildingNum[col][row-1] != 0) up = 1;
		if (mapBuildingNum[col][row+1] != 0) down = 1;
		
		int connections = up + down + left + right; // total number of connections to adjacent buildings
		
		// exits the function if the building doesn't need to be updated
		if ((buildingNum != 0) && (connections == building[buildingNum].getConnections())) return;
		
		if (connections == 4) // 4 buildings connected to it
		{
			// rewrites the number at this building's index
			mapBuildingNum[col][row] = calculateBuildingNumber(buildingID, connections, false);
			
			// rewrites this buildings rotation to 0 because it has 4 connections, so no rotation necessary
			mapRotationNum[col][row] = 0;
			
			// calls the recalculateTile() function for all buildings around it 
			recalculateTile(col-1, row, getBuildingID(mapBuildingNum[col-1][row]));
			recalculateTile(col+1, row, getBuildingID(mapBuildingNum[col+1][row]));
			recalculateTile(col, row-1, getBuildingID(mapBuildingNum[col][row-1]));
			recalculateTile(col, row+1, getBuildingID(mapBuildingNum[col][row+1]));
		}
		else if (connections == 3) // 3 buildings connected to it
		{
			// rewrites the number at this building's index
			mapBuildingNum[col][row] = calculateBuildingNumber(buildingID, connections, false);
			
			// rewrites this building's rotation based on which sides its connections are on
			if (up == 1)
			{
				if (left == 1 && right == 1) mapRotationNum[col][row] = 2;
				else if (left == 1 && down == 1) mapRotationNum[col][row] = 3;
				else mapRotationNum[col][row] = 1;
			}
			else mapRotationNum[col][row] = 0;
			
			// calls the recalculateTile() function for all buildings around it 
			if (up == 1) recalculateTile(col, row-1, getBuildingID(mapBuildingNum[col][row-1]));
			if (down == 1) recalculateTile(col, row+1, getBuildingID(mapBuildingNum[col][row+1]));
			if (left == 1) recalculateTile(col-1, row, getBuildingID(mapBuildingNum[col-1][row]));
			if (right == 1) recalculateTile(col+1, row, getBuildingID(mapBuildingNum[col+1][row]));
		}
		else if (connections == 2) // 2 buildings connected to it
		{
			// need to differentiate between buildings with 2 connections that look
			// like "|" or ones that look like "L"
			if (up - down == 0 || left - right == 0) // checks if it should be a line building
			{
				// rewrites the number at this building's index
				mapBuildingNum[col][row] = calculateBuildingNumber(buildingID, connections, true);
				
				// rewrites this building's rotation; only 2 possibilities: | or -
				if (up == 1 && down == 1) mapRotationNum[col][row] = 1;
				else mapRotationNum[col][row] = 0;
			}
			else // if not a line, must be an L building
			{
				// rewrites the number at this building's index
				mapBuildingNum[col][row] = calculateBuildingNumber(buildingID, connections, false);
				
				// rewrites this building's rotation based on which sides its connections are on
				if (up == 1)
				{
					if (right == 1) mapRotationNum[col][row] = 1;
					else mapRotationNum[col][row] = 2;
				}
				else
				{
					if (left == 1) mapRotationNum[col][row] = 3;
					else mapRotationNum[col][row] = 0;
				}
			}
			
			// calls the recalculateTile() function for all buildings around it 
			if (up == 1) recalculateTile(col, row-1, getBuildingID(mapBuildingNum[col][row-1]));
			if (down == 1) recalculateTile(col, row+1, getBuildingID(mapBuildingNum[col][row+1]));
			if (left == 1) recalculateTile(col-1, row, getBuildingID(mapBuildingNum[col-1][row]));
			if (right == 1) recalculateTile(col+1, row, getBuildingID(mapBuildingNum[col+1][row]));
		}
		else if (connections == 1) // 1 building connected to it
		{
			// rewrites the number at this building's index
			mapBuildingNum[col][row] = calculateBuildingNumber(buildingID, connections, false);
			
			// rewrite current building's rotation and recalculates connected tile
			if (up == 1) 
			{
				mapRotationNum[col][row] = 2;
				recalculateTile(col, row-1, getBuildingID(mapBuildingNum[col][row-1]));
			}
			if (down == 1) 
			{
				mapRotationNum[col][row] = 0;
				recalculateTile(col, row+1, getBuildingID(mapBuildingNum[col][row+1]));
			}
			if (left == 1) 
			{
				mapRotationNum[col][row] = 3;
				recalculateTile(col-1, row, getBuildingID(mapBuildingNum[col-1][row]));
			}
			if (right == 1) 
			{
				mapRotationNum[col][row] = 1;
				recalculateTile(col+1, row, getBuildingID(mapBuildingNum[col+1][row]));
			}
		}
		else // no buildings connected
		{
			// rewrites the building number at this index, nothing else needed
			mapBuildingNum[col][row] = calculateBuildingNumber(buildingID, connections, false);
			mapRotationNum[col][row] = 0;
		}
	}
	
	// resets buildingmap.txt and buildingrotationmap.txt to a full map of 0's
	public void resetBuildingMap()
	{
		try
		{
			URL url1 = getClass().getClassLoader().getResource("resources/maps/buildingmap.txt");
			URL url2 = getClass().getClassLoader().getResource("resources/maps/buildingrotationmap.txt");
			URL url3 = getClass().getClassLoader().getResource("resources/maps/buildinghealthmap.txt");
			FileWriter writer1 = new FileWriter(new File(url1.getFile()));
			FileWriter writer2 = new FileWriter(new File(url2.getFile()));
			FileWriter writer3 = new FileWriter(new File(url3.getFile()));
			
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
			writer3.write(blankFile);
			
			writer1.close();
			writer2.close();
			writer3.close();
		} catch (Exception e) 
		{
			System.out.println(e);
		}
	}
	
	// checks collision with buildings
	public void checkBuildingCollision(GameObject object)
	{
		// grab coordinates of the corners of the rectangle bound of the game object
		int objectLeftWorldX = object.getWorldX() + object.getBounds().x - object.getScreenX();
		int objectRightWorldX = object.getWorldX() + object.getBounds().x + object.getBounds().width - object.getScreenX();
		int objectTopWorldY = object.getWorldY() + object.getBounds().y - object.getScreenY();
		int objectBottomWorldY = object.getWorldY() + object.getBounds().y + object.getBounds().height - object.getScreenY();
		
		// screen coordinates of the object's hitbox
		int objectLeftScreenX = object.getBounds().x;
		int objectRightScreenX = object.getBounds().x + object.getBounds().width;
		int objectTopScreenY = object.getBounds().y;
		int objectBottomScreenY = object.getBounds().y + object.getBounds().height;
		
		// turn coordinates into X&Y indexes to be used to find the buildings on specified tiles
		int leftIndex = objectLeftWorldX/Game.TILE_SIZE;
		int rightIndex = objectRightWorldX/Game.TILE_SIZE;
		int topIndex = objectTopWorldY/Game.TILE_SIZE;
		int bottomIndex = objectBottomWorldY/Game.TILE_SIZE;
		
		// checks if the object is even on any tiles with buildings on them
		if ((mapBuildingNum[leftIndex][topIndex] != 0)
		 || (mapBuildingNum[rightIndex][topIndex] != 0)
		 || (mapBuildingNum[leftIndex][bottomIndex] != 0)
		 || (mapBuildingNum[rightIndex][bottomIndex] != 0))
		{
			// array of bounds to check for collision
			Rectangle[] buildingsToCheck = new Rectangle[4];
			
			// fills the array above with buildings to check; the if statements are
			// there to prevent duplicates. uses getBuildingBounds() function to get
			// correspinding rectangle for each tile
			if (leftIndex != rightIndex)
			{
				if (topIndex != bottomIndex)
				{
					buildingsToCheck[0] = getBuildingBounds(leftIndex, topIndex);
					buildingsToCheck[1] = getBuildingBounds(rightIndex, topIndex);
					buildingsToCheck[2] = getBuildingBounds(leftIndex, bottomIndex);
					buildingsToCheck[3] = getBuildingBounds(rightIndex, bottomIndex);
				}
				else
				{
					buildingsToCheck[0] = getBuildingBounds(leftIndex, topIndex);
					buildingsToCheck[1] = getBuildingBounds(rightIndex, topIndex);
				}
			}
			else
			{
				if (topIndex != bottomIndex)
				{
					buildingsToCheck[0] = getBuildingBounds(leftIndex, topIndex);
					buildingsToCheck[1] = getBuildingBounds(leftIndex, bottomIndex);
				}
				else
				{
					buildingsToCheck[0] = getBuildingBounds(leftIndex, topIndex);
				}
			}
			
			// represents X and Y values that the object will be stopped at
			int setX = -1;
			int setY = -1;
			
			// collision behaves differently with zombies; switching to different function
			if (object.getId() == ID.Zombie && (buildingsToCheck[0] != null || buildingsToCheck[1] != null || buildingsToCheck[2] != null || buildingsToCheck[3] != null))
			{
				checkZombie((Zombie)object, buildingsToCheck);
				return;
			}
			
			for (Rectangle bound : buildingsToCheck) // iterates through for each rectangle/"bound"
			{
				if (bound != null)
				{
					// booleans represent if the object is exactly on the corner of the bound
					boolean topLeftCorner = (objectLeftScreenX == bound.x + bound.width + 1 && objectTopScreenY == bound.y + bound.height + 1);
					boolean topRightCorner = (objectRightScreenX == bound.x - 1 && objectTopScreenY == bound.y + bound.height + 1);
					boolean bottomLeftCorner = (objectLeftScreenX == bound.x + bound.width + 1 && objectBottomScreenY == bound.y - 1);
					boolean bottomRightCorner = (objectRightScreenX == bound.x - 1 && objectBottomScreenY == bound.y - 1);
					
					// this whole section handles exact corner collision; it was glitching when this would happen so
					// it prioritizes up/down movement while stopping the object's X speed
					if (object.getSpeedX() != 0 && object.getSpeedY() != 0 && (topLeftCorner || topRightCorner || bottomLeftCorner || bottomRightCorner))
					{						
						if (topLeftCorner && object.getSpeedX() < 0 && object.getSpeedY() < 0)
						{
							setX = ((bound.x + bound.width) + Game.player.getWorldX() - Game.player.getScreenX() - (objectLeftWorldX - object.getWorldX()) + 1);
							setY = ((bound.y + bound.height) + Game.player.getWorldY() - Game.player.getScreenY() - (objectTopWorldY - object.getWorldY()) + 1);
							object.setXTileCollision(true);
						}
						else if (topRightCorner && object.getSpeedX() > 0 && object.getSpeedY() < 0)
						{
							setX = (bound.x - object.getBounds().width + Game.player.getWorldX() - Game.player.getScreenX() - (objectLeftWorldX - object.getWorldX()) - 1);
							setY = ((bound.y + bound.height) + Game.player.getWorldY() - Game.player.getScreenY() - (objectTopWorldY - object.getWorldY()) + 1);
							object.setXTileCollision(true);
						}
						else if (bottomLeftCorner && object.getSpeedX() < 0 && object.getSpeedY() > 0)
						{
							setX = ((bound.x + bound.width) + Game.player.getWorldX() - Game.player.getScreenX() - (objectLeftWorldX - object.getWorldX()) + 1);
							setY = (bound.y - object.getBounds().height + Game.player.getWorldY() - Game.player.getScreenY() - (objectTopWorldY - object.getWorldY()) - 1);
							object.setXTileCollision(true);
						}
						else if (bottomRightCorner && object.getSpeedX() > 0 && object.getSpeedY() > 0)
						{
							setX = (bound.x - object.getBounds().width + Game.player.getWorldX() - Game.player.getScreenX() - (objectLeftWorldX - object.getWorldX()) - 1);
							setY = (bound.y - object.getBounds().height + Game.player.getWorldY() - Game.player.getScreenY() - (objectTopWorldY - object.getWorldY()) - 1);
							object.setXTileCollision(true);
						}
					}
					else // non corner collision
					{
						if (object.getSpeedX() < 0)
						{
							if (objectLeftScreenX + object.getSpeedX() < bound.x + bound.width + 1 && objectLeftScreenX + object.getSpeedX() > bound.x
						    && ((objectTopScreenY < bound.y + bound.height) && (objectTopScreenY > bound.y)
						    || (objectBottomScreenY > bound.y) && (objectBottomScreenY < bound.y + bound.height)))
							{
								setX = ((bound.x + bound.width) + Game.player.getWorldX() - Game.player.getScreenX() - (objectLeftWorldX - object.getWorldX()) + 1);
								object.setXTileCollision(true);
							}
						}
						else if (object.getSpeedX() > 0)
						{
							if (objectRightScreenX + object.getSpeedX() > bound.x - 1 && objectRightScreenX + object.getSpeedX() < bound.x + bound.width
							&& ((objectTopScreenY < bound.y + bound.height) && (objectTopScreenY > bound.y)
							|| (objectBottomScreenY > bound.y) && (objectBottomScreenY < bound.y + bound.height)))
							{
								setX = (bound.x - object.getBounds().width + Game.player.getWorldX() - Game.player.getScreenX() - (objectLeftWorldX - object.getWorldX()) - 1);
								object.setXTileCollision(true);
							}
						}
						if (object.getSpeedY() < 0)
						{
							if (objectTopScreenY + object.getSpeedY() < bound.y + bound.height + 1 && objectTopScreenY + object.getSpeedY() > bound.y
							&& ((objectLeftScreenX > bound.x) && (objectLeftScreenX < bound.x + bound.width)
							|| (objectRightScreenX > bound.x) && (objectRightScreenX < bound.x + bound.width)))
							{
								setY = ((bound.y + bound.height) + Game.player.getWorldY() - Game.player.getScreenY() - (objectTopWorldY - object.getWorldY()) + 1);
								object.setYTileCollision(true);
							}
						}
						else if (object.getSpeedY() > 0)
						{
							if (objectBottomScreenY + object.getSpeedY() > bound.y - 1 && objectBottomScreenY + object.getSpeedY() < bound.y + bound.height
							&& ((objectLeftScreenX > bound.x) && (objectLeftScreenX < bound.x + bound.width)
							|| (objectRightScreenX > bound.x) && (objectRightScreenX < bound.x + bound.width)))
							{
								setY = (bound.y - object.getBounds().height + Game.player.getWorldY() - Game.player.getScreenY() - (objectTopWorldY - object.getWorldY()) - 1);
								object.setYTileCollision(true);
							}
						}
					}
				}
			}
			
			// sets the objects X and Y if it was updated, AKA if they ran into something
			if (setX != -1) object.setWorldX(setX);
			if (setY != -1) object.setWorldY(setY);
		}
		else if (object.getId() == ID.Zombie)
		{
			Zombie obj_zombie = (Zombie)object;
			obj_zombie.setDestroyingBuilding(false, 0, 0);
		}
	}
	
	public void checkZombie(Zombie zomb, Rectangle[] buildings)
	{
		for (Rectangle bound : buildings)
		{
			Rectangle zombieFutureBound = new Rectangle(zomb.getBounds().x + zomb.getSpeedX() - Game.player.getSpeedX(), zomb.getBounds().y + zomb.getSpeedY() - Game.player.getSpeedY(), zomb.getBounds().width, zomb.getBounds().height);
			
			boolean localDestroyingBuilding = false;
			
			if (bound != null)
			{
				if (zombieFutureBound.intersects(bound))
				{
					int col = (bound.x + (bound.width / 2) + Game.player.getWorldX() - Game.player.getScreenX()) / Game.TILE_SIZE;
					int row = (bound.y + (bound.height / 2) + Game.player.getWorldY() - Game.player.getScreenY()) / Game.TILE_SIZE;
					
					localDestroyingBuilding = true;
					
					if (!zomb.getDestroyingBuilding())
					{
						zomb.setDestroyingBuilding(true, col, row);
					}
					return;
				}
			}
			
			if (!localDestroyingBuilding)
			{
				zomb.setDestroyingBuilding(false, 0, 0);
			}
		}
	}
	
	public void damageBuilding(int col, int row, int dmg)
	{
		int currentBuildingHealth = mapHealthNum[col][row] - dmg;
		
		if (currentBuildingHealth > 0) mapHealthNum[col][row] = currentBuildingHealth;
		else
		{
			mapHealthNum[col][row] = 0;
			mapBuildingNum[col][row] = 0;
			mapRotationNum[col][row] = 0;
			
			if (mapBuildingNum[col][row-1] != 0) recalculateTile(col, row-1, getBuildingID(mapBuildingNum[col][row-1]));
			if (mapBuildingNum[col][row+1] != 0) recalculateTile(col, row+1, getBuildingID(mapBuildingNum[col][row+1]));
			if (mapBuildingNum[col-1][row] != 0) recalculateTile(col-1, row, getBuildingID(mapBuildingNum[col-1][row]));
			if (mapBuildingNum[col+1][row] != 0) recalculateTile(col+1, row, getBuildingID(mapBuildingNum[col+1][row]));
		}
	}
	
	// returns the bounds of a building at its corresponding row and col
	public Rectangle getBuildingBounds(int col, int row)
	{	
		int buildingNum = mapBuildingNum[col][row]; // grab building number
		
		if (buildingNum == 0) return null; // no bounds if theres no building there
		
		// finds the screen coordinates of the tile that the building is one, useful because
		// the returned bounds of the building is in screen coordinates
		int buildingScreenX = (col * Game.TILE_SIZE) - Game.player.getWorldX() + Game.player.getScreenX();
		int buildingScreenY = (row * Game.TILE_SIZE) - Game.player.getWorldY() + Game.player.getScreenY();
		
		Rectangle returnRectangle = null;
		
		// calculates the bounds of the building based on the type of building and its rotation:
		//
		// all buildings that dont look like "-" or "|" have a square as their bounds 
		// buildings that look like "-" or "|" have a skinnier rectangle as their bound
		if (building[buildingNum].getConnections() != 2) returnRectangle = new Rectangle(buildingScreenX + (int)((1.0/8)*Game.TILE_SIZE), buildingScreenY + (int)((1.0/8)*Game.TILE_SIZE), (int)(Game.TILE_SIZE * (6.0/8)), (int)(Game.TILE_SIZE * (6.0/8)));
		else
		{
			if (mapBuildingNum[col-1][row] != 0 && mapBuildingNum[col+1][row] != 0) returnRectangle = new Rectangle(buildingScreenX, buildingScreenY + (int)((7.0/24)*Game.TILE_SIZE), Game.TILE_SIZE, (int)(Game.TILE_SIZE * (10.0/24)));
			else if (mapBuildingNum[col][row-1] != 0 && mapBuildingNum[col][row+1] != 0) returnRectangle = new Rectangle(buildingScreenX + (int)((7.0/24)*Game.TILE_SIZE), buildingScreenY, (int)(Game.TILE_SIZE * (10.0/24)), Game.TILE_SIZE);
			else returnRectangle = new Rectangle(buildingScreenX + (int)((1.0/8)*Game.TILE_SIZE), buildingScreenY + (int)((1.0/8)*Game.TILE_SIZE), (int)(Game.TILE_SIZE * (6.0/8)), (int)(Game.TILE_SIZE * (6.0/8)));
		}
		
		return returnRectangle;
	}
	
	public int getBuildingID(int buildingNum)
	{
		if (buildingNum < 10) return 20;
		else if (buildingNum < 20) return 21;
		else return 0;
	}
	
	public int calculateBuildingNumber(int type, int connections, boolean line)
	{
		if (type == 20) // wood
		{
			if (connections == 1) return 1;
			else if (connections == 2)
			{
				if (line) return 6;
				else return 2;
			}
			else if (connections == 3) return 3;
			else if (connections == 4) return 4;
			else return 5;
		}
		else if (type == 21) // stone
		{
			if (connections == 1) return 11;
			else if (connections == 2)
			{
				if (line) return 16;
				else return 12;
			}
			else if (connections == 3) return 13;
			else if (connections == 4) return 14;
			else return 15;
		}
		return 0;
	}
	
	public int[][] getBuildingMap()
	{
		return mapBuildingNum;
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