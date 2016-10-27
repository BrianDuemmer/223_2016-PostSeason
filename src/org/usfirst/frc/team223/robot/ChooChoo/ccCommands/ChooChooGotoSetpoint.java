package org.usfirst.frc.team223.robot.ChooChoo.ccCommands;

import org.usfirst.frc.team223.AdvancedX.AngleUtil;
import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/** Moves the choo choo to a specified position. The direction must be
 *  specified, to avoid the Choo Choo firing or not firing when it shouldn't
 *
 */
public class ChooChooGotoSetpoint extends Command {

	double newSet;
	
	/** Moves the choo choo to a specified position. The direction must be
	 *  specified, to avoid the Choo Choo firing or not firing when it shouldn't
	 */
    public ChooChooGotoSetpoint(double setpoint, boolean goForward, boolean forceRotation) {
    	double currPos = Robot.chooChooSubsys.getPosition();
    	
    	requires(Robot.chooChooSubsys);
    	
    	//normalize the setpoint angle
    	setpoint = AngleUtil.norm360(setpoint);
    	
    	// Get the minimum amount of complete rotations, and add the setpoint to that
    	if(currPos >= 0)
        	newSet = 360 * Math.floor(currPos / 360) + setpoint;
    	else
        	newSet = 360 * Math.ceil(currPos / 360) + setpoint;
    	
    	//make sure it moves the right way
    	if(!goForward)
    		newSet -= 360;
    	
    	// If we are already on target and we want a full rotation,
    	// Add or subtract 360 from the setpoint, depending on direction
    	if(forceRotation && Robot.chooChooSubsys.onAbsoluteTarget(newSet))
    		newSet += goForward ? 360 : -360;
    }


    protected void initialize() {
    	// Turn on the PID and move towards newSet
    	Robot.chooChooSubsys.setSetpoint(newSet);
    	Robot.chooChooSubsys.enable();
    }


    protected void execute() {
    }


    protected boolean isFinished() {
    	// Turn off the PID
    	Robot.chooChooSubsys.disable();
    	
    	// Stop if on target
    	boolean stop = Robot.chooChooSubsys.onTarget();
    	
    	// or the back button is pressed
    	stop |= OI.button_oBack.get();
    	
    	// or we are not zeroed
    	stop |= !Robot.chooChooSubsys.hasBeenZeroed;
    	
        return stop;
    }


    protected void end() {
    }


    protected void interrupted() {}
}
