
package org.usfirst.frc.team223.robot;

import org.usfirst.frc.team223.robot.Auto.CrossDefenseBasic;
import org.usfirst.frc.team223.robot.ChooChoo.ChooChoo;
import org.usfirst.frc.team223.robot.IntakeLift.IntakeLift;
import org.usfirst.frc.team223.robot.drive.driveTrain;
import org.usfirst.frc.team223.robot.intakeWheels.IntakeWheels;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;


/**
 * Main entry - point class for the robot project
 * @author Brian Duemmer
 *
 */
public class Robot extends IterativeRobot {

	public static OI oi;
	public static driveTrain driveSubsys;
	public static IntakeLift intakeLiftSubsys;
	public static IntakeWheels intakeWheelsSubsys;
	public static ChooChoo chooChooSubsys;
	Command auto;

    Command autonomousCommand;
    SendableChooser chooser;

    
    // initialize the subsystems / the OI
    public void robotInit() 
    {
		driveSubsys = new driveTrain();
		intakeLiftSubsys = new IntakeLift();
		intakeWheelsSubsys = new IntakeWheels();
		chooChooSubsys = new ChooChoo();
		oi = new OI();
		auto = new CrossDefenseBasic(6, 11, OI.AUTO_STD__DEF_INTAKE__ANGLE);
		printToDS("Starting Code...", "");
    }
	
    
    public void disabledInit(){
    	// for now don't do anything here. This may be used eventually.
    }
	
    public void autonomousInit() {
    	auto.start();
    }
    
    public void teleopInit() {
        auto.cancel();
    }
    
    public void log()
    {
    	//TODO log stuff
    }
    
    
	public void disabledPeriodic() {   Scheduler.getInstance().run();   }
    public void autonomousPeriodic() {   Scheduler.getInstance().run();   }
    public void teleopPeriodic() 
    {
    	Scheduler.getInstance().run(); 
    	
//    	printToDS("Active Command: " +chooChooSubsys.getCurrentCommand().getName(), "ChooChoo");
//    	intakeLiftSubsys.log();
//    	chooChooSubsys.log();
//    	driveSubsys.logEnc(true, true);
    }
    
    /**
     * Prints a message to the DS console, such that it reads
     * ERROR 223 : (msg) at (loc)
     * @param msg the message to send
     * @param loc the location that sent it
     */
    public static void printToDS(String msg, String loc)
    {
    	FRCNetworkCommunicationsLibrary.HALSendError(true, 223, false, msg, loc, "", true);
    }
}

















