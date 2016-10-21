package org.usfirst.frc.team223.AdvancedX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Class that allows for a specific command to be called when a state change
 * occurs on a digital input. Useful for avoiding polling loops. When an interrupt
 * fires, a new instance of Command type "handler" is automatically created and run
 * @author Duemmer
 *
 */
public class InterruptableLimit extends DigitalInput {
	
	private boolean normallyOpen;
	
	private double lastHitTime;
	
	
	/**Initializes the InterruptableLimit object.
	 * 
	 * @param index DigitalInput channel that the limit is connected to
	 * 
	 * @param handler Command to be run upon an interrupt. 
	 *  
	 * @param normallyOpen If true, the limit switch is normally open
	 * 
	 * @param fireOnHit If true, enables interrupts upon the switch being hit
	 * 
	 * @param fireOnRelease If true, enables interrupts upon the switch being released
	 */	
	public InterruptableLimit(int index, Command handler, boolean normallyOpen, boolean fireOnHit, boolean fireOnRelease, double debounceTime)
	{
		// initialize the input channel
		super(index);
		
		// Make sure handler in't null
		assert(handler != null);
		
		// get the type of the handler command
		Class<? extends Command> handlerType = handler.getClass();
		
		/*
		 * If normally open is false, then the rising edge on the input would
		 * corresponding to the switch being hit and vice versa. If normally
		 * open is true, then the opposite is true: a rising edge would mean
		 * the switch is being released, and so forth. 
		 */
		boolean fireOnRising = normallyOpen ? fireOnRelease : fireOnHit;
		boolean fireOnFalling = normallyOpen ? fireOnHit : fireOnRelease;
		
		
		
		/**
		 * Allocates a new instance of the InterrupHandlerFunction class. Overrides
		 * the overridableParameter and interruptFired methods in order to allow
		 * running of a command with type of handlerType.
		 */
		InterruptHandlerFunction<Class<? extends Command>> ISR = new InterruptHandlerFunction<Class<? extends Command>>()
		{
			// Override this method to give us a parameter to pass to the interruptFired routine
			@Override
			public Class<? extends Command> overridableParameter()
			{
				return handlerType;
			}
			
			
			
			// Override this method for the new InterruptHandlerFunction to do what we want
			@Override
			public void interruptFired(int interruptAssertedMask, Class<? extends Command> param) 
			{
				static
				// Current time
				double currTime = Timer.getFPGATimestamp();
				
				// if true, enough time has elapsed to run again
				if(currTime - lastHitTime > debounceTime)
				{
					
				}
				
				Command cmdInst = null;
				try {
					cmdInst = param.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
				
				if(cmdInst != null)
					cmdInst.start();
			}
		};
				
		// set up the handler
		this.requestInterrupts(ISR);
	}
	
	
	/**
	 * Returns true if the limit is hit. Accounts for whether or not the limit is
	 * normally open or closed
	 * @return true if the limit is pressed, false otherwise
	 */
	public boolean get()
	{
		return super.get() ^ normallyOpen;
	}
}


















