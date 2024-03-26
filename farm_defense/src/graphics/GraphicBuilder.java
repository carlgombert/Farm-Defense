package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class GraphicBuilder {
	
	private int[][][] image;
	private int[] color = new int[4];
	
	public GraphicBuilder(int width, int height) {
		image = new int[height][width][4];
	}
	
	public void setColor(Color color) {
		this.color[0] = color.getRed();
		this.color[1] = color.getGreen();
		this.color[2] = color.getBlue();
		this.color[3] = color.getAlpha();
	}
	
	
	
	public int[][][] getImageData() {
		return image;
	}
	
	public BufferedImage getImage() {
		BufferedImage bi = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_INT_ARGB_PRE);
		
		for(int y = 0; y < image.length; y++) {
			for(int x = 0; x < image[0].length; x++) {
				Color color = new Color(image[y][x][0], image[y][x][1], image[y][x][2], image[y][x][3]);
				bi.setRGB(x, y, color.getRGB());
				
			}
		}
		return bi;
	}

	public void fillRect(int x, int y, int width, int height) {
		
		for(int h = y; h < height; h++) {
			for(int w = x; w < width; w++) {
				if(h > 0 && w > 0 && h < this.image.length && w < this.image[0].length) {
					this.image[h][w][0] = color[0];
					this.image[h][w][1] = color[1];
					this.image[h][w][2] = color[2];
					this.image[h][w][3] = color[3];
				}
			}
		}
	}

	public void drawImage(Image image, int x, int y, int i, int j, Object object) {
		drawImage((BufferedImage)image, x, y);
	}
	
	public void drawImage(Image image, int x, int y, Object object) {
		drawImage((BufferedImage)image, x, y);
	}
	
	public void drawImage(Image image, int x, int y) {
		drawImage((BufferedImage)image, x, y);
	}
	
	public void drawImage(BufferedImage image, int x, int y, int i, int j, Object object) {
		drawImage(image, x, y);
	}
	
	public void drawImage(BufferedImage image, int x, int y, Object object) {
		drawImage(image, x, y);
	}
	
	public void drawImage(BufferedImage image, int x, int y) {
		int width = image.getWidth();
		int height = image.getHeight();
		
		for(int h = y; h < height; h++) {
			for(int w = x; w < width; w++) {
				if(h > 0 && w > 0 && h < this.image.length && w < this.image[0].length) {
					Color color = new Color(image.getRGB(w-x, h-y));
					this.image[h][w][0] = color.getRed();
					this.image[h][w][1] = color.getGreen();
					this.image[h][w][2] = color.getBlue();
					this.image[h][w][3] = color.getAlpha();
				}
			}
		}
	}

	public void fillOval(int x, int y, int radiusX, int radiusY) {
		int width = 2*radiusX;
		int height = 2*radiusY;
		
		for(int h = y; h < height; h++) {
			for(int w = x; w < width; w++) {
				double radius = (double)(w*w) / (double)(radiusX*radiusX);
				radius += (double)(h*h) / (double)(radiusY*radiusY);
				if(radius > 0.999999 && radius < 1.0000001) {
					if(h > 0 && w > 0 && h < this.image.length && w < this.image[0].length) {
						this.image[h][w][0] = color[0];
						this.image[h][w][1] = color[1];
						this.image[h][w][2] = color[2];
						this.image[h][w][3] = color[3];
					}
				}
			}
		}
	}

	public void drawRect(int x, int y, int width, int height) {
		
	}

	public void drawLine(int x1, int y1, int x2, int y2, int thickness) {
		
	}

	public void drawString(String string, int x, int y) {
		
	}

	public void setFont(Font font) {
		
	}

	public void fillRoundRect(int x, int y, int width, int height, int m, int n) {
		fillRect(x, y, width, height);
	}
}
