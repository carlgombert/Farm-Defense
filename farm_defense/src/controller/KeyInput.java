package controller;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import controller.objectHandling.ID;
import model.GameObject;
import model.Sound;
import model.gameObjects.NPC;
import model.gameObjects.Player;
import model.gameObjects.Zombie;
import model.gameObjects.projectile.Projectile;
import util.MathUtil;
import view.map.TileManager;

public class KeyInput extends KeyAdapter implements MouseListener, MouseMotionListener, ActionListener
{
	
	private Player player;
	
	private boolean canShoot = true;
	private boolean canReload = true;
	
	private boolean canBuild = true;
	private boolean canFarm = true;
	private boolean canPlant = true;
	
	private boolean interactionBoolean = false;
	
	private int mouseX;
	private int mouseY;
	
	public KeyInput() 
	{
		this.player = Game.player;
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}
	
	public void mouseReleased(MouseEvent e) 
	{
		canShoot = true;
		canBuild = true;
		canFarm = true;
		canPlant = true;
	}
	
	public void mousePressed(MouseEvent e) 
	{
		if (player.getWeaponState() == player.stateGun()) // only shoots if the player's weapon state is 'Gun'
		{
			if (canShoot == true && player.getAmmo() != 0)
			{
				int offset = 35; // for some reason getWorldX/Y is offset from the player, this counteracts that
				
				// creates spawn variables for where the bullet should spawn
				int spawnx = player.getWorldX() + offset;
				int spawny = player.getWorldY() + offset;
				
				// calculates the mouse X and Y relative to the player
				mouseX = (e.getX() - player.getScreenX() - offset);
				mouseY = (e.getY() - player.getScreenY() - offset);
				
				// calculates the angle between the player and the current mouse location
				double angle = Math.atan2(mouseY, mouseX); // atan2 seems to work better than atan, idk why
	
				// create bullet
				Projectile bullet = new Projectile(spawnx, spawny, ID.Projectile, angle);
				Sound.pistolSound();
				Game.handler.addObject(bullet);
				
				canShoot = false; // prevents the player from being able to shoot infinitely fast by holding down the mouse button
				player.setAmmo(player.getAmmo() - 1); // remove 1 from the player's ammo
			}
		}
		else if (player.getWeaponState() == player.stateBuild() && canBuild)
		{
			canBuild = false;
			
			Game.buildingManager.setMouseX(e.getX());
			Game.buildingManager.setMouseY(e.getY());
			
			// tells buildingManager to create a building
			Game.buildingManager.createBuilding();
		}
		else if (player.getWeaponState() == player.stateTilling() && canFarm)
		{
			canFarm = false;
			
			Game.farmingManager.setMouseX(e.getX());
			Game.farmingManager.setMouseY(e.getY());
			
			// tells farmingManager to create a farmland tile
			Game.farmingManager.tillFarmland();
		}
		else if (player.getWeaponState() == player.statePlanting() && canPlant)
		{
			canPlant = false;
			
			Game.farmingManager.setMouseX(e.getX());
			Game.farmingManager.setMouseY(e.getY());
			
			// tells farmingManager to plant a seed on selected farmland tile
			Game.farmingManager.plantSeed();
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
			
		if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) player.setUp(-5);
		if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) player.setDown(5);
		if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) player.setLeft(-5);
		if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) player.setRight(5);
		
		if(key == KeyEvent.VK_R && canReload == true) // player reload
		{
			player.setAmmo(10);
			canReload = false;
			Sound.reloadSound();
		}
		
		// checks the handler for NPCs within the player's hitbox, and sets their interaction boolean to true.
		// this allows us to handle what happens when the npc is interacted with within the npc object rather
		// than in here.
		if(key == KeyEvent.VK_E && !interactionBoolean)
		{
			interactionBoolean = true;
			
			LinkedList<NPC> NPCs = new LinkedList<NPC>();
			
			for(int i = 0; i < Game.handler.object.size(); i++) 
			{
				GameObject tempObject = Game.handler.object.get(i);
				if (tempObject.getId() == ID.NPC) 
				{
					NPCs.add((NPC)tempObject);
				}
			}
			
			for (int j = 0; j < NPCs.size(); j++)
			{
				NPC tempNPC = NPCs.get(j);
				
				if(Game.player.getBounds().intersects(tempNPC.getBounds()))
				{
					tempNPC.setInteraction(true);
				}
			}
		}
		
		if (key == KeyEvent.VK_F) Game.inventory.addItem(30, 5); // adds 5 carrot seeds to the players inventory (for now)
		if (key == KeyEvent.VK_G) Game.inventory.addItem(20, 5); // adds 5 wood walls to the players inventory (for now)
		if (key == KeyEvent.VK_H) Game.farmingManager.advanceAllStages(); // advances the stage of all the crops on the map (for now)
		
		
		if(Game.tm.visible) { //if trade menu is open, number key bindings swap to buying items
			if (key == KeyEvent.VK_1) Game.tm.buy(1);;
			if (key == KeyEvent.VK_2) Game.tm.buy(2);;
			if (key == KeyEvent.VK_3) Game.tm.buy(3);;
			if (key == KeyEvent.VK_4) Game.tm.buy(4);;
			if (key == KeyEvent.VK_5) Game.tm.buy(5);;
			if (key == KeyEvent.VK_6) Game.tm.buy(6);;
			if (key == KeyEvent.VK_7) Game.tm.buy(7);;
			if (key == KeyEvent.VK_8) Game.tm.buy(8);;
			if (key == KeyEvent.VK_9) Game.tm.buy(9);;
			if (key == KeyEvent.VK_0) Game.tm.buy(10);;
		}
		else if(Game.turm.visible) { // if turret upgrade menu is open, number key bindings swap to upgrading
			if (key == KeyEvent.VK_1) Game.turm.buy(1);;
			if (key == KeyEvent.VK_2) Game.turm.buy(2);;
			if (key == KeyEvent.VK_3) Game.turm.buy(3);;
		}
		else {
			if (key == KeyEvent.VK_1) Game.inventory.setSelected(0);;
			if (key == KeyEvent.VK_2) Game.inventory.setSelected(1);;
			if (key == KeyEvent.VK_3) Game.inventory.setSelected(2);;
			if (key == KeyEvent.VK_4) Game.inventory.setSelected(3);;
			if (key == KeyEvent.VK_5) Game.inventory.setSelected(4);;
			if (key == KeyEvent.VK_6) Game.inventory.setSelected(5);;
			if (key == KeyEvent.VK_7) Game.inventory.setSelected(6);;
			if (key == KeyEvent.VK_8) Game.inventory.setSelected(7);;
			if (key == KeyEvent.VK_9) Game.inventory.setSelected(8);;
			if (key == KeyEvent.VK_0) Game.inventory.setSelected(9);;
		}
			
		if(key == KeyEvent.VK_ESCAPE) {
			System.exit(1);
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if((key == KeyEvent.VK_UP || key == KeyEvent.VK_W)) player.setUp(0);
		if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) player.setDown(0);
		if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) player.setLeft(0);
		if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) player.setRight(0);
		
		if(key == KeyEvent.VK_R) canReload = true;
		if(key == KeyEvent.VK_E) interactionBoolean = false;
	}

	public void mouseMoved(MouseEvent e)
	{
		// sends the mouse coordinates to the mousehelper so it can render highlight on tiles
		Game.mapHelper.setMouseX(e.getX());
		Game.mapHelper.setMouseY(e.getY());
	}
	
	public void mouseDragged(MouseEvent e) 
	{
		// sends the mouse coordinates to the mousehelper so it can render highlight on tiles
		Game.mapHelper.setMouseX(e.getX());
		Game.mapHelper.setMouseY(e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
