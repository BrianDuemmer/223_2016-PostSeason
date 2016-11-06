package org.usfirst.frc.team223.robot.intakeWheels.iWheelsCommands;

import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Sets the output of the intake wheels for a specified time before turning them
 * back off
 * 
 * @author Brian Duemmer
 */
public class RunIWheelsForTime extends Command {

	// FPGA timestamp when command finishes
	double endTime;
	double out;
	
	
	/** Construct the new command
	 * 
	 * @param secsToRun Seconds to run for
	 * @param output The output to be sent to the motor during the specified time 
	 * interval
	 */
    public RunIWheelsForTime(double secsToRun, double output) {
    	requires(Robot.intakeWheelsSubsys);
    	out = output;
    	endTime = Timer.getFPGATimestamp() + secsToRun;
    }

    // Set the motor's output
    protected void initialize() {
    	Robot.intakeWheelsSubsys.setOutput(out);
    }
    
    
    protected void execute() {
    }

    // Finish after the necessary time allotted has finished
    protected boolean isFinished() {
        return Timer.getFPGATimestamp() >= endTime;
    }

    // Turn off the motor
    protected void end() {
    	Robot.intakeWheelsSubsys.setOutput(0);
    }


    protected void interrupted() {
    }
}
