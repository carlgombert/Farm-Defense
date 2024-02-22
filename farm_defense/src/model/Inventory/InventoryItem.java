package model.Inventory;

import java.awt.image.BufferedImage;

public class InventoryItem 
{
	public BufferedImage image;
	public int count;
	public int ID;
	
	public InventoryItem(int passedID, BufferedImage img, int itemCount)
	{
		ID = passedID;
		image = img;
		count = itemCount;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public void changeCount(int amt)
	{
		count += amt;
	}
	
	public int getID()
	{
		return ID;
	}
}
