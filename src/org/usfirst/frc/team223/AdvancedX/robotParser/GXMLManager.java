package org.usfirst.frc.team223.AdvancedX.robotParser;


import org.usfirst.frc.team223.AdvancedX.LoggerUtil.RoboLogger;
import org.usfirst.frc.team223.AdvancedX.robotParser.GXMLparser.BASIC_TYPE;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
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
 * robot code. In addition, all instances of {@link GXMLparser} and
 * {@link GXMLAllocator} must be obtained using {@link #obtainParser}
 * and {@link #obtainAllocator}.
 * @author Brian Duemmer
 *
 */
public abstract class GXMLManager implements Runnable
{
	// Active parser object, bound to the document
	protected GXMLparser parserRef;
	
	// name of the NetworkTables key name that will be used for handshaking
	protected String handshakeKey;
	
	// name of key that indicates a successful reload
	protected String successKey;
	
	// name of the key that reports the filename back to the dashboard
	protected String fileNameKey;
	
	// the object that this class will log information to
	protected Logger logger;
	protected RoboLogger roboLogger;
	
	// the table that will be used for the communication
	protected NetworkTable nt;
	
	// Boolean that is true only on the first run of the ScheduledExecutor, and false
	// all other times
	private boolean firstRun = true;
	
	// target filename
	protected String fileName;
	
	// target namespace
	protected String namespace = "";
	
	// thread that runs the manager check
	protected Thread manager;
	
	// ms between reload checks
	protected int updateRate;
	
	
	
	
	
	
	/**
	 * Create a new instance of {@link GXMLManager} that is bound to the configuration
	 * file at <code>filePath</code>
	 */
	public GXMLManager(String fileName, RoboLogger roboLogger, NetworkTable nt)
	{
		// update instance variables
		this.fileName = fileName;
		
		// get the namespace
		this.namespace = getNamespace(fileName);
		
		// initialize the logger
		this.roboLogger = roboLogger;
		this.logger = roboLogger.getLogger("GXMLManager_" + namespace);
		
		// construct the keys
		this.handshakeKey = "CONFIGXML_" + this.namespace + "_RELOAD";
		this.fileNameKey = "CONFIGXML_" + this.namespace + "_FILENAME";
		this.successKey = "CONFIGXML_" + this.namespace + "_SUCCESS";
		
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
		String namespace;
		
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
	 * Obtains the current instance of the {@link GXMLparser}. This will automatically
	 * attempt to allocate the document if it has not been already.
	 */
	public GXMLparser obtainParser()
	{
		// if the parserRef is null, allocate a new instance of it. If it is not,
		// use the existing parserRef
		if(this.parserRef == null)
			this.parserRef = new GXMLparser(fileName, roboLogger);
		
		return this.parserRef;
	}
	
	
	
	
	/**
	 * Obtains a new instance of a GXMLAllocator object
	 */
	public GXMLAllocator obtainAllocator(String name)
	{
		return new GXMLAllocator(roboLogger, name);
	}
	
	
	
	
	/**
	 * Starts the {@link GXMLManager}. This will run {@link #reload()} all config data initially,
	 * and will subsequently run {@link #reload()} whenever the boolean NT entry specified for
	 * handshaking is set to true.
	 * @param updateRate The period, in ms, that the handshake key is checked for updates
	 */
	public void start(int updateRate)
	{
		this.logger.info("Starting GXMLManager Background thread with update rate of " + updateRate + " ms");
		
		// create a thread to run this
		this.manager = new Thread(this);
		
		// setup the thread
		this.manager.setDaemon(true);
		this.manager.setName(this.namespace + "_MANAGER_THREAD");
		this.updateRate = updateRate;
		
		// start it
		this.manager.start();
	}
	
	
	
	
	
	
	/**
	 * periodically check if the Dashboard has requested
	 * a config reload. If it did, call the {@link #reload()} method.
	 * Update NT keys as necessary
	 */
	@Override
	public void run()
	{	
		// loop infinitely
		while(true)
		{
			// set to true initially so if something doesnt need to be run, it does not affect the outcome adversley
			boolean success = true;
			
			// see if the Dashboard has requested an update, or this is the first call. Reload if it did.
			if(nt.getBoolean(handshakeKey, false) || this.firstRun)
			{
				// Prevent access to the scheduler class everywhere else while we do this
				synchronized(Scheduler.class)
				{
					// kill any running commands
					Scheduler.getInstance().removeAll();
					
					// if not the first run, an update from the dashboard was requested
					if(!this.firstRun)
							logger.info("Reload request recieved from Dashboard. Attempting Startup cycle...");
					else
						logger.info("Attempting initial startup cycle...");
					
					// Keep trying to reload data until it is successful
					do
					{
						// only free() if not a first call, as the data wasn't loaded yet
						if(!this.firstRun)
						{
							logger.info("Attempting to free data...");
							try 
							{
								success = free();
								logger.info("Shutdown cycle finished with success status: " + success);
							} catch (Exception e) {
								logger.error("Error during shutdown cycle. DETAILS:", e);
								success = false;
							}
						} else 
							logger.info("This is the first call, so free() was not called");
						
						// load() all of the data
						try
						{
							logger.info("Attempting to load() data");
							
							// both load() and free() must be successful in order for the cycle to be considered successful
							success &= load();
						} catch(Exception e) {
							logger.error("Exception encountered during load()! DETAILS: ", e);
							success = false;
						}
						
						// Set the success flag to the return value from reload()
						nt.putBoolean(successKey, success);
						logger.info("Reload cycle finished with success status: " +success);
					} while(!success);
					
					// Set the handshake flag to false
					nt.putBoolean(handshakeKey, false);
					
					// update the filename key to the value of the FILE_NAME attribute of the document.
					nt.putString(fileNameKey, (String)this.obtainParser().getKeyByPath("FILE_NAME", BASIC_TYPE.STRING));
				}
			}
			
			// make sure that firstRun will always be false after this
			this.firstRun = false;
			
			// delay for the necessary time, converting from ms to seconds
			Timer.delay(((double)this.updateRate) / 1000.0);
		}
	}
	
	
	
	
	
	/**
	 * This method must be defined by the user. It specifies what to do in order
	 * to load all of the robot's data freshly from the configuration file.
	 * This will be called automatically by the {@link GXMLManager}.
	 * 
	 * @return true if the data was loaded successfully
	 */
	public abstract boolean load();
	
	
	
	
	/**
	 * This method must be defined by the user. It specifies what to do in order
	 * to free all of the robot's data freshly from the configuration file.
	 * This will be called automatically by the {@link GXMLManager}.
	 * 
	 * @return true if the data was freed successfully
	 */
	public abstract boolean free();






	public RoboLogger getRoboLogger() {
		return roboLogger;
	}
}
























