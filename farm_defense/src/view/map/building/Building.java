package view.map.building;

import java.awt.image.BufferedImage;

public class Building 
{
	// images for each rotation of the building
	private BufferedImage image0;
	private BufferedImage image1;
	private BufferedImage image2;
	private BufferedImage image3;
	
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

	public BufferedImage getImage0() {
		return image0;
	}

	public BufferedImage getImage1() {
		return image1;
	}

	public BufferedImage getImage2() {
		return image2;
	}

	public BufferedImage getImage3() {
		return image3;
	}

	public void setImage0(BufferedImage image0) {
		this.image0 = image0;
	}

	public void setImage1(BufferedImage image1) {
		this.image1 = image1;
	}

	public void setImage2(BufferedImage image2) {
		this.image2 = image2;
	}

	public void setImage3(BufferedImage image3) {
		this.image3 = image3;
	}
}
