package org.usfirst.frc.team223.robot.drive.driveCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/** Sets the output of the drivetrain from the driver joystick
 *
 */
public class setDriveFromJoy extends Command {

    public setDriveFromJoy() {
    	requires(Robot.driveSubsys);
    	this.setInterruptible(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// Calculate the base outputs
    	double yOut = OI.stick_dL.getY();
    	double xOut = OI.stick_dR.getX();
    	
    	// Scale the output for fine adjust if necessary
    	if(OI.button_dL.get())
    	{
    		yOut *= OI.DRIVE_FINE__ADJ__OUT;
    		xOut *= OI.DRIVE_FINE__ADJ__OUT;
    	}
    	
    	// set the output
    	Robot.driveSubsys.driveArcade(yOut, xOut);
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
