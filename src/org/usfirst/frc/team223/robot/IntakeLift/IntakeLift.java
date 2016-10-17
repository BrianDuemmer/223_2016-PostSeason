package org.usfirst.frc.team223.robot.IntakeLift;

import org.usfirst.frc.team223.robot.OI;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;


public class IntakeLift extends PIDSubsystem {
	
	CANTalon intakeLiftMot;

    // Initialize
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

    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
    	return 0.0;
    }
    
    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
    }
}
