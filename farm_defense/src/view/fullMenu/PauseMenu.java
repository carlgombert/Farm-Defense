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

public class PauseMenu {
	private static Rectangle continueButton = new Rectangle(Game.WIDTH/2 - 100, 2*(Game.HEIGHT/3) - 15, 200, 30);
	private static Rectangle helpButton = new Rectangle(0, 0, 30, 30);
	
	private static Font font = TxtFileUtil.createFont("resources/fonts/menuFont.ttf", 24);
	
	private static Color backgroundColor = new Color(230, 180, 52);
	private static Color buttonColor = new Color(54, 52, 17);
	private static Color textColor = new Color(240, 236, 225);
	
	private static String helpScreenText = TxtFileUtil.readURL(TxtFileUtil.readFile("resources/text/controls.txt")).lines().collect(Collectors.joining());
	
	private static boolean helpScreen = false;
	
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
		else {
			
			g.setColor(buttonColor);
			g.fillRect(continueButton.x, continueButton.y, continueButton.width, continueButton.height);
			
			g.setFont(font);
			g.setColor(buttonColor);
			g.drawString("?", helpButton.x+10, helpButton.y+25);
			
			g.setColor(textColor);
			g.drawString("Continue", continueButton.x+50, continueButton.y+23);
		}

	}
	
	public static void checkButton(int x, int y) {
		if(continueButton.contains(x, y) && !helpScreen) {
			Game.gamestate = GameState.Running;
		}
		else if(helpButton.contains(x, y) && !helpScreen) {
			helpScreen = true;
		}
		else if(helpButton.contains(x, y) && helpScreen) {
			helpScreen = false;
		}
	}
}
