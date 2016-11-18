package org.usfirst.frc.team223.robot.Auto;

import org.usfirst.frc.team223.robot.IntakeLift.intakeCommands.IntakeLiftGotoPos;
import org.usfirst.frc.team223.robot.drive.driveCommands.DriveVelForTime;
import org.usfirst.frc.team223.robot.generalCommands.ZeroLiftAndCC;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Folds up and crosses the low bar during auto
 * @author Brian Duemmer
 */
public class CrossDefenseBasic extends CommandGroup {
    
    public  CrossDefenseBasic(double vel, double dist, double armAngle) {
    	// Calculate drive time
    	double driveTime = dist / vel;
    	
    	
       // Zero the mechanisms
    	addSequential(new ZeroLiftAndCC());
    	
    	// Bring up the intakeLift
    	addSequential(new IntakeLiftGotoPos(armAngle));
    	
    	// Drive straight for the proper distance
    	addSequential(new DriveVelForTime(vel, driveTime, 0));
    }
}
