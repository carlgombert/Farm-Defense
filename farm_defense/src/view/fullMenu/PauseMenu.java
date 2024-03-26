package view.fullMenu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.stream.Collectors;

import controller.Game;
import controller.Game.GameState;
import util.GraphicsUtil;
import util.TxtFileUtil;

public class PauseMenu {
	private static Rectangle CONTINUE_BUTTON = new Rectangle(Game.WIDTH/2 - 100, 2*(Game.HEIGHT/3) - 15, 200, 30);
	private static Rectangle HELP_BUTTON = new Rectangle(0, 0, 30, 30);
	
	private static Font FONT = TxtFileUtil.createFont("resources/fonts/menuFont.ttf", 24);
	
	private static Color BACKGROUND_COLOR = new Color(230, 180, 52);
	private static Color BUTTON_COLOR = new Color(54, 52, 17);
	private static Color TEXT_COLOR = new Color(240, 236, 225);
	
	private static String HELP_SCREEN_TEXT = TxtFileUtil.readURL(TxtFileUtil.readFile("resources/text/controls.txt")).lines().collect(Collectors.joining());
	
	private static boolean helpScreen = false;
	
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
		else {
			
			g.setColor(BUTTON_COLOR);
			g.fillRect(CONTINUE_BUTTON.x, CONTINUE_BUTTON.y, CONTINUE_BUTTON.width, CONTINUE_BUTTON.height);
			
			g.setFont(FONT);
			g.setColor(BUTTON_COLOR);
			g.drawString("?", HELP_BUTTON.x+10, HELP_BUTTON.y+25);
			
			g.setColor(TEXT_COLOR);
			g.drawString("Continue", CONTINUE_BUTTON.x+50, CONTINUE_BUTTON.y+23);
		}

	}
	
	public static void checkButton(int x, int y) {
		if(CONTINUE_BUTTON.contains(x, y) && !helpScreen) {
			Game.gamestate = GameState.Running;
		}
		else if(HELP_BUTTON.contains(x, y) && !helpScreen) {
			helpScreen = true;
		}
		else if(HELP_BUTTON.contains(x, y) && helpScreen) {
			helpScreen = false;
		}
	}
}
