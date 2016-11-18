package org.usfirst.frc.team223.robot.generalCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetFlashlight extends Command {
	
	private static double lastFallTime;

	// No requires statement - this is not dependent on a subsystem
    public SetFlashlight() {}


    protected void initialize() 
    {
    	// figure out whether the relay is on or off
    	Value out = OI.button_dR.get()  ?  Value.kForward : Value.kOff;
    	
    	
    	// See if enough time has elapsed to turn on the relay, and turn it on if it has
    	if(out == Value.kForward && lastFallTime + OI.FLASHLIGHT_HOLD__TIME < Timer.getFPGATimestamp())
    	{
    		// Set the output
    		Robot.driveSubsys.getLight().set(out);
    	}
    	
    	
    	
    	// If we want to turn it off, just turn it off, and update the lastFallTime
    	if(out == Value.kOff && lastFallTime + OI.FLASHLIGHT_HOLD__TIME > Timer.getFPGATimestamp())
    	{
    		// Set the output
    		Robot.driveSubsys.getLight().set(out);
    	}
    	
    	
    	// If we want to turn it off, just turn it off, and update the lastFallTime
    	if(out == Value.kOff && lastFallTime + OI.FLASHLIGHT_HOLD__TIME < Timer.getFPGATimestamp())
    	{
    		// update the lastFallTime
    		lastFallTime = Timer.getFPGATimestamp();
    	
    		// Set the output
    		Robot.driveSubsys.getLight().set(out);
    	}
    		
    }

    
    protected void execute() {}
    protected boolean isFinished() {   return true;   }
    protected void end() {}
    protected void interrupted() {}
}
