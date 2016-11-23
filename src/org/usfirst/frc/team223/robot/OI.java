package org.usfirst.frc.team223.robot;

import javax.xml.xpath.XPathExpressionException;

import org.usfirst.frc.team223.AdvancedX.*;
import org.usfirst.frc.team223.AdvancedX.robotParser.*;
import org.usfirst.frc.team223.robot.ChooChoo.ccCommands.*;
import org.usfirst.frc.team223.robot.IntakeLift.intakeCommands.*;
import org.usfirst.frc.team223.robot.generalCommands.*;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator a
 * interface to the commands and command groups that allow control of the robot.
 * 
 * @author Brian Duemmer
 */
public class OI {
	
	
	////////////////// Human Input ///////////////////
	public static Joystick driverController;
	public static Joystick operatorController;
	
	public static SmartControlStick stick_dL;
	public static SmartControlStick stick_dR;
	
	public static SmartControlStick stick_oL;
	public static SmartControlStick stick_oR;
	
	// Driver controller buttons
	public static JoystickButton button_dA;
	public static JoystickButton button_dB;
	public static JoystickButton button_dX;
	public static JoystickButton button_dY;
	public static JoystickButton button_dL;
	public static JoystickButton button_dR;
	public static JoystickButton button_dStart;
	public static JoystickButton button_dBack;
	
	
	// Operator controller buttons
	public static JoystickButton button_oA;
	public static JoystickButton button_oB;
	public static JoystickButton button_oX;
	public static JoystickButton button_oY;
	public static JoystickButton button_oL;
	public static JoystickButton button_oR;
	public static JoystickButton button_oStart;
	public static JoystickButton button_oBack;
	
	//////////////////// Parser Data ////////////////////
	public static String CONFIG_FILE_PATH = "media/sda1/MainConfig.xml";
	
	
	
	//////////////// Drive Subsystem /////////////////
	
	public TankCascadeData 		DRIVE_DATA;
	public TankCascadeController DRIVE_HDL;
	
	public double				DRIVE_VEL__FOR__TIME_BRAKE__TIME;
	public int					DRIVE_DEFAULT__BRAKE__COUNT;
	
	public int					FLASHLIGHT_RELAY_PORT;
	public double				FLASHLIGHT_HOLD__TIME;
	public Relay				FLASHLIGHT_HDL;
	
	
	
	//////////// IntakeLift Subsystem /////////////
	
	public MotorData			INTAKELIFT_MOTOR_DATA;
	public CANTalon				INTAKELIFT_MOTOR_HDL;
	
	public PIDData				INTAKELIFT_PID_DATA;
	
	public EncoderData			INTAKELIFT_ENCODER_DATA;
	public Encoder				INTAKELIFT_ENCODER_HDL;
	
	public LimitData			INTAKELIFT_LIMIT_DATA;
	public InterruptableLimit	INTAKELIFT_LIMIT_HDL;
	
	// Setpoints
	public double				INTAKELIFT_SETPOINT_BALL__GRAB__ANGLE;
	public double				INTAKELIFT_SETPOINT_LIMIT__POS;
	public double				INTAKELIFT_SETPOINT_MAXDOWN;
	public double				INTAKELIFT_SETPOINT_MAXUP;
		
	
	////////////// IntakeWheels Subsystem /////////////
	
	public MotorData			INTAKEWHEELS_MOTOR_DATA;
	public CANTalon				INTAKEWHEELS_MOTOR_HDL;
	
	
	//////////////// ChooChoo Subsystem ///////////////
	
	public MotorData			CHOOCHOO_MOTOR_DATA;
	public CANTalon				CHOOCHOO_MOTOR_HDL;
	
	public PIDData				CHOOCHOO_PID_DATA;
	
	public EncoderData			CHOOCHOO_ENCODER_DATA;
	
	public LimitData			CHOOCHOO_LIMIT_DATA;
	public InterruptableLimit	CHOOCHOO_LIMIT_HDL;
	
	// Setpoints
	public double				CHOOCHOO_SETPOINT_BEAM__HIT__ANGLE;
	public double				CHOOCHOO_SETPOINT_LOAD__ANGLE;
	public double				CHOOCHOO_SETPOINT_UNLOAD__ANGLE;
	
	
	//////////// Auto Configutation Values ////////////
	public double				AUTO_LOWBAR_INTAKE__ANGLE;
	public double				AUTO_STD__DEF_INTAKE__ANGLE;
	
	
	
	/////////// General Configutation Values //////////
	
	public double 				ZEROLIFTANDCC_CC_START_DELAY;
	
	
	
	public OI() {
		
		driverController = new Joystick(0);
		operatorController = new Joystick(1);
		
		// bind the buttons for the driver controller
		button_dA = new JoystickButton(driverController, 1);
		button_dB = new JoystickButton(driverController, 2);
		button_dX = new JoystickButton(driverController, 3);
		button_dY = new JoystickButton(driverController, 4);
		button_dL = new JoystickButton(driverController, 5);
		button_dR = new JoystickButton(driverController, 6);
		button_dStart = new JoystickButton(driverController, 7);
		button_dBack = new JoystickButton(driverController, 8);
		
		// bind the analog sticks for the driver controller
		stick_dL = new SmartControlStick(driverController, 0, 1, 9);
		stick_dL.setParams(false, true, 0.1, 1);
		stick_dR = new SmartControlStick(driverController, 4, 5, 10);
		stick_dR.setParams(false, true, 0.1, 1);
		
		
		// bind the buttons for the operator controller
		button_oA = new JoystickButton(operatorController, 1);
		button_oB = new JoystickButton(operatorController, 2);
		button_oX = new JoystickButton(operatorController, 3);
		button_oY = new JoystickButton(operatorController, 4);
		button_oL = new JoystickButton(operatorController, 5);
		button_oR = new JoystickButton(operatorController, 6);
		button_oStart = new JoystickButton(operatorController, 8);
		button_oBack = new JoystickButton(operatorController, 7);
		
		// bind the analog sticks to the operator controller
		stick_oL = new SmartControlStick(operatorController, 0, 1, 9);
		stick_oL.setParams(false, true, 0.1, 1);
		stick_oR = new SmartControlStick(operatorController, 4, 5, 10);
		stick_oR.setParams(false, true, 0.1, 1);
		
		/////////////////////////// Driver Buttons ////////////////////////////
		
		// When Rb is pressed, update the flashlight
		button_dR.whenPressed(new SetFlashlight());
		button_dR.whenReleased(new SetFlashlight());
		
		////////////////////////// Operator Buttons ///////////////////////////
		
		// when X is pressed, bring the IntakeLift to the ball grab angle
		button_oX.whenPressed(new IntakeLiftGotoPos(INTAKELIFT_SETPOINT_BALL__GRAB__ANGLE));
		
		// When A is pressed, load the ChooChoo
		button_oA.whenPressed(new LoadCC(true));
		
		// When the left stick is pressed, unload the ChooChoo
		stick_oL.getButton().whenPressed(new UnloadCC());
		
		// When start is pressed, zero the intakeLift and ChooChoo
		button_oStart.whenPressed(new ZeroLiftAndCC());

	}
	
	
	
	
	/**
	 * Loads the entire robot's data from the specified file, and populates all of the data
	 * needed to run the robot. Be sure to call {@link allocateRobot()} afterwards.
	 * @param configPath the path to the configuration file to read
	 * @return if <code>TRUE</code>, then the file was loaded successfully
	 */
	public boolean loadData(String configPath)
	{
		GXMLparser parser = null;
		
		// try to initialize the parser. if it fails, print an error, and return false
		try {
			parser = new GXMLparser(configPath);
		} catch (Exception e) 
		{
			Robot.print("Failed to open Configuration file at path \"" + configPath + "\"", true);
			return false;
		}
		
		// Try to parse the robot data
		
		// Drivetrain
		try {   
			this.DRIVE_DATA = parser.parseTankCascade("DriveTrain"); 
			Robot.print("Loaded TankCascadeController \"DriveTrain\"", false);
			} 
		catch (XPathExpressionException e) {   Robot.print("Failed to load TankCascadeController \"DriveTrain\"", true);   }
		
		
		
		//Intake Lift
		try {   
			this.INTAKELIFT_ENCODER_DATA = parser.parseEncoder("IntakeLift/encoder"); 
			Robot.print("Loaded Encoder \"IntakeLift/encoder\"", false);
			} 
		catch (XPathExpressionException e) {   Robot.print("Failed to load Encoder \"IntakeLift/encoder\"", true);   }
		
		try {   
			this.INTAKELIFT_MOTOR_DATA = parser.parseMotor("IntakeLift/motor"); 
			Robot.print("Loaded Motor \"IntakeLift/motor\"", false);
			} 
		catch (XPathExpressionException e) {   Robot.print("Failed to load Motor \"IntakeLift/motor\"", true);   }
		
		try {   
			this.INTAKELIFT_PID_DATA = parser.parsePID("IntakeLift/PID"); 
			Robot.print("Loaded PID \"IntakeLift/PID\"", false);
			} 
		catch (XPathExpressionException e) {   Robot.print("Failed to load PID \"IntakeLift/PID\"", true);   }
		
		try {   
			this.INTAKELIFT_LIMIT_DATA = parser.parseLimit("IntakeLift/limit"); 
			Robot.print("Loaded Limit \"IntakeLift/limit\"", false);
			} 
		catch (XPathExpressionException e) {   Robot.print("Failed to load Limit \"IntakeLift/limit\"", true);   }
		
		try {
			this.INTAKELIFT_SETPOINT_BALL__GRAB__ANGLE = parser.parseSetpoint("IntakeLift/setpoints", "ballGrabAngle");
			Robot.print("Loaded setpoint \"ballGrabAngle\" on IntakeLift", false);
			}
		catch (XPathExpressionException e) {   Robot.print("Failed to load setpoint \"ballGrabAngle\" on IntakeLift", true);   }
		
		try {
			this.INTAKELIFT_SETPOINT_LIMIT__POS = parser.parseSetpoint("IntakeLift/setpoints", "limitPos");
			Robot.print("Loaded setpoint \"limitPos\" on IntakeLift", false);
			}
		catch (XPathExpressionException e) {   Robot.print("Failed to load setpoint \"limitPos\" on IntakeLift", true);   }
		
		try {
			this.INTAKELIFT_SETPOINT_MAXDOWN = parser.parseSetpoint("IntakeLift/setpoints", "maxDown");
			Robot.print("Loaded setpoint \"maxDown\" on IntakeLift", false);
			}
		catch (XPathExpressionException e) {   Robot.print("Failed to load setpoint \"maxDown\" on IntakeLift", true);   }
		
		try {
			this.INTAKELIFT_SETPOINT_MAXUP = parser.parseSetpoint("IntakeLift/setpoints", "maxUp");
			Robot.print("Loaded setpoint \"maxUp\" on IntakeLift", false);
			}
		catch (XPathExpressionException e) {   Robot.print("Failed to load setpoint \"maxUp\" on IntakeLift", true);   }
		
	
		
		
		// Choo Choo
		try {   
			this.CHOOCHOO_ENCODER_DATA= parser.parseEncoder("ChooChoo/encoder"); 
			Robot.print("Loaded Encoder \"ChooChoo/encoder\"", false);
			} 
		catch (XPathExpressionException e) {   Robot.print("Failed to load Encoder \"ChooChoo/encoder\"", true);   }
		
		try {   
			this.CHOOCHOO_MOTOR_DATA = parser.parseMotor("ChooChoo/motor"); 
			Robot.print("Loaded Motor \"ChooChoo/motor\"", false);
			} 
		catch (XPathExpressionException e) {   Robot.print("Failed to load Motor \"ChooChoo/motor\"", true);   }
		
		try {   
			this.CHOOCHOO_PID_DATA = parser.parsePID("ChooChoo/PID"); 
			Robot.print("Loaded PID \"ChooChoo/PID\"", false);
			} 
		catch (XPathExpressionException e) {   Robot.print("Failed to load PID \"ChooChoo/PID\"", true);   }
		
		try {   
			this.CHOOCHOO_LIMIT_DATA = parser.parseLimit("ChooChoo/limit"); 
			Robot.print("Loaded Limit \"ChooChoo/limit\"", false);
			} 
		catch (XPathExpressionException e) {   Robot.print("Failed to load Limit \"ChooChoo/limit\"", true);   }
		
		try {
			this.CHOOCHOO_SETPOINT_BEAM__HIT__ANGLE = parser.parseSetpoint("ChooChoo/setpoints", "beamHitAngle");
			Robot.print("Loaded setpoint \"beamHitAngle\" on ChooChoo", false);
			}
		catch (XPathExpressionException e) {   Robot.print("Failed to load setpoint \"beamHitAngle\" on ChooChoo", true);   }
		
		try {
			this.CHOOCHOO_SETPOINT_LOAD__ANGLE = parser.parseSetpoint("ChooChoo/setpoints", "loadAngle");
			Robot.print("Loaded setpoint \"loadAngle\" on ChooChoo", false);
			}
		catch (XPathExpressionException e) {   Robot.print("Failed to load setpoint \"loadAngle\" on ChooChoo", true);   }
		
		try {
			this.CHOOCHOO_SETPOINT_UNLOAD__ANGLE = parser.parseSetpoint("ChooChoo/setpoints", "unloadAngle");
			Robot.print("Loaded setpoint \"unloadAngle\" on ChooChoo", false);
			}
		catch (XPathExpressionException e) {   Robot.print("Failed to load setpoint \"unloadAngle\" on ChooChoo", true);   }

		
		return true;
	}
	
	
	
	
	
	
	/**
	 * Allocates all of the physical attributes of the robot (motors, PID loops, etc.). this
	 * automatically loads everything from the data elements loaded from {@link loadData(...)}
	 */
	public void allocateRobot()
	{
		
	}
}

