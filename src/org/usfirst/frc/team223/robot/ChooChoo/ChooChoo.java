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
	
	// Previous position of the encoder
	private double		prevEncPos;
	
	// Encoder wrap angle
	private double		encWrap;

    /**
     * Initialize the ChooChoo (catapult) system. Here all of the subsystem 
     * dependents are handled, as well as the PID controller. 
     * Important values are loaded from OI.java
     */
    public ChooChoo() 
    {
    	// Set the PID constants
    	super(OI.CHOOCHOO_PID_KP, OI.CHOOCHOO_PID_KI, OI.CHOOCHOO_PID_KD);
    	
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
    	
    	prevEncPos = 0;
    }
    
    
    // Set the default command to output to the motor from the joystick
    public void initDefaultCommand() {   setDefaultCommand(new SetCCfromJoy());   }
    
    
    
    /**
     * Get the encoder position, while also accounting for encoder inversion 
     * and offset
     */
    protected double returnPIDInput() 
    {
    	double currPos = getRawEncPos();
    	
    	// true if wrapped forward
    	if(prevEncPos >= 360 - OI.CHOOCHOO_ENCODER_WRAP__THRESHOLD && currPos <= OI.CHOOCHOO_ENCODER_WRAP__THRESHOLD)
    		encWrap += 360;
    	
    	//true if wrapped backwards
    	if(currPos > 360 - OI.CHOOCHOO_ENCODER_WRAP__THRESHOLD && prevEncPos <= OI.CHOOCHOO_ENCODER_WRAP__THRESHOLD)
    		encWrap -= 360;
    	
    	return currPos + encWrap - encoderOffset;
    }
    
    
    
    /**
     * Gets the position of the encoder without accounting for encoderOffset
     */
	public double getRawEncPos() 
	{
		return OI.CHOOCHOO_ENCODER_INVERT ? chooChooMot.get() * -1 : chooChooMot.get();
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




















