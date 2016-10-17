package org.usfirst.frc.team223.robot.drive.driveCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/** Sets the output of the drivetrain from the driver joystick
 *
 */
public class setFromJoy extends Command {

    public setFromJoy() {
    	requires(Robot.driveSubsys);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// Cheesy drive the robot
    	Robot.driveSubsys.driveArcade(OI.stick_dL.getY(), OI.stick_dR.getX());
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
