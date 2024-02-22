package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import controller.Game;
import model.Inventory.InventoryItem;
import model.gameObjects.Player;

public class HUD {
	
	private Player player;
	
	private Color darkBrown = new Color(127, 72, 0);
	private Color lightBrown = new Color(222, 160, 79);
	
	private int inventoryWidth = Game.HEIGHT / 14;
	private int inventoryHeight = inventoryWidth * 10;
	
	private int inventoryStartY = (Game.HEIGHT - inventoryHeight) / 2;
	
	// the small distance between the edge of the light brown and the edge of the dark brown in the inventory
	private int padding = inventoryWidth / 10;
	
	private int inventorySelected = 0;
	
	public HUD(Player player) {
		this.player = player;
	}
	
	public void render(Graphics g) 
	{
		g.setColor(Color.black);
		g.fillRect(13, 13, 204, 36);
		
		g.setColor(new Color(214, 48, 33));
		g.fillRect(15, 15, 200, 32);
		
		g.setColor(new Color(97, 172, 49));
		g.fillRect(15, 15, (int) Math.round(player.getHealth() / 2), 32);
		
		g.setFont(null);
		g.setColor(Color.white);
		g.drawString(player.getHealth() + "/400", 13, 65);
		
		g.setColor(new Color(148, 141, 62));
		for(int i = 0; i < player.getAmmo(); i++) {
			g.fillOval(540 + (20*i), 25, 8, 16);
		}
		
		g.setColor(Color.white);
		g.drawString("" + player.getAmmo(), 540, 60);
		
		// render inventory backdrop
		g.setColor(darkBrown);
		g.fillRect(0, inventoryStartY, inventoryWidth, inventoryHeight);
		
		// render slots in inventory
		g.setColor(lightBrown);
		for (int i = 0; i < 10; i++)
		{
			g.fillRect(padding, (inventoryWidth * i) + inventoryStartY + padding, inventoryWidth - (padding * 2), inventoryWidth - (padding * 2));
		}
		
		// render selection box in inventory
		g.setColor(Color.white);
		g.fillRect(0, inventoryStartY + (inventoryWidth * inventorySelected), padding, inventoryWidth);
		g.fillRect(0, inventoryStartY + (inventoryWidth * inventorySelected) - padding, inventoryWidth, padding*2);
		g.fillRect(padding + getInventoryBoxWidth(), inventoryStartY + (inventoryWidth * inventorySelected), padding*2, inventoryWidth);
		g.fillRect(0, inventoryStartY + (inventoryWidth * inventorySelected) + padding + getInventoryBoxWidth(), inventoryWidth, padding*2);
		
		// selection box rounded corners
		g.fillRoundRect(inventoryWidth - padding, inventoryStartY - padding + (inventorySelected * inventoryWidth), padding * 2, padding * 2, 10, 10);
		g.fillRoundRect(inventoryWidth - padding, inventoryStartY + getInventoryBoxWidth() + padding + (inventorySelected * inventoryWidth), padding * 2, padding * 2, 10, 10);
		
		// render inventory item and its count if its above 1
		InventoryItem[] playerInventory = Game.inventory.getInventory();
		
		for (int i = 0; i < playerInventory.length; i++)
		{
			if (playerInventory[i] != null) 
			{
				g.drawImage(playerInventory[i].getImage(), padding, inventoryStartY + padding + (i * inventoryWidth), getInventoryBoxWidth(), getInventoryBoxWidth(), null);
				
				g.setColor(Color.white);
				g.setFont(new Font("TimesRoman", Font.PLAIN, padding * 4)); 
				if (playerInventory[i].getCount() > 1) g.drawString("" + playerInventory[i].getCount(), padding, inventoryStartY + getInventoryBoxWidth() + padding + (inventoryWidth * i));
			}
		}
	}
	
	// width of the light brown slots in the inventory
	public int getInventoryBoxWidth() 
	{
		return inventoryWidth - (padding * 2);
	}
	
	public void setInventorySelection(int s)
	{
		inventorySelected = s;
	}
}
