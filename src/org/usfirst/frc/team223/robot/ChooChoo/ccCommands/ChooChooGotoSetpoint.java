package org.usfirst.frc.team223.robot.ChooChoo.ccCommands;

import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/** Moves the choo choo to a specified position. This command accomplishes this
 * by running the motor full forward or reverse, depending on the direction
 * given, until it is a certain angle away from the setpoint and moving towards
 * it. It then kicks on the PID to bring it in close.
 *
 */
public class ChooChooGotoSetpoint extends Command {

	double setpoint;
	
    public ChooChooGotoSetpoint(double setpoint, boolean goForward) {
    	requires(Robot.chooChooSubsys);
    	if(setpoint > 360)
    	
    }


    protected void initialize() {
    	Robot.chooChooSubsys.disable();
    }


    protected void execute() {
    }


    protected boolean isFinished() {
        return false;
    }


    protected void end() {
    }


    protected void interrupted() {}
}
