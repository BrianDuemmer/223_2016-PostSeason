package org.usfirst.frc.team223.AdvancedX;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 * Implements a single side of a standard tank style drivetrain. This has an embedded
 * PID that controls the velocity of the wheels. This is usually used as a subset of a full
 * drivetrain class that implements two of these, to allow for finer, more independent control
 * @author Brian Duemmer
 *
 */
public class DriveSide extends PIDSubsystem
{
	// Encoder object
	PIDSource pidSrc;
	
	// All of the motors
	private List<SpeedController> motors;
	
	// Encoder data
	double distPerPulse;
	boolean invert;
	
	//max output
	double maxOut;
	
	
	/**
	 * Constructor for the DriveSide. Make sure to configure the motors, PID, 
	 * and PIDsource before use
	 * @param period the time, in seconds, between PID updates
	 */
	public DriveSide( double period)
	{
		super(0, 0, 0, period);
		motors = new ArrayList<SpeedController>();
		
		// Set the maximum output to a safe default
		maxOut = 1;
	}
	
	
	
	
	/**
	 * Constructor for the DriveSide. Make sure to configure the motors, PID, 
	 * and PIDsource before use
	 */
	public DriveSide()
	{
		super(0, 0, 0);
		motors = new ArrayList<SpeedController>();
		
		// Set the maximum output to a safe default
		maxOut = 1;
	}
	
	
	
	
	/**
	 * Sets the PID constants for the drive controller
	 * @param kp Proportional gain term
	 * @param ki Integral term
	 * @param kd Derivative term
	 * @param kf Feedforward term
	 */
	public void setPID(double kp, double ki, double kd, double kf)
	{
		this.getPIDController().setPID(kp, ki, kd, kf);
	}


	
	
	
	/**
	 * Sets the PID source for the driveSide. this is usually an encoder / CANTalon.
	 * @param src the PIDsource to use
	 */
	public void setPIDSource(PIDSource src)
	{   
		pidSrc = src;   
		pidSrc.setPIDSourceType(PIDSourceType.kRate);
	}
	
	
	/**
	 * Sets the scalings for the PIDSource
	 * @param distPerPulse the value that is multiplied by the value returned by the PIDSource
	 * @param invert if true, multiplies the output of PIDSource by -1
	 */
	public void setPIDSrcScalings(double distPerPulse, boolean invert)
	{
		this.distPerPulse = distPerPulse;
		this.invert = invert;
	}
	
	
	/**
	 * Adds a motor to the driveSide
	 * @param mot a fully initialized motor. Don't use it anywhere else
	 */
	public void addMotor(SpeedController mot){   motors.add(mot);   }
	
	
	
	
	/**
	 * returns the input from the PID source provided by {@link setPIDSource}
	 */
	public double getPID() 
	{
		double pidVal;
		
		// if the PID Source is null, return 0
		if(pidSrc == null)
			return 0;
		
		
		// if the PID Source is not null and is a CANTalon, use a workaround to deal with the
		// fact that pidGet will NOT return speed, even if rate is the PIDSource mode.
		if(pidSrc.getClass() == CANTalon.class)
			pidVal = ((CANTalon)pidSrc).getSpeed();
			
		
		// if the PID Source is not null and not a CANTalon, just use pidGet
		else
			pidVal = pidSrc.pidGet();
		
		// Scale and invert as necessary
		pidVal *= distPerPulse;
		pidVal *= invert  ?  -1 : 1;
		return pidVal;

	}

	
	
	/**
	 * Sets the raw output to the motors. Automatically disables
	 * PID control.
	 * @param out the output to send
	 */
	public void setRawOutput(double out)
	{
		// Turn off the PID and reset
		this.getPIDController().reset();
		
		// Set the output
		output(out);
	}
	
	
	
	
	/** Sets the target velocity. Automatically enables the PID
	 * 
	 * @param setpoint
	 */
	public void setSetpoint(double setpoint)
	{
		// Don't run if a PID Source hasn't been set
		if(pidSrc != null)
		{
			// set the setpoint
			super.setSetpoint(setpoint);
			
			// enable the PID
			this.enable();
		}
	}
	
	
	
	/**
	 * Directly send an output to the motors
	 * @param output the output to send
	 */
	protected void output(double output) {
		
		// Coerce the output to an allowable range specified by maxOut
		double newOut = output > 0  ?  Math.min(output, maxOut) : Math.max(output, maxOut * -1);
		
		// Iterate through all of the motors and set their outputs
		for(SpeedController i : motors)
			i.set(newOut);
		
	}

	@Override
	protected void initDefaultCommand() {}
	
	@Override
	protected double returnPIDInput() {   return getPID();   }
	
	@Override
	protected void usePIDOutput(double output) {
		
		// only output if the PID is enabled
		if(this.getPIDController().isEnabled())
			output(output);
		
	}
	
	/**
	 * Returns a string containing some information about the PIDSource, in the format
	 * 
	 * "name - value"
	 */
	public String reportPIDSource(String name)
	{
		String ret = "";
		
		if(pidSrc != null)
		{
			ret = name + ": \t" + String.format("%.3f", new Double(this.returnPIDInput()));
		}
		
		return ret;
	}
	
	
	
	/**
	 * Sets the amount of motors to set into brake mode.
	 * Only works for {@link CANTalon}s.
	 */
	public void setBrakeCount(int motCt)
	{
		// iterate over the motor list
		for(SpeedController i : motors)
		{
			// if the motor is a CANTalon, and motCt > 0, turn on the brakes and decrement motCt
			if(i.getClass() == CANTalon.class && motCt > 0)
			{
				motCt--;
				((CANTalon)i).enableBrakeMode(true);
			}
			
			// if the motor is a CANTalon, and motCt <= 0, turn off the brakes
			else if(i.getClass() == CANTalon.class)
				((CANTalon)i).enableBrakeMode(false);
		}
	}
	
	
	
	
	
	/**
	 * Sets the maximum allowable output to send to the motors.
	 * this should be between [0,1]. Default is 1.
	 * @param output
	 */
	public void setMaxOutput(double output) {   maxOut = output;   }




	/**
	 * @return the motors
	 */
	public List<SpeedController> getMotors() {
		return motors;
	}




	/**
	 * @param motors the motors to set
	 */
	public void setMotors(List<SpeedController> motors) {
		this.motors = motors;
	}
	
	
	
	
	/**
	 * Frees all of the resources allocated by the DriveSide
	 */
	public void free()
	{
		// iterate through the motors and deallocate them
		for(SpeedController i : motors)
		{
			// If it is a PWM controller, free() it
			if(i.getClass().isAssignableFrom(PWM.class) && i != null)
				((PWM) i).free();
			
			// If it is a CANTalon, delete() it
			if(i.getClass() == CANTalon.class && i != null)
				((CANTalon) i).delete();
			
			// If it is none of the above, do nothing
		}
		
		
		// free() the PIDController
		if(this.getPIDController() != null)
			this.getPIDController().free();
		
		
		// free() the PIDSource. Try to cast it to a free()-able type. If we cannot, just ignore it
		if(this.pidSrc.getClass() == CANTalon.class && this.pidSrc != null)
			((CANTalon) this.pidSrc).delete();
		
		else if(this.pidSrc.getClass().isAssignableFrom(SensorBase.class) && this.pidSrc != null)
			((SensorBase) this.pidSrc).free();
		
		// Put another free() here if necessary
		else{}
		
	}
}













