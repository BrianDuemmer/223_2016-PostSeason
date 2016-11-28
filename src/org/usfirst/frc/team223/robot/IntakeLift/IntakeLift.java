package org.usfirst.frc.team223.robot.IntakeLift;

import org.apache.log4j.Logger;

import org.usfirst.frc.team223.AdvancedX.*;
import org.usfirst.frc.team223.AdvancedX.robotParser.EncoderData;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLAllocator;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLparser;
import org.usfirst.frc.team223.AdvancedX.robotParser.LimitData;
import org.usfirst.frc.team223.AdvancedX.robotParser.MotorData;
import org.usfirst.frc.team223.AdvancedX.robotParser.PIDData;
import org.usfirst.frc.team223.robot.IntakeLift.intakeCommands.*;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 * Class for the intake lift subsystem. Does not include the intake wheels.
 * @author Brian Duemmer
 *
 */
public class IntakeLift extends Subsystem {
	
	private Logger logger;

	//////////// IntakeLift Subsystem /////////////
	
	private MotorData			MOTOR_DATA;
	private CANTalon			MOTOR_HDL;
	
	private PIDData				PID_DATA;
	private PIDController		PID_HDL;
	
	private EncoderData			ENCODER_DATA;
	private Encoder				ENCODER_HDL;
	
	private LimitData			LIMIT_DATA;
	private InterruptableLimit	LIMIT_HDL;
	
	// Setpoints
	public double				SETPOINT_BALL__GRAB__ANGLE;
	public double				SETPOINT_LIMIT__POS;
	
	
	
	// Offset for the encoder. This value is subtracted from the value returned from encoder.getPosition();
	private double 		encoderOffset;
	
	// This tells us if the intakeLift has been zeroed since startup
	public boolean		hasBeenZeroed;
	
	// If true, the IntakeLift is in dedicated PID mode (not on position hold)
	public boolean		inPIDmove;

	
	
	
	
	
	/////////////////////////////////// Dependent Classes ///////////////////////////////
	
	// Class to act as a PIDInput for the PID loop
	private class IntakeLiftPIDSource implements PIDSource
	{
		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {}

		@Override
		public PIDSourceType getPIDSourceType() {   return PIDSourceType.kDisplacement;   }

		
	    /** Gets the properly scaled and adjusted position of
	     * the intake lift
	     * 
	     * @return the position of the intake lift encoder
	     */
		@Override
		public double pidGet() {   return getPos();   }	
	}
	
	
	// Class to act as a PIDOutput for the PID loop
	private class IntakeLiftPIDOutput implements PIDOutput
	{
		@Override
		public void pidWrite(double output) 
		{
	    	// Only update the output if the PID is enabled
	    	if(getPIDHandle().isEnabled())
	    		setOutput(output); 
		}
	}
	
	
	/////////////////////////////////////// Methods /////////////////////////////////////
	
    /**
     * Initialize the Intake Lift system. Here the motor, encoder, and limit
     * switch are initialized, as well as the PID controller. Important values
     * are loaded from OI.java
     */
    public IntakeLift(GXMLparser parser, Logger logger)   {   init(parser, logger);   }
    
    
    
    
    
    /**
     * Initializes the {@link IntakeLift} Subsystem.  Reads all data from
     * the configuration file, and allocates it accordingly
     * @param parser the {@link GXMLparser} that is bound to the configuration file
     * @param logger the Log4j logger object that will be used to print out any messages
     */
    public void init(GXMLparser parser, Logger logger)
    {
    	this.logger = logger;
    	
    	// Allocator object to allocate the parsed data into objects
    	GXMLAllocator allocator = new GXMLAllocator(logger);
    	
    	
    	// log us entering the init routine
    	this.logger.info("\n\r\n\r\n\r===================================== Initializing IntakeLift Subsystem =====================================");
    	
    	// Parse the data from the config file
		this.ENCODER_DATA = parser.parseEncoder("IntakeLift/encoder"); 
		this.MOTOR_DATA = parser.parseMotor("IntakeLift/motor");  
		this.PID_DATA = parser.parsePID("IntakeLift/PID"); 
		this.LIMIT_DATA = parser.parseLimit("IntakeLift/limit"); 
		this.SETPOINT_BALL__GRAB__ANGLE = parser.parseSetpoint("IntakeLift/setpoints", "ballGrabAngle");
		this.SETPOINT_LIMIT__POS = parser.parseSetpoint("IntakeLift/setpoints", "limitPos");
		
		
		// Allocate the data
		logger.info("\n\rAllocating IntakeLift data...");
		
		this.MOTOR_HDL = (CANTalon) allocator.allocateMotor(this.MOTOR_DATA);
		this.ENCODER_HDL = allocator.allocateRegEncoder(this.ENCODER_DATA);
		this.LIMIT_HDL = allocator.allocateLimit(this.LIMIT_DATA, new IntakeLimitISR());
		this.PID_HDL = allocator.allocatePID(PID_DATA, new IntakeLiftPIDSource(), new IntakeLiftPIDOutput());
		
    	// set hasBeenZeroed to false
    	hasBeenZeroed = false;
    	
    	// Enable interrupts
    	this.LIMIT_HDL.enableInterrupts();
    	
		logger.info("Finished initializing IntakeLift");
    }
    
    
    
    
    /**
     * Deallocates all physical objects ties to the IntakeLift. This must be called before
     * {@link IntakeLift#init(GXMLparser, Logger)} in order to prevent conflicts
     */
    public void cleanup()
    {
    	// log that we are shutting down
    	logger.info("Shutting down IntakeLift...");
    	
    	// free the resources
    	if(this.LIMIT_HDL != null)
    		this.LIMIT_HDL.free();
    	
    	if(this.MOTOR_HDL != null)
    		this.MOTOR_HDL.delete();
    	
    	if(this.PID_HDL != null)
    		this.PID_HDL.free();
    }
    
    
    
    
    // Set the default command to output to the motor from the joystick
    public void initDefaultCommand() {   setDefaultCommand(new SetIntakeLiftFromJoy());   }
    
    
    
    
    /** Set the output of the Intake Lift motor
     * 
     * @param output the value to send to the encoder
     */
    public void setOutput(double output) {   this.MOTOR_HDL.set(output);   }
 
    
    
    /** Gets the raw position of the intake lift
     * 
     * @return the raw position of the intake lift encoder
     */
    public double getRawEncPos() {   return this.ENCODER_HDL.getDistance();   }
    
    
    
    /**Sets the offset for the IntakeLift encoder
     * 
     * @param newOffset the new offset
     */
    public void setEncOffset(double newOffset) {   encoderOffset = newOffset;   }
    
    /**
     * Gets the position of the IntakeLift
     * @return
     */
    public double getPos() {   return ENCODER_HDL.getDistance() - encoderOffset;   }
    
    
    // Getter methods
    public InterruptableLimit getLimitHandle() {   return this.LIMIT_HDL;   }
    public CANTalon getMotorHandle() {   return this.MOTOR_HDL;   }
    public Encoder getEncoderHandle() {   return this.ENCODER_HDL;   }





	/**
	 * @return the pID_HDL
	 */
	public PIDController getPIDHandle() {
		return PID_HDL;
	}

    
    
}



















