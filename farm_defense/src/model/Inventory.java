package model;

import java.awt.image.BufferedImage;

import controller.Game;
import util.ImageUtil;

public class Inventory 
{
	public InventoryItem[] inventory = new InventoryItem[10];
	public int selected;
	
	// image list of every possible image that the inventory item could be
	public BufferedImage[] itemImages = new BufferedImage[51];
	
	public Inventory()
	{
		loadInventoryImages();
		
		addItem(1, 1, 1);
		addItem(10, 1, 2);
		addItem(0, 1, 3);
		addItem(30, 5);
		addItem(20, 5, 6);
		
		setSelected(0);
	}
	
	public void loadInventoryImages()
	{
		// item id 0 is the hoe
		itemImages[0] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_hoe.png");
		
		// ids 1-9 will be guns
		itemImages[1] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_gun.png");
		
		// ids in the 10s will be melee weapons
		itemImages[10] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_sword.png");
		
		// ids in the 20s will be walls
		itemImages[20] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_wall_wood.png");
		
		// ids in the 30s will be seeds
		itemImages[30] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_seeds_carrot.png");
		itemImages[31] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_seeds_corn.png");
		
		// ids in the 40s will be grown crops
		itemImages[40] = ImageUtil.addImage(16, 16, "resources/inventory/inventory_carrot.png");
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
		inventory[slot - 1] = new InventoryItem(ID, itemImages[ID], count);
	}
	
	// add an item to the player's inventory at the next empty slot, or stacks if some of the item is alr in the inventory
	public void addItem(int ID, int count)
	{
		// first check if should be stacked with items already in the inventory
		for (int i = 0; i < 10; i++)
		{
			if (inventory[i] != null && inventory[i].getID() == ID)
			{
				inventory[i].changeCount(count);
				setSelected(selected);
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
			inventory[emptySlot] = new InventoryItem(ID, itemImages[ID], count);
			setSelected(selected);
		}
		// else statement here for what to do if the player tries to buy something with a full inventory
	}
	
	// subtract a number of items from the selected slot of the player's inventory
	public void minusItem(int amt)
	{
		if (inventory[selected].getCount() - amt <= 0) 
		{
			inventory[selected] = null;
			Game.player.setWeaponState(Game.player.stateEmpty());
		}
		else inventory[selected].changeCount(-amt);
	}
	
	public BufferedImage getCurrentImage()
	{
		return inventory[selected].getImage();
	}
	
	public int getCurrentID()
	{
		return inventory[selected].getID();
	}
}
