package org.usfirst.frc.team223.robot.generalCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.ChooChoo.ccCommands.ZeroCC;
import org.usfirst.frc.team223.robot.IntakeLift.intakeCommands.ZeroIntakeLift;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;

/** Zeroes both the intakeLift and the choo choo
 *
 */
public class ZeroLiftAndCC extends CommandGroup {
    
    public  ZeroLiftAndCC() {
    	// Start to zero the intake lift
    	addParallel(new ZeroIntakeLift());
    	
    	// Sleep for a bit
		Timer.delay(OI.ZEROLIFTANDCC_CC_START_DELAY);
		
		// Zero the chooChoo after some time has elapsed
		addSequential(new ZeroCC());
    }
}
