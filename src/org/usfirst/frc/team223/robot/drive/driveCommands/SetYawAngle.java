package org.usfirst.frc.team223.robot.drive.driveCommands;

import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Sets the robot's current yaw angle. Also optionally disables the CascadeController, 
 * so be sure to re-enable it when necessary
 * 
 * @author Brian Duemmer
 */
public class SetYawAngle extends Command {

	// Current yaw
	double currYaw;
	
	// if true, disables the cascade controller
	boolean disable;
	
	
	
	
	/**
	 * Sets the robot's current yaw angle, and optionally disables the cascade controller
	 * @param currYaw
	 */
    public SetYawAngle(double currYaw, boolean disable) {
    	requires(Robot.driveSubsys);
    	
    	// update instance variables
    	this.currYaw = currYaw;
    	this.disable = disable;
    }


    protected void initialize() { Robot.driveSubsys.getController().setYaw(currYaw, disable);   }
    protected void execute() {}
    protected boolean isFinished() {   return true;   }
    protected void end() {}
    protected void interrupted() {}
}
