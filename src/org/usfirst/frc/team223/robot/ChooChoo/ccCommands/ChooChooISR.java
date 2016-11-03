package org.usfirst.frc.team223.robot.ChooChoo.ccCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ChooChooISR extends Command {

    public ChooChooISR() {
    	// there is no requires() here because in this case, this ISR is purely
    	// passive (will not modify any outputs), and we don't want it to interrupt
    	// other commands.
    }

    
    protected void initialize() 
    {
    	double newChooChooOffset = Robot.chooChooSubsys.getRawEncPos() - OI.CHOOCHOO_SETPOINT_BEAM__HIT__ANGLE;
    	Robot.chooChooSubsys.setEncOffset(newChooChooOffset);
    	
    	//make sure the rest of code knows that we have been zeroed
    	Robot.chooChooSubsys.hasBeenZeroed = true;
    	
    	// Log us entering the ISR
    	Robot.printToDS("Entering BeamISR ", "ChooChoo");
    }
    
   
    // all this has to do is update a variable, so it finishes essentially instantly
    protected boolean isFinished() {   return Robot.chooChooSubsys.hasBeenZeroed;   }
    protected void end() {}
    protected void interrupted() {}
    protected void execute() {}
}
