package util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {
	
	public static BufferedImage addImage(int width, int height, String file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(ImageUtil.class.getClassLoader().getResource(file));
		} catch (IOException e) {
			System.out.println("bug here");
			e.printStackTrace();
		}
		image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return image;
	}
}
