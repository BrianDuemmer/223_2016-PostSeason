package org.usfirst.frc.team223.robot.drive;

import org.usfirst.frc.team223.AdvancedX.DriveSide;
import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;
import org.usfirst.frc.team223.robot.drive.driveCommands.*;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDSourceType;
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
    
    // DriveSides
    private DriveSide leftSide;
    private DriveSide rightSide;
    
    
    public driveTrain()
    {
    	super();
    	
    	// Initialize the driveSides
    	leftSide = new DriveSide();
    	rightSide = new DriveSide();
    	
    	// Initialize the left side
    	
    	// Initialize the left Side drive motors
    	driveL1 = new CANTalon(OI.DRIVE_MOTOR_L1_ID);
    	driveL1.setInverted(OI.DRIVE_MOTOR_L1_INVERT);
    	driveL1.enableBrakeMode(OI.DRIVE_BRAKE_HALF || OI.DRIVE_BRAKE_FULL);
    	
    	driveL2 = new CANTalon(OI.DRIVE_MOTOR_L2_ID);
    	driveL2.setInverted(OI.DRIVE_MOTOR_L2_INVERT);
    	driveL2.enableBrakeMode(OI.DRIVE_BRAKE_FULL);
    	
    	// add the motors
    	leftSide.addMotor(driveL1);
    	leftSide.addMotor(driveL2);
    	
    	// Configure the encoder
    	driveL1.configEncoderCodesPerRev(OI.DRIVE_ENCODER_L_CP__FOOT);
    	driveL1.reverseSensor(OI.DRIVE_ENCODER_L_INVERT);
    	driveL1.setPIDSourceType(PIDSourceType.kRate);
    	
    	leftSide.setPIDSource(driveL1);
    	
    	// Configure the PID
    	leftSide.setPID
    			(
    					OI.DRIVE_L_PID_KP,
    					OI.DRIVE_L_PID_KI,
    					OI.DRIVE_L_PID_KD,
    					OI.DRIVE_L_PID_KF
    			);
    	
    	
    	
    	// Right Side drive motors
    	
    	//Right side drive motors
    	driveR1 = new CANTalon(OI.DRIVE_MOTOR_R1_ID);
    	driveR1.setInverted(OI.DRIVE_MOTOR_R1_INVERT);
    	driveR1.enableBrakeMode(OI.DRIVE_BRAKE_HALF || OI.DRIVE_BRAKE_FULL);
    	
    	driveR2 = new CANTalon(OI.DRIVE_MOTOR_R2_ID);
    	driveR2.setInverted(OI.DRIVE_MOTOR_R2_INVERT);
    	driveR2.enableBrakeMode(OI.DRIVE_BRAKE_FULL);
    	
    	// add the motors
    	rightSide.addMotor(driveR1);
    	rightSide.addMotor(driveR2);
    	
    	// Configure the encoder
    	driveR1.configEncoderCodesPerRev(OI.DRIVE_ENCODER_R_CP__FOOT);
    	driveR1.reverseSensor(OI.DRIVE_ENCODER_R_INVERT);
    	driveR1.setPIDSourceType(PIDSourceType.kRate);
    	
    	rightSide.setPIDSource(driveR1);
    	
    	// Configure the PID
    	rightSide.setPID
    			(
    					OI.DRIVE_R_PID_KP,
    					OI.DRIVE_R_PID_KI,
    					OI.DRIVE_R_PID_KD,
    					OI.DRIVE_R_PID_KF
    			);
    }

    
    
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new setDriveFromJoy());
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
    	leftSide.setRawOutput(outLeft);
    	rightSide.setRawOutput(outRight);
    	
    }
}

