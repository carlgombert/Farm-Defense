package util;

/**
 * Utility functions for calculations
 */
public class MathUtil {
	
	/**
    * clamps a value to be no more or less than the min or max
    *
    * @param  value integer value to clamp
    * @param  min minimum value allowed for the integer
    * @param  max maximum value allowed for the integer
    * @return         returns the value after being clamped
    */
	public static int clamp(int value, int min, int max) {
	   return Math.min(Math.max(value, min), max);
	}
	
	/**
    * finds the angle between two coordinates
    *
    * @param  x1 x value for the first object
    * @param  y1 y value for the first object
    * @param  x2 x value for the second object
    * @param  y2 y value for the second object
    * @return         returns the angle in radians
    */
	public static double angleBetweenPoints(int x1, int y1, int x2, int y2) {
		if (x2 - x1 == 0) {
			if(y2 < y1) {
				return (Math.PI / 2);
			}
			else {
				return 3*(Math.PI / 2);
			}
		}
		double angle = Math.atan2((y2 - y1),(x2 - x1));
		return angle;
	}
	
	/**
    * finds the distance between two coordinates
    *
    * @param  x1 x value for the first object
    * @param  y1 y value for the first object
    * @param  x2 x value for the second object
    * @param  y2 y value for the second object
    * @return         returns the distance between the points
    */
	public static double Distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2));
	}
	
	/**
    * generates a random number in a given range
    *
    * @param  min minumum value for the random number
    * @param  max maxiumum value for the random number
    * @return         returns the random integer
    */
	public static int randomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
}
