package util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
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
	
	/**
    * creates a resized version of the given image
    *
    * @param  img buffered image to be resized
    * @param  newW desired image width
    * @param  newH desired image height
    * @return 	new image of desired size
    */
	public static BufferedImage resize(BufferedImage img, int newW, int newH) {  
	    int w = img.getWidth();  
	    int h = img.getHeight();  
	    BufferedImage dimg = new BufferedImage(newW, newH, img.getType());  
	    Graphics2D g = dimg.createGraphics();  
	    g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
	    g.dispose();  
	    return dimg;  
	}
}
