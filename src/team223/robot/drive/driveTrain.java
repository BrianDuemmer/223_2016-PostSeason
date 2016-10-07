package team223.robot.drive;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import team223.robot.drive.driveCommands.*;

/**
 *
 */
public class driveTrain extends Subsystem {
	
	// drivetrain speed controllers
    private SpeedController driveL1;
    private SpeedController driveL2;
    private SpeedController driveR1;
    private SpeedController driveR2;
    
    public driveTrain()
    {
    	super();
    	driveL1 = new TalonSRX(1);
    	driveL2 = new TalonSRX(4);
    	
    	driveR1 = new TalonSRX(5);
    	driveR2 = new TalonSRX(6);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new setFromJoy());
    }
    
    
    /** Drives the robot from an open loop forward and turn value.
     * This is mostly used for driver control.
     * 
     * @param fwd the forward value from a joystick
     * @param turn the x axis output from a joystick
     */
    public void driveArcade(double fwd, double turn)
    {
    	// calculate the base outputs
    	double outLeft = fwd - turn;
    	double outRight = fwd + turn;
    	
    	// if the outputs are not in the domain of [-1,1], coerce them to fit into said domain
    	outLeft = (outLeft > 1) ?  1 : outLeft;
    	outLeft = (outLeft < -1) ?  -1 : outLeft;
    	
    	outRight = (outRight > 1) ?  1 : outRight;
    	outRight = (outRight < -1) ?  -1 : outRight;
    	
    	// set the outputs
    	driveL1.set(outLeft);
    	driveL2.set(outLeft);
    	
    	driveR1.set(outRight);
    	driveR2.set(outRight);
    }
}

