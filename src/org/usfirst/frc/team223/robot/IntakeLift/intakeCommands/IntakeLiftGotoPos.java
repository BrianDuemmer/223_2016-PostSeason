package org.usfirst.frc.team223.robot.IntakeLift.intakeCommands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

/**
 * Moves the intakeLift to a specified position via PID
 */
public class IntakeLiftGotoPos extends Command {

	// setpoint to go to
	double _setpoint;
	
	/**
	 * 
	 * @param setpoint Setpoint to go to
	 */
    public IntakeLiftGotoPos(double setpoint) {
    	requires(Robot.intakeLiftSubsys);
    	_setpoint = setpoint;
    }

    protected void initialize() {
    	// Enable the PID controller
    	Robot.intakeLiftSubsys.enable();
    	
    	// Set the setpoint
    	Robot.intakeLiftSubsys.setSetpoint(_setpoint);
    }

    
    protected void execute() {
    }

    // Stop when either on target or when the back button is pressed or if
    // the intake lift hasn't been zeroed
    protected boolean isFinished() {
        return 	Robot.intakeLiftSubsys.onTarget() 		|| 
        		OI.button_oBack.get() 					/*|| 
        		!Robot.intakeLiftSubsys.hasBeenZeroed*/;
    }

    // Stop the motor, and disable the PID
    protected void end() {
    	Robot.intakeLiftSubsys.setOutput(0);
    	Robot.intakeLiftSubsys.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
