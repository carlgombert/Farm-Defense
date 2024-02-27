package view.map.farming;

import java.awt.image.BufferedImage;

public class Crop 
{
	// diff crop stages
	private BufferedImage image1;
	private BufferedImage image2;
	private BufferedImage image3;
	
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
