package view.fullMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import controller.Game;
import controller.Game.GameState;
import util.ImageUtil;

public class DeathMenu {
	
	private static Rectangle menuButton = new Rectangle(Game.WIDTH/2 - 100, 2*(Game.HEIGHT/3) - 15, 200, 30);
	
	private static Image deathIcon = ImageUtil.addImage(200, 200, "resources/icon/deathScreen.png");
	
	
	public static void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		g.drawImage(deathIcon, (Game.WIDTH/2)-100, Game.HEIGHT/4, null);
		
		g.setColor(new Color(214, 56, 100));
		g.setFont(Game.DEFAULT_FONT);
		g.drawString("return to menu", menuButton.x+50, menuButton.y+23);
	}
	
	public static void checkButton(int x, int y) {
		if(menuButton.contains(x, y)) {
			Game.gamestate = GameState.MainMenu;
		}
	}
}
