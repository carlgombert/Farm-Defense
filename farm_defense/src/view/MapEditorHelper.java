package view;

import java.awt.Color;
import java.awt.Graphics;

import controller.Game;


// makes sure that the highlight that is drawn on the tiles where the mouse is when
// the player is in building or tilling mode gets rendered last, so it is drawn over all buildings,
// farmlands, crops, tiles, etc.
public class MapEditorHelper 
{	
	public int mouseX;
	public int mouseY;
	
	public int mouseWorldX;
	public int mouseWorldY;
	
	public void render(Graphics g)
	{
		// draw white highlight on tiles when player is in build mode, tilling mode, or planting mode
		if (Game.player.getWeaponState() == Game.player.stateTilling() 
		 || Game.player.getWeaponState() == Game.player.stateBuild()
		 || Game.player.getWeaponState() == Game.player.statePlanting()) 
		{
			// rendering
			Color whiteOverlay = new Color(255, 255, 255, 50);
			
			// reverts the screen mouse coordinates to world coordinates 
			mouseWorldX = mouseX + Game.player.getWorldX() - Game.player.getScreenX();
			mouseWorldY = mouseY + Game.player.getWorldY() - Game.player.getScreenY();
			
			// gets the tile coordinates that the mouse is on and reverts them back to screen coordinates
			int tileX = (mouseWorldX - (mouseWorldX%48)) - Game.player.getWorldX() + Game.player.getScreenX();
			int tileY = (mouseWorldY - (mouseWorldY%48)) - Game.player.getWorldY() + Game.player.getScreenY();
			
			// draw highlight
			g.setColor(whiteOverlay);
			g.fillRect(tileX, tileY, 48, 48);
			
			// draw transparent inventory slot item by mouse
			g.drawImage(Game.inventory.getCurrentImage(), mouseX + 10, mouseY + 10, Game.tileSize / 2, Game.tileSize / 2, null);
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
