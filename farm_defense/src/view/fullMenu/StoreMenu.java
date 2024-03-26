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
	private static Rectangle EXIT_BUTTON = new Rectangle(0, 0, 30, 30);
	
	private final static Font FONT = TxtFileUtil.createFont("resources/fonts/menuFont.ttf", 24);
	
	private final static Color BACKGROUND_COLOR = new Color(230, 180, 52);
	private final static Color BUTTON_COLOR = new Color(54, 52, 17);
	private final static Color TEXT_COLOR = new Color(240, 236, 225);
	
	public static void render(Graphics g) {
		
		g.setColor(new Color(0, 0, 0, 0.2f));
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		g.setFont(FONT);
		
		g.setColor(Color.red);
		g.drawString("X", EXIT_BUTTON.x+10, EXIT_BUTTON.y+25);
	}
	
	public static void checkButton(int x, int y) {
		if(EXIT_BUTTON.contains(x, y)) {
			Game.gamestate = GameState.Running;
		}
	}
}
