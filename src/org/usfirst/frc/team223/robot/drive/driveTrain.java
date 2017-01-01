package org.usfirst.frc.team223.robot.drive;

import org.usfirst.frc.team223.AdvancedX.TankCascadeController;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLAllocator;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLManager;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLparser;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLparser.BASIC_TYPE;
import org.usfirst.frc.team223.AdvancedX.robotParser.GyroNavX;
import org.usfirst.frc.team223.AdvancedX.robotParser.TankCascadeData;
import org.usfirst.frc.team223.robot.drive.driveCommands.setDriveFromJoy;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import net.sf.microlog.core.Logger;

/** Acts as the drivetrain Subsystem of the robot.
 *
 *@author Brian Duemmer
 */
public class driveTrain extends Subsystem {
	
	private Logger logger;
	private NetworkTable nt;
    
	//////////////// Drive Subsystem /////////////////
	
	public TankCascadeData 			DRIVE_DATA;
	private TankCascadeController	DRIVE_HDL;
	
	public GyroNavX 				navx;
	
	public double					VEL__FOR__TIME_BRAKE__TIME;
	public double					FINE_ADJ_OUT;
	public int						DRIVE_DEFAULT__BRAKE__COUNT;
	
	public int						FLASHLIGHT_RELAY_PORT;
	public double					FLASHLIGHT_HOLD__TIME;
	private Relay					FLASHLIGHT_HDL;
	
	
	
	
	/**
	 *  Initialize the driveTrain
	 */
	public driveTrain(GXMLManager manager, NetworkTable nt)
	{
		this.nt = nt;
		init(manager);
	}
	
	
    /**
     * Initialize the DriveTrain system. Reads all data from
     * the configuration file, and allocates it accordingly
     */
	public void init(GXMLManager manager)
	{
		logger = manager.getRoboLogger().getLogger("DriveTrain");
		
		GXMLparser parser = manager.obtainParser();
		
    	// Allocator to use for allocating the parsed data into objects
    	GXMLAllocator allocator = manager.obtainAllocator("DriveTrainAllocator");
    	
    	// log us entering the init routine
    	logger.info("\n\r\n\r\n\r====================================== Initializing DriveTrain Subsystem ======================================");
    	
    	// Parse the data
    	this.DRIVE_DATA = parser.parseTankCascade("DriveTrain"); 
    	this.FLASHLIGHT_RELAY_PORT = (Integer) parser.getKeyByPath("General/flashlight/relayPort", BASIC_TYPE.INT);
    	this.FLASHLIGHT_HOLD__TIME = (Double) parser.getKeyByPath("General/flashlight/relayPort", BASIC_TYPE.DOUBLE);
    	this.FINE_ADJ_OUT = (Double) parser.getKeyByPath("DriveTrain/fineAdj", BASIC_TYPE.DOUBLE);
    	
    	// Allocate the data
		logger.info("\n\rAllocating DriveTrain data...");
		
		// Allocate the NavX
		logger.info("Allocating NavX...");
    	navx = new GyroNavX(Port.kMXP);
    	logger.info("Finished allocatng NavX");
    	
    	this.DRIVE_HDL = allocator.allocateTankCascadeController(this.DRIVE_DATA, navx);
    	this.FLASHLIGHT_HDL = new Relay(this.FLASHLIGHT_RELAY_PORT);
    	
    	logger.info("Finished initializing DriveTrain");
	}
	
	
	
	
	
    /**
     * Deallocates all physical objects ties to the DriveTrain. This must be called before
     * {@link DriveTrain#init(GXMLManager)} in order to prevent conflicts
     */
	public void cleanup()
	{
    	// log that we are shutting down
    	logger.info("Shutting down DriveTrain...");
    	
    	// free the resources
		if(this.DRIVE_HDL != null)
			this.DRIVE_HDL.free();
		
		if(this.FLASHLIGHT_HDL != null)
		{
			this.FLASHLIGHT_HDL.free();
			logger.info("Successfully freed flashlight");
		}
	}
	
	
	
	
    /**
     * Set the default command to {@link setDriveFromJoy}
     */
    public void initDefaultCommand() {   setDefaultCommand(new setDriveFromJoy());   }
    
    
    /** Drives the robot from an open loop forward and turn value.
     * This is mostly used for driver control.
     * 
     * @param fwd the forward value from a joystick
     * @param turn the x axis output from a joystick
     */
    public void driveArcade(double fwd, double turn)
    {
    	// calculate the base outputs
    	double outLeft = fwd + turn;
    	double outRight = fwd - turn;
    	
    	// if the outputs are not in the domain of [-1,1], coerce them to fit into said domain
    	outLeft = (outLeft > 1) ?  1 : outLeft;
    	outLeft = (outLeft < -1) ?  -1 : outLeft;
    	
    	outRight = (outRight > 1) ?  1 : outRight;
    	outRight = (outRight < -1) ?  -1 : outRight;
    	
    	// set the outputs
    	this.DRIVE_HDL.setRawOut(outLeft, outRight);
    }
	
    
    
	
	/**
	 * @return the Cascade Controller
	 */
	public TankCascadeController getController() {
		return this.DRIVE_HDL;
	}



	/**
	 * @return the light
	 */
	public Relay getLight() {
		return this.FLASHLIGHT_HDL;
	}



	/**
	 * @param light the light to set
	 */
	public void setLight(Relay light) {
		this.FLASHLIGHT_HDL = light;
	}

}















