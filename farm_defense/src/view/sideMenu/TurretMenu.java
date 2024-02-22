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
	
	public static ArrayList<String> upgrades = new ArrayList<String>(); // list of upgrade options
	
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
			g.fillRect(11 * Game.tileSize + 5, (10-i) * Game.tileSize + 5, (int) (Game.tileSize * 4.5) - 10, Game.tileSize - 10);
			
			g.setFont(null);
			g.setColor(Color.black);
			g.drawString(
					"Upgrade " + upgrades.get(i) + " Cost: " + turret.getLevel(upgrades.get(i))*10+ " enter " + (i+1)
					, 11 * Game.tileSize + 10, (10-i) * Game.tileSize + 25);
		}
	}
	
	public void buy(int ID) {
		turret.setLevel(upgrades.get(ID-1));
	}
	
	public void setTurret(Turret turret) {
		this.turret = turret;	
	}
}
