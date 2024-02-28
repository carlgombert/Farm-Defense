package view.fullMenu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.stream.Collectors;
import java.awt.Image;

import controller.Game;
import controller.Game.GameState;
import util.GraphicsUtil;
import util.ImageUtil;
import util.TxtFileUtil;

/**
 * MainMenu class is rendered when the user is on the main menu.
 * Besides rendering the menu, this class also checks if the user has
 * clicked any buttons
 */
public class MainMenu {
	
	private final static Rectangle START_BUTTON = new Rectangle(Game.WIDTH/2 - 100, 2*(Game.HEIGHT/3) - 15, 200, 30);
	private final static Rectangle CREDITS_BUTTON = new Rectangle(Game.WIDTH/2 - 100, 2*(Game.HEIGHT/3) + 20, 200, 30);
	private final static Rectangle HELP_BUTTON = new Rectangle(0, 0, 30, 30);
	
	private final static Image ICON = ImageUtil.addImage(100, 100, "resources/icon/icon.png");
	
	private final static Font FONT = TxtFileUtil.createFont("resources/fonts/menuFont.ttf", 24);
	
	private final static Color BACKGROUND_COLOR = new Color(230, 180, 52);
	private final static Color BUTTON_COLOR = new Color(54, 52, 17);
	private final static Color TEXT_COLOR = new Color(240, 236, 225);
	
	private final static String HELP_SCREEN_TEXT = TxtFileUtil.readURL(TxtFileUtil.readFile("resources/text/controls.txt")).lines().collect(Collectors.joining());
	private final static String CREDITS_SCREEN_TEXT = TxtFileUtil.readURL(TxtFileUtil.readFile("resources/text/credits.txt")).lines().collect(Collectors.joining());
	
	private static boolean helpScreen = false;
	private static boolean creditsScreen = false;
	
	public static void render(Graphics g) {
		
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		if(helpScreen) {
			g.setFont(FONT);
			g.setColor(BUTTON_COLOR);
			GraphicsUtil.drawStringMultiLine((Graphics2D) g, HELP_SCREEN_TEXT, 10, 10, 90);
			
			g.setColor(Color.red);
			g.drawString("X", HELP_BUTTON.x+10, HELP_BUTTON.y+25);
		}
		else if(creditsScreen) {
			g.setFont(FONT);
			g.setColor(BUTTON_COLOR);
			GraphicsUtil.drawStringMultiLine((Graphics2D) g, CREDITS_SCREEN_TEXT, 10, 10, 90);
			
			g.setColor(Color.red);
			g.drawString("X", HELP_BUTTON.x+10, HELP_BUTTON.y+25);
		}
		else {
			g.drawImage(ICON, Game.WIDTH/2 - 50, Game.HEIGHT/4 - 50, 100, 100, null);
			
			g.setColor(BUTTON_COLOR);
			g.fillRect(START_BUTTON.x, START_BUTTON.y, START_BUTTON.width, START_BUTTON.height);
			
			g.fillRect(CREDITS_BUTTON.x, CREDITS_BUTTON.y, CREDITS_BUTTON.width, CREDITS_BUTTON.height);
			
			g.setFont(FONT);
			g.setColor(BUTTON_COLOR);
			g.drawString("?", HELP_BUTTON.x+10, HELP_BUTTON.y+25);
			
			g.setColor(TEXT_COLOR);
			g.drawString("Start", START_BUTTON.x+70, START_BUTTON.y+23);
			
			g.drawString("Credits", CREDITS_BUTTON.x+57, CREDITS_BUTTON.y+23);
		}

	}
	
	public static void checkButton(int x, int y) {
		if(START_BUTTON.contains(x, y) && !(helpScreen || creditsScreen)) {
			Game.gamestate = GameState.Running;
		}
		else if(CREDITS_BUTTON.contains(x, y) && !(helpScreen || creditsScreen)){
			creditsScreen = true;
		}
		else if(HELP_BUTTON.contains(x, y) && !(helpScreen || creditsScreen)) {
			helpScreen = true;
		}
		else if(HELP_BUTTON.contains(x, y) && (helpScreen || creditsScreen)) {
			helpScreen = false;
			creditsScreen = false;
		}
	}
	
}
