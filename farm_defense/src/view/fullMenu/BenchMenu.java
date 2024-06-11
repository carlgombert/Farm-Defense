package view.fullMenu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import controller.Game;
import controller.Game.GameState;
import model.Sound;
import model.items.Item;
import model.items.ItemManager;
import util.ImageUtil;
import util.TxtFileUtil;

public class BenchMenu {
	private final static Rectangle EXIT_BUTTON = new Rectangle(Game.WIDTH-60, 0, 60, 30);
	
	private final static Font FONT_LARGE = TxtFileUtil.createFont("resources/fonts/menuFont.ttf", 24);
	private final static Font FONT_SMALL = TxtFileUtil.createFont("resources/fonts/menuFont.ttf", 14);
	
	private final static Image SOLD_OUT_ICON = ImageUtil.addImage(150, 150, "resources/icon/sold_out.png");
	
	private final static Color DARK_BROWN = new Color(127, 72, 0);
	private final static Color BACKGROUND_COLOR = new Color(222, 160, 79);
	
	private final static Rectangle[] displays = new Rectangle[8];
	
	private static boolean displaysLoaded = false;
	
	private final static Rectangle BUY_1_BUTTON = new Rectangle(24, 48*7, 3*48, 48);
	
	private final static Rectangle BUY_2_BUTTON = new Rectangle(BUY_1_BUTTON.x+4*48, 48*7, 3*48, 48);
	
	private final static Rectangle BUY_3_BUTTON = new Rectangle(BUY_2_BUTTON.x+4*48, 48*7, 3*48, 48);
	
	private final static Rectangle BUY_4_BUTTON = new Rectangle(BUY_3_BUTTON.x+4*48, 48*7, 3*48, 48);
	
	private static Item[] items = {
			ItemManager.getItem(20),
			ItemManager.getItem(50),
			ItemManager.getItem(51),
			ItemManager.getItem(30)
	};
	
	public static void render(Graphics g) {
		if(!displaysLoaded) {
			loadDisplays();
		}
		
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		g.setFont(FONT_LARGE);
		g.setColor(Color.BLACK);
		g.drawString("EXIT", EXIT_BUTTON.x+5, EXIT_BUTTON.y+25);
		
		g.drawString("COINS: "+Game.player.getCoins(), 5, 25);
		
		for(int i = 0; i < displays.length; i++) {
			g.setColor(DARK_BROWN);
			g.fillRect(displays[i].x, displays[i].y, displays[i].width, displays[i].height);
			
			if(i % 2 == 0) {
				g.setColor(Color.WHITE);
				g.setFont(FONT_SMALL);
				g.drawString(items[i/2].getName(), displays[i].x+5, displays[i].y+25);
			}
			else {
				g.setColor(Color.WHITE);
				g.drawImage(items[i/2].getLargerImage(), displays[i].x+24, displays[i].y+48, null);
				g.drawString("Cost: "+ items[i/2].getCost(), displays[i].x+24, displays[i].y+12*15);
			}
		}
		
		g.setColor(DARK_BROWN);
		
		g.fillRect(BUY_1_BUTTON.x, BUY_1_BUTTON.y, BUY_1_BUTTON.width, BUY_1_BUTTON.height);
		g.fillRect(BUY_3_BUTTON.x, BUY_3_BUTTON.y, BUY_3_BUTTON.width, BUY_3_BUTTON.height);
		g.fillRect(BUY_4_BUTTON.x, BUY_4_BUTTON.y, BUY_4_BUTTON.width, BUY_4_BUTTON.height);
		
		g.fillRect(BUY_2_BUTTON.x, BUY_2_BUTTON.y, BUY_2_BUTTON.width, BUY_2_BUTTON.height);
		
		g.setColor(Color.WHITE);
		g.setFont(FONT_SMALL);
		g.drawString("BUY", BUY_1_BUTTON.x+6*9+1, BUY_1_BUTTON.y+28);
		g.drawString("BUY", BUY_2_BUTTON.x+6*9+1, BUY_2_BUTTON.y+28);
		g.drawString("BUY", BUY_3_BUTTON.x+6*9+1, BUY_3_BUTTON.y+28);
		g.drawString("BUY", BUY_4_BUTTON.x+6*9+1, BUY_4_BUTTON.y+28);
		
	}
	
	public static void checkButton(int x, int y) {
		if(EXIT_BUTTON.contains(x, y)) {
			Sound.clickSound();
			Game.player.setSpeedY(0);
			Game.gamestate = GameState.Running;
		}
		if(BUY_1_BUTTON.contains(x, y)) {
			if(items[0].getCost() <= Game.player.getCoins()) {
				Sound.buySound();
				Game.inventory.addItem(items[0].getID(), 1);
				Game.player.setCoins(Game.player.getCoins() -  items[0].getCost());
			}
		}
		if(BUY_2_BUTTON.contains(x, y) && !maxTurrets) {
			if(items[1].getCost() <= Game.player.getCoins()) {
				Sound.buySound();
				Game.inventory.addItem(items[1].getID(), 1);
				Game.player.setCoins(Game.player.getCoins() -  items[1].getCost());
				if(items[1].getID() == 50) {
					items[1].setCost(items[1].getCost() + turretsPurchased*100);
					if(turretsPurchased > 3) {
						maxTurrets = true;
					}
				}
			}
		}
		if(BUY_3_BUTTON.contains(x, y)) {
			if(items[2].getCost() <= Game.player.getCoins()) {
				Sound.buySound();
				Game.inventory.addItem(items[2].getID(), 1);
				Game.player.setCoins(Game.player.getCoins() -  items[2].getCost());
			}
		}
		if(BUY_4_BUTTON.contains(x, y)) {
			if(items[3].getCost() <= Game.player.getCoins()) {
				Sound.buySound();
				Game.inventory.addItem(items[3].getID(), 1);
				Game.player.setCoins(Game.player.getCoins() -  items[3].getCost());
			}
		}
		if(SELL_BUTTON.contains(x, y)) {
			for(int i = 40; i < 50; i++) {
				if(Game.inventory.getItems().containsKey(i)) {
					Sound.sellSound();
					int price = Game.inventory.getItems().get(i).getPrice();
					Game.player.setCoins(Game.player.getCoins() + Game.inventory.clearItem(i) * price);
				}
			}
		}
	}
	
	private static void loadDisplays() {
		for(int i = 0; i < 8; i+=2) {
			displays[i] = new Rectangle(24 + 2*48*i, 48, 48*3, 48);
			displays[i+1] = new Rectangle(24 + 2*48*i, 120, 48*3, 192);
		}
		displaysLoaded = true;
	}
	
	public static void updateItems() {
		int days = (int)Math.round(Game.nightCount);
		if(days == 2) {
			items[0] = ItemManager.getItem(21);
		}
		if(days < 7) {
			items[3] = ItemManager.getItem(30+days);
		}
	}
}
