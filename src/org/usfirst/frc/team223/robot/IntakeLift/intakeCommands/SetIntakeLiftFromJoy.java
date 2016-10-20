package org.usfirst.frc.team223.robot.IntakeLift.intakeCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetIntakeLiftFromJoy extends Command {

    public SetIntakeLiftFromJoy() {
    	requires(Robot.intakeLiftSubsys);
    }

    
    protected void initialize() {
    }

    // set the output
    protected void execute() {
    	double output = OI.operatorController.getRawAxis(2) - OI.operatorController.getRawAxis(3);
    	Robot.intakeLiftSubsys.setOutput(output);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
