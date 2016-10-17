package org.usfirst.frc.team223.robot.drive;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.drive.driveCommands.*;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class driveTrain extends Subsystem {
	
	// drivetrain speed controllers
    private CANTalon driveL1;
    private CANTalon driveL2;
    private CANTalon driveR1;
    private CANTalon driveR2;
    
    public driveTrain()
    {
    	super();
    	// Left Side drive motors
    	driveL1 = new CANTalon(OI.MOTOR_DRIVEL1_ID);
    	driveL1.setInverted(OI.MOTOR_DRIVEL1_INVERT);
    	driveL1.enableBrakeMode(OI.MOTOR_DRIVEL1_BRAKE);
    	
    	driveL2 = new CANTalon(OI.MOTOR_DRIVEL2_ID);
    	driveL2.setInverted(OI.MOTOR_DRIVEL2_INVERT);
    	driveL2.enableBrakeMode(OI.MOTOR_DRIVEL2_BRAKE);
    	
    	//Right side drive motors
    	driveR1 = new CANTalon(OI.MOTOR_DRIVER1_ID);
    	driveR1.setInverted(OI.MOTOR_DRIVER1_INVERT);
    	driveR1.enableBrakeMode(OI.MOTOR_DRIVER1_BRAKE);
    	
    	driveR2 = new CANTalon(OI.MOTOR_DRIVER2_ID);
    	driveR2.setInverted(OI.MOTOR_DRIVER2_INVERT);
    	driveR2.enableBrakeMode(OI.MOTOR_DRIVER2_BRAKE);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new setFromJoy());
    }
    
    
    /** Drives the robot from an open loop forward and turn value.
     * This is mostly used for driver control.
     * 
     * @param fwd the forward value from a joystick
     * @param turn the x axis output from a joystick
     */
    public void driveArcade(double fwd, double turn)
    {
    	// calculate the base outputs
    	double outLeft = fwd + turn;
    	double outRight = fwd - turn;
    	
    	// if the outputs are not in the domain of [-1,1], coerce them to fit into said domain
    	outLeft = (outLeft > 1) ?  1 : outLeft;
    	outLeft = (outLeft < -1) ?  -1 : outLeft;
    	
    	outRight = (outRight > 1) ?  1 : outRight;
    	outRight = (outRight < -1) ?  -1 : outRight;
    	
    	// set the outputs
    	driveL1.set(outLeft);
    	driveL2.set(outLeft);
    	
    	driveR1.set(outRight);
    	driveR2.set(outRight);
    }
}

