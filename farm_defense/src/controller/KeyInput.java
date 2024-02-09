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
import model.gameObjects.NPC;
import model.gameObjects.Player;
import model.gameObjects.Projectile;
import model.gameObjects.Zombie;
import util.MathUtil;
import view.map.TileManager;

public class KeyInput extends KeyAdapter implements MouseListener, MouseMotionListener
{
	
	private Player player;
	
	private boolean canShoot = true;
	private boolean canReload = true;
	
	private boolean interactionBoolean = false;
	
	private int mouseX;
	private int mouseY;
	
	public KeyInput(Player player) 
	{
		this.player = player;
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}
	
	public void mouseReleased(MouseEvent e) 
	{
		canShoot = true;
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
				Game.handler.addObject(bullet);
				
				canShoot = false; // prevents the player from being able to shoot infinitely fast by holding down the mouse button
				player.setAmmo(player.getAmmo() - 1); // remove 1 from the player's ammo
			}
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
		
		// 1, 2, and 3 change the weapon state of the player. build mode (3) is temporary, it will probably
		// be different in the future
		if (key == KeyEvent.VK_1) player.setWeaponState(player.stateGun());
		if (key == KeyEvent.VK_2) player.setWeaponState(player.stateMelee());
		if (key == KeyEvent.VK_3) player.setWeaponState(player.stateBuild());
			
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
		// sends the coordinates of the mouse to the tilemanager if the player is in build mode
		// in order to draw the highlight on the tiles
		if (player.getWeaponState() == player.stateBuild())
		{
			TileManager.setMouseX(e.getX());
			TileManager.setMouseY(e.getY());
		}
	}
	
	public void mouseDragged(MouseEvent e) 
	{
		// sends the coordinates of the mouse to the tilemanager if the player is in build mode
		// in order to draw the highlight on the tiles
		if (player.getWeaponState() == player.stateBuild())
		{
			TileManager.setMouseX(e.getX());
			TileManager.setMouseY(e.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
