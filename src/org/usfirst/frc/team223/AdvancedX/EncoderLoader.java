package org.usfirst.frc.team223.AdvancedX;


/**
 * Enum containing all encoder information. Each element is named after a specific encoder, and the
 * data values specify how the encoder is initialized
 * 
 * @param BooleanIsCAN whether the encoder is on an SRX or the RIO
 * @param IntIdA A channel on the RIO, or the CANid
 * @param IntIdB B channel on the RIO, unused on the SRX. Set to -1 to use 1x decoding.
 * @param IntIdZ Index channel on the RIO, unused on the SRX. Set to -1 to disable the index channel.
 * @param BooleanInvert If true, inverts the data coming from the encoder
 * @param IntCpr counts per revolution on the encoder
 * 
 * @author Duemmer
 *
 */
public class EncoderLoader {
	
	
	// Instance variables
	private final boolean isCAN;
	private final boolean invert;
	private final int idA;
	private final int idB;
	private final int idZ;
	private final int cpr;
	
	/** Initializes the encoder objects. 
	 * 
	 * @param isCAN whether the encoder is on an SRX or the RIO
	 * @param idA A channel on the RIO, or the CANid
	 * @param idB B channel on the RIO, unused on the SRX. Set to -1 to use 1x decoding.
	 * @param idZ Index channel on the RIO, unused on the SRX. Set to -1 to disable the index channel.
	 * @param invert If true, inverts the data coming from the encoder
	 * @param cpr counts per revolution on the encoder
	 */
	EncoderLoader(boolean isCAN, int idA, int idB, int idZ, boolean invert, int cpr)
	{
		this.isCAN = isCAN;
		this.invert = invert;
		this.idA = idA;
		this. idB = idB;
		this.idZ = idZ;
		this.cpr = cpr;
	}
}












