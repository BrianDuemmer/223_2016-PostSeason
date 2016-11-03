package org.usfirst.frc.team223.robot.ChooChoo.ccCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetCCfromJoy extends Command {

    public SetCCfromJoy() 
    {   
    	requires(Robot.chooChooSubsys); 
    	this.setInterruptible(true);
    }
    protected boolean isFinished() {   return false;   }
    
    // Say that we are entering the command
    protected void initialize() {  Robot.printToDS("Entering setFromJoy ", "ChooChoo");}
    
    // Say that we are leaving the command
    protected void end() { Robot.printToDS("Leaving setFromJoy ", "ChooChoo"); }
    protected void interrupted() {}

    // Disable the PID, then set the output
    protected void execute() {
    	Robot.chooChooSubsys.disable();
    	
    	double out = OI.stick_oL.getX();
    	Robot.chooChooSubsys.setOutput(out);
    	
    	//this.setInterruptible(true);
    }



}
