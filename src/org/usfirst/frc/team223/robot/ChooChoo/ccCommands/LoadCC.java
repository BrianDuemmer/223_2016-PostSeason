package org.usfirst.frc.team223.robot.ChooChoo.ccCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/** Moves the Choo Choo into the loaded position, and optionally fires
 * it.
 */
public class LoadCC extends CommandGroup {

    public LoadCC(boolean canFire) {
    	// See if the position of the ChooChoo is down and in the right position
    	boolean loaded = Robot.chooChooSubsys.onAbsoluteTarget(OI.CHOOCHOO_SETPOINT_LOAD__ANGLE);
    	loaded &= Robot.chooChooSubsys.chooChooBeam.get();
    	
    	// If we aren't loaded, or we are allowed to fire, run the choo choo
    	if(!loaded || (loaded && canFire))
    		addSequential(new ChooChooGotoSetpoint(OI.CHOOCHOO_SETPOINT_LOAD__ANGLE, true, true));
    }
}
