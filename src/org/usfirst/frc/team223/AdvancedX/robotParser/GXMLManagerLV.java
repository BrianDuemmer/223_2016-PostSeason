package org.usfirst.frc.team223.AdvancedX.robotParser;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.usfirst.frc.team223.AdvancedX.LoggerUtil.RoboLogger;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLParser.BASIC_TYPE;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import net.sf.microlog.core.Logger;

/**
 * Manages the connection between the robot using the GXML configuration
 * framework and a LabVIEW ConigXML plugin bound to this device. Instances
 * of this class will automatically create a daemon thread that runs in 
 * the background, which will automatically communicate and handshake
 * with the dashboard.<P/>
 * 
 * An instance of this class must be created at the beginning of 
 * robot code. In addition, all instances of {@link GXMLParser} and
 * {@link GXMLAllocator} must be obtained using {@link #obtainParser}
 * and {@link #obtainAllocator}.
 * @author Brian Duemmer
 *
 */
public abstract class GXMLManagerLV implements Runnable
{
	// Active parser object, bound to the document
	protected GXMLParser parserRef;
	
	// name of the NetworkTables key name that will be used for handshaking
	protected String handshakeKey;
	
	// name of key that indicates a successful reload
	protected String successKey;
	
	// name of the key that reports the filename back to the dashboard
	protected String fileNameKey;
	
	// the object that this class will log information to
	protected Logger logger;
	
	// the table that will be used for the communication
	protected NetworkTable nt;
	
	// Boolean that is true only on the first run of the ScheduledExecutor, and false
	// all other times
	private boolean firstRun = true;
	
	// target filename
	protected String fileName;
	
	
	
	
	
	/**
	 * Utility class to allow us to set the threads created to run in the background as Daemon
	 * @author Brian Duemmer
	 *
	 */
	private class DaemonFactory implements ThreadFactory
	{
		@Override
		public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
		}
	}
	
	
	
	
	
	
	/**
	 * Create a new instance of {@link GXMLManagerLV} that is bound to the configuration
	 * file at <code>filePath</code>
	 */
	public GXMLManagerLV(String fileName, RoboLogger roboLogger, NetworkTable nt)
	{
		// update instance variables
		this.fileName = fileName;
		
		// get the namespace
		String namespace = getNamespace(fileName);
		
		// initialize the logger
		this.logger = roboLogger.getLogger("GXMLManager_" + namespace);
		
		// construct the keys
		this.handshakeKey = "CONFIGXML_" + namespace + "_RELOAD";
		this.fileNameKey = "CONFIGXML_" + namespace + "_FILENAME";
		this.successKey = "CONFIGXML_" + namespace + "_SUCCESS";
		
		this.nt = nt;
	}
	
	
	
	
	
	
	/**
	 * Obtains the namespace of the configuration setup based on the path.
	 * This is the name of the file, without an extension.
	 * 
	 * @param filePath the path to the configuration file
	 * @return the namespace 
	 */
	protected String getNamespace(String filePath)
	{
		String  namespace;
		
		// get the index of the last "/" and last "."
		int startIdx = filePath.lastIndexOf("/")+1;
		int endIdx = filePath.lastIndexOf(".");
		
		// get the filename based on that information
		if(startIdx > 0 && endIdx > 0)
			namespace = filePath.substring(startIdx, endIdx);
		else if(startIdx > 0)
			namespace = filePath.substring(startIdx);
		else
			namespace = filePath;
		
		return namespace;
	}
	

	
	
	
	
	/**
	 * Obtains the current instance of the {@link GXMLParser}. This will automatically
	 * attempt to allocate the document if it has not been already.
	 */
	public GXMLParser obtainParser(Logger logger)
	{
		// if the parserRef is null, allocate a new instance of it. If it is not,
		// use the existing parserRef
		if(this.parserRef == null)
			this.parserRef = new GXMLParser(fileName, logger);
		
		return this.parserRef;
	}
	
	
	
	
	/**
	 * Obtains a new instance of a GXMLAllocator object
	 */
	public GXMLAllocator obtainAllocator(Logger logger)
	{
		return new GXMLAllocator(logger);
	}
	
	
	
	
	/**
	 * Starts the {@link GXMLManagerLV}. This will run {@link #reload()} all config data initially,
	 * and will subsequently run {@link #reload()} whenever the boolean NT entry specified for
	 * handshaking is set to true.
	 * @param updateRate The period, in ms, that the handshake key is checked for updates
	 */
	public void start(int updateRate)
	{
		this.logger.info("Starting GXMLManagerLV Background thread with update rate of " + updateRate + " ms");
		ThreadFactory fact = new DaemonFactory();
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor(fact);
		exec.scheduleWithFixedDelay(this, updateRate, updateRate, TimeUnit.MILLISECONDS);
	}
	
	
	
	
	
	
	/**
	 * periodically check if the Dashboard has requested
	 * a config reload. If it did, call the {@link #reload()} method.
	 * Update NT keys as necessary
	 */
	@Override
	public void run()
	{	
		// Attempt to perform an update check, and potentially an update. Be sure to catch any exceptions
		// here, as they can stop the whole program
		try
		{
			// see if the Dashboard has requested an update, or this is the first call. Reload if it did.
			if(nt.getBoolean(handshakeKey, false) || this.firstRun)
			{
				// if not the first run, an update from the dashboard was requested
				if(!this.firstRun)
						logger.info("Reload request recieved from Dashboard. Attempting Startup cycle...");
				else
					logger.info("Attempting initial startup cycle...");
					
				// Set the success flag to the return value from reload()
				nt.putBoolean(successKey, reload());
				
				// Set the handshake flag to false
				nt.putBoolean(handshakeKey, false);
				
				// update the filename key to the value of the FILE_NAME attribute of the document.
				nt.putString(fileNameKey, (String)this.obtainParser(logger).getKeyByPath("FILE_NAME", BASIC_TYPE.STRING));
			}
			
			// make sure that firstRun will always be false after this
			this.firstRun = false;
			
			
		} catch(Exception e)
		{
			logger.error("Exception occured during Robot startup cycle. DETAILS: ", e);
		}
	}
	
	
	
	
	
	/**
	 * This method must be defined by the user. It specifies what to do in order
	 * to reload all of the robot's data freshly from the configuration file.
	 * This will be called automatically by the {@link GXMLManagerLV}.
	 * 
	 * @return true if the data was reloaded successfully
	 */
	public abstract boolean reload();
}
























