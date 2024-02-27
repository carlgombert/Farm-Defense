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
	
	private static Rectangle startButton = new Rectangle(Game.WIDTH/2 - 100, 2*(Game.HEIGHT/3) - 15, 200, 30);
	private static Rectangle creditsButton = new Rectangle(Game.WIDTH/2 - 100, 2*(Game.HEIGHT/3) + 20, 200, 30);
	private static Rectangle helpButton = new Rectangle(0, 0, 30, 30);
	
	private static Image icon = ImageUtil.addImage(100, 100, "resources/icon/icon.png");
	
	private static Font font = TxtFileUtil.createFont("resources/fonts/menuFont.ttf", 24);
	
	private static Color backgroundColor = new Color(230, 180, 52);
	private static Color buttonColor = new Color(54, 52, 17);
	private static Color textColor = new Color(240, 236, 225);
	
	private static String helpScreenText = TxtFileUtil.readURL(TxtFileUtil.readFile("resources/text/controls.txt")).lines().collect(Collectors.joining());
	private static String creditsScreenText = TxtFileUtil.readURL(TxtFileUtil.readFile("resources/text/credits.txt")).lines().collect(Collectors.joining());
	
	private static boolean helpScreen = false;
	private static boolean creditsScreen = false;
	
	public static void render(Graphics g) {
		
		g.setColor(backgroundColor);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		if(helpScreen) {
			g.setFont(font);
			g.setColor(buttonColor);
			GraphicsUtil.drawStringMultiLine((Graphics2D) g, helpScreenText, 10, 10, 90);
			
			g.setColor(Color.red);
			g.drawString("X", helpButton.x+10, helpButton.y+25);
		}
		else if(creditsScreen) {
			g.setFont(font);
			g.setColor(buttonColor);
			GraphicsUtil.drawStringMultiLine((Graphics2D) g, creditsScreenText, 10, 10, 90);
			
			g.setColor(Color.red);
			g.drawString("X", helpButton.x+10, helpButton.y+25);
		}
		else {
			g.drawImage(icon, Game.WIDTH/2 - 50, Game.HEIGHT/4 - 50, 100, 100, null);
			
			g.setColor(buttonColor);
			g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
			
			g.fillRect(creditsButton.x, creditsButton.y, creditsButton.width, creditsButton.height);
			
			g.setFont(font);
			g.setColor(buttonColor);
			g.drawString("?", helpButton.x+10, helpButton.y+25);
			
			g.setColor(textColor);
			g.drawString("Start", startButton.x+70, startButton.y+23);
			
			g.drawString("Credits", creditsButton.x+57, creditsButton.y+23);
		}

	}
	
	public static void checkButton(int x, int y) {
		if(startButton.contains(x, y) && !(helpScreen || creditsScreen)) {
			Game.gamestate = GameState.Running;
		}
		else if(creditsButton.contains(x, y) && !(helpScreen || creditsScreen)){
			creditsScreen = true;
		}
		else if(helpButton.contains(x, y) && !(helpScreen || creditsScreen)) {
			helpScreen = true;
		}
		else if(helpButton.contains(x, y) && (helpScreen || creditsScreen)) {
			helpScreen = false;
			creditsScreen = false;
		}
	}
	
}
