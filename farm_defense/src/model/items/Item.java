package model.items;

import java.awt.image.BufferedImage;

import util.ImageUtil;

public class Item 
{
	private BufferedImage image;
	private BufferedImage largerImage;
	private int ID;
	private int price;
	private int cost;
	private String name;
	
	public Item(int passedID, BufferedImage img, String name)
	{
		ID = passedID;
		image = img;
		this.setName(name);
		
		largerImage = ImageUtil.resize(img, 48*2, 48*2);
		
		switch(ID) {
			case 20: // wood wall
				cost = 30;
				break;
			case 21: // stone wall
				cost = 60;
				break;
			case 30: // carrot seed
				cost = 50;
				break;
			case 31: // corn seed
				cost = 60;
				break;
			case 32: // cauliflower seed
				cost = 80;
				break;
			case 33: // potato seed
				cost = 100;
				break;
			case 34: // pepper seed
				cost = 130;
				break;
			case 35: // watermelon seed
				cost = 150;
				break;
			case 36: // sunflower seed
				cost = 280;
				break;
			case 40: // carrot
				price = 90;
				break;
			case 41: // corn
				price = 102;
				break;
			case 42: // cauliflower
				price = 127;
				break;
			case 43: // potato
				price = 151;
				break;
			case 44: // pepper
				price = 185;
				break;
			case 45: // watermelon
				price = 208;
				break;
			case 46: // sunflower
				price = 353;
				break;
			case 50: // turret
				cost = 150;
				break;
			case 51: // torch
				cost = 70;
				break;
		}
	}
	
	public BufferedImage getImage()
	{
		return image;
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
	
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BufferedImage getLargerImage() {
		return largerImage;
	}

	public void setLargerImage(BufferedImage largerImage) {
		this.largerImage = largerImage;
	}
}
