package org.usfirst.frc.team223.robot.IntakeLift;

import org.usfirst.frc.team223.AdvancedX.*;
import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.IntakeLift.intakeCommands.*;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.PIDSubsystem;


public class IntakeLift extends PIDSubsystem {
	

	CANTalon 			intakeLiftMot;
	Encoder 			intakeLiftEncoder;
	InterruptableLimit	limit;
	
	double 				encoderOffset;

    /**
     * Initialize the Intake Lift system. Here the motor, encoder, and limit
     * switch are initialized, as well as the PID controller. Important values
     * are loaded from OI.java
     */
    public IntakeLift() 
    {
    	// set the PID constants
    	super(OI.INTAKELIFT_PID_KP, OI.INTAKELIFT_PID_KI, OI.INTAKELIFT_PID_KD);
    	
    	// set the tolerance
    	setAbsoluteTolerance(OI.INTAKELIFT_PID_TOLERANCE);
    	
    	
    	
    	// initialize motor
    	intakeLiftMot = new CANTalon(OI.INTAKELIFT_MOTOR_ID);
    	intakeLiftMot.setInverted(OI.INTAKELIFT_MOTOR_INVERT);
    	intakeLiftMot.enableBrakeMode(OI.INTAKELIFT_MOTOR_BRAKE);
    	
    	
    	
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
		    			false
    			);
    	
    	// enable interrupts
    	limit.enableInterrupts();
    }
    
    // Set the default command to output to the motor from the joystick
    public void initDefaultCommand() {   setDefaultCommand(new SetIntakeLiftFromJoy());   }

    // get the distance returned from the encoder
    protected double returnPIDInput() {   return intakeLiftEncoder.getDistance();   }
    
    protected void usePIDOutput(double output) {   setOutput(output);   }
    
    // Set the output of the motor
    public void setOutput(double output) {   intakeLiftMot.set(output);   }
    
}



















