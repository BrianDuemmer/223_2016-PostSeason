package org.usfirst.frc.team223.AdvancedX;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Provides a simple, easy method to pass multiple arguments into an
 * InterruptableLimit ISR
 * @author Duemmer
 *
 */
public class InterruptableLimitArg {
	public Class<? extends Command> type;
	public double lastHitTime;
	
	InterruptableLimitArg(Class<? extends Command> type, double lastHitTime)
	{
		this.type = type;
		this.lastHitTime = lastHitTime;
	}
}
