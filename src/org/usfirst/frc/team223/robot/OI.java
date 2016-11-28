package org.usfirst.frc.team223.robot;

import org.apache.log4j.Logger;
import org.usfirst.frc.team223.AdvancedX.*;
import org.usfirst.frc.team223.AdvancedX.robotParser.*;
import org.usfirst.frc.team223.robot.ChooChoo.ccCommands.*;
import org.usfirst.frc.team223.robot.IntakeLift.intakeCommands.*;
import org.usfirst.frc.team223.robot.generalCommands.*;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.interfaces.Gyro;

/**
 * This class is the glue that binds the controls on the physical operator a
 * interface to the commands and command groups that allow control of the robot.
 * 
 * @author Brian Duemmer
 */
public class OI {
	
	
	////////////////// Human Input ///////////////////
	public Joystick driverController;
	public Joystick operatorController;
	
	public SmartControlStick stick_dL;
	public SmartControlStick stick_dR;
	
	public SmartControlStick stick_oL;
	public SmartControlStick stick_oR;
	
	// Driver controller buttons
	public JoystickButton button_dA;
	public JoystickButton button_dB;
	public JoystickButton button_dX;
	public JoystickButton button_dY;
	public JoystickButton button_dL;
	public JoystickButton button_dR;
	public JoystickButton button_dStart;
	public JoystickButton button_dBack;
	
	
	// Operator controller buttons
	public JoystickButton button_oA;
	public JoystickButton button_oB;
	public JoystickButton button_oX;
	public JoystickButton button_oY;
	public JoystickButton button_oL;
	public JoystickButton button_oR;
	public JoystickButton button_oStart;
	public JoystickButton button_oBack;
	
	//////////////////// Parser Data ////////////////////
	public String CONFIG_FILE_PATH = "media/sda1/MainConfig.xml";
	
	//////////////////// Logger Data ////////////////////
	public Logger logger;
		
	
	
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

}

