package view.fullMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;

import controller.Game;
import model.GameState;
import util.ImageUtil;

public class MainMenu {
	
	private static Rectangle startButton = new Rectangle(Game.WIDTH/2 - 100, 2*(Game.HEIGHT/3) - 15, 200, 30);
	
	private static Image icon = ImageUtil.addImage(100, 100, "resources/icon/icon.png");
	
	private static Color backgroundColor = new Color(230, 180, 52);
	private static Color buttonColor = new Color(54, 52, 17);
	private static Color textColor = new Color(240, 236, 225);
	
	public static void render(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		g.drawImage(icon, Game.WIDTH/2 - 50, Game.HEIGHT/4 - 50, 100, 100, null);
		
		g.setColor(buttonColor);
		g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
		
		g.setColor(textColor);
		g.drawString("Start", startButton.x+85, startButton.y+20);
	}
	
	public static void checkButton(int x, int y) {
		if(startButton.contains(x, y)) {
			Game.gamestate = GameState.Running;
		}
	}
	
}
