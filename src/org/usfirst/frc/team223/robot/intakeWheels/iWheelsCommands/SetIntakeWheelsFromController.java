package org.usfirst.frc.team223.robot.intakeWheels.iWheelsCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Sets the output of the intake wheels from a controller
 */
public class SetIntakeWheelsFromController extends Command {
	
    public SetIntakeWheelsFromController() {
    	requires(Robot.intakeWheelsSubsys);
    }

    protected void initialize() {
    	
    	// If left bumper pressed, output ball. If Right pressed, intake ball.
    	// if neither or both are pressed, output zero
    	double outA = OI.button_oL.get() ?   1 : 0;
    	double outB = OI.button_oR.get() ?  -1 : 0;
    	
    	Robot.intakeWheelsSubsys.setOutput((outA + outB) * OI.INTAKEWHEELS_MOTOR_SCALAR);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
