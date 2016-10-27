package org.usfirst.frc.team223.robot.ChooChoo;

import org.usfirst.frc.team223.AdvancedX.InterruptableLimit;
import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.ChooChoo.ccCommands.ChooChooISR;
import org.usfirst.frc.team223.robot.ChooChoo.ccCommands.SetCCfromJoy;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;



public class ChooChoo extends PIDSubsystem {

	// Physical objects that are part of the subsystem
	CANTalon 					chooChooMot;
	public InterruptableLimit	chooChooBeam;
	
	
	// Tells us if we have been zeroed
	public boolean		hasBeenZeroed;
	
	// Offset for the encoder. This value is subtracted from the 
	// value returned from encoder.getPosition().
	private double 		encoderOffset;

	
	
	
////////////////////////////////// Methods ////////////////////////////////////	
	
	
    /**
     * Initialize the ChooChoo (catapult) system. Here all of the subsystem 
     * dependents are handled, as well as the PID controller. 
     * Important values are loaded from OI.java
     */
    public ChooChoo() 
    {
    	// Set the PID constants
    	super(OI.CHOOCHOO_PID_KP, OI.CHOOCHOO_PID_KI, OI.CHOOCHOO_PID_KD);
    	
    	// Set the tolerance
    	setAbsoluteTolerance(OI.CHOOCHOO_PID_TOLERANCE);
    	
    	// set hasBeenZeroed to false
    	hasBeenZeroed = false;
    	
    	// initialize motor
    	chooChooMot = new CANTalon(OI.CHOOCHOO_MOTOR_ID);
    	chooChooMot.setInverted(OI.CHOOCHOO_MOTOR_INVERT);
    	chooChooMot.enableBrakeMode(OI.CHOOCHOO_MOTOR_BRAKE);
    	
    	// initialize the encoder
    	chooChooMot.configEncoderCodesPerRev((int)(1 / OI.CHOOCHOO_ENCODER_DEGREES__PER__COUNT));
    	
    	// initialize the beam sensor
    	chooChooBeam = new InterruptableLimit
		    	(
		    			OI.CHOOCHOO_BEAM_ID, 
		    			new ChooChooISR(), 
		    			OI.CHOOCHOO_BEAM_NORMALLY__OPEN,
		    			true, 
		    			false,
		    			OI.CHOOCHOO_BEAM_DEBOUNCE__TIME
    			);
    	
    	// enable interrupts
    	chooChooBeam.enableInterrupts();
    }
    
    
    // Set the default command to output to the motor from the joystick
    public void initDefaultCommand() {   setDefaultCommand(new SetCCfromJoy());   }
    
    
    
    /**
     * Get the encoder position, while also accounting for encoder inversion 
     * and offset
     */
    protected double returnPIDInput() 
    {
    	return getRawEncPos() - encoderOffset;
    }
    
    
    
    /**
     * Gets the position of the encoder without accounting for encoderOffset
     */
	public double getRawEncPos() 
	{
		return OI.CHOOCHOO_ENCODER_INVERT ? chooChooMot.get() * -1 : chooChooMot.get();
	}
	
	
	
	/**
	 * Returns true if the choo choo mechanism is absolutely on target.
	 * This ignores wrapping angles, and looks to see if we are physically
	 * within the tolerance. For example, with a threshold of 10, a setpoint
	 * of 750, and a position of 25, this would return true.
	 * 
	 * @return if the absolute error is within the range specified by
	 * OI.CHOOCHOO_PID_TOLERANCE
	 */
	public boolean onAbsoluteTarget()
	{
		// Get the absolute position and setpoint
		double pos = returnPIDInput() % 360;
		double setpt = getSetpoint() % 360;

		// See if we are within the tolerance for the PID
		boolean onTarget = Math.abs(pos - setpt) <= OI.CHOOCHOO_PID_TOLERANCE;
		return onTarget;
	}
	
	
	
	/**
	 * Returns true if the choo choo mechanism is absolutely on target.
	 * This ignores wrapping angles, and looks to see if we are physically
	 * within the tolerance. For example, with a threshold of 10, a setpoint
	 * of 750, and a position of 25, this would return true.
	 * 
	 * @return if the absolute error is within the range specified by
	 * OI.CHOOCHOO_PID_TOLERANCE
	 */
	public boolean onAbsoluteTarget(double target)
	{
		// Get the absolute position and setpoint
		double pos = returnPIDInput() % 360;
		target %= 360;

		// See if we are within the tolerance for the PID
		boolean onTarget = Math.abs(pos - target) <= OI.CHOOCHOO_PID_TOLERANCE;
		return onTarget;
	}
    
    
	
    /** Set the output of the Choo Choo motor
     * 
     * @param output the value to send to the encoder
     */
    public void setOutput(double output) {   chooChooMot.set(output);   }
    
    
    
    /**Sets the offset for the IntakeLift encoder
     * 
     * @param newOffset the new offset
     */
	public void setEncOffset(double newChooChooOffset) {   encoderOffset = newChooChooOffset;   }
    protected void usePIDOutput(double output) {   setOutput(output);   }
}




















