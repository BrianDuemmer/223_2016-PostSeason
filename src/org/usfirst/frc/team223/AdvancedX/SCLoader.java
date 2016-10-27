package org.usfirst.frc.team223.AdvancedX;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

/** Class to load and access all of the robot's speed controllers dynamically.
 *  This class should be fed a list containing all of the speed controller descriptors
 *  needed for the robot, and will automatically allocate and configure all of
 *  them, which are stored in a hashtable that can be accesed by calling the
 *  "getSCtable()" method.
 */
public class SCLoader {
	
	// ArrayList to hold all of the motor descriptors
	List<SCDescriptor> descriptors = new ArrayList<SCDescriptor>();	
	
	// table to be used to hold the speedControllers
	Hashtable<SCDescriptor, SpeedController> SCs = new Hashtable<SCDescriptor, SpeedController>();
	

	/** Initializes a new SCLoader. Only one of these should be necessary for the entirety of robot code
	 * to load all of the speed controllers.
	 * 
	 * @param SCDlist arrayList of SCDescriptors to be allocated.
	 */
	public SCLoader(List<SCDescriptor> SCDlist)
	{
		this.descriptors = SCDlist;
		loadSCs();
	}

	
	
	
	
	/**
	 * This function initializes all of the speed controllers, and puts them into a hashtable, where each one can be
	 * accessed from the table using one of the above enum identifiers. This also allows for other sensors to
	 * access these controllers easily, needing only an ID number to get the motor controller's handle.
	 * This is particularly useful for the SRX's with integrated sensor peripherals.
	 */
	private void loadSCs()
	{
		// iterate through the list and allocate the proper speed controller
		for(SCDescriptor i : descriptors)
		{
			/*
			 * Try to add a new motor to SCs. if the current element "i" is of type CAN, add a new CANTalon.
			 * If it is not, allocate a VictorSP. If an error occurs, and the allocation fails, print out
			 * some diagnostic information to the console.
			 */
			try { SCs.put(i, (i.isCAN)   ?   new CANTalon(i.id)   :   new VictorSP(i.id)); } 
			
			// if an error occurs, report it to the console
			catch(Exception e)
			{
				// print some diagnostic information
				System.out.println("Failed to allocate motor : \"");
				System.out.println(i.name);
				System.out.println("\nError message:\n");
				System.out.println(e.getMessage());
			}	
		}
	}
	
	
	
	
	@SuppressWarnings("unused")
	private void configSCs()
	{
		// iterate through the list and get the proper speed controller
		for(SCDescriptor i : descriptors)
		{
			
		}
	}
	
	
	
	
	
	/**
	 * Returns a complete SCDescriptor given a name "name". If no descriptor matches the given name,
	 * return null
	 * @param name the name to search for
	 * @return a matching SCDescriptor, or null if not found
	 */
	public SCDescriptor getDescByName(String name)
	{
		// Check each element to see if the name matches
		for(SCDescriptor i : descriptors)
			if( i.name.equals(name))
				return i;
		
		// if nothing is found, return null
		return null;
	}
	
	
	
	
	
	/**
	 * Returns a complete SCDescriptor given an index and type. If no descriptor matches,
	 * return null
	 * @param idx Speed Controller index number (CAN or PWM id) to search for
	 * @param isCAN If the desired speedController is CAN or PWM
	 * @return a matching SCDescriptor, or null if not found
	 */
	public SCDescriptor getDescByIdxType(int idx, boolean isCAN)
	{
		// Check each element to see if the id's and type (CAN or PWM) matches
		for(SCDescriptor i : descriptors)
			if( i.id == idx && !(i.isCAN ^ isCAN))
				return i;
		
		// if nothing is found, return null
		return null;
	}
	
	
	
	
	
	/**
	 * Gets the SpeedController Table
	 * @return SpeedController Hashtable
	 */
	public Hashtable<SCDescriptor, SpeedController> getSCtable()
	{
		return SCs;
	}
	
	
	
	
	
	/**
	 * Gets a SpeedController reference from a descriptor
	 * @param desc Speed Controller Descriptor
	 * @return the SpeedController described by desc
	 */
	public SpeedController getSC(SCDescriptor desc)
	{
		return SCs.get(desc);
	}
}
	













