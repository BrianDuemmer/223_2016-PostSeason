package org.usfirst.frc.team223.robot.IntakeLift.intakeCommands;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IntakeLimitISR extends Command {

    public IntakeLimitISR() {
    	// there is no requires() here because in this case, this ISR is purely
    	// passive (will not modify any outputs), and Ideally will not interrupt
    	// other commands.
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
