package org.usfirst.frc.team223.AdvancedX;

/**
 * Provides a convenient class fo doing some basic angle utilities
 * @author develoer
 *
 */
public class AngleUtil {
	
	/**
	 * Takes in any angle and converts it to a range of [0 , 360)
	 * @param angle the angle to scale
	 * @return the scaled angle
	 */
	public static double norm360(double angle)
	{
		double newAngle = angle % 360;
		return newAngle;
	}
	
	/**
	 * Takes in any angle and converts it to a range of [-180 , 180)
	 * @param angle the angle to scale
	 * @return the scaled angle
	 */
	public static double norm180(double angle)
	{
		double newAngle = angle % 360;
		newAngle -= 180;
		return newAngle;
	}
}
