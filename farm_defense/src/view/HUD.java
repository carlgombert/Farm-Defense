package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import controller.Game;
import controller.Game.GameState;
import model.Sound;
import model.Inventory.InventoryItem;
import model.gameObjects.Player;
import util.TxtFileUtil;

public class HUD {
	
	private Player player;
	
	private final Color DARK_BROWN = new Color(127, 72, 0);
	private final Color LIGHT_BROWN = new Color(222, 160, 79);
	
	private final int INVENTORY_WIDTH = Game.HEIGHT / 14;
	private final int INVENTORY_HEIGHT = INVENTORY_WIDTH * 10;
	
	private final int INVENTORY_START_Y = (Game.HEIGHT - INVENTORY_HEIGHT) / 2;
	
	private final Rectangle INVENTORY_BOUNDS = new Rectangle(0, INVENTORY_START_Y, INVENTORY_WIDTH, INVENTORY_HEIGHT);
	
	private final Font FONT = TxtFileUtil.createFont("resources/fonts/menuFont.ttf", 24);
	
	// the small distance between the edge of the light brown and the edge of the dark brown in the inventory
	private final int PADDING = INVENTORY_WIDTH / 10;
	
	private final Rectangle PAUSE_BUTTON = new Rectangle(Game.WIDTH-30, 0, 30, 30);
	
	private final Rectangle START_NIGHT_BUTTON = new Rectangle(Game.WIDTH-90, Game.HEIGHT-60, 80, 25);
	
	private final Rectangle CLOCK = new Rectangle(7*48, 0, 2*48, 48);
	
	private int inventorySelected = 0;
	
	public HUD(Player player) {
		this.player = player;
	}
	
	public void render(Graphics g) 
	{
		g.setFont(Game.DEFAULT_FONT);
		g.setColor(Color.black);
		g.fillRect(13, 13, 204, 36);
		
		g.setColor(new Color(214, 48, 33));
		g.fillRect(15, 15, 200, 32);
		
		g.setColor(new Color(97, 172, 49));
		g.fillRect(15, 15, (int) Math.round(player.getHealth() / 2), 32);
		
		g.setColor(Color.white);
		g.drawString(player.getHealth() + "/400\tCoins: " + player.getCoins(), 13, 65);
		
		g.setFont(FONT);
		g.setColor(LIGHT_BROWN);
		g.drawString("| |", PAUSE_BUTTON.x+10, PAUSE_BUTTON.y+25);
		
		g.setFont(Game.DEFAULT_FONT);
		g.setColor(new Color(148, 141, 62));
		for(int i = 0; i < player.getAmmo(); i++) {
			g.fillOval(540 + (20*i), 35, 8, 16);
		}
		
		g.setFont(null);
		g.setColor(Color.white);
		g.drawString("" + player.getAmmo(), 540, 70);
		
		// render inventory backdrop
		g.setColor(DARK_BROWN);
		g.fillRect(0, INVENTORY_START_Y, INVENTORY_WIDTH, INVENTORY_HEIGHT);
		
		// render slots in inventory
		g.setColor(LIGHT_BROWN);
		for (int i = 0; i < 10; i++)
		{
			g.fillRect(PADDING, (INVENTORY_WIDTH * i) + INVENTORY_START_Y + PADDING, INVENTORY_WIDTH - (PADDING * 2), INVENTORY_WIDTH - (PADDING * 2));
		}
		
		// render selection box in inventory
		g.setColor(Color.white);
		g.fillRect(0, INVENTORY_START_Y + (INVENTORY_WIDTH * inventorySelected), PADDING, INVENTORY_WIDTH);
		g.fillRect(0, INVENTORY_START_Y + (INVENTORY_WIDTH * inventorySelected) - PADDING, INVENTORY_WIDTH, PADDING*2);
		g.fillRect(PADDING + getInventoryBoxWidth(), INVENTORY_START_Y + (INVENTORY_WIDTH * inventorySelected), PADDING*2, INVENTORY_WIDTH);
		g.fillRect(0, INVENTORY_START_Y + (INVENTORY_WIDTH * inventorySelected) + PADDING + getInventoryBoxWidth(), INVENTORY_WIDTH, PADDING*2);
		
		// selection box rounded corners
		g.fillRoundRect(INVENTORY_WIDTH - PADDING, INVENTORY_START_Y - PADDING + (inventorySelected * INVENTORY_WIDTH), PADDING * 2, PADDING * 2, 10, 10);
		g.fillRoundRect(INVENTORY_WIDTH - PADDING, INVENTORY_START_Y + getInventoryBoxWidth() + PADDING + (inventorySelected * INVENTORY_WIDTH), PADDING * 2, PADDING * 2, 10, 10);
		
		// render inventory item and its count if its above 1
		InventoryItem[] playerInventory = Game.inventory.getInventory();
		
		for (int i = 0; i < playerInventory.length; i++)
		{
			if (playerInventory[i] != null) 
			{
				g.drawImage(playerInventory[i].getImage(), PADDING, INVENTORY_START_Y + PADDING + (i * INVENTORY_WIDTH), getInventoryBoxWidth(), getInventoryBoxWidth(), null);
				
				g.setColor(Color.white);
				g.setFont(new Font("TimesRoman", Font.PLAIN, PADDING * 4)); 
				if (playerInventory[i].getCount() > 1) g.drawString("" + playerInventory[i].getCount(), PADDING, INVENTORY_START_Y + getInventoryBoxWidth() + PADDING + (INVENTORY_WIDTH * i));
			}
		}
		
		if(!Game.night) {
			g.setColor(Color.red.darker());	
			g.fillRect(START_NIGHT_BUTTON.x, START_NIGHT_BUTTON.y, START_NIGHT_BUTTON.width, START_NIGHT_BUTTON.height);
			
			g.setFont(Game.DEFAULT_FONT);
			g.setColor(Color.WHITE);
			g.drawString("Start Night", START_NIGHT_BUTTON.x+6, START_NIGHT_BUTTON.y+17);
		}
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(CLOCK.x+5, CLOCK.y+5, CLOCK.width-10, CLOCK.height-10);
		
		g.setFont(Game.DEFAULT_FONT_LARGE);
		g.setColor(Color.red);
		if(Game.timeHours < 10) {
			g.drawString("0"+Game.timeHours, CLOCK.x+13, CLOCK.y+32);
		}
		else {
			g.drawString(""+Game.timeHours, CLOCK.x+13, CLOCK.y+32);
		}
		g.drawString(":", CLOCK.x+48-3, CLOCK.y+32);
		
		if(Game.timeMinutes < 10) {
			g.drawString("0"+Game.timeMinutes, CLOCK.x+48+3, CLOCK.y+32);
		}
		else {
			g.drawString(""+Game.timeMinutes, CLOCK.x+48+3, CLOCK.y+32);
		}
	}
	
	// width of the light brown slots in the inventory
	public int getInventoryBoxWidth() 
	{
		return INVENTORY_WIDTH - (PADDING * 2);
	}
	
	public void setInventorySelection(int s)
	{
		inventorySelected = s;
	}
	
	public boolean checkButton(int x, int y) {
		if(PAUSE_BUTTON.contains(x, y)) {
			Sound.clickSound();
			Game.gamestate = GameState.Paused;
			return true;
		}
		if(START_NIGHT_BUTTON.contains(x, y) && !Game.night) {
			Sound.clickSound();
			Game.switchNight();
			return true;
		}
		if(INVENTORY_BOUNDS.contains(x, y)) {
			y -= INVENTORY_START_Y;
			int slotClicked = y / INVENTORY_WIDTH;
			Game.inventory.setSelected(slotClicked);
			return true;
		}
		return false;
	}
}
