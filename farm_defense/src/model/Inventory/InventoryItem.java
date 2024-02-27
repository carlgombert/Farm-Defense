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
			case 40: // carrot
				price = 90;
			case 41: // corn
				price = 102;
			case 42: // cauliflower
				price = 127;
			case 43: // potato
				price = 151;
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
