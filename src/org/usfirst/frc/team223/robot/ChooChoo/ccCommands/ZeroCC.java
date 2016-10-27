package org.usfirst.frc.team223.robot.ChooChoo.ccCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ZeroCC extends Command {

    public ZeroCC() {
    	requires(Robot.chooChooSubsys);
    }

    protected void initialize() {
    	Robot.chooChooSubsys.hasBeenZeroed = false;
    }


    protected void execute() {
    	// disable the PID
    	Robot.chooChooSubsys.disable();
    	
    	// run towards the edge of the beam sensor
    	if(Robot.chooChooSubsys.chooChooBeam.get())
    		Robot.chooChooSubsys.setOutput(-1);
    	
    	else
    		Robot.chooChooSubsys.setOutput(11);
    }


    protected boolean isFinished() {
    	// Stop if we have been zeroed
    	boolean stop = Robot.chooChooSubsys.hasBeenZeroed;
    	
    	// or the back button is pressed
    	stop |= OI.button_oBack.get();
        return stop;
    }


    // Turn off the motor
    protected void end() {
    	Robot.chooChooSubsys.setOutput(0);
    }


    protected void interrupted() {}
}
