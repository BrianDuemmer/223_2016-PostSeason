package org.usfirst.frc.team223.robot.generalCommands;

import org.usfirst.frc.team223.robot.ChooChoo.ccCommands.ZeroCC;
import org.usfirst.frc.team223.robot.IntakeLift.intakeCommands.ZeroIntakeLift;

import edu.wpi.first.wpilibj.command.CommandGroup;

/** Zeroes both the intakeLift and the choo choo
 *@author Brian Duemmer
 */
public class ZeroLiftAndCC extends CommandGroup {
	
    
    public  ZeroLiftAndCC() {
    	// Start to zero the intake lift
    	addParallel(new ZeroIntakeLift());
		
		// Zero the chooChoo after some time has elapsed
		addSequential(new ZeroCC());
    }
}
