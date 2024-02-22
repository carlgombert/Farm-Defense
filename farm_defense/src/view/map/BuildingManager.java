package view.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import controller.Game;
import model.GameObject;
import util.ImageUtil;

public class BuildingManager 
{
	public static Building[] building;
	public static int mapBuildingNum[][];
	public static int mapRotationNum[][];
	
	public static int mouseX;
	public static int mouseY;
	public static int mouseWorldX;
	public static int mouseWorldY;
	
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
		mapBuildingNum = new int[Game.mapCol][Game.mapRow];
		mapRotationNum = new int[Game.mapCol][Game.mapRow];
		
		// resets the building map, deleting this will save the player's buildings across games
		resetBuildingMap();
		
		loadBuildingMap(); // building map keeps track of the buildings on the map
		loadBuildingRotation(); // keeps tracking the rotation of each building on the map
		getBuildingImage();
	}
	
	public void getBuildingImage()
	{
		/*  
		 *  creates new building based on the id of the building seen in the .txt file
		 *
		 *  different images (image0, image1, etc) for the same building corresponds to different
		 *  rotations of the building
		 *
		 *  setConnections sets the amount of other buildings that this building is connected to
		 *  useful for other calculations in the future
		 */
		building [1] = new Building();
		building [1].image0 = ImageUtil.addImage(48, 48, "resources/buildings/wall_corner_1_0.png");
		building [1].image1 = ImageUtil.addImage(48, 48, "resources/buildings/wall_corner_1_1.png");
		building [1].image2 = ImageUtil.addImage(48, 48, "resources/buildings/wall_corner_1_2.png");
		building [1].image3 = ImageUtil.addImage(48, 48, "resources/buildings/wall_corner_1_3.png");
		building [1].setConnections(1);
		
		building [2] = new Building();
		building [2].image0 = ImageUtil.addImage(48, 48, "resources/buildings/wall_corner_2_0.png");
		building [2].image1 = ImageUtil.addImage(48, 48, "resources/buildings/wall_corner_2_1.png");
		building [2].image2 = ImageUtil.addImage(48, 48, "resources/buildings/wall_corner_2_2.png");
		building [2].image3 = ImageUtil.addImage(48, 48, "resources/buildings/wall_corner_2_3.png");
		building [2].setConnections(2);
		
		building [3] = new Building();
		building [3].image0 = ImageUtil.addImage(48, 48, "resources/buildings/wall_corner_3_0.png");
		building [3].image1 = ImageUtil.addImage(48, 48, "resources/buildings/wall_corner_3_1.png");
		building [3].image2 = ImageUtil.addImage(48, 48, "resources/buildings/wall_corner_3_2.png");
		building [3].image3 = ImageUtil.addImage(48, 48, "resources/buildings/wall_corner_3_3.png");
		building [3].setConnections(3);
		
		building [4] = new Building();
		building [4].image0 = ImageUtil.addImage(48, 48, "resources/buildings/wall_corner_4.png");
		building [4].setConnections(4);
		
		building [5] = new Building();
		building [5].image0 = ImageUtil.addImage(48, 48, "resources/buildings/wall_corner_0.png");
		building [5].setConnections(0);
		
		building [6] = new Building();
		building [6].image0 = ImageUtil.addImage(48, 48, "resources/buildings/wall_side_0.png");
		building [6].image1 = ImageUtil.addImage(48, 48, "resources/buildings/wall_side_1.png");
		building [6].setConnections(2);
	}
	
	// loads mapBuildingNum[][] with the building numbers from the buildingmap.txt file
	public void loadBuildingMap() 
	{
		try {
			File file = new File("resources/maps/buildingmap.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			int col = 0;
			int row = 0;
			while (col < Game.mapCol && row < Game.mapRow) {
				String line = br.readLine ();
				while (col < Game.mapCol) {
					String numbers[] = line.split("\t");
					int num = Integer.parseInt (numbers[col]);
					mapBuildingNum[col][row] = num;
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
	
	// loads mapRotationNum[][] with the rotation numbers from the buildingrotationmap.txt file
	public void loadBuildingRotation()
	{
		try 
		{
			File file = new File("resources/maps/buildingrotationmap.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			int col = 0;
			int row = 0;
			while (col < Game.mapCol && row < Game.mapRow) {
				String line = br.readLine();
				while (col < Game.mapCol) {
					String numbers[] = line.split("\t");
					int num = Integer.parseInt (numbers[col]);
					mapRotationNum[col][row] = num;
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
				
				if (rotation == 0) image = building[tileNum].image0;
				if (rotation == 1) image = building[tileNum].image1;
				if (rotation == 2) image = building[tileNum].image2;
				if (rotation == 3) image = building[tileNum].image3;
				
				g.drawImage(image, screenX, screenY, 48, 48, null);
			}
			
			col++;
			
			if (col == Game.mapCol) {
				col = 0;
				row++;
			}
		}
	}
	
	// creates a building based on where the mouse is on the screen
	public void createBuilding()
	{
		// reverts the screen mouse coordinates to world coordinates 
		mouseWorldX = mouseX + Game.player.getWorldX() - Game.player.getScreenX();
		mouseWorldY = mouseY + Game.player.getWorldY() - Game.player.getScreenY();
		
		// grabs the col and row of the tile that the mouse is hovering over
		int col = mouseWorldX / 48;
		int row = mouseWorldY / 48;
		
		// checks to make sure the tile that the player is trying to build on is empty
		if (mapBuildingNum[col][row] == 0)
		{
			try 
			{
				// subtract 1 from the num of buildings in the player's inventory
				Game.inventory.minusItem(1);
				
				File file = new File("resources/maps/buildingmap.txt");
				BufferedReader br = new BufferedReader(new FileReader(file));
				
				String line = br.readLine();;
				lines = new String[45]; // string array of every line in the buildingmap.txt file
				
				int arrayNum = 0;
				
				while (line != null)
				{
					lines[arrayNum] = line;
					arrayNum++;
					line = br.readLine();
				}
				
				// calculates what the building should be based on what the buildings are around it
				//
				// col + col accounts for the tabs in between the numbers, because each line is read
				// as a full string
				recalculateTile(col + col, row);
		
				// once the tiles are all fully calculated, rewrite the buildingmap.txt file
				String newFile = "";
				
				for(int i = 0; i < 40; i++)
				{
					newFile += lines[i];
					if (i != 39) newFile += "\n";
				}
				
				FileWriter writer = new FileWriter(file);
				
				writer.write(newFile);
				
				br.close();
				writer.close();
				
				// reloads the map once the file is rewritten
				Game.buildingManager.loadBuildingMap();
	 		} catch (Exception e) 
			{
				System.out.println(e);
			}
		}
	}
	
	// calculates what building should be on the tile based on the buildings around it
	//
	// this function will call recacalulateTile() on all of the tiles around it to 
	// recalculate them because there will now be a new building attached to it
	//
	// as of now, it calls recalculate on every tile regardless of whether or not it needs
	// to be updated. this can be fixed later if needed for optimization
	public void recalculateTile(int col, int row)
	{
		int charIndex = col; // index of building being updated
		int leftIndex = charIndex - 2; // index of building to the left
		int rightIndex = charIndex + 2; // index of building to the right
		
		// current building number in the .txt file
		int currentNum = Integer.parseInt(String.valueOf(lines[row].charAt(charIndex)));
		
		// integers representing if there is a building next to the current building in the
		// specified direction. 0 = false; 1 = true
		// these are integers instead of booleans because (left - right == 0) is used to 
		// figure out if there is a building to the left and the right, and vice versa
		int left = 0;
		int right = 0;
		int up = 0;
		int down = 0;
		
		String firstHalf; // substring of the first half of the whole line of text before the building index
		String secondHalf; // substring of the rest of the string
		String newString; // a new string representing the whole row of text with the updated building
		
		// sets variables to 1 if there is a building next to the current building in the 
		// specified direction
		if (!(lines[row].charAt(leftIndex) == '0')) left = 1;
		if (!(lines[row].charAt(rightIndex) == '0')) right = 1;
		if (!(lines[row-1].charAt(charIndex) == '0')) up = 1;
		if (!(lines[row+1].charAt(charIndex) == '0')) down = 1;
		
		int connections = up + down + left + right; // total number of connections to adjacent buildings
		
		// exits the function if the building doesn't need to be updated
		if ((currentNum != 0) && (connections == building[currentNum].getConnections())) return;
		
		if (connections == 4) // 4 buildings connected to it
		{
			// rewrites the number at this building's index to 4 using substrings
			firstHalf = lines[row].substring(0, charIndex);
			secondHalf = lines[row].substring(charIndex + 1, lines[row].length());
			newString = firstHalf + "4" + secondHalf;
			lines[row] = newString; // global string array with all lines of text in the .txt file
			
			// rewrites this buildings rotation to 0 because it has 4 connections, so no rotation necessary
			rewriteRotation(charIndex, row, 0);
			
			// calls the recalculateTile() function for all buildings around it 
			recalculateTile(leftIndex, row);
			recalculateTile(rightIndex, row);
			recalculateTile(charIndex, row-1);
			recalculateTile(charIndex, row+1);
		}
		else if (connections == 3) // 3 buildings connected to it
		{
			// rewrites the number at this building's index to 3 using substrings
			firstHalf = lines[row].substring(0, charIndex);
			secondHalf = lines[row].substring(charIndex + 1, lines[row].length());
			newString = firstHalf + "3" + secondHalf;
			lines[row] = newString;
			
			// rewrites this building's rotation based on which sides its connections are on
			if (up == 1)
			{
				if (left == 1 && right == 1) rewriteRotation(charIndex, row, 2);
				else if (left == 1 && down == 1) rewriteRotation(charIndex, row, 3);
				else rewriteRotation(charIndex, row, 1);
			}
			else rewriteRotation(charIndex, row, 0);
			
			// calls the recalculateTile() function for all buildings around it 
			if (up == 1) recalculateTile(charIndex, row-1);
			if (down == 1) recalculateTile(charIndex, row+1);
			if (left == 1) recalculateTile(leftIndex, row);
			if (right == 1) recalculateTile(rightIndex, row);
		}
		else if (connections == 2) // 2 buildings connected to it
		{
			// grabs the substrings of the text all around the building's index
			firstHalf = lines[row].substring(0, charIndex);
			secondHalf = lines[row].substring(charIndex + 1, lines[row].length());
			
			// need to differentiate between buildings with 2 connections that look
			// like "|" or ones that look like "L"
			if (up - down == 0 || left - right == 0) // checks if it should be a line building
			{
				// rewrites the number at this building's index to 6
				newString = firstHalf + "6" + secondHalf;
				
				// rewrites this building's rotation; only 2 possibilities: | or -
				if (up == 1 && down == 1) rewriteRotation(charIndex, row, 1);
				else rewriteRotation(charIndex, row, 0);
			}
			else // if not a line, must be an L building
			{
				// rewrites the number at this building's index to 2
				newString = firstHalf + "2" + secondHalf;
				
				// rewrites this building's rotation based on which sides its connections are on
				if (up == 1)
				{
					if (right == 1) rewriteRotation(charIndex, row, 1);
					else rewriteRotation(charIndex, row, 2);
				}
				else
				{
					if (left == 1) rewriteRotation(charIndex, row, 3);
					else rewriteRotation(charIndex, row, 0);
				}
			}
			
			lines[row] = newString;
			
			// calls the recalculateTile() function for all buildings around it 
			if (up == 1) recalculateTile(charIndex, row-1);
			if (down == 1) recalculateTile(charIndex, row+1);
			if (left == 1) recalculateTile(leftIndex, row);
			if (right == 1) recalculateTile(rightIndex, row);
		}
		else if (connections == 1) // 1 building connected to it
		{
			// rewrites the number at this building's index to 1 using substrings
			firstHalf = lines[row].substring(0, charIndex);
			secondHalf = lines[row].substring(charIndex + 1, lines[row].length());
			newString = firstHalf + "1" + secondHalf;
			lines[row] = newString;
			
			// rewrite current building's rotation and recalculates connected tile
			if (up == 1) 
			{
				rewriteRotation(charIndex, row, 2);
				recalculateTile(charIndex, row-1);
			}
			if (down == 1) 
			{
				rewriteRotation(charIndex, row, 0);
				recalculateTile(charIndex, row+1);
			}
			if (left == 1) 
			{
				rewriteRotation(charIndex, row, 3);
				recalculateTile(leftIndex, row);
			}
			if (right == 1) 
			{
				rewriteRotation(charIndex, row, 1);
				recalculateTile(rightIndex, row);
			}
		}
		else // no buildings connected
		{
			// rewrites the building number at this index to 5, nothing else needed
			firstHalf = lines[row].substring(0, charIndex);
			secondHalf = lines[row].substring(charIndex + 1, lines[row].length());
			newString = firstHalf + "5" + secondHalf;
			lines[row] = newString;
		}
	}
	
	// rewrites the rotation of the tile based on the arguments passed:
	// col and row self explanatory, rot is the rotation number to be written
	public void rewriteRotation(int col, int row, int rot)
	{
		try
		{
			File file = new File("resources/maps/buildingrotationmap.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String line = br.readLine();
			String[] rotationLines = new String[45];
			
			int arrayNum = 0;
			
			while (line != null)
			{
				rotationLines[arrayNum] = line;
				arrayNum++;
				line = br.readLine();
			}
			
			FileWriter writer = new FileWriter(file);
			
			String firstHalf = rotationLines[row].substring(0, col);
			String secondHalf = rotationLines[row].substring(col + 1, rotationLines[row].length());
			String newString = firstHalf + rot + secondHalf;
			rotationLines[row] = newString;
			
			String newFile = "";
			
			for(int i = 0; i < 40; i++)
			{
				newFile += rotationLines[i];
				if (i != 39) newFile += "\n";
			}
			
			writer.write(newFile);
			
			br.close();
			writer.close();
			
			// reload building rotation array
			Game.buildingManager.loadBuildingRotation();
		} catch (Exception e) 
		{
			System.out.println(e);
		}
	}
	
	// resets buildingmap.txt and buildingrotationmap.txt to a full map of 0's
	public void resetBuildingMap()
	{
		try
		{
			File file1 = new File("resources/maps/buildingmap.txt");
			File file2 = new File("resources/maps/buildingrotationmap.txt");
			FileWriter writer1 = new FileWriter(file1);
			FileWriter writer2 = new FileWriter(file2);
			
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
		int leftIndex = objectLeftWorldX/Game.tileSize;
		int rightIndex = objectRightWorldX/Game.tileSize;
		int topIndex = objectTopWorldY/Game.tileSize;
		int bottomIndex = objectBottomWorldY/Game.tileSize;
		
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
	}
	
	// returns the bounds of a building at its corresponding row and col
	public Rectangle getBuildingBounds(int col, int row)
	{	
		int buildingNum = mapBuildingNum[col][row]; // grab building number
		
		if (buildingNum == 0) return null; // no bounds if theres no building there
		
		// finds the screen coordinates of the tile that the building is one, useful because
		// the returned bounds of the building is in screen coordinates
		int buildingScreenX = (col * Game.tileSize) - Game.player.getWorldX() + Game.player.getScreenX();
		int buildingScreenY = (row * Game.tileSize) - Game.player.getWorldY() + Game.player.getScreenY();
		
		Rectangle returnRectangle = null;
		
		// calculates the bounds of the building based on the type of building and its rotation:
		//
		// all buildings that dont look like "-" or "|" have a square as their bounds 
		// buildings that look like "-" or "|" have a skinnier rectangle as their bound
		if (building[buildingNum].getConnections() != 2) returnRectangle = new Rectangle(buildingScreenX + (int)((1.0/8)*Game.tileSize), buildingScreenY + (int)((1.0/8)*Game.tileSize), (int)(Game.tileSize * (6.0/8)), (int)(Game.tileSize * (6.0/8)));
		else
		{
			if (mapBuildingNum[col-1][row] != 0 && mapBuildingNum[col+1][row] != 0) returnRectangle = new Rectangle(buildingScreenX, buildingScreenY + (int)((7.0/24)*Game.tileSize), Game.tileSize, (int)(Game.tileSize * (10.0/24)));
			else if (mapBuildingNum[col][row-1] != 0 && mapBuildingNum[col][row+1] != 0) returnRectangle = new Rectangle(buildingScreenX + (int)((7.0/24)*Game.tileSize), buildingScreenY, (int)(Game.tileSize * (10.0/24)), Game.tileSize);
			else returnRectangle = new Rectangle(buildingScreenX + (int)((1.0/8)*Game.tileSize), buildingScreenY + (int)((1.0/8)*Game.tileSize), (int)(Game.tileSize * (6.0/8)), (int)(Game.tileSize * (6.0/8)));
		}
		
		return returnRectangle;
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