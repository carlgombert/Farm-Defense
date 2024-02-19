package view;

import java.awt.Color;
import java.awt.Graphics;

import model.gameObjects.Player;

public class HUD {
	
	private Player player;
	
	public HUD(Player player) {
		this.player = player;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(13, 13, 204, 36);
		
		g.setColor(new Color(214, 48, 33));
		g.fillRect(15, 15, 200, 32);
		
		g.setColor(new Color(97, 172, 49));
		g.fillRect(15, 15, (int) Math.round(player.getHealth() / 2), 32);
		
		g.setFont(null);
		g.setColor(Color.white);
		g.drawString(player.getHealth() + "/400", 13, 65);
		
		g.setColor(new Color(148, 141, 62));
		for(int i = 0; i < player.getAmmo(); i++) {
			g.fillOval(540 + (20*i), 25, 8, 16);
		}
		
		g.setColor(Color.white);
		g.drawString("" + player.getAmmo(), 540, 60);
	}
}
