package org.usfirst.frc.team223.robot.drive.driveCommands;

import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/** Drives at a certain velocity for a certain time, and at a certain angle
 *
 */
public class DriveVelForTime extends Command {

	double vel;
	double time;
	double degPerFoot;
	
	// setpoint velocities
	double leftVel;
	double rightVel;
	
	double radius;
	
    public DriveVelForTime(double vel, double time, double degPerFoot) {
    	requires(Robot.driveSubsys);
    	
    	// Update instance variables
    	this.vel = vel;
    	this.time = time;
    	this.degPerFoot = degPerFoot;
    	
    	// Calculate radius. we want it to only be positive
    	radius = Math.abs(180 / (Math.PI * degPerFoot));
    
    	if()
    	
    }

 
    protected void initialize() {
    }


    protected void execute() {
    }


    protected boolean isFinished() {
        return false;
    }


    protected void end() {
    }


    protected void interrupted() {
    }
}
