package view.fullMenu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.stream.Collectors;

import controller.Game;
import controller.Game.GameState;
import util.GraphicsUtil;
import util.ImageUtil;
import util.TxtFileUtil;

public class StoreMenu {
	private final static Rectangle EXIT_BUTTON = new Rectangle(Game.WIDTH-60, 0, 60, 30);
	
	private final static Font FONT = TxtFileUtil.createFont("resources/fonts/menuFont.ttf", 24);
	
	private final static Color BACKGROUND_COLOR = new Color(222, 160, 79);
	private final static Color DARK_BROWN = new Color(127, 72, 0);
	private final static Color BUTTON_COLOR = new Color(54, 52, 17);
	private final static Color TEXT_COLOR = new Color(240, 236, 225);
	
	private final static Rectangle[] displays = new Rectangle[8];
	
	private static boolean displaysLoaded = false;
	
	public static void render(Graphics g) {
		if(!displaysLoaded) {
			loadDisplays();
		}
		
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		g.setFont(FONT);
		g.setColor(Color.BLACK);
		g.drawString("EXIT", EXIT_BUTTON.x+5, EXIT_BUTTON.y+25);
		
		g.setColor(DARK_BROWN);
		for(int i = 0; i < displays.length; i++) {
			g.fillRect(displays[i].x, displays[i].y, displays[i].width, displays[i].height);
		}
	}
	
	public static void checkButton(int x, int y) {
		if(EXIT_BUTTON.contains(x, y)) {
			Game.player.setSpeedY(0);
			Game.gamestate = GameState.Running;
			
		}
	}
	
	private static void loadDisplays() {
		for(int i = 0; i < 8; i+=2) {
			displays[i] = new Rectangle(24 + 2*48*i, 48, 48*3, 48);
			displays[i+1] = new Rectangle(24 + 2*48*i, 120, 48*3, 192);
		}
		displaysLoaded = true;
	}
}
