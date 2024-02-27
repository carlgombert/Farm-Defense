package model;

/**
 * The TradeItem class holds information about items that can be sold in the
 * in game shop
 */
public class TradeItem {
	private String name;
	private int cost;
	private int ID;
	
	public TradeItem(String name, int cost, int ID) {
		this.setName(name);
		this.setCost(cost);
		this.setID(ID);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
}
