package org.usfirst.frc.team223.robot.IntakeLift.intakeCommands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team223.robot.OI;
import org.usfirst.frc.team223.robot.Robot;

/** Set the encoder offset such that the value returned by intakeLift.getEncPos()
 * is the angle specified by INTAKELIFT_SETPOINT_LIMIT__POS, essentially setting the
 * zero point for the Intake lift
 * 
 * @author Brian Duemmer
 *
 */
public class IntakeLimitISR extends Command {

    public IntakeLimitISR() {
    	// there is no requires() here because in this case, this ISR is purely
    	// passive (will not modify any outputs), and we don't want it to interrupt
    	// other commands.
    }

    protected void end() {}
    protected void interrupted() {}
    protected void initialize() 
    {
      	// Calculate and set the new encoder offset
    	double newIntakeEncoderOffset = Robot.intakeLiftSubsys.getRawEncPos() - OI.INTAKELIFT_SETPOINT_LIMIT__POS;
    	Robot.intakeLiftSubsys.setEncOffset(newIntakeEncoderOffset);
    	
    	//make sure the rest of code knows that we have been zeroed
    	Robot.intakeLiftSubsys.hasBeenZeroed = true;
    }
    protected void execute() {}
    protected boolean isFinished() {
    	// all this has to do is update a variable, so it finishes essentially instantly
        return Robot.intakeLiftSubsys.hasBeenZeroed;
    }
}
