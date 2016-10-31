package org.usfirst.frc.team223.robot.IntakeLift;

import org.usfirst.frc.team223.AdvancedX.*;
import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;
import org.usfirst.frc.team223.robot.IntakeLift.intakeCommands.*;
import org.usfirst.frc.team223.robot.generalCommands.DummyCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.PIDSubsystem;


public class IntakeLift extends PIDSubsystem {
	
	// Physical objects that are part of the subsystem
	CANTalon 					intakeLiftMot;
	Encoder 					intakeLiftEncoder;
	public InterruptableLimit	limit;
	
	// Offset for the encoder. This value is subtracted from the value returned from encoder.getPosition();
	private double 		encoderOffset;
	
	// This tells us if the intakeLift has been zeroed since startup
	public boolean		hasBeenZeroed;
	
	// If true, the IntakeLift is in dedicated PID mode (not on position hold)
	public boolean		inPIDmove;

    /**
     * Initialize the Intake Lift system. Here the motor, encoder, and limit
     * switch are initialized, as well as the PID controller. Important values
     * are loaded from OI.java
     */
    public IntakeLift() 
    {    	
    	// set the PID constants
    	super(OI.INTAKELIFT_PID_KP, OI.INTAKELIFT_PID_KI, OI.INTAKELIFT_PID_KD);
    	
    	// set hasBeenZeroed to false
    	hasBeenZeroed = false;
    	
    	// set the tolerance
    	setAbsoluteTolerance(OI.INTAKELIFT_PID_TOLERANCE);
    	
    	// set the allowable range for PID moves
    	setInputRange(OI.INTAKELIFT_SETPOINT_MAXDOWN, OI.INTAKELIFT_SETPOINT_MAXUP);
    	
    	
    	// initialize motor
    	intakeLiftMot = new CANTalon(OI.INTAKELIFT_MOTOR_ID);
    	intakeLiftMot.setInverted(OI.INTAKELIFT_MOTOR_INVERT);
    	intakeLiftMot.enableBrakeMode(OI.INTAKELIFT_MOTOR_BRAKE);
    	intakeLiftMot.setSafetyEnabled(false);
    	
    	
    	
    	// initialize the encoder
    	intakeLiftEncoder = new Encoder
    			( 	OI.INTAKELIFT_ENCODER_ID_A,
    				OI.INTAKELIFT_ENCODER_ID_B,
    				OI.INTAKELIFT_ENCODER_INVERT,
    				Encoder.EncodingType.k4X
				);
    	
    	intakeLiftEncoder.setDistancePerPulse(OI.INTAKELIFT_ENCODER_DEGREES__PER__COUNT);
    	intakeLiftEncoder.setMaxPeriod(OI.INTAKELIFT_ENCODER_MAX__PERIOD);
    	intakeLiftEncoder.setMinRate(OI.INTAKELIFT_ENCODER_MIN__RATE__SEC / OI.INTAKELIFT_ENCODER_MAX__PERIOD);
    	
    	// initialize the limit switch
    	limit = new InterruptableLimit
		    	(
		    			OI.INTAKELIFT_LIMIT_ID, 
		    			new IntakeLimitISR(), 
		    			OI.INTAKELIFT_LIMIT_NORMALLY__OPEN, 
		    			true, 
		    			false,
		    			OI.INTAKELIFT_LIMIT_DEBOUNCE__TIME
    			);
    	
    	// enable interrupts
    	limit.enableInterrupts();
    }
    
    // Set the default command to output to the motor from the joystick
    public void initDefaultCommand() {   setDefaultCommand(new SetIntakeLiftFromJoy());   }
    protected void usePIDOutput(double output) {   setOutput(output);   }

    
    
    /** Gets the properly scaled and adjusted position of
     * the intake lift
     * 
     * @return the position of the intake lift encoder
     */
    protected double returnPIDInput() 
    {   
    	double ret = intakeLiftEncoder.getDistance() - encoderOffset;    
    	return ret;
   	}
    
    
    
    /** Set the output of the Intake Lift motor
     * 
     * @param output the value to send to the encoder
     */
    public void setOutput(double output) {   intakeLiftMot.set(output);   }
 
    
    
    /** Gets the raw position of the intake lift
     * 
     * @return the raw position of the intake lift encoder
     */
    public double getRawEncPos() {   return intakeLiftEncoder.getDistance();   }
    
    
    
    /**Sets the offset for the IntakeLift encoder
     * 
     * @param newOffset the new offset
     */
    public void setEncOffset(double newOffset) {   encoderOffset = newOffset;   }
    
    public void log()
    {
    	if(OI.ROBOT_ISDEBUG)
    	{
    		String msg = "Intake Encoder Pos: " + Double.toString(getPosition()) + "\n";
    		msg += "Intake Encoder offset: " + Double.toString(encoderOffset) + "\n\n";
    		
    		// print the message to the console
    		Robot.printToDS(msg , "");
    		
    	}
    }
    
}



















