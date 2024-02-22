package view.map;

import java.awt.image.BufferedImage;

public class Crop 
{
	// diff crop stages
	public BufferedImage image1;
	public BufferedImage image2;
	public BufferedImage image3;
	
	public BufferedImage getCurrentStage(int stage)
	{
		switch (stage)
		{
			case 1: return image1;
			case 2: return image2;
			case 3: return image3;
		}
		return null;
	}
}
