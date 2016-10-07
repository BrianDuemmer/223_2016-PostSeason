package team223.AdvancedX;

/**
 * Simple class used to describe a speed controller on the robot. This class operates like a C-style struct,
 * and has no methods other than a constructor, which populates its' instance variables.
 * @author Duemmer
 *
 */
public class SCDescriptor {

	String name;
	public int id;
	public boolean isCAN;
	public boolean invert;
	public int pdpChannel;
	public boolean useBrake;
	
	
	/** Initialize a new SCDescriptor
	 * 
	 * @param name name of the motor
	 * @param id PWM channel / CANid
	 * @param isCAN Talon SRX if true, victorSP if false
	 * @param invert if true, invert the motor
	 * @param pdpChannel PDP Channel
	 * @param useBrake If true, enables Brake mode on SRXs. Does nothing for PWM controllers.
	 */
	public SCDescriptor(String name, int id, boolean isCAN, boolean invert, int pdpChannel, boolean useBrake)
	{
		this.name = name;
		this.id = id;
		this.isCAN = isCAN;
		this.invert = invert;
		this.pdpChannel = pdpChannel;
		this.useBrake = useBrake;
	}
	
}
