package view.fullMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import controller.Game;
import controller.Game.GameState;
import util.ImageUtil;

public class BankruptMenu {
	private static Rectangle menuButton = new Rectangle(Game.WIDTH/2 - 100, 2*(Game.HEIGHT/3) - 15, 200, 30);
	
	private static Image deathIcon = ImageUtil.addImage(200, 200, "resources/icon/bankruptScreen.png");
	
	
	public static void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		g.drawImage(deathIcon, (Game.WIDTH/2)-100, Game.HEIGHT/4, null);
		
		g.setFont(Game.DEFAULT_FONT);
		g.setColor(new Color(214, 56, 100));
		
		g.drawString("You don't have any crops or the money to buy seeds", (Game.WIDTH/2)-170, Game.HEIGHT/4+130);
		
		g.drawString("Total coins earned: " + Game.player.getCoinsEarned(), (Game.WIDTH/2)-170, Game.HEIGHT/4+160);
		
		g.drawString("Zombies killed: " + Game.handler.getZombiesKilled(), (Game.WIDTH/2)-170, Game.HEIGHT/4+180);
		
		g.drawString("return to menu", menuButton.x+50, menuButton.y+23);
	}
	
	public static void checkButton(int x, int y) {
		if(menuButton.contains(x, y)) {
			Game.restart();
			Game.gamestate = GameState.MainMenu;
		}
	}
}
