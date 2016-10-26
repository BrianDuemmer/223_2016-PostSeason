package org.usfirst.frc.team223.robot.ChooChoo.ccCommands;

import org.usfirst.frc.team223.AdvancedX.AngleUtil;
import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/** Moves the choo choo to a specified position. This command accomplishes this
 * by running the motor full forward or reverse, depending on the direction
 * given, until it is a certain angle away from the setpoint and moving towards
 * it. It then kicks on the PID to bring it in close.
 *
 */
public class ChooChooGotoSetpoint extends Command {

	double newSet;
	
    public ChooChooGotoSetpoint(double setpoint, boolean goForward) {
    	double currPos = Robot.chooChooSubsys.getPosition();
    	
    	requires(Robot.chooChooSubsys);
    	
    	//normalize the setpoint angle
    	setpoint = AngleUtil.norm360(setpoint);
    	
    	// Wrap it the proper amount of times
    	newSet = 360 * Math.floor(currPos / 360) + setpoint;
    	
    	// Subtract 360 if currPos >= 0 and < 360
    	if(currPos >= 0 && currPos < 360)
    		newSet -= 360;
    	
    	//make sure it moves the right way
    	if(goForward)
    		newSet += 360;
    }


    protected void initialize() {
    	// Turn on the PID and move towards newSet
    	Robot.chooChooSubsys.setSetpoint(newSet);
    	Robot.chooChooSubsys.enable();
    }


    protected void execute() {
    }


    protected boolean isFinished() {
    	boolean stop = Robot.chooChooSubsys.onTarget();
    	stop |= OI.button_oBack.get();
        return stop;
    }


    protected void end() {
    }


    protected void interrupted() {}
}
