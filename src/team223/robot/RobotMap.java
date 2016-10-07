package team223.robot;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import team223.AdvancedX.SCDescriptor;
import team223.AdvancedX.SCLoader;

/**
 * This class contains all configuration data for the robot, such as port
 * numbers for motors and sensor, PDP channel information, PID constants,
 * sensor configurations, etc. The data stores in this file will probably,
 * at some point, be read and loaded dynamically through a configuration
 * or XML file, although for now, these are set statically
 */
public class RobotMap 
{
	// Speed Controller Loader
	public static SCLoader SCldr;
	
	// List to hold all of the SpeedControllerDescriptors
	public static List<SCDescriptor> SCDlist = new ArrayList<SCDescriptor>();
	
	
	
	
	/**
	 * This is where all of the static SCDs should be specified by the user.
	 * There may also be a method to dynamically load these from an XML
	 * file, though that is not yet implemented.
	 * Each motor should be added like:
	 * 
	 * SCDlist.add(new SCDescriptor("name", ID, isCAN, invert, PDPchannel, useBrake));
	 */
	private void staticPopulateSCDlist()
	{
		// make sure the list is empty before adding any new descriptors
		SCDlist.clear();
		
		// Add all of the motors
		//@TODO make sure these are correct
		SCDlist.add(new SCDescriptor("driveL1", 0, true, false, 0, true));
		SCDlist.add(new SCDescriptor("driveL2", 1, true, false, 1, true));
		SCDlist.add(new SCDescriptor("driveR1", 2, true, false, 2, true));
		SCDlist.add(new SCDescriptor("driveR2", 3, true, false, 3, true));
		SCDlist.add(new SCDescriptor("intakeLift", 4, true, false, 4, true));
		SCDlist.add(new SCDescriptor("intakeWheels", 5, true, false, 5, true));
		SCDlist.add(new SCDescriptor("chooChoo", 6, true, false, 6, true));
	}
	
	
	public RobotMap() 
	{
		// Populate the Speed controller descriptor list
		staticPopulateSCDlist();
		
		// allocate all of the speed controllers
		SCldr = new SCLoader(SCDlist);
	}
	
	
}






















