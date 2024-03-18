package util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Utility functions for images
 */
public class ImageUtil {
	
	/**
    * creates an image with the given file
    *
    * @param  width width of the image
    * @param  height height of the image
    * @param  file path to image file starting with the root of the src package
    * @return 	returns image created from file
    */
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
