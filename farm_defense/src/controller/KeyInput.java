package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import controller.Game.GameState;
import controller.objectHandling.ID;
import model.GameObject;
import model.Sound;
import model.gameObjects.NPC;
import model.gameObjects.Projectile;
import model.gameObjects.Turret;
import view.fullMenu.BankruptMenu;
import view.fullMenu.DeathMenu;
import view.fullMenu.MainMenu;
import view.fullMenu.PauseMenu;
import view.map.LightManager;

/**
 * The KeyInput class handles the mouse and key input from the user.
 */
public class KeyInput extends KeyAdapter implements MouseListener, MouseMotionListener, ActionListener
{	
	private boolean canShoot = true;
	private boolean canReload = true;
	
	private boolean canBuild = true;
	private boolean canFarm = true;
	private boolean canPlant = true;
	private boolean canTurret = true;
	private boolean canTorch = true;
	
	private boolean interactionBoolean = false;
	
	private int mouseX;
	private int mouseY;
	
	public KeyInput() {}
	
	public void mouseClicked(MouseEvent e) {
		
	}
	
	/**
     * After mouse release, Resets flags for various actions to allow them in the next frame.
     */
	public void mouseReleased(MouseEvent e) 
	{
		canShoot = true;
		canBuild = true;
		canFarm = true;
		canPlant = true;
		canTurret = true;
		canTorch = true;
	}
	
	public void mousePressed(MouseEvent e) 
	{
		if(Game.gamestate == GameState.Running) {
			if (Game.player.getWeaponState() == Game.player.stateGun()) // only shoots if the Game.player's weapon state is 'Gun'
			{
				if (canShoot == true && Game.player.getAmmo() != 0)
				{
					int offset = 35; // for some reason getWorldX/Y is offset from the Game.player, this counteracts that
					
					// creates spawn variables for where the bullet should spawn
					int spawnx = Game.player.getWorldX() + offset;
					int spawny = Game.player.getWorldY() + offset;
					
					// calculates the mouse X and Y relative to the Game.player
					mouseX = (e.getX() - Game.player.getScreenX() - offset);
					mouseY = (e.getY() - Game.player.getScreenY() - offset);
					
					// calculates the angle between the Game.player and the current mouse location
					double angle = Math.atan2(mouseY, mouseX); // atan2 seems to work better than atan, idk why
		
					// create bullet
					Projectile bullet = new Projectile(spawnx, spawny, ID.Projectile, angle);
					Sound.pistolSound();
					Game.handler.addObject(bullet);
					
					canShoot = false; // prevents the Game.player from being able to shoot infinitely fast by holding down the mouse button
					Game.player.setAmmo(Game.player.getAmmo() - 1); // remove 1 from the Game.player's ammo
				}
			}
			else if (Game.player.getWeaponState() == Game.player.stateBuild() && canBuild)
			{
				canBuild = false;
				
				Game.buildingManager.setMouseX(e.getX());
				Game.buildingManager.setMouseY(e.getY());
				
				// tells buildingManager to create a building
				Game.buildingManager.createBuilding();
			}
			else if (Game.player.getWeaponState() == Game.player.stateTilling() && canFarm)
			{
				canFarm = false;
				
				Game.farmingManager.setMouseX(e.getX());
				Game.farmingManager.setMouseY(e.getY());
				
				// tells farmingManager to create a farmland tile
				Game.farmingManager.tillFarmland();
			}
			else if (Game.player.getWeaponState() == Game.player.statePlanting() && canPlant)
			{
				canPlant = false;
				
				Game.farmingManager.setMouseX(e.getX());
				Game.farmingManager.setMouseY(e.getY());
				
				// tells farmingManager to plant a seed on selected farmland tile
				Game.farmingManager.plantSeed();
			}
			else if (Game.player.getWeaponState() == Game.player.stateTurret() && canTurret)
			{
				canTurret = false;
				
				// reverts the screen mouse coordinates to world coordinates 
				int mouseWorldX = e.getX() + Game.player.getWorldX() - Game.player.getScreenX();
				int mouseWorldY = e.getY() + Game.player.getWorldY() - Game.player.getScreenY();
				
				// adjust turret coordinates to fit in tile
				int turretX = (mouseWorldX - (mouseWorldX%48));
				int turretY = (mouseWorldY - (mouseWorldY%48));
				
				//center turret coordinates in tile
				turretX += 4;
				turretY += 4;
				
				Game.handler.addObject(new Turret(turretX, turretY, ID.Turret));
				
				Game.inventory.minusItem(1);
			}
			else if (Game.player.getWeaponState() == Game.player.stateTorch() && canTorch)
			{
				canTorch = false;
				
				LightManager.addLight(
						e.getX() - Game.player.getScreenX() + Game.player.getWorldX(), 
						e.getY() - Game.player.getScreenY() + Game.player.getWorldY());
				
				Game.inventory.minusItem(1);
			}
			Game.hud.checkButton(e.getX(), e.getY());
		}
		if(Game.gamestate == GameState.MainMenu) {
			MainMenu.checkButton(e.getX(), e.getY());
		}
		if(Game.gamestate == GameState.Paused) {
			PauseMenu.checkButton(e.getX(), e.getY());
		}
		if(Game.gamestate == GameState.Dead) {
			DeathMenu.checkButton(e.getX(), e.getY());
		}
		if(Game.gamestate == GameState.Bankrupt) {
			BankruptMenu.checkButton(e.getX(), e.getY());
		}
	}
	
	public void keyPressed(KeyEvent e) {
		if(Game.gamestate == GameState.Running) {
			int key = e.getKeyCode();
				
			if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) Game.player.setUp(-5);
			if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) Game.player.setDown(5);
			if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) Game.player.setLeft(-5);
			if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) Game.player.setRight(5);
			
			if(key == KeyEvent.VK_R && canReload == true) // Game.player reload
			{
				Game.player.setAmmo(10);
				canReload = false;
				Sound.reloadSound();
			}
			
			// checks the handler for NPCs within the Game.player's hitbox, and sets their interaction boolean to true.
			// this allows us to handle what happens when the npc is interacted with within the npc object rather
			// than in here.
			if(key == KeyEvent.VK_E && !interactionBoolean)
			{
				interactionBoolean = true;
				
				LinkedList<NPC> NPCs = new LinkedList<NPC>();
				
				for(int i = 0; i < Game.handler.getObject().size(); i++) 
				{
					GameObject tempObject = Game.handler.getObject().get(i);
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
			
			
			if(Game.tm.isVisible() && !Game.night) { //if trade menu is open, number key bindings swap to buying items
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
				if (key == KeyEvent.VK_Q) Game.tm.sell();;
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
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if((key == KeyEvent.VK_UP || key == KeyEvent.VK_W)) Game.player.setUp(0);
		if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) Game.player.setDown(0);
		if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) Game.player.setLeft(0);
		if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) Game.player.setRight(0);
		
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
