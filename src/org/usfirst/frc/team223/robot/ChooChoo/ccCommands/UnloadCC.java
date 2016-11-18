package org.usfirst.frc.team223.robot.ChooChoo.ccCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Unloads the Choo Choo mechanism
 * 
 * @author Brian Duemmer
 */
public class UnloadCC extends CommandGroup {
    
    public  UnloadCC() {
    	
    	// goto the unload position, go in reverse, do not force move
    	addSequential(new ChooChooGotoSetpoint(OI.CHOOCHOO_SETPOINT_UNLOAD__ANGLE, false, false));
    	
    }
}
