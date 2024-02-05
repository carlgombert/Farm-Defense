package controller;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import controller.objectHandling.ID;
import model.gameObjects.Player;
import model.gameObjects.Projectile;
import util.MathUtil;

public class KeyInput extends KeyAdapter implements MouseListener{
	
	private Player player;
	
	private boolean canShoot = true;
	private boolean canReload = true;
	
	private int mouseX;
	private int mouseY;
	
	public KeyInput(Player player) {
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
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
			
		if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) player.setSpeedY(-5);
		if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) player.setSpeedY(5);
		if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) player.setSpeedX(-5);
		if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) player.setSpeedX(5);
		
		if(key == KeyEvent.VK_R && canReload == true)
		{
			player.setAmmo(10);
			canReload = false;
		}
			
		if(key == KeyEvent.VK_ESCAPE) {
			System.exit(1);
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) player.setSpeedY(0);
		if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) player.setSpeedY(0);
		if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) player.setSpeedX(0);
		if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) player.setSpeedX(0);
		
		if(key == KeyEvent.VK_R) canReload = true;
	}

	

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
