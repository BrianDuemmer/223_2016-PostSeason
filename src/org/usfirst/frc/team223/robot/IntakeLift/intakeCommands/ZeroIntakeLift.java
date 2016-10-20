package org.usfirst.frc.team223.robot.IntakeLift.intakeCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ZeroIntakeLift extends Command {

	
    public ZeroIntakeLift() {
    	requires(Robot.intakeLiftSubsys);
    }

    /* Set hasBeenZeroed to false, so we can easily detect when we hit our zero,
     * as when we do, the intakeLift limit ISR will set it back to true for us,
     * when we hit the zero point.
     */
    protected void initialize() {
    	Robot.intakeLiftSubsys.hasBeenZeroed = false;
    }


    // Make the motor move the intakeLift towards the zero point
    protected void execute() {
    	if(Robot.intakeLiftSubsys.limit.get())
    		Robot.intakeLiftSubsys.setOutput(0.5);
    	else
    		Robot.intakeLiftSubsys.setOutput(-0.5);
    }

    // Reset either after we are successfully zeroed, or when the back button on
    // the operator controller is pressed
    protected boolean isFinished() {
        return Robot.intakeLiftSubsys.hasBeenZeroed || OI.button_oBack.get();
    }

    // Turn off the motor
    protected void end() {
    	Robot.intakeLiftSubsys.setOutput(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
