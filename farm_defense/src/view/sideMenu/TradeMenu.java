package view.sideMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import controller.Game;
import model.TradeItem;

public class TradeMenu {
	
	public boolean visible = true;
	
	private Color darkBrown = new Color(127, 72, 0);
	private Color lightBrown = new Color(222, 160, 79);
	
	public static ArrayList<TradeItem> items = new ArrayList<TradeItem>();
	
	public TradeMenu() {
		visible = false;
		
		items.add(new TradeItem("WALL", 50, 20));
		items.add(new TradeItem("TURRET", 50, 20));
		items.add(new TradeItem("SEEDS", 50, 20));
	}
	
	public void render(Graphics g) {
		// create box for each available item starting in bottom right and moving upwards
		for(int i = 0; i < items.size(); i++) {
			g.setColor(lightBrown);
			g.fillRect(11 * Game.tileSize + 5, (10-i) * Game.tileSize + 5, (int) (Game.tileSize * 4.5) - 10, Game.tileSize - 10);
			
			g.setFont(null);
			g.setColor(Color.black);
			g.drawString(
					items.get(i).getName() + " cost: " + items.get(i).getCost() + " buy: enter " + (i+1)
					, 11 * Game.tileSize + 10, (10-i) * Game.tileSize + 25);
		}
	}
	
	public void buy(int i) {
		int ID = items.get(i-1).getID();
		Game.inventory.addItem(ID, 1);
	}
}