package model.gameObjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import controller.Game;
import controller.objectHandling.ID;
import model.GameObject;

public class ZombieSpawner extends GameObject
{
	private int spawnRate;
	private Random random = new Random(); // random number generator
	
	private long lastSpawn;
	private boolean spawn = true;
	
	private int numStones = 0;
	private int[][] allTiles;
	private int[][] gravestones;
	
	public ZombieSpawner(int sRate, ID id)
	{
		super(0, 0, id);
		spawnRate = sRate;
		
		allTiles = Game.tileManager.getMap(); // copies a 2d int array of all the tiles in the game
		gravestones = new int[50][2]; // 2d int array that will store the locations of all gravestones in the map, max 50
		
		int col = 0;
		int row = 0;
		
		// filters the array of all the tiles, extracting the gravestone tiles and putting them into the gravestones array
		// arranged as follows:
		// [ X ][ X ][ X ] <-- all the x locations of the gravestones, 
		// [ Y ][ Y ][ Y ] <-- all the y locations of the gravestones, all based on the row and columns
		while (col < Game.mapCol && row < Game.mapRow)
		{
			if (allTiles[col][row] == 13)
			{
				gravestones[numStones][0] = col;
				gravestones[numStones][1] = row;
				numStones++; // keeps track of the number of gravestones extracted
			}
			
			col++;
			
			if (col == Game.mapCol) 
			{
				col = 0;
				row++;
			}
		}
	}

	public void tick() 
	{
		if (spawn)
		{
			int spawnStone = random.nextInt(0, numStones); // chooses a random gravestone from the array
			int zombieSpawnX = gravestones[spawnStone][0] * 48; // calculate worldX and Y of gravestone based on the row and column
			int zombieSpawnY = gravestones[spawnStone][1] * 48;
			
			int offSet = 15; // it spawns the zombies bottom right of the gravestone, subtracting this offset centers it
			
			Game.handler.addObject(new Zombie(zombieSpawnX - offSet, zombieSpawnY - offSet, ID.Zombie)); // create zombie
			
			lastSpawn = System.currentTimeMillis();
			spawn = false;
		}
		else
		{
			// will wait a default of 3 seconds minues the passed spawnRate of the spawner, each number represents 1/10 of a second
			// ex. spawnRate of 0 would be 3 seconds per spawn, spawnRate of 5 would be 2.5s, spawnRate of 10 would be 2s, etc.
			if (System.currentTimeMillis() > (lastSpawn + 3000 - (spawnRate * 100)))
			{
				spawn = true;
			}
		}
	}

	public void render(Graphics g) {}
	
	@Override
	public Rectangle getBounds() {return null;}

	@Override
	public Rectangle getSize() {return null;}
}