package view.sideMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import controller.Game;
import model.TradeItem;
import model.gameObjects.Turret;

public class TurretMenu {
public boolean visible = true;
	
	private Color darkBrown = new Color(127, 72, 0);
	private Color lightBrown = new Color(222, 160, 79);
	
	private ArrayList<String> upgrades = new ArrayList<String>(); // list of upgrade options
	
	private Turret turret; // keeps track of turret being upgraded
	
	public TurretMenu() {
		visible = false;
		
		upgrades.add("damage");
		upgrades.add("range");
		upgrades.add("cooldown");
		
	}
	
	public void render(Graphics g) {
		// create box for each available upgrade
		for(int i = 0; i < upgrades.size(); i++) {
			g.setColor(lightBrown);
			g.fillRect(11 * Game.TILE_SIZE + 5, (10-i) * Game.TILE_SIZE + 5, (int) (Game.TILE_SIZE * 4.5) - 10, Game.TILE_SIZE - 10);
			
			g.setFont(null);
			g.setColor(Color.black);
			g.drawString(
					"Upgrade " + upgrades.get(i) + " Cost: " + turret.getLevel(upgrades.get(i))*10+ " enter " + (i+1)
					, 11 * Game.TILE_SIZE + 10, (10-i) * Game.TILE_SIZE + 25);
		}
	}
	
	public void buy(int ID) {
		turret.setLevel(upgrades.get(ID-1));
	}
	
	public void setTurret(Turret turret) {
		this.turret = turret;	
	}
}
