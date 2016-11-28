package org.usfirst.frc.team223.robot.ChooChoo.ccCommands;

import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Interrupt service routine for the ChooChoo beam sensor. Gets run whenever 
 * the beam sensor is hit.
 * @author Brian Duemmer
 */
public class ChooChooISR extends Command {

    public ChooChooISR() {
    	// there is no requires() here because in this case, this ISR is purely
    	// passive (will not modify any outputs), and we don't want it to interrupt
    	// other commands.
    }

    
    protected void initialize() 
    {
    	double newChooChooOffset = Robot.chooChooSubsys.getRawEncPos() - Robot.chooChooSubsys.SETPOINT_BEAM__HIT__ANGLE;
    	
    	// Account for angle wrapping. If it is negative, add 360
    	newChooChooOffset %= 360;
    	newChooChooOffset  +=  newChooChooOffset > 0  ?  0 : 360;
    	
    	Robot.chooChooSubsys.setEncOffset(newChooChooOffset);
    	
    	//make sure the rest of code knows that we have been zeroed
    	Robot.chooChooSubsys.hasBeenZeroed = true;
    	
    }
    
   
    // all this has to do is update a variable, so it finishes essentially instantly
    protected boolean isFinished() {   return Robot.chooChooSubsys.hasBeenZeroed;   }
    protected void end() {}
    protected void interrupted() {}
    protected void execute() {}
}
