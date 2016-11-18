package org.usfirst.frc.team223.robot.drive;

import org.usfirst.frc.team223.AdvancedX.DriveSide;
import org.usfirst.frc.team223.AdvancedX.TankCascadeController;
import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;
import org.usfirst.frc.team223.robot.drive.driveCommands.*;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Gyro;

/** Acts as the drivetrain Subsystem of the robot.
 *
 *@author Brian Duemmer
 */
public class driveTrain extends Subsystem {
	
	// drivetrain speed controllers
    private CANTalon driveL1;
    private CANTalon driveL2;
    private CANTalon driveR1;
    private CANTalon driveR2;
    
    
    // NavX
//    private AHRS navx;
    
    
    // Cascade controller
    private TankCascadeController controller;
    
    // Flashlight relay
    private Relay light;
    
    
    
    public driveTrain()
    {
    	// Call the superclass constructor
    	super();
    	
    	// Initialize the driveSides to run at the proper period
    	DriveSide leftSide = new DriveSide(OI.DRIVE_SLAVE__PID_PERIOD);
    	DriveSide rightSide = new DriveSide(OI.DRIVE_SLAVE__PID_PERIOD);
    	
    	
    	
    	/////////////////////// Left Side Drive motors /////////////////////////
    	
    	// Initialize the left Side drive motors
    	driveL1 = new CANTalon(OI.DRIVE_MOTOR_L1_ID);
    	driveL1.setInverted(OI.DRIVE_MOTOR_L1_INVERT);
    	
    	driveL2 = new CANTalon(OI.DRIVE_MOTOR_L2_ID);
    	driveL2.setInverted(OI.DRIVE_MOTOR_L2_INVERT);
    	
    	// Configure the SRX to use the encoder
    	driveL1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	driveL1.setPIDSourceType(PIDSourceType.kRate);
    	driveL1.setEncPosition(0);
    	
    	// add the motors
    	leftSide.addMotor(driveL1);
    	leftSide.addMotor(driveL2);
    	
    	// Configure the DriveSide
    	leftSide.setPIDSource(driveL1);
    	leftSide.setPIDSrcScalings(OI.DRIVE_ENCODER_L_CP__FOOT, OI.DRIVE_ENCODER_L_INVERT);
    	leftSide.setMaxOutput(OI.DRIVE_MAX__OUTPUT);
    	leftSide.setBrakeCount(OI.DRIVE_DEFAULT__BRAKE__COUNT);
    	
    	// Configure the PID
    	leftSide.setPID
    			(
    					OI.DRIVE_L_PID_KP,
    					OI.DRIVE_L_PID_KI,
    					OI.DRIVE_L_PID_KD,
    					OI.DRIVE_L_PID_KF
    			);
    	
    	
    	
    	
    	
    	////////////////////// Right Side drive motors ////////////////////////
    	
    	//Right side drive motors
    	driveR1 = new CANTalon(OI.DRIVE_MOTOR_R1_ID);
    	driveR1.setInverted(OI.DRIVE_MOTOR_R1_INVERT);
    	
    	driveR2 = new CANTalon(OI.DRIVE_MOTOR_R2_ID);
    	driveR2.setInverted(OI.DRIVE_MOTOR_R2_INVERT);
    	
    	// Configure the SRX to use the encoder
    	driveR1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	driveR1.setPIDSourceType(PIDSourceType.kRate);
    	driveR1.setEncPosition(0);
    	
    	// add the motors
    	rightSide.addMotor(driveR1);
    	rightSide.addMotor(driveR2);
    	
    	// Configure the DriveSide
    	rightSide.setPIDSource(driveR1);
    	rightSide.setPIDSrcScalings(OI.DRIVE_ENCODER_R_CP__FOOT, OI.DRIVE_ENCODER_R_INVERT);
    	rightSide.setMaxOutput(OI.DRIVE_MAX__OUTPUT);
    	rightSide.setBrakeCount(OI.DRIVE_DEFAULT__BRAKE__COUNT);
    	
    	// Configure the PID
    	rightSide.setPID
    			(
    					OI.DRIVE_R_PID_KP,
    					OI.DRIVE_R_PID_KI,
    					OI.DRIVE_R_PID_KD,
    					OI.DRIVE_R_PID_KF
    			);
    	
    	
    	
    	
    	// Configre the navX
//    	navx = new AHRS(SerialPort.Port.kMXP);
    	
    	
    	// Initialize the Cascade controller
    	controller = new TankCascadeController(leftSide, rightSide, (Gyro) null, OI.DRIVE_MASTER__PID_PERIOD, OI.DRIVE_SLAVE__PID_PERIOD);
    	
    	// Configure the position PID
    	controller.setPosPID
    			(
    					OI.DRIVE_POS_PID_KP,
    					OI.DRIVE_POS_PID_KI,
    					OI.DRIVE_POS_PID_KD,
    					OI.DRIVE_POS_PID_KF
				);
    	
    	// Configure the turn PID
    	controller.setTurnPID
    			(
    					OI.DRIVE_TURN_PID_KP,
    					OI.DRIVE_TURN_PID_KI,
    					OI.DRIVE_TURN_PID_KD,
    					OI.DRIVE_TURN_PID_KF
				);
    	
    	// Initialize the flashlight
    	light = new Relay(OI.FLASHLIGHT_RELAY_PORT, Relay.Direction.kForward);
    }

    
    
    /**
     * Set the default command to {@link setDriveFromJoy}
     */
    public void initDefaultCommand() {   setDefaultCommand(new setDriveFromJoy());   }
    
    
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
    	controller.setRawOut(outLeft, outRight);
    }
	
    
    
	
	/**
	 * @return the Cascade Controller
	 */
	public TankCascadeController getController() {
		return controller;
	}
	
	
	
	
	/**
	 * Logs the encoder positions and velocities to the driverstation
	 * @param showLeft if true, prints the data for the left encoder
	 * @param showRight if true, prints the data for the right encoder
	 */
	public void logEnc(boolean showLeft ,boolean showRight)
	{
		if(showLeft)
			Robot.printToDS(controller.getLeftSide().reportPIDSource("Left side"), "Drive");
		
		if(showRight)
			Robot.printToDS(controller.getRightSide().reportPIDSource("Right side"), "Drive");
	}



	/**
	 * @return the light
	 */
	public Relay getLight() {
		return light;
	}



	/**
	 * @param light the light to set
	 */
	public void setLight(Relay light) {
		this.light = light;
	}

}















