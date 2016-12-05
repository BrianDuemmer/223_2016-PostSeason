
package org.usfirst.frc.team223.robot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLparser;
import org.usfirst.frc.team223.robot.ChooChoo.ChooChoo;
import org.usfirst.frc.team223.robot.IntakeLift.IntakeLift;
import org.usfirst.frc.team223.robot.drive.driveTrain;
import org.usfirst.frc.team223.robot.intakeWheels.IntakeWheels;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
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
	public static Logger logger;
	Command auto;

    Command autonomousCommand;
    SendableChooser chooser;

    
    // initialize the subsystems / the OI
    public void robotInit() 
    {
    	// Initialize the Logger
    	logger = LogManager.getLogger("Robot");
		
		// Initialize the bulk of the robot
		initSystems();
		
		// initialize the OI
		oi = new OI();
    }
    
    
    
    
	/**
	 * Loads the entire robot's data from the specified file, and populates all of the data
	 * needed to run the robot.
	 */
    public void initSystems()
    {
    	// log us entering this routine
    	logger.info("=================================================================================================================================");
    	logger.info("================================================= Initializing Robot Systems ====================================================");
    	logger.info("=================================================================================================================================");
    	
    	// Initialize the parser
    	GXMLparser parser = new GXMLparser(oi.CONFIG_FILE_PATH, logger);
    	
    	// Initialize the subsystems
    	Robot.driveSubsys = new driveTrain(parser, logger);
    	Robot.intakeLiftSubsys = new IntakeLift(parser, logger);
    	Robot.intakeWheelsSubsys = new IntakeWheels(parser, logger);
    	Robot.chooChooSubsys = new ChooChoo(parser, logger);
    	
    	// Log that we have finished initializing the robot
    	logger.info("============================================= Finished Initializing Robot Systems ===============================================");
    }
    
    
    
    
    
    public void freeSystems()
    {
    	// log us entering this routine
    	logger.info("=================================================================================================================================");
    	logger.info("================================================ Shutting down Robot Systems ====================================================");
    	logger.info("=================================================================================================================================");

    	
    	// Shut down the subsystems
    	Robot.driveSubsys.cleanup();
    	Robot.intakeWheelsSubsys.cleanup();
    	Robot.intakeLiftSubsys.cleanup();
    	Robot.chooChooSubsys.cleanup(); 
    	
    	// log that we have finished shutting down the robot
    	logger.info("============================================Finished Shutting down Robot Systems ================================================");
    }
	
    
    
    public void disabledInit(){
    	// for now don't do anything here. This may be used eventually.
    }
	
    public void autonomousInit() {   auto.start();   }
    
    public void teleopInit() {   auto.cancel();   }
    
    public void log() {}
    
    
	public void disabledPeriodic() {   Scheduler.getInstance().run();   }
    public void autonomousPeriodic() {   Scheduler.getInstance().run();   }
    public void teleopPeriodic() {   Scheduler.getInstance().run();   }
    
}

















