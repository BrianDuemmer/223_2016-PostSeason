package org.usfirst.frc.team223.AdvancedX.robotParser;

/**
 * POD Type to store data for a PID loop, as parsed from {@link GXMLparser}
 * @author Brian Duemmer
 *
 */
public class PIDData 
{
	public double kp;
	public double ki;
	public double kd;
	public double kf;
	public double period;
	public double tolerance;
	
	/**
	 * Sets the parameters for the PIDData Object
	 */
	public PIDData(double kp, double ki, double kd, double kf, double period, double tolerance)
	{
		this.kp = kp;
		this.ki = ki;
		this.kd = kd;
		this.kf = kf;
		this.period = period;
		this.tolerance = tolerance;
	}
	
	/**
	 * Default constructor
	 */
	public PIDData(){}
	
	public String toString()
	{
		String ret = "";
		
		ret += "kp: " + kp + "\n";
		ret += "ki: " + ki + "\n";
		ret += "kd: " + kd + "\n";
		ret += "kf: " + kf + "\n";
		ret += "period: " + period + "\n";
		ret += "tolerance: " + tolerance + "\n";
		
		return ret;
	}

}
