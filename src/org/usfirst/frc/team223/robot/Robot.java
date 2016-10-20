
package org.usfirst.frc.team223.robot;

import org.usfirst.frc.team223.robot.IntakeLift.IntakeLift;
import org.usfirst.frc.team223.robot.drive.driveTrain;
import org.usfirst.frc.team223.robot.intakeWheels.IntakeWheels;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;


public class Robot extends IterativeRobot {

	public static OI oi;
	public static driveTrain driveSubsys;
	public static IntakeLift intakeLiftSubsys;
	public static IntakeWheels intakeWheelsSubsys;

    Command autonomousCommand;
    SendableChooser chooser;

    
    // initialize the subsystems / the OI
    public void robotInit() {
    	DriverStation.reportError("Starting code..", false);
		driveSubsys = new driveTrain();
		intakeLiftSubsys = new IntakeLift();
		intakeWheelsSubsys = new IntakeWheels();
		oi = new OI();
    }
	
    
    public void disabledInit(){
    	// for now don't do anything here. This may be used eventually.
    }
	
    public void autonomousInit() {
    	//TODO write autonomous
    }
    
    public void teleopInit() {
        //TODO after writing auto, make sure to kill it here, before it lays eggs
    }
    
    public void log()
    {
    	//TODO log stuff
    }
    
    
	public void disabledPeriodic() {   Scheduler.getInstance().run();   }
    public void autonomousPeriodic() {   Scheduler.getInstance().run();   }
    public void teleopPeriodic() {   Scheduler.getInstance().run();   }
}

















