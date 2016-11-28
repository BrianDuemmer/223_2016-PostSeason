package org.usfirst.frc.team223.robot.ChooChoo.ccCommands;

import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/** Moves the Choo Choo into the loaded position, and optionally fires
 * it.
 * 
 * @author Brian Duemmer
 */
public class LoadCC extends CommandGroup {

    public LoadCC(boolean canFire) {
    	
    	// See if the position of the ChooChoo is down and in the right position
    	boolean loaded = Robot.chooChooSubsys.onAbsoluteTarget(Robot.chooChooSubsys.SETPOINT_BEAM__HIT__ANGLE);
    	loaded &= Robot.chooChooSubsys.getLIMIT_HDL().get();
    	
    	// If we aren't loaded, or we are allowed to fire, run the choo choo
    	if(!loaded || (loaded && canFire))
    		addSequential(new ChooChooGotoSetpoint(Robot.chooChooSubsys.SETPOINT_LOAD__ANGLE, true, true));
    }
}
