
package org.usfirst.frc.team223.robot;


import org.usfirst.frc.team223.AdvancedX.LoggerUtil.RoboLogger;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLManager;
import org.usfirst.frc.team223.robot.ChooChoo.ChooChoo;
import org.usfirst.frc.team223.robot.IntakeLift.IntakeLift;
import org.usfirst.frc.team223.robot.drive.driveTrain;
import org.usfirst.frc.team223.robot.intakeWheels.IntakeWheels;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import net.sf.microlog.core.Level;
import net.sf.microlog.core.Logger;


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
	
	public static NetworkTable nt;
	
	public static RoboLogger roboLogger;
	private static Logger logger;
	
	private Command auto;
	
	public static GXMLManager gxmlManager;
	
	public static final String configPath = "/media/sda1/MainConfig.xml";
	
	private static String prevDriveCmd = "";
	private static String prevIntakeLiftCmd = "";
	private static String prevIntakeWheelsCmd = "";
	private static String prevChooChooCmd = "";

    
	
	
	
	
    // initialize the subsystems / the OI
    public void robotInit() 
    {
    	// initialize the roboLogger
    	roboLogger = new RoboLogger("/media/sda1/logging", 5801, Level.WARN);
    	
    	// Initialize the Logger
    	logger = roboLogger.getLogger("RobotMain");
    	
    	// attempt to initialize NetworkTables
    	logger.info("Attempting to initialize NetworkTables...");
    	try
    	{
    		NetworkTable.setServerMode();
    		NetworkTable.setPort(1735);
    		NetworkTable.initialize();
    		nt = NetworkTable.getTable("SmartDashboard");
    		logger.info("Successfully initialized NetworkTables");
    	} 
    	catch(Exception e){
    		logger.fatal("Failed to initialize networkTables! DETAILS: ", e);
    	}
    	
    	
    	// Initialize the configuration manager
    	gxmlManager = new GXMLManager(configPath, roboLogger, nt) 
		{
    		public boolean load() {   return initSystems();   }
    		public boolean free() {   return freeSystems();   }
		};

		gxmlManager.start(1000);
    }
    
    
    
    
	/**
	 * Loads the entire robot's data from the specified file, and populates all of the data
	 * needed to run the robot.
	 */
    public boolean initSystems()
    {
    	boolean success = true;
    	
    	// log us entering this routine
    	logger.info("=================================================================================================================================");
    	logger.info("================================================= Initializing Robot Systems ====================================================");
    	logger.info("=================================================================================================================================");
    	
    	// Initialize the subsystems
    	try
    	{
	    	Robot.driveSubsys = new driveTrain(gxmlManager, nt);
	    	Robot.intakeLiftSubsys = new IntakeLift(gxmlManager, nt);
	    	Robot.intakeWheelsSubsys = new IntakeWheels(gxmlManager, nt);
	    	Robot.chooChooSubsys = new ChooChoo(gxmlManager, nt);
    	} catch (Exception e)
    	{
    		logger.error("Exception occured while initializing subsystems. DETAILS: ", e);
    		success = false;
    	}
    	
		// try to initialize the OI
    	try 
    	{  
    		logger.info("Attempting to initialize OI...");
    		oi = new OI();  
    		logger.info("OI initialized successfully");
    	}
    	catch (Exception e) {
    		logger.fatal("Exeption encountered while initializing the OI. DETAILS" , e);
    		success = false;
    	}
    	
    	// Log that we have finished initializing the robot
    	logger.info("============================================= Finished Initializing Robot Systems ===============================================");
    	
    	return success;
    }
    
    
    
    
    
    public boolean freeSystems()
    {
    	boolean success = true;
    	
    	// log us entering this routine
    	logger.info("=================================================================================================================================");
    	logger.info("================================================ Shutting down Robot Systems ====================================================");
    	logger.info("=================================================================================================================================");
    	
    	try
    	{
    		logger.info("Attempting to shutdown Robot Systems...");
    		
	    	Robot.driveSubsys.cleanup();
	    	Robot.intakeWheelsSubsys.cleanup();
	    	Robot.intakeLiftSubsys.cleanup();
	    	Robot.chooChooSubsys.cleanup();
	    	
	    	logger.info("Successfully shut down robot systems");
    	} 
    	catch(NullPointerException e)
    	{
    		logger.warn("Encountered NullPointerException while shutting down Robot");
    	}
    	
    	catch(Exception e)
    	{
    		logger.error("Failed to shut down robot systems. DETAILS: " ,e);
    		success = false;
    	}
    	
    	// log that we have finished shutting down the robot
    	logger.info("============================================Finished Shutting down Robot Systems ================================================");
    	
    	return success;
    }
	
    
    
    public void disabledInit(){
    	// for now don't do anything here. This may be used eventually.
    }
	
    
    
    
    public void autonomousInit() {   auto.start();   }
    
    
    
    
    public void teleopInit() 
    {   
    	try {   auto.cancel();   } catch (Exception e)
    	{   logger.error("Failed to stop Autonomous command! DETAILS:", e);   }
    }
    
    
    
    
    
    
    public void logCommandChange() 
    {
    	String currDriveCmd = driveSubsys.getCurrentCommand().getName();
    	String currIntakeLiftCmd = intakeLiftSubsys.getCurrentCommand().getName();
    	String currIntakeWheelsCmd = intakeWheelsSubsys.getCurrentCommand().getName();
    	String currChooChooCmd = chooChooSubsys.getCurrentCommand().getName(); 	
    	
    	
    	if(!currDriveCmd.equals(prevDriveCmd))
    		logger.info("Command for Drivetrain Subsystem is now \"" +currDriveCmd);
    	
    	if(!currIntakeLiftCmd.equals(prevIntakeLiftCmd))
    		logger.info("Command for IntakeLift Subsystem is now \"" +currIntakeLiftCmd);
    	
    	if(!currIntakeWheelsCmd.equals(prevIntakeWheelsCmd))
    		logger.info("Command for IntakeWheels Subsystem is now \"" +currIntakeWheelsCmd);
    	
    	if(!currChooChooCmd.equals(prevChooChooCmd))
    		logger.info("Command for ChooChoo Subsystem is now \"" +currChooChooCmd);
    	
    	
    	prevDriveCmd = currDriveCmd;
    	prevIntakeLiftCmd = currIntakeLiftCmd;
    	prevIntakeWheelsCmd = currIntakeWheelsCmd;
    	prevChooChooCmd = currChooChooCmd;

    }
    
    
    
    
	public void disabledPeriodic() 
	{   
		Scheduler.getInstance().run();
	}
	
	
	
	
	
    public void autonomousPeriodic() {   Scheduler.getInstance().run();   }
    public void teleopPeriodic() 
    {   
    	Scheduler.getInstance().run();
    	logCommandChange();
    }
    
}

















