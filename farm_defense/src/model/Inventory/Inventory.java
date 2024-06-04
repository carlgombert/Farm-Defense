package model.Inventory;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import controller.Game;
import model.items.ItemManager;
import util.ImageUtil;
import view.fullMenu.StoreMenu;

public class Inventory 
{
	private InventoryItem[] inventory = new InventoryItem[10];
	private int selected;
	
	// once an item is encountered, it is added to the map of ID's mapped to the item
	private HashMap<Integer, InventoryItem> items = new HashMap<Integer, InventoryItem>();
	
	private int seedCropCount;
	
	public Inventory()
	{
		ItemManager.loadItems();
		
		addItem(1, 1, 1);
		addItem(10, 1, 2);
		addItem(0, 1, 3);
		addItem(30, 5);
		addItem(33, 5);
		addItem(20, 5, 6);
		addItem(51, 5);
		addItem(21, 500);
		addItem(40, 20);
		
		setSelected(0);
	}
	
	public void setSelected(int s)
	{
		selected = s;
		Game.hud.setInventorySelection(s);
		
		// set player state based on the selected item
		if (inventory[selected] != null)
		{
			if (inventory[selected].getID() == 0) Game.player.setWeaponState(Game.player.stateTilling());
			else if (inventory[selected].getID() < 10) Game.player.setWeaponState(Game.player.stateGun());
			else if (inventory[selected].getID() < 20) Game.player.setWeaponState(Game.player.stateMelee());
			else if (inventory[selected].getID() < 30) Game.player.setWeaponState(Game.player.stateBuild());
			else if (inventory[selected].getID() < 40) Game.player.setWeaponState(Game.player.statePlanting());
			else if (inventory[selected].getID() < 50) Game.player.setWeaponState(Game.player.stateEmpty());
			else if (inventory[selected].getID() == 50) Game.player.setWeaponState(Game.player.stateTurret());
			else if (inventory[selected].getID() == 51) Game.player.setWeaponState(Game.player.stateTorch());
			else Game.player.setWeaponState(Game.player.stateEmpty());
		}
		else Game.player.setWeaponState(Game.player.stateEmpty());
	}
	
	public InventoryItem[] getInventory()
	{
		return inventory;
	}
	
	// add an item to the player's inventory at the specified slot, see above for item IDs
	public void addItem(int ID, int count, int slot)
	{
		getItems().put(ID, new InventoryItem(ItemManager.getItem(ID), count));
		inventory[slot - 1] = new InventoryItem(ItemManager.getItem(ID), count);
		
		if(ID > 29 && ID < 50) {
			seedCropCount += count;
			if(seedCropCount > 0) {
				Game.player.setBadInventory(false);
			}
		}
	}
	
	
	// add an item to the player's inventory at the next empty slot, or stacks if some of the item is alr in the inventory
	public void addItem(int ID, int count)
	{
		getItems().put(ID, new InventoryItem(ItemManager.getItem(ID), count));
		if(ID == 50) {
			StoreMenu.increaseTurretsPurchased(count);
		}
		// first check if should be stacked with items already in the inventory
		for (int i = 0; i < 10; i++)
		{
			if (inventory[i] != null && inventory[i].getID() == ID)
			{
				inventory[i].changeCount(count);
				setSelected(selected);
				
				if(ID > 29 && ID < 50) {
					seedCropCount += count;
					if(seedCropCount > 0) {
						Game.player.setBadInventory(false);
					}
					System.out.println(seedCropCount);
				}
				return;
			}
		}
		
		// find and add item to next empty slot
		int emptySlot = -1;
		
		for (int i = 0; i < 10; i++)
		{
			if (inventory[i] == null && emptySlot == -1) emptySlot = i;
		}
		
		if (emptySlot != -1)
		{
			inventory[emptySlot] = new InventoryItem(ItemManager.getItem(ID), count);
			setSelected(selected);
		}
		// else statement here for what to do if the player tries to buy something with a full inventory
		
		if(ID > 29 && ID < 50) {
			seedCropCount += count;
			if(seedCropCount > 0) {
				Game.player.setBadInventory(false);
			}
		}
	}
	
	// subtract a number of items from the selected slot of the player's inventory
	public void minusItem(int amt)
	{
		if (inventory[selected].getCount() - amt <= 0) 
		{
			if(inventory[selected].getID() > 29 && inventory[selected].getID() < 50) {
				seedCropCount -= inventory[selected].getCount();
				if(seedCropCount <= 0) {
					Game.player.setBadInventory(true);
				}
			}
			
			Game.player.setWeaponState(Game.player.stateEmpty());
			
			inventory[selected] = null;
		}
		else {
			inventory[selected].changeCount(-amt);
			
			if(inventory[selected].getID() > 29 && inventory[selected].getID() < 50) {
				seedCropCount -= amt;
				if(seedCropCount <= 0) {
					Game.player.setBadInventory(true);
				}
			}
		}
	}
	
	// clear all of a specific item from the players inventory and return the count
	public int clearItem(int ID) {
		int count = 0;
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] != null && inventory[i].getID() == ID) {
				count += inventory[i].getCount();
				inventory[i] = null;
			}
		}
		
		if(ID > 29 && ID < 50) {
			seedCropCount -= count;
			if(seedCropCount <= 0) {
				Game.player.setBadInventory(true);
			}
		}
		
		return count;
	}
	
	public InventoryItem getItem(int id) {
		return items.get(id);	
	}
	
	
	public BufferedImage getCurrentImage()
	{
		if(inventory[selected] != null) {
			return inventory[selected].getImage();
		}
		return null;
	}
	
	public int getCurrentID()
	{
		return inventory[selected].getID();
	}

	public HashMap<Integer, InventoryItem> getItems() {
		return items;
	}

	public void setItems(HashMap<Integer, InventoryItem> items) {
		this.items = items;
	}
}
