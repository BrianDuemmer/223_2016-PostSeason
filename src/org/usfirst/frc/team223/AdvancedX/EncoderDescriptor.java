package org.usfirst.frc.team223.AdvancedX;


/**
 * Class to describe an encoder on the robot
 * @author Duemmer
 *
 */
public class EncoderDescriptor {

	// Instance variables
	public String name;
	public boolean isCAN;
	public boolean invert;
	public int idA;
	public int idB;
	public int idZ;
	public int cpr;
	
	/** Initializes the encoder object. 
	 * 
	 * @param name the name of the encoder
	 * @param isCAN whether the encoder is on an SRX or the RIO
	 * @param idA A channel on the RIO, or the CANid
	 * @param idB B channel on the RIO, unused on the SRX. Set to -1 to use 1x decoding.
	 * @param idZ Index channel on the RIO, unused on the SRX. Set to -1 to disable the index channel.
	 * @param invert If true, inverts the data coming from the encoder
	 * @param cpr counts per revolution on the encoder
	 */
	EncoderDescriptor(String name, boolean isCAN, int idA, int idB, int idZ, boolean invert, int cpr)
	{
		this.isCAN = isCAN;
		this.invert = invert;
		this.idA = idA;
		this. idB = idB;
		this.idZ = idZ;
		this.cpr = cpr;
		this.name = name;
	}
}
