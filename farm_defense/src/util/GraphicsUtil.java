package util;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * Utility functions for graphics
 */
public class GraphicsUtil {
	
	/**
    * Draws a multi line string to a graphics object
    *
    * @param  g 2d graphics object to draw the text onto
    * @param  text text to be drawn to g. use ">" character as line seperator
    * @param  lineWidth the number of pixels between each line
    * @param  x x coordinate of the top right of the text
    * @param  y y coordinate of the top right of the text
    */
	public static void drawStringMultiLine(Graphics2D g, String text, int lineWidth, int x, int y) {
	    FontMetrics m = g.getFontMetrics();
	    if(m.stringWidth(text) < lineWidth) {
	        g.drawString(text, x, y);
	    } else {
	        String[] words = text.split(">");
	        String currentLine = words[0];
	        for(int i = 1; i < words.length; i++) {
	            if(m.stringWidth(currentLine+words[i]) < lineWidth) {
	                currentLine += " "+words[i];
	            } else {
	                g.drawString(currentLine, x, y);
	                y += m.getHeight();
	                currentLine = words[i];
	            }
	        }
	        if(currentLine.trim().length() > 0) {
	            g.drawString(currentLine, x, y);
	        }
	    }
	}
}
