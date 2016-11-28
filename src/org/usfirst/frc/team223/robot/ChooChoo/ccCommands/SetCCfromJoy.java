package org.usfirst.frc.team223.robot.ChooChoo.ccCommands;

import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Sets the ChooChoo output from the joystick
 * 
 * @author Brian Duemmer
 */
public class SetCCfromJoy extends Command {

    public SetCCfromJoy() 
    {   
    	requires(Robot.chooChooSubsys); 
    	this.setInterruptible(true);
    }
    protected boolean isFinished() {   return false;   }
    

    protected void initialize() {}
    

    protected void end() {}
    protected void interrupted() {}

    // Disable the PID, then set the output
    protected void execute() {
    	Robot.chooChooSubsys.getPIDHandle().disable();
    	
    	double out = Robot.oi.stick_dL.getX();
    	Robot.chooChooSubsys.setOutput(out);
    }



}
