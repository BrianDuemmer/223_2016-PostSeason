package org.usfirst.frc.team223.robot.ChooChoo.ccCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Unloads the Choo Choo mechanism
 */
public class UnloadCC extends CommandGroup {
    
    public  UnloadCC() {
    	// Log us entering the command
    	Robot.printToDS("Entering UnloadCC ", "ChooChoo");
    	
    	// goto the unload position, go in reverse, do not force move
    	addSequential(new ChooChooGotoSetpoint(OI.CHOOCHOO_SETPOINT_UNLOAD__ANGLE, false, false));
    	
    	// Log us leaving the command
    	Robot.printToDS("Leaving UnloadCC ", "ChooChoo");
    }
}
