package org.usfirst.frc.team223.robot.IntakeLift.intakeCommands;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Zeroes the intakeLift on the robot
 */
public class ZeroIntakeLift extends Command {

	/**
	 * Zeroes the intakeLift on the robot
	 */
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
    	// disable the PID
    	Robot.intakeLiftSubsys.disable();
    	
    	// Go towards the edge of the limit
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

    // Turn off the motor, and update the position hold setpoint
    protected void end() {
    	Robot.intakeLiftSubsys.setOutput(0);
    	
    	// If not in dedicated PID mode, hold at the limit position
    	if(!Robot.intakeLiftSubsys.inPIDmove)
    		Robot.intakeLiftSubsys.setSetpoint(OI.INTAKELIFT_SETPOINT_LIMIT__POS);
    	
    	// if we are, carry on with the move
    	else
    		Robot.intakeLiftSubsys.enable();
    }


    protected void interrupted() {}
}
