/**
 * 
 */
package org.usfirst.frc.team223.AdvancedX.robotParser;

import org.usfirst.frc.team223.AdvancedX.DriveSide;
import org.usfirst.frc.team223.AdvancedX.InterruptableLimit;
import org.usfirst.frc.team223.AdvancedX.TankCascadeController;
import org.usfirst.frc.team223.robot.Robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import net.sf.microlog.core.Logger;

/**
 * This class acts along with {@link GXMLParser}, in that it takes data parsed into data objects
 * and uses it to allocate an instance of the object itself. Note that only a single instance
 * of this class is enough to allocate all of the robot's objects
 * 
 * @author Brian Duemmer
 */
public class GXMLAllocator {
	
	// Log4j logger object to print and log data
	private Logger logger = Robot.roboLogger.getLogger("GXML Allocator");
	
	
	
	
	/**
	 * Allocate a new instance of the GXMLAllocator. This should only have to be called
	 * once in the code
	 * @param logger the Log4j Logger to log data too
	 */
	public GXMLAllocator(Logger logger)
	{
		logger.info("Creating new instance of GXMLAllocator");
		this.logger = logger;
	}
	
	
	
	/**
	 * Allocates a new {@link PIDController}
	 * 
	 * @param data the PIDData to be used in the {@link PIDController} loop
	 * @param pidSource the {@link PIDSource} that the {@link PIDController} will use
	 * @param pidOutput the {@link PIDOutput} that the {@link PIDController} will use
	 * @param name the name of the object to use for logging purposes
	 * 
	 * @return a new {@link PIDController}
	 */
	public PIDController allocatePID(PIDData data, PIDSource pidSource, PIDOutput pidOutput)
	{
	// Log us entering the routine
		logger.info("Attempting to allocate PID...");
		
	// log if either the PIDSource or the PIDOutput is null
		if(pidSource == null)
		{
			logger.error("PIDSource for PID is null. Returning null...");
			return null;
		}
		
		if(pidOutput == null)
		{
			logger.error("PIDOutput for PID is null. Returning null...");
			return null;
		}
		
		
		// Attempt to allocate all of the data
		try
		{
		// PIDController object to return
			PIDController ret = new PIDController(data.kp, data.ki, data.kd, data.kf, pidSource, pidOutput, data.period);
			
		// Configure the tolerance
			ret.setAbsoluteTolerance(data.tolerance);
			
		// Configure the input range
			ret.setInputRange(data.min, data.max);
			
		// Configure if the input is continuous
			ret.setContinuous(data.continuous);
			
		// Say that the PID has been allocated successfully
			logger.info("Successfully allocated PID");
			
			return ret;
		} catch (Exception e)
		{
			logger.error("Error allopcating PID controller! DEATILS:\r\n", e);
		}
		
		// If this was reached, there was an error
		return null;
	}
	
	
	
	
	
	
	/**
	 * Allocates a new {@link SpeedController}
	 * 
	 * @param data the {@link MotorData} to be used in the {@link SpeedController}
	 * 
	 * @return a new {@link SpeedController}
	 */
	public SpeedController allocateMotor(MotorData data)
	{
	// Log us entering the routine
		logger.info("Attempting to allocate Motor...");
		
	// SpeedController object to return
		SpeedController ret;
		
	// Allocate the proper type of SpeedController
		if(data.type.selection.equals("CANTalon"))
		{
			ret = new CANTalon(data.id);
			
		// Set brake mode here if a CANTalon
			((CANTalon)ret).enableBrakeMode(data.brake);
		} 
		else 
			ret = new VictorSP(data.id);
		
	// Configure the common parameters
		ret.setInverted(data.invert);
		
	// Log us exiting the routine
		logger.info("Finished Allocating Motor");
	
	// return the newly allocated motor
		return ret;
	}
	
	
	
	
	
	/**
	 * Allocates a new {@link Encoder}
	 * 
	 * @param data the {@link EncoderData} to be used in the {@link Encoder}
	 * 
	 * @return a new {@link Encoder}
	 */
	public Encoder allocateRegEncoder(EncoderData data)
	{
	// Log us entering the routine
		logger.info("Attempting to allocate Regular Encoder...");
		
		Encoder ret;
		
	// Initialize the Encoder object to return
		if(data.IDXchannel >= 0)
			ret = new Encoder(data.Achannel, data.Bchannel, data.IDXchannel, data.invert);
		
		else
			ret = new Encoder(data.Achannel, data.Bchannel, data.invert);
		
	// Configure the counts per revolution
		ret.setDistancePerPulse(data.distPerCount);
		
	// Log us exiting the routine
		logger.info("Finished Allocating Regular Encoder");
		
	// Return the encoder
		return ret;
	}
	

	
	
	
	/**
	 * Allocates a new {@link InterruptableLimit}
	 * 
	 * @param data the {@link LimitData} to be used in the {@link InterruptableLimit}
	 * 
	 * @return a new {@link InterruptableLimit}
	 */
	public InterruptableLimit allocateLimit(LimitData data, Command handlerCommand)
	{
		logger.info("Allocating new InterruptableLimit...");
		
	// See if the handler ommand is null, print a warning if it is
		if(handlerCommand == null)
			logger.error("Handler Command for InterruptableLimit is null!");
		
	// See which sides will trigger an interrupt
		boolean onHit = data.interruptEdge.selection.equals("onHit") || data.interruptEdge.selection.equals("onBoth");
		boolean onRelease = data.interruptEdge.selection.equals("onRelease") || data.interruptEdge.selection.equals("onBoth");
		
	// Initialize the Limit
		InterruptableLimit ret = new InterruptableLimit(data.id, handlerCommand, data.normallyOpen, onHit, onRelease, data.debounceTime);
		
	// return
		logger.info("Finished allocating new InterruptableLimit");
		return ret;
	}
	
	
	
	/**
	 * Allocates a new {@link CANTalon} encoder
	 * 
	 * @param data the {@link EncoderData} to be used in the {@link CANTalon} encoder
	 * @param motor the {@link CANTalon} that the encoder will be bound to
	 */
	public void allocateCANEncoder(EncoderData data, CANTalon motor)
	{
	// Log us entering the routine
		logger.info("Attempting to allocate CAN Encoder...");
		
	// make sure motor is not null
		if(motor == null)
		{
			logger.warn("CANTAlon refernece passed to AllocateCANEncoder is null");
		}
		
	// Set the device configurations
		motor.configEncoderCodesPerRev((int)(1 / data.distPerCount));
		motor.enableZeroSensorPositionOnIndex(data.IDXchannel > 0, true);
		motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		
	// Log us exiting the routine
		logger.info("Finished Allocating CAN Encoder");
	}
	
	
	
	
	
	
	/**
	 * Allocates a new {@link DriveSide}
	 * 
	 * @param data the {@link DriveSideData} to be used in the {@link DriveSide}
	 * 
	 * @return a new {@link DriveSide}
	 */
	public DriveSide allocateDriveSide(DriveSideData data)
	{
		
	// Log us entering the routine
		logger.info("Attempting to allocate DriveSide...");
		
	// Initialize the DriveSide object to return
		DriveSide ret = new DriveSide(data.pid.period);
		
	// Configure the PID
		ret.setPID(data.pid.kp, data.pid.ki, data.pid.kd, data.pid.kf);
		ret.setAbsoluteTolerance(data.pid.tolerance);
		
	// Say that we are allocating the motors
		logger.info("Allocating motors to DriveSide...");
		
	// Add the motors
		for(MotorData i : data.motors)
		{
		// Allocate the motor data element
			SpeedController mot = allocateMotor(i);
			
		// Add it to the DriveSide
			ret.addMotor(mot);
		}
		
	// Say that we are finished allocating the motors
		logger.info("Finished allocating motors to DriveSide");
		
	// PIDSource to use for the DriveSide
		PIDSource src;
		
	// Allocate the proper type of encoder
		if(data.encoder.Bchannel < 0)
		{
			logger.info("Allocating a CAN Encoder for the Driveside");
		// Allocate a CANTalon Encoder. It is assumed that the encoder is connected to the first motor in MotorData
			src = (CANTalon)ret.getMotors().get(0);
			allocateCANEncoder(data.encoder, (CANTalon)ret.getMotors().get(0));
		}
		
		else
		{
			logger.info("Allocating a standard encoder for DriveSide");
			src = allocateRegEncoder(data.encoder);
		}
		
	// Set the PIDSource
		ret.setPIDSource(src);
		ret.setPIDSrcScalings(data.encoder.distPerCount, data.encoder.invert);
		
	
		logger.info("Finished allocating DriveSide");
		
	// Return the DriveSide
		return ret;
	}
	
	
	
	
	/**
	 * Allocates a new {@link TankCascadeController}
	 * 
	 * @param data the {@link TankCascadeData} to be used in the {@link TankCascadeController}
	 * 
	 * @return a new {@link TankCascadeController}
	 */
	public TankCascadeController allocateTankCascadeController(TankCascadeData data, Gyro gyro)
	{
		logger.info("Allocating TankCascadeController...");
		
	// print a warning if gyro is null
		if(gyro == null)
			logger.error("Gyro for TankCascadeController is null!");
		
	// Allocate the DriveSides
		DriveSide left = allocateDriveSide(data.leftData);
		DriveSide right = allocateDriveSide(data.rightData);
		
	// Initialize the TCC
		TankCascadeController ret = new TankCascadeController(left, right, gyro, data.distancePID.period, data.anglePID.period);
		
	// Set the master PIDs
		logger.info("Setting Master PIDs...");
		ret.setPosPID(data.distancePID.kp, data.distancePID.ki, data.distancePID.kd, data.distancePID.kf);
		ret.setTurnPID(data.anglePID.kp, data.anglePID.ki, data.anglePID.kd, data.anglePID.kf);
		
	//set the yaw to 0 initially
		logger.info("Setting yaw to 0 initially");
		ret.setYaw(0, true);
		
		logger.info("Finished allocating TankCascadeController");
		
		return ret;
	}
}















