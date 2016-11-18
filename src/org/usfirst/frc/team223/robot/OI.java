package org.usfirst.frc.team223.robot;

import org.usfirst.frc.team223.AdvancedX.SmartControlStick;
import org.usfirst.frc.team223.robot.Auto.CrossDefenseBasic;
import org.usfirst.frc.team223.robot.ChooChoo.ccCommands.*;
import org.usfirst.frc.team223.robot.IntakeLift.intakeCommands.*;
import org.usfirst.frc.team223.robot.drive.driveCommands.DriveVelForTime;
import org.usfirst.frc.team223.robot.drive.driveCommands.SetYawAngle;
import org.usfirst.frc.team223.robot.generalCommands.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator a
 * interface to the commands and command groups that allow control of the robot.
 * 
 * @author Brian Duemmer
 */
public class OI {
	
	// if tru, the robot is in debug mode. Set this to false for competition.
	public static boolean	ROBOT_ISDEBUG = true;
	
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
	
	public static double		DRIVE_ENCODER_L_CP__FOOT = 0.01291423;
	public static double		DRIVE_ENCODER_R_CP__FOOT = 0.01291423;
	
	public static boolean		DRIVE_ENCODER_L_INVERT = true;
	public static boolean		DRIVE_ENCODER_R_INVERT = false;
	
	public static double		DRIVE_FINE__ADJ__OUT = 0.375;
	public static double		DRIVE_MAX__OUTPUT = 0.999;
	
	public static double		DRIVE_WHEELBASE_WIDTH = 1.708;
	
	
	public static double		DRIVE_L_PID_KP = 0.275;
	public static double		DRIVE_L_PID_KI = 0.075;
	public static double		DRIVE_L_PID_KD = 0.0;
	public static double		DRIVE_L_PID_KF = 0.2;
	
	public static double		DRIVE_R_PID_KP = 0.275;
	public static double		DRIVE_R_PID_KI = 0.075;
	public static double		DRIVE_R_PID_KD = 0.0;
	public static double		DRIVE_R_PID_KF = 0.2;
	
	public static double		DRIVE_SLAVE__PID_PERIOD = 0.05;
	
	//TODO Tune the position and turning PIDs
	public static double		DRIVE_POS_PID_KP = 0.0;
	public static double		DRIVE_POS_PID_KI = 0.0;
	public static double		DRIVE_POS_PID_KD = 0.0;
	public static double		DRIVE_POS_PID_KF = 0.0;
	
	public static double		DRIVE_TURN_PID_KP = 0.0;
	public static double		DRIVE_TURN_PID_KI = 0.0;
	public static double		DRIVE_TURN_PID_KD = 0.0;
	public static double		DRIVE_TURN_PID_KF = 0.0;
	
	public static double		DRIVE_MASTER__PID_PERIOD = 0.2;
	
	public static double		DRIVE_VEL__FOR__TIME_BRAKE__TIME = 0.75;
	public static int			DRIVE_DEFAULT__BRAKE__COUNT = 1;
	
	public static int			FLASHLIGHT_RELAY_PORT = 0;
	public static double		FLASHLIGHT_HOLD__TIME = 2;
	
	
	
	//////////// IntakeLift Subsystem /////////////
	
	// motor configuration
	public static int 			INTAKELIFT_MOTOR_ID = 2;
	public static boolean 		INTAKELIFT_MOTOR_INVERT = true;
	public static boolean 		INTAKELIFT_MOTOR_BRAKE = true;
	
	// PID values
	public static double		INTAKELIFT_PID_KP = 0.3;
	public static double		INTAKELIFT_PID_KI = 0.01;
	public static double		INTAKELIFT_PID_KD = 0.0001;
	public static double		INTAKELIFT_PID_TOLERANCE = 0.5;
	
	// Setpoints
	public static double		INTAKELIFT_SETPOINT_BALL__GRAB__ANGLE = 0;
	public static double		INTAKELIFT_SETPOINT_LIMIT__POS = 8;
	public static double		INTAKELIFT_SETPOINT_MAXDOWN = -4;
	public static double		INTAKELIFT_SETPOINT_MAXUP = 118;
	
	// Encoder
	public static int 			INTAKELIFT_ENCODER_ID_A = 0;
	public static int 			INTAKELIFT_ENCODER_ID_B = 1;
	public static double 		INTAKELIFT_ENCODER_DEGREES__PER__COUNT = 0.08284475384;
	public static boolean		INTAKELIFT_ENCODER_INVERT = true;
	public static double		INTAKELIFT_ENCODER_MAX__PERIOD = 0.5;
	public static double		INTAKELIFT_ENCODER_MIN__RATE__SEC = 0.5;
	
	// Limit Switch
	public static int			INTAKELIFT_LIMIT_ID = 3;
	public static boolean		INTAKELIFT_LIMIT_NORMALLY__OPEN = false;
	public static double		INTAKELIFT_LIMIT_DEBOUNCE__TIME = 0.1;
	
	
	
	////////////// IntakeWheels Subsystem /////////////
	
	// motor configuration
	public static int 			INTAKEWHEELS_MOTOR_ID = 7;
	public static boolean 		INTAKEWHEELS_MOTOR_INVERT = false;
	public static boolean 		INTAKEWHEELS_MOTOR_BRAKE = false;
	public static double		INTAKEWHEELS_MOTOR_SCALAR = 0.7;
	
	
	
	//////////////// ChooChoo Subsystem ///////////////
	
	// motor configuration
	public static int 			CHOOCHOO_MOTOR_ID = 3;
	public static boolean 		CHOOCHOO_MOTOR_INVERT = false;
	public static boolean 		CHOOCHOO_MOTOR_BRAKE = true;
	
	// PID values
	public static double		CHOOCHOO_PID_KP = 0.275;
	public static double		CHOOCHOO_PID_KI = 0.0001;
	public static double		CHOOCHOO_PID_KD = 0.0;
	public static double		CHOOCHOO_PID_KF = 0.25;
	public static double		CHOOCHOO_PID_TOLERANCE = 5.0;
	
	// Setpoints
	public static double		CHOOCHOO_SETPOINT_BEAM__HIT__ANGLE = 295.0;
	public static double		CHOOCHOO_SETPOINT_LOAD__ANGLE = 310.0;
	public static double		CHOOCHOO_SETPOINT_UNLOAD__ANGLE = 0;

	// Encoder
	public static int 			CHOOCHOO_ENCODER_CAN_ID = 2;
	public static double 		CHOOCHOO_ENCODER_DEGREES__PER__COUNT = 0.00293005598116326649909393815397;
	public static boolean		CHOOCHOO_ENCODER_INVERT = false;
	public static double		CHOOCHOO_ENCODER_WRAP__THRESHOLD = 20;
	
	// Beam Sensor
	public static int			CHOOCHOO_BEAM_ID = 2;
	public static boolean		CHOOCHOO_BEAM_NORMALLY__OPEN = false;
	public static double		CHOOCHOO_BEAM_DEBOUNCE__TIME = 0.01;
	
	
	//////////// Auto Configutation Values ////////////
	public static double		AUTO_LOWBAR_INTAKE__ANGLE = 0;
	public static double		AUTO_STD__DEF_INTAKE__ANGLE = 70;
	
	
	
	/////////// General Configutation Values //////////
	
	public static double 		ZEROLIFTANDCC_CC_START_DELAY= 1.5;
	
	
	
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
}

