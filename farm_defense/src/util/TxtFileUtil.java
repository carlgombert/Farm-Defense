package util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Utility functions text and txt files
 */
public class TxtFileUtil {

	public static URL readFile(String file) {
		URL url = TxtFileUtil.class.getClassLoader().getResource(file);
		return url;
	}
	
	public static BufferedReader readURL(URL url) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(url.openStream()));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error reading txt files");
		}
		return br;
	}
	
	//method for taking a ttf file and creating a font
	public static Font createFont(String file, float size) {
		Font customFont = null;
		
		//creating the font
		try {
			URL fontPath = TxtFileUtil.class.getClassLoader().getResource(file);
		    customFont = Font.createFont(Font.TRUETYPE_FONT, fontPath.openStream()).deriveFont(size);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    //register the font
		    ge.registerFont(customFont);
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}
		
		return customFont;
	}
}
