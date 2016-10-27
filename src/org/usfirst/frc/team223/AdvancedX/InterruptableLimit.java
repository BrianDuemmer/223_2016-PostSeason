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
	
	
	/**Initializes the InterruptableLimit object.
	 * 
	 * @param index DigitalInput channel that the limit is connected to
	 * 
	 * @param handler Command to be run upon an interrupt. Note that the constructor for
	 * this command cannot have any parameters.
	 *  
	 * @param normallyOpen If true, the limit switch is normally open
	 * 
	 * @param fireOnHit If true, enables interrupts upon the switch being hit
	 * 
	 * @param fireOnRelease If true, enables interrupts upon the switch being released
	 * 
	 * @param debounceTime time (in seconds) to debounce the digital signal coming in
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
		
		// Original pin state
		boolean origState = super.get();
		
		// Create the arg object to pass to the ISR
		InterruptableLimitArg arg = new InterruptableLimitArg(handlerType, this, fireOnRising, fireOnFalling, origState, debounceTime);
		
		/**
		 * Allocates a new instance of the InterrupHandlerFunction class. Overrides
		 * the overridableParameter and interruptFired methods in order to allow
		 * running of a command with type of handlerType.
		 */
		InterruptHandlerFunction<InterruptableLimitArg> ISR = new InterruptHandlerFunction<InterruptableLimitArg>()
		{
			
			// Override this method to give us a parameter to pass to the interruptFired routine
			@Override
			public InterruptableLimitArg overridableParameter(){   return arg;   }
			
			
			// Override this method for the new InterruptHandlerFunction to do what we want
			@Override
			public void interruptFired(int interruptAssertedMask, InterruptableLimitArg param) 
			{
				// Timestamps
				double currTime = Timer.getFPGATimestamp();
				double riseTime = param.input.readRisingTimestamp();
				double fallTime = param.input.readFallingTimestamp();
				
				// Edge type
				boolean isRising;
				
				// Tells us if the edge is correct
				boolean correctEdge;
				
				// Tells if enough time has elapsed
				boolean enoughTime;
				
				
				
				// If both are 0, the no interrupts have occurred yet.
				// Use the original State to tell what edge just occurred
				if(riseTime == 0 && fallTime == 0)
					isRising = !param.originalState;
				
				// If they aren't zero, then we have tracked interrupts.
				// Whichever edge has the older (smaller) timestamp just occurred
				else
				{
					isRising = fallTime > riseTime;
				}
				
				// See if enough time has elapsed since the last interrupt
				enoughTime = currTime - Math.max(riseTime, fallTime) > param.debounceTime;
				
				// See if the edge is correct
				correctEdge = (param.onRising && isRising) || (param.onFalling && !isRising);
				
				
				
				
				// If the edge is right and enough time has elapsed, run the handler command
				if(enoughTime && correctEdge)
				{	
					// run the command
					Command cmdInst = null;
					try {
						cmdInst = param.type.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
					
					if(cmdInst != null)
						cmdInst.start();
				}
			}
		};
				
		// set up the handler
		this.requestInterrupts(ISR);
		
		// We want to catch both edges, in order to debounce properly
		this.setUpSourceEdge(true, true);
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


















