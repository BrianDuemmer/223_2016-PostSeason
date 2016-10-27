package org.usfirst.frc.team223.AdvancedX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Provides a simple, easy POD class to pass multiple arguments into an
 * InterruptableLimit ISR
 * @author Duemmer
 *
 */
public class InterruptableLimitArg {
	public Class<? extends Command> type;
	public DigitalInput input;
	public boolean onRising;
	public boolean onFalling;
	public boolean originalState;
	public double debounceTime;
	
	
	/**
	 * Provides a simple, easy POD class to pass multiple arguments into an
	 * InterruptableLimit ISR
	 * @param type command type to run
	 * @param input reference to the digitalInput
	 * @param onRising Whether to fire on the rising edge
	 * @param onFalling Whether to fire on the falling edge
	 * @param originalState The original pin state
	 * @param debounceTime the time (in seconds) that a pin state must hold in order to be counted as an interrupt
	 */
	InterruptableLimitArg(Class<? extends Command> type, DigitalInput input, boolean onRising, boolean onFalling, boolean originalState, double debounceTime)
	{
		this.type = type;
		this.input = input;
		this.onRising = onRising;
		this.onFalling = onFalling;
		this.originalState = originalState;
		this.debounceTime = debounceTime;
	}
}
