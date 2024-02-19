package view.map;

import java.awt.image.BufferedImage;

public class Building 
{
	// images for each rotation of the building
	public BufferedImage image0;
	public BufferedImage image1;
	public BufferedImage image2;
	public BufferedImage image3;
	
	// represents the number of the how many other buildings it is connected to
	public int connections;
	
	public BufferedImage getImage(int num)
	{
		if (num == 0) return image0;
		else if (num == 1) return image1;
		else if (num == 2) return image2;
		else if (num == 3)return image3;
		else return null;
	}
	
	public void setConnections(int c)
	{
		connections = c;
	}
	
	public int getConnections()
	{
		return connections;
	}
}
