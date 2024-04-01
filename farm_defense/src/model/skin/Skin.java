package model.skin;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import util.ImageUtil;

public class Skin {

	HashMap<Integer, BufferedImage[]> skinImages = new HashMap<Integer, BufferedImage[]>();
	
	public Skin(String directory) {
		BufferedImage[] front = {ImageUtil.addImage(75, 75, directory+"/front_right.png"), ImageUtil.addImage(75, 75, directory+"/front_left.png")};
		BufferedImage[] back = {ImageUtil.addImage(75, 75, directory+"/back_right.png"), ImageUtil.addImage(75, 75, directory+"/back_left.png")};
		BufferedImage[] left = {ImageUtil.addImage(75, 75, directory+"/left_right.png"), ImageUtil.addImage(75, 75, directory+"/left_left.png")};
		BufferedImage[] right = {ImageUtil.addImage(75, 75, directory+"/right_right.png"), ImageUtil.addImage(75, 75, directory+"/right_left.png")};
		
		skinImages.put(0, front);
		skinImages.put(1, back);
		skinImages.put(2, left);
		skinImages.put(3, right);
	}
	
	public BufferedImage[] getImage(int direction) {
		return skinImages.get(direction);
	}
}
