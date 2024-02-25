package model.Inventory;

import java.awt.image.BufferedImage;

public class InventoryItem 
{
	public BufferedImage image;
	private int count;
	public int ID;
	private int price;
	
	public InventoryItem(int passedID, BufferedImage img, int itemCount)
	{
		ID = passedID;
		image = img;
		count = itemCount;
		
		switch(ID) {
			case 40:
				price = 70;
			case 41:
				price = 81;
		}
	}
	
	public BufferedImage getImage()
	{
		return image;
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
	
	public int getID()
	{
		return ID;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
}
