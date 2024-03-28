package model.Inventory;

import java.awt.image.BufferedImage;

import model.items.Item;
import util.ImageUtil;

public class InventoryItem extends Item
{
	private int count;
	
	public InventoryItem(int passedID, BufferedImage img, int itemCount, String name)
	{
		super(passedID, img, name);
		this.count = itemCount;
	}
	
	public InventoryItem(Item item, int itemCount)
	{
		super(item.getID(), item.getImage(), item.getName());
		this.count = itemCount;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public void setCount(int count)
	{
		this.count = count;
	}
	
	public void changeCount(int amt)
	{
		count += amt;
	}
}
