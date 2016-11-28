package org.usfirst.frc.team223.robot.intakeWheels.iWheelsCommands;

import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Sets the output of the intake wheels from a controller
 * 
 * @author Brian Duemmer
 */
public class SetIntakeWheelsFromController extends Command {
	
    public SetIntakeWheelsFromController() {
    	requires(Robot.intakeWheelsSubsys);
    }

    protected void initialize() {
    	
    	// If left bumper pressed, output ball. If Right pressed, intake ball.
    	// if neither or both are pressed, output zero
    	double outA = Robot.oi.button_oL.get() ?   1 : 0;
    	double outB = Robot.oi.button_oR.get() ?  -1 : 0;
    	
    	Robot.intakeWheelsSubsys.setOutput((outA + outB) * Robot.intakeWheelsSubsys.MOTOR_DATA.maxOut);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
