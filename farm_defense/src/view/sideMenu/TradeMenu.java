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
	
	private boolean visible = true;
	
	private Color darkBrown = new Color(127, 72, 0);
	private Color lightBrown = new Color(222, 160, 79);
	
	private static ArrayList<TradeItem> items = new ArrayList<TradeItem>();
	
	public TradeMenu() {
		setVisible(false);
		
		items.add(new TradeItem("Wall", 50, 20));
		items.add(new TradeItem("Turret", 100, 50));
		items.add(new TradeItem("Carrot Seeds", 50, 30));
		items.add(new TradeItem("Corn Seeds", 60, 31));
	}
	
	public void render(Graphics g) {
		// create box for each available item starting in bottom right and moving upwards
		if(!Game.night) {
			for(int i = 0; i < items.size(); i++) {
				g.setColor(lightBrown);
				g.fillRect(11 * Game.TILE_SIZE + 5, (10-i) * Game.TILE_SIZE + 5, (int) (Game.TILE_SIZE * 4.5) - 10, Game.TILE_SIZE - 10);
				
				g.setFont(null);
				g.setColor(Color.black);
				g.drawString(
						items.get(i).getName() + " cost: " + items.get(i).getCost() + " buy: enter " + (i+1)
						, 11 * Game.TILE_SIZE + 10, (10-i) * Game.TILE_SIZE + 25);
			}
			g.setColor(Color.red);
			g.fillRect(11 * Game.TILE_SIZE + 5, (10-items.size()) * Game.TILE_SIZE + 5, (int) (Game.TILE_SIZE * 4.5) - 10, Game.TILE_SIZE - 10);
			
			g.setFont(null);
			g.setColor(lightBrown);
			g.drawString(
					"Sell items: enter Q"
					, 11 * Game.TILE_SIZE + 10, (10-items.size()) * Game.TILE_SIZE + 25);
		}
		else {
			g.setColor(lightBrown);
			g.fillRect(11 * Game.TILE_SIZE + 5, 10* Game.TILE_SIZE + 5, (int) (Game.TILE_SIZE * 4.5) - 10, Game.TILE_SIZE - 10);
			
			g.setFont(null);
			g.setColor(Color.black);
			g.drawString("CLOSED UNTIL MORNING", 11 * Game.TILE_SIZE + 10, 10 * Game.TILE_SIZE + 25);
		}
	}
	
	// sells all crops in the players inventory
	public void sell() {
		for(int i = 40; i < 50; i++) {
			if(Game.inventory.getItems().containsKey(i)) {
				int price = Game.inventory.getItems().get(i).getPrice();
				Game.player.setCoins(
						Game.player.getCoins() + Game.inventory.clearItem(i) * price
						);
			}
		}
	}
	
	public void buy(int i) {
		i = Math.min(i, items.size());
		int ID = items.get(i-1).getID();
		if(items.get(i-1).getCost() <= Game.player.getCoins()) {
			Game.inventory.addItem(ID, 1);
			Game.player.setCoins(Game.player.getCoins() -  items.get(i-1).getCost());
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}