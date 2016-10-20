package org.usfirst.frc.team223.robot;

import org.usfirst.frc.team223.AdvancedX.SmartControlStick;
import org.usfirst.frc.team223.robot.IntakeLift.intakeCommands.*;
import org.usfirst.frc.team223.robot.generalCommands.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
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
	
	
	
	
	//////////////// Drive Subsystem /////////////////
	
	// motor configuration
	public static int 			DRIVE_MOTOR_L1_ID = 1;
	public static boolean	 	DRIVE_MOTOR_L1_INVERT = false;
	
	public static int 			DRIVE_MOTOR_L2_ID = 4;
	public static boolean 		DRIVE_MOTOR_L2_INVERT = false;
	
	public static int 			DRIVE_MOTOR_R1_ID = 5;
	public static boolean 		DRIVE_MOTOR_R1_INVERT = true;
	
	public static int 			DRIVE_MOTOR_R2_ID = 6;
	public static boolean 		DRIVE_MOTOR_R2_INVERT = true;
	
	/* braking configuration. if DRIVE_BRAKE_HALF is true, then only one motor
	 * on each side will be in brake mode. If DRIVE_BRAKE_FULL is true, then
	 * both motors on each side will brake.
	 */
	public static boolean		DRIVE_BRAKE_HALF = true;
	public static boolean		DRIVE_BRAKE_FULL = true;
	
	
	
	//////////// IntakeLift Subsystem /////////////
	
	// motor configuration
	public static int 			INTAKELIFT_MOTOR_ID = 2;
	public static boolean 		INTAKELIFT_MOTOR_INVERT = true;
	public static boolean 		INTAKELIFT_MOTOR_BRAKE = true;
	
	// PID values
	public static double		INTAKELIFT_PID_KP = 0.3;
	public static double		INTAKELIFT_PID_KI = 0.01;
	public static double		INTAKELIFT_PID_KD = 0.0001;
	public static double		INTAKELIFT_PID_TOLERANCE = 2.0;
	
	// Setpoints
	public static double		INTAKELIFT_SETPOINT_BALL__GRAB__ANGLE = 15;
	public static double		INTAKELIFT_SETPOINT_LIMIT__POS = 15;
	public static double		INTAKELIFT_SETPOINT_MAXDOWN = 8;
	public static double		INTAKELIFT_SETPOINT_MAXUP = 95;
	
	// Encoder
	public static int 			INTAKELIFT_ENCODER_ID_A = 0;
	public static int 			INTAKELIFT_ENCODER_ID_B = 1;
	public static double 		INTAKELIFT_ENCODER_DEGREES__PER__COUNT = 0.0640113798;
	public static boolean		INTAKELIFT_ENCODER_INVERT = true;
	public static double		INTAKELIFT_ENCODER_MAX__PERIOD = 0.5;
	public static double		INTAKELIFT_ENCODER_MIN__RATE__SEC = 0.5;
	
	// Limit Switch
	public static int			INTAKELIFT_LIMIT_ID = 3;
	public static boolean		INTAKELIFT_LIMIT_NORMALLY__OPEN = false;
	
	
	
	////////////// IntakeWheels Subsystem /////////////
	
	// motor configuration
	public static int 			INTAKEWHEELS_MOTOR_ID = 7;
	public static boolean 		INTAKEWHEELS_MOTOR_INVERT = false;
	public static boolean 		INTAKEWHEELS_MOTOR_BRAKE = false;
	
	
	
	/////////// General Configutation Values //////////
	
	public static double 			ZEROLIFTANDCC_CC_START_DELAY= 0.75;
	
	
	
	public OI() {
		
		driverController = new Joystick(0);
		operatorController = new Joystick(1);
		
		// bind the buttons for the driver controller
		button_dA = new JoystickButton(driverController, 0);
		button_dB = new JoystickButton(driverController, 1);
		button_dX = new JoystickButton(driverController, 2);
		button_dY = new JoystickButton(driverController, 3);
		button_dL = new JoystickButton(driverController, 4);
		button_dR = new JoystickButton(driverController, 5);
		button_dStart = new JoystickButton(driverController, 8);
		button_dBack = new JoystickButton(driverController, 9);
		
		// bind the analog sticks for the driver controller
		stick_dL = new SmartControlStick(driverController, 0, 1, 6);
		stick_dL.setParams(false, true, 0.1, 1);
		stick_dR = new SmartControlStick(driverController, 4, 5, 7);
		stick_dR.setParams(false, true, 0.1, 1);
		
		
		// bind the buttons for the operator controller
		button_oA = new JoystickButton(operatorController, 0);
		button_oB = new JoystickButton(operatorController, 1);
		button_oX = new JoystickButton(operatorController, 2);
		button_oY = new JoystickButton(operatorController, 3);
		button_oL = new JoystickButton(operatorController, 4);
		button_oR = new JoystickButton(operatorController, 5);
		button_oStart = new JoystickButton(operatorController, 8);
		button_oBack = new JoystickButton(operatorController, 9);
		
		
		// bind the commands for the operator buttons
		
		// when B is pressed, bring the IntakeLift to the ball grab angle
		button_oB.whileHeld(new IntakeLiftGotoPos(INTAKELIFT_SETPOINT_BALL__GRAB__ANGLE));
		
		// When start is pressed, zero the intakeLift and ChooChoo
		button_oStart.whenPressed(new ZeroLiftAndCC());
		
		// bind the analog sticks to the operator controller
		stick_oL = new SmartControlStick(operatorController, 0, 1, 6);
		stick_oL.setParams(false, true, 0.1, 1);
		stick_oR = new SmartControlStick(operatorController, 4, 5, 7);
		stick_oR.setParams(false, true, 0.1, 1);
		
		
		
		
	}
}

