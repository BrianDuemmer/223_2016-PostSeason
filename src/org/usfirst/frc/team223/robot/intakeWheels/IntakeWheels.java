package org.usfirst.frc.team223.robot.intakeWheels;

import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLAllocator;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLparser;
import org.usfirst.frc.team223.AdvancedX.robotParser.MotorData;
import org.usfirst.frc.team223.robot.RoboLogger;
import org.usfirst.frc.team223.robot.Robot;
import org.usfirst.frc.team223.robot.intakeWheels.iWheelsCommands.SetIntakeWheelsFromController;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import net.sf.microlog.core.Logger;

/**
 * The class that contains the intake wheels subsystem. Does not include 
 * intake lift.
 * 
 * @author Brian Duemmer
 */
public class IntakeWheels extends Subsystem {
    
	private Logger logger;	
	
	////////////// IntakeWheels Subsystem /////////////
	
	public MotorData			MOTOR_DATA;
	private CANTalon			MOTOR_HDL;
	
	
	/**
	 * Initializes the IntakeWheels Subsystem
	 * @param parser the GXMLparser bound to the configuration file for the robot
	 * @param logger the log4j logger that will print out any log messages
	 */
	public IntakeWheels(GXMLparser parser, RoboLogger roboLogger)   {   init(parser, roboLogger);   }
	
	
	
	
    /**
     * Initialize the IntakeWheels system. Reads all data from
     * the configuration file, and allocates it accordingly
     */
	public void init(GXMLparser parser, RoboLogger roboLogger)
	{
		this.logger = roboLogger.getLogger("IntakeWheels");	
    	
    	// Allocator to use for allocating the parsed data into objects
    	GXMLAllocator allocator = new GXMLAllocator(logger);
    	
    	
    	// log us entering the init routine
    	logger.info("\n\r\n\r\n\r===================================== Initializing IntakeWheels Subsystem =====================================");
    	
    	// Parse the data
    	this.MOTOR_DATA = parser.parseMotor("IntakeWheels/motor");
    	
    	// Allocate the data
    	this.MOTOR_HDL = (CANTalon) allocator.allocateMotor(this.MOTOR_DATA);
    	
    	logger.info("Finished initializing IntakeWheels");
	}
	
	
	
	
	
    /**
     * Deallocates all physical objects ties to the IntakeWheels. This must be called before
     * {@link IntakeWheels#init(GXMLparser, Logger)} in order to prevent conflicts
     */
	public void cleanup()
	{
    	// log that we are shutting down
    	logger.info("Shutting down IntakeWheels...");
    	
    	// free the resources
    	if(this.MOTOR_HDL != null)
    		this.MOTOR_HDL.delete();
	}
	
	
	
	
	
    public void initDefaultCommand() {
    	setDefaultCommand(new SetIntakeWheelsFromController());
    }
    
    /**
     * Sets the output of the motor
     * @param output the output value to send to the motor
     */
    public void setOutput(double output) {   this.MOTOR_HDL.set(output);   }
    
    
    
    
    public CANTalon getMotorHandle() {   return this.MOTOR_HDL;   }
}

