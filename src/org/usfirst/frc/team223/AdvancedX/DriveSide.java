package org.usfirst.frc.team223.AdvancedX;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 * Implements a single side of a standard tank style drivetrain. This has an embedded
 * PID that controls the velocity of the wheels. This is usually used as a subset of a full
 * drivetrain class that implements two of these, to allow for finer, more independent control
 * @author Duemmer
 *
 */
public class DriveSide extends PIDSubsystem
{
	// Encoder object
	PIDSource pidSrc;
	
	// All of the motors
	List<SpeedController> motors;
	
	
	/**
	 * Constructor for the DriveSide. Don't forget
	 */
	public DriveSide()
	{
		super(0, 0, 0);
		motors = new ArrayList<SpeedController>();
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
	 * Sets the PID source for the driveSide. this is usually an encoder / CANTalon. Make sure
	 * it is set to velocity, not displacement
	 * @param src the PIDsource to use
	 */
	public void setPIDSource(PIDSource src){   pidSrc = src;   }
	
	
	/**
	 * Adds a motor to the driveSide
	 * @param mot a fully initialized motor. Don't use it anywhere else
	 */
	public void addMotor(SpeedController mot){   motors.add(mot);   }
	
	
	
	
	/**
	 * returns the input from the PID source provided by {@link setPIDSource}
	 */
	public double getPID() {
		// if the pid source is valid, use that
		if(pidSrc != null)
			return pidSrc.pidGet();
		
		// if it is null, return 0.
		else
			return 0;
	}

	
	
	/**
	 * Sets the raw output to the motors. Automatically disables
	 * PID control.
	 * @param out the output to send
	 */
	public void setRawOutput(double out)
	{
		// Turn off the PID
		this.disable();
		
		// Set the output
		output(out);
	}
	
	
	
	
	/** Sets the target velocity. Automatically enables the PID
	 * 
	 * @param setpoint
	 */
	public void setSetpoint(double setpoint)
	{
		// set the setpoint
		super.setSetpoint(setpoint);
		
		// enable the PID
		this.enable();
	}
	
	
	
	/**
	 * Directly send an output to the motors
	 * @param output the output to send
	 */
	protected void output(double output) {
		
		// Iterate through all of the motors and set their outputs
		for(SpeedController i : motors)
			i.set(output);
		
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
}
