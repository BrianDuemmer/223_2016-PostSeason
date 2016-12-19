
package org.usfirst.frc.team223.robot;


import org.usfirst.frc.team223.AdvancedX.LoggerUtil.RoboLogger;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLManagerLV;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLParser;
import org.usfirst.frc.team223.robot.ChooChoo.ChooChoo;
import org.usfirst.frc.team223.robot.IntakeLift.IntakeLift;
import org.usfirst.frc.team223.robot.drive.driveTrain;
import org.usfirst.frc.team223.robot.intakeWheels.IntakeWheels;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
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
	
	public static GXMLManagerLV gxmlManager;

    
    // initialize the subsystems / the OI
    public void robotInit() 
    {
    	// Initialize the configuration manager
    	gxmlManager = new GXMLManagerLV("/media/sda1/MainConfig.xml", roboLogger) 
    	{
			@Override
			public boolean reload() 
			{
				freeSystems();
				initSystems();
				
				// TODO Determine if the file loading was successful
				return true;
			}
		};
    	
		
		
		
    	// Initialize the Logger
    	logger = roboLogger.getLogger("RobotMain");
		
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
    	// initialize the roboLogger
    	roboLogger = new RoboLogger("/media/sda1/logging");
    	
    	// log us entering this routine
    	logger.info("=================================================================================================================================");
    	logger.info("================================================= Initializing Robot Systems ====================================================");
    	logger.info("=================================================================================================================================");
    	
    	// Initialize the parser
    	GXMLParser parser = gxmlManager.obtainParser(roboLogger.getLogger("GXML Parser"));
    	
    	// Initialize the subsystems
    	Robot.driveSubsys = new driveTrain(parser, roboLogger);
    	Robot.intakeLiftSubsys = new IntakeLift(parser, roboLogger);
    	Robot.intakeWheelsSubsys = new IntakeWheels(parser, roboLogger);
    	Robot.chooChooSubsys = new ChooChoo(parser, roboLogger);
    	
    	
    	// attempt to initialize NetworkTables
    	logger.info("Attempting to initialize NetworkTables...");
    	try
    	{
    		NetworkTable.setServerMode();
    		NetworkTable.setPort(1735);
    		NetworkTable.initialize();
    		nt = NetworkTable.getTable("Robot223");
    		logger.info("Successfully initialized NetworkTables");
    	} 
    	catch(Exception e){
    		logger.fatal("Failed to initialize networkTables! DETAILS: ", e);
    	}
    	
    	
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
    
    
    
    
    public void teleopInit() 
    {   
    	try {   auto.cancel();   } catch (Exception e)
    	{   logger.error("Failed to stop Autonomous command! DETAILS:", e);   }
    }
    
    
    
    
    
    
    public void logCommands() 
    {
    	// Format an intro message
    	String logMsg = "\r\n\r\nCURRENT COMMANDS";
    	
    	// Add a line for each subsystem to logMsg describing the active command
    	logMsg += "DriveTrain: " + Robot.driveSubsys.getCurrentCommand().getName() + "\r\n";
    	logMsg += "Choo Choo: " + Robot.chooChooSubsys.getCurrentCommand().getName() + "\r\n";
    	logMsg += "Intake Lift: " + Robot.intakeLiftSubsys.getCurrentCommand().getName() + "\r\n";
    	logMsg += "Intake Wheels: " + Robot.intakeWheelsSubsys.getCurrentCommand().getName() + "\r\n";
    	
    	// log the message
    	logger.info(logMsg);
    }
    
    
    
    
	public void disabledPeriodic() {   Scheduler.getInstance().run();   }
    public void autonomousPeriodic() {   Scheduler.getInstance().run();   }
    public void teleopPeriodic() 
    {   
    	Scheduler.getInstance().run();   
//    	logCommands();
    	
    	
    	if(Robot.nt != null)
    	{
    		Robot.nt.putNumber("MyDouble", Timer.getFPGATimestamp());
    		logger.info("val: " + nt.getBoolean("CONFIGXML_MainConfig_RELOAD", false));
    	}
    }
    
}

















