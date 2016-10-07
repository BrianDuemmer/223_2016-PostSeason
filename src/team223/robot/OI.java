package team223.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import team223.AdvancedX.SmartControlStick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	// initialize the controllers
	private Joystick driverController = new Joystick(0);
	private Joystick operatorController = new Joystick(1);
	
	public static SmartControlStick stick_dL;
	public static SmartControlStick stick_dR;
	
	public static SmartControlStick stick_oL;
	public static SmartControlStick stick_oR;
	
	public OI() {
		
		// bind the buttons for the driver controller
		JoystickButton button_dA = new JoystickButton(driverController, 0);
		JoystickButton button_dB = new JoystickButton(driverController, 1);
		JoystickButton button_dX = new JoystickButton(driverController, 2);
		JoystickButton button_dY = new JoystickButton(driverController, 3);
		JoystickButton button_dL = new JoystickButton(driverController, 4);
		JoystickButton button_dR = new JoystickButton(driverController, 5);
		JoystickButton button_dStart = new JoystickButton(driverController, 8);
		JoystickButton button_dBack = new JoystickButton(driverController, 9);
		
		// bind the analog sticks for the driver controller
		stick_dL = new SmartControlStick(driverController, 0, 1, 6);
		stick_dL.setParams(false, true, 0.1, 1);
		stick_dR = new SmartControlStick(driverController, 4, 5, 7);
		stick_dR.setParams(false, true, 0.1, 1);
		
		
		// bind the buttons for the operator controller
		JoystickButton button_oA = new JoystickButton(operatorController, 0);
		JoystickButton button_oB = new JoystickButton(operatorController, 1);
		JoystickButton button_oX = new JoystickButton(operatorController, 2);
		JoystickButton button_oY = new JoystickButton(operatorController, 3);
		JoystickButton button_oL = new JoystickButton(operatorController, 4);
		JoystickButton button_oR = new JoystickButton(operatorController, 5);
		JoystickButton button_oStart = new JoystickButton(operatorController, 8);
		JoystickButton button_oBack = new JoystickButton(operatorController, 9);
		
		// bind the analog sticks to the operator controller
		stick_oL = new SmartControlStick(operatorController, 0, 1, 6);
		stick_oL.setParams(false, true, 0.1, 1);
		stick_oR = new SmartControlStick(operatorController, 4, 5, 7);
		stick_oR.setParams(false, true, 0.1, 1);
		
		
		
		
	}
}

