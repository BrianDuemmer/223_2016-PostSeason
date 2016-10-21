package org.usfirst.frc.team223.robot.IntakeLift.intakeCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Sets the output of the intakeLift from a controller. Also runs the state
 * Maintenance PID, which deals with the fact that the lift is so heavy, that it
 * falls under its own weight, even with brake mode enabled.
 */
public class SetIntakeLiftFromJoy extends Command {

	// Setpoint for the position-maintainer PID
	static double posHoldSetpoint;
	
	
	/**
	 * Sets the output of the intakeLift from a controller. Also runs the state
	 * Maintenance PID, which deals with the fact that the lift is so heavy, that it
	 * falls under its own weight, even with brake mode enabled.
	 */
    public SetIntakeLiftFromJoy() {   requires(Robot.intakeLiftSubsys);  }
    protected void initialize() {}
    protected boolean isFinished() {   return false;   }
    protected void end() {}
    protected void interrupted() {}
    
  
    
    protected void execute() {
    	double manualOutput = OI.operatorController.getRawAxis(3) - OI.operatorController.getRawAxis(2);
 
    	
    	// if the manual output setting is nonzero, disable the PID and send it to the motor
    	if(manualOutput != 0)
    	{
    		// Disable the PID
        	Robot.intakeLiftSubsys.disable();
        	
        	// Set the output to the motor
    		Robot.intakeLiftSubsys.setOutput(manualOutput);
    		
    		// Update the position-maintainer setpoint
    		posHoldSetpoint = Robot.intakeLiftSubsys.getPosition();
    	}
    	
    	
    	// Only run the position-maintainer loop if we have been zeroed
    	else if(Robot.intakeLiftSubsys.hasBeenZeroed)
    	{
    		// Enable the PID
    		Robot.intakeLiftSubsys.enable();
    		
    		// Tell it to try to go to the position - hold setpoint
    		Robot.intakeLiftSubsys.setSetpoint(posHoldSetpoint);
    	}
    	
    	
    	// If there is no manual output, and we havn't been zeroed, just turn off
    	// the motor and PID
    	else
    	{
    		// Disable the PID
        	Robot.intakeLiftSubsys.disable();
        	
        	// Turn the motor off
    		Robot.intakeLiftSubsys.setOutput(0);
    	}
    }
}
