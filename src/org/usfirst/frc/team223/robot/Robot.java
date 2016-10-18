
package org.usfirst.frc.team223.robot;

import org.usfirst.frc.team223.robot.IntakeLift.IntakeLift;
import org.usfirst.frc.team223.robot.drive.driveTrain;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {

	public static OI oi;
	public static driveTrain driveSubsys;
	public static IntakeLift intakeLiftSubsys;

    Command autonomousCommand;
    SendableChooser chooser;

    
    // initialize the subsystems / the OI
    public void robotInit() {
		oi = new OI();
		driveSubsys = new driveTrain();
		intakeLiftSubsys = new IntakeLift();
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
//    public void testPeriodic() {   LiveWindow.run();   }
}

















