package org.usfirst.frc.team223.robot.intakeWheels;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.intakeWheels.iWheelsCommands.SetIntakeWheelsFromController;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The class that contains the intake wheels subsystem. Does not include 
 * intake lift.
 * 
 * @author Brian Duemmer
 */
public class IntakeWheels extends Subsystem {
    
	// Physical objects that are part of the subsystem
	CANTalon 		intakeWheelsMotor;
	
	public IntakeWheels()
	{
		// Configure the motor
		intakeWheelsMotor = new CANTalon(OI.INTAKEWHEELS_MOTOR_ID);
		intakeWheelsMotor.setInverted(OI.INTAKEWHEELS_MOTOR_INVERT);
		intakeWheelsMotor.enableBrakeMode(OI.INTAKEWHEELS_MOTOR_BRAKE);
	}
	
    public void initDefaultCommand() {
    	setDefaultCommand(new SetIntakeWheelsFromController());
    }
    
    /**
     * Sets the output of the motor
     * @param output the output value to send to the motor
     */
    public void setOutput(double output) {   intakeWheelsMotor.set(output);   }
}

