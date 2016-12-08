package org.usfirst.frc.team223.robot.ChooChoo;

import org.usfirst.frc.team223.AdvancedX.InterruptableLimit;
import org.usfirst.frc.team223.AdvancedX.robotParser.EncoderData;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLAllocator;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLparser;
import org.usfirst.frc.team223.AdvancedX.robotParser.LimitData;
import org.usfirst.frc.team223.AdvancedX.robotParser.MotorData;
import org.usfirst.frc.team223.AdvancedX.robotParser.PIDData;
import org.usfirst.frc.team223.robot.Robot;
import org.usfirst.frc.team223.robot.ChooChoo.ccCommands.ChooChooISR;
import org.usfirst.frc.team223.robot.ChooChoo.ccCommands.SetCCfromJoy;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import net.sf.microlog.core.Logger;


/**
 * Subsystem to control the Choo Choo (catapult) mechanism
 * @author Brian Duemmer
 *
 */
public class ChooChoo extends Subsystem {
	
	private Logger logger = Robot.roboLogger.getLogger("Choo Choo");
	
	
	// Data and objects to be acquired from parsing a configuration file
	private MotorData			MOTOR_DATA;
	private CANTalon			MOTOR_HDL;
	
	private PIDData				PID_DATA;
	private PIDController		PID_HDL;
	
	private EncoderData			ENCODER_DATA;
	
	private LimitData			LIMIT_DATA;
	private InterruptableLimit	LIMIT_HDL;
	
	// Setpoints
	public double				SETPOINT_BEAM__HIT__ANGLE;
	public double				SETPOINT_LOAD__ANGLE;
	public double				SETPOINT_UNLOAD__ANGLE;
	
	
	
	// Tells us if we have been zeroed
	public boolean		hasBeenZeroed;
	
	// Offset for the encoder. This value is subtracted from the 
	// value returned from encoder.getPosition().
	private double 		encoderOffset;

	
	
	
/////////////////////////////// Sub - Classes /////////////////////////////////
	
	// Class to act as a PIDInput for the PID loop
	private class ChooChooPIDSource implements PIDSource
	{
		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {}

		@Override
		public PIDSourceType getPIDSourceType() {   return PIDSourceType.kDisplacement;   }

		@Override
		public double pidGet() {   return getPos();   }
	}
	
	
	
	
	// Class to act as a PIDOutput for the PID loop
	private class ChooChooPIDOutput implements PIDOutput
	{
		@Override
		public void pidWrite(double output) {   setOutput(output);   }
	}
	
	
////////////////////////////////// Methods ////////////////////////////////////	
	
	
    /**
     * Initialize the ChooChoo (catapult) system. Here all of the subsystem 
     * dependents are handled, as well as the PID controller. 
     */
    public ChooChoo(GXMLparser parser, Logger logger) 
    {
    	// initialize the subsystem
    	init(parser, logger);
    }
    
    
    
    
    
    /**
     * Initialize the ChooChoo (catapult) system. Reads all data from
     * the configuration file, and allocates it accordingly
     */
    public void init(GXMLparser parser, Logger logger)
    {
    	this.logger = logger;
    	
    	// Allocator to use for allocating the parsed data into objects
    	GXMLAllocator allocator = new GXMLAllocator(this.logger);
    	
    	
    	// log us entering the init routine
    	this.logger.info("\n\r\n\r\n\r====================================== Initializing ChooChoo Subsystem ======================================");
    	
    	// parse the data
    	this.ENCODER_DATA= parser.parseEncoder("ChooChoo/encoder");  
		this.MOTOR_DATA = parser.parseMotor("ChooChoo/motor");  
		this.PID_DATA = parser.parsePID("ChooChoo/PID"); 
		this.LIMIT_DATA = parser.parseLimit("ChooChoo/limit"); 
		this.SETPOINT_BEAM__HIT__ANGLE = parser.parseSetpoint("ChooChoo/setpoints", "beamHitAngle");
		this.SETPOINT_LOAD__ANGLE = parser.parseSetpoint("ChooChoo/setpoints", "loadAngle");
		this.SETPOINT_UNLOAD__ANGLE = parser.parseSetpoint("ChooChoo/setpoints", "unloadAngle");
		
		
		// allocate the data
		logger.info("\n\rAllocating ChooChoo data...");
		
		this.MOTOR_HDL = (CANTalon) allocator.allocateMotor(this.MOTOR_DATA);
		this.LIMIT_HDL = allocator.allocateLimit(this.LIMIT_DATA, new ChooChooISR());
		this.PID_HDL = allocator.allocatePID(PID_DATA, new ChooChooPIDSource(), new ChooChooPIDOutput());
		
		// Configure the Encoder on the CANTalon
		allocator.allocateCANEncoder(this.ENCODER_DATA, this.MOTOR_HDL);
		
		
		
    	// set hasBeenZeroed to false
    	hasBeenZeroed = false;
    	
    	// enable interrupts on the beam sensor
    	this.LIMIT_HDL.enableInterrupts();
		
		logger.info("Finished initializing ChooChoo");
    }
    
    
    
    
    
    /**
     * Deallocates all physical objects ties to the ChooChoo. This must be called before
     * {@link ChooChoo#init(GXMLparser, Logger)} in order to prevent conflicts
     */
    public void cleanup()
    {
    	// log that we are shutting down
    	logger.info("Shutting down ChooChoo...");
    	
    	// free the resources
    	if(this.LIMIT_HDL != null)
    		this.LIMIT_HDL.free();
    	
    	if(this.MOTOR_HDL != null)
    		this.MOTOR_HDL.delete();
    	
    	if(this.PID_HDL != null)
    		this.PID_HDL.free();
    }
    
    
    
    
    
    
    // Set the default command to output to the motor from the joystick
    public void initDefaultCommand() {   setDefaultCommand(new SetCCfromJoy());   }
    
    
    
    /**
     * Get the encoder position, while also accounting for encoder inversion 
     * and offset
     */
    public double getPos(){   return getRawEncPos() - encoderOffset;   }
    
    
    
    /**
     * Gets the position of the encoder without accounting for encoderOffset
     */
	public double getRawEncPos() 
	{
		// Get the raw encoder position, inverting if necessary
		double ret = this.MOTOR_HDL.getEncPosition();
		ret *= this.ENCODER_DATA.invert  ?  -1 : 1;
		
		// Scale it and return
		ret *= this.ENCODER_DATA.distPerCount;
		return ret;
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
		double pos = getPos() % 360;
		double setpt = this.PID_HDL.getSetpoint() % 360;

		// See if we are within the tolerance for the PID
		boolean onTarget = Math.abs(pos - setpt) <= this.PID_DATA.tolerance;
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
		// read our current position
		double pos = getPos();
		
		// Scale all of our ranges to be between 0 and 360
		pos %= 360;
		
		target %= 360;
		
 		// See if we are within the tolerance threshold
		boolean onTarget = Math.abs(target - pos) <= this.PID_DATA.tolerance;
		
		return onTarget;
	}
    
    
	
    /** Set the output of the Choo Choo motor
     * 
     * @param output the value to send to the encoder
     */
    public void setOutput(double output){   this.MOTOR_HDL.set(output);   }
    
    
    
    /**Sets the offset for the IntakeLift encoder
     * 
     * @param newOffset the new offset
     */
	public void setEncOffset(double newChooChooOffset) {   encoderOffset = newChooChooOffset;   }
    protected void usePIDOutput(double output) 
    {   
    	// Only update the output if the PID is enabled
    	if(this.PID_HDL.isEnabled())
    		setOutput(output);
    	
    }


/**
	 * @return the pID_HDL
	 */
	public PIDController getPIDHandle() {
		return PID_HDL;
	}





	/**
	 * @return the lIMIT_HDL
	 */
	public InterruptableLimit getLIMIT_HDL() {
		return LIMIT_HDL;
	}

}




















