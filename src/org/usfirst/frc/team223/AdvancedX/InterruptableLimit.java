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
	 * @param handler Command to be run upon an interrupt. 
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
		
		// Create the arg object to pass to the ISR
		InterruptableLimitArg arg = new InterruptableLimitArg(handlerType, this, fireOnRising, fireOnFalling);
		
		/**
		 * Allocates a new instance of the InterrupHandlerFunction class. Overrides
		 * the overridableParameter and interruptFired methods in order to allow
		 * running of a command with type of handlerType.
		 */
		InterruptHandlerFunction<InterruptableLimitArg> ISR = new InterruptHandlerFunction<InterruptableLimitArg>()
		{
			private double lastHitTime = 0;
			private boolean prevState;
			
			// Override this method to give us a parameter to pass to the interruptFired routine
			@Override
			public InterruptableLimitArg overridableParameter()
			{
				prevState = arg.input.get();
				return arg;
			}
			
			
			// Override this method for the new InterruptHandlerFunction to do what we want
			@Override
			public void interruptFired(int interruptAssertedMask, InterruptableLimitArg param) 
			{
				// Current time
				double currTime = Timer.getFPGATimestamp();
				
				// did the proper edge occur?
				boolean correctEdge = prevState ? param.onFalling : param.onRising;
				
				// if true, enough time has elapsed to run again, and the edge is correct
				boolean canRun = currTime - lastHitTime > debounceTime && correctEdge;
				
				if(canRun)
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
				
				// update the lastHitTime
				lastHitTime = currTime;
				
				// Disable interrupts for a certain time
				param.input.disableInterrupts();
				Timer.delay(debounceTime);
				param.input.enableInterrupts();
			}
		};
				
		// set up the handler
		this.requestInterrupts(ISR);
		
		// Set the proper interrupt state
		this.setUpSourceEdge(fireOnRising, fireOnFalling);
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


















