package org.usfirst.frc.team223.robot.drive.driveCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/** Drives at a certain velocity for a certain time, and at a certain angle
 *
 *@author Brian Duemmer
 */
public class DriveVelForTime extends Command {
	
	// setpoint velocities
	double leftVel;
	double rightVel;
	
	// timestamps
	double runTime;
	double stopTime;
	
	double radius;
	
    public DriveVelForTime(double vel, double time, double degPerFoot) {
    	requires(Robot.driveSubsys);
    	
    	// Wheelbase compensation factor
    	double wbComp = OI.DRIVE_WHEELBASE_WIDTH * 0.5;
    	
    	// If degrees per foot is zero, just drive straight
    	if(degPerFoot == 0)
    	{
    		// set radius to NaN
    		radius = Double.NaN;
    		
    		// set each side velocity to be the same
    		leftVel = vel;
    		rightVel = vel;
    	}
    	
    	//If degrees per foot is nonzero, calculate a turn
    	else
    	{
	    	// Calculate radius. we want it to only be positive
	    	radius = Math.abs(180 / (Math.PI * degPerFoot));
	    	
	    	// Invert the wheelbase width compensation if necessary
	    	wbComp *= Math.signum(degPerFoot);
	    	
	    	// Calculate the adjusted velocities for each side
	    	leftVel =  	(vel * (radius + wbComp) / radius);
	    	rightVel = 	(vel * (radius - wbComp) / radius);
    	}
    	
    	// Save the running time
    	runTime = time;
    }

 
    // Record the stopping time
    protected void initialize() {   stopTime = Timer.getFPGATimestamp() + runTime;   }


    protected void execute() {
    	// Set the setpoints for each side. Eventually a smarter correction
    	// algorithm will be used here
    	Robot.driveSubsys.getController().setRawVel(leftVel, rightVel);
    }


    protected boolean isFinished() 
    {
    	//Stop when the the time runs out
    	boolean stop = Timer.getFPGATimestamp() > stopTime;
    	
    	// Or the back button is pressed
    	stop |= OI.button_dBack.get();
    	
        return stop;
    }


    protected void end() 
    {
    	// Turn off the PIDs and stop the motors
    	Robot.driveSubsys.getController().setRawOut(0, 0);
    }


    protected void interrupted() {
    }
}
