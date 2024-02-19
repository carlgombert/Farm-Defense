package util;

public class MathUtil {

	public static int clamp(int value, int min, int max) {
	   return Math.min(Math.max(value, min), max);
	}
	
	public static double angleBetweenPoints(int x1, int y1, int x2, int y2) {
		if (x2 - x1 == 0) return (Math.PI / 2);
		double angle = Math.atan((y2 - y1)/(x2 - x1));
		if(x2 - x1 < 0) {
			angle += Math.PI;
		}
		return angle;
	}
	
	public static double Distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2));
	}
	
	public static int randomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
}
