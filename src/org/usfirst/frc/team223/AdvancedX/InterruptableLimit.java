package org.usfirst.frc.team223.AdvancedX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Class that allows for a specific command to be called when a state change
 * occurs on a digital input. Useful for avoiding polling loops.
 * @author Duemmer
 *
 */
public class InterruptableLimit extends DigitalInput {
	
	/**Initializes the InterruptableLimit object.
	 * 
	 * @param index DigitalInput channel that the limit is connected to
	 * @param handler Command to be run upon an interrupt. Make this a command
	 * 		  that runs quickly (don't have it do much more than update a few variables)
	 * @param normallyOpen If true, the limit switch is normally open
	 * @param fireOnHit If true, enables interrupts upon the switch being hit
	 * @param fireOnRelease If true, enables interrupts upon the switch being released
	 */
	public InterruptableLimit(int index, Command handler, boolean normallyOpen, boolean fireOnHit, boolean fireOnRelease)
	{
		// initialize the input channel
		super(index);
		
		/*
		 * If normally open is false, then the rising edge on the input would
		 * corresponding to the switch being hit and vice versa. If normally
		 * open is true, then the opposite is true: a rising edge would mean
		 * the switch is being released, and so forth. 
		 */
		boolean fireOnRising = normallyOpen ? fireOnRelease : fireOnHit;
		boolean fireOnFalling = normallyOpen ? fireOnHit : fireOnRelease;
		
		// set the triggering mode
		this.setUpSourceEdge(fireOnRising, fireOnFalling);
		
		
		// set up the handler
		this.requestInterrupts(new InterruptHandlerFunction<Object>()
		{
			// Override this method for the new InterruptHandlerFunction to do what we want
			@Override
			public void interruptFired(int interruptAssertedMask, Object param) 
			{
				// run the command
				handler.start();
			}
		});
	}
}
