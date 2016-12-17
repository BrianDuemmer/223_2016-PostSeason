package org.usfirst.frc.team223.robot;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.usfirst.frc.team223.AdvancedX.LoggerUtil.FilteredConsoleAppender;
import org.usfirst.frc.team223.AdvancedX.LoggerUtil.FilteredSocketAppender;
import org.usfirst.frc.team223.AdvancedX.LoggerUtil.MyFileAppender;

import net.sf.microlog.core.Level;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import net.sf.microlog.core.format.PatternFormatter;




/**
 * Team class for logging robot data. Configure this as needed.
 * @author Brian Duemmer
 *
 */
public class RoboLogger {

	// Format string for all loggers
	private static String fmtString = "%d{HH:mm:ss:SSS} [%P] [%c{1}] %m\r\n";
	
	// Set the logging directory
	private static String logDir = "/media/sda1/logging";
	
	// Appender stuff
	private static PatternFormatter formPattern;
	
	private static FilteredConsoleAppender consoleAppender;
	private static Level consoleLevel = Level.WARN;
	
	private static MyFileAppender fileAppender;
	private static Level fileLevel = Level.TRACE;
	
	private static FilteredSocketAppender socketAppender;
	private static Level socketLevel = Level.TRACE;
	private static int port = 5801;
	
	
	
	
	
	public void init() 
	{
		// make sure the logging dir exists
		mkdirIfNeeded(logDir);
		
		// initialize the appenders
		initAppenders();
		
		// Initialize the logger
		Logger log = LoggerFactory.getLogger(RoboLogger.class);
		configLogger(log);

	}
	
	
	
	
	
	public Logger getLogger(String name)
	{
		// Initialize the logger
		Logger log = LoggerFactory.getLogger(name);
		configLogger(log);
		
		return log;
	}
	
	
	
	
	private Logger configLogger(Logger log)
	{
		// set the root level. Set to trace so that the appenders can take control
		// of what gets logged and what doesn't
		log.setLevel(Level.TRACE);
		
		// Add the appenders
		log.addAppender(consoleAppender);
		log.addAppender(fileAppender);
		log.addAppender(socketAppender);
		
		
		// return the finished logger
		return log;
	}
	
	
	
	
	private void initAppenders()
	{
		// Setup the pattern formatter to the proper pattern
		formPattern = new PatternFormatter();
		formPattern.setPattern(fmtString);
		
		// Setup console appender and add it to the logger
		consoleAppender = new FilteredConsoleAppender();
		consoleAppender.setFormatter(formPattern);
		consoleAppender.setLevel(consoleLevel);
		
		// Setup the socket appender
		socketAppender = new FilteredSocketAppender(port);
		socketAppender.setFormatter(formPattern);
		socketAppender.setMinLevel(socketLevel);
		socketAppender.open();
		
		
		
		// FileAppender
		
		// initialize the calendar to the date
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		// get the date components
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		// format the subdirectory name
		String dirName = year +"-"+ month +"-" + day;
		
		// get the time components
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		
		// format the log file name
		String logName = hour +"h-"+ minute +"m-"+ second + "s_roboLog.log";
		
		
		// make sure the logging subdirectory exists
		mkdirIfNeeded(logDir +"/"+ dirName);
		
		// full path to the log file
		String fullLogPath = logDir +"/"+ dirName +"/"+ logName;
		
		// Initialize the FileAppender
		fileAppender = new MyFileAppender();
		fileAppender.setFormatter(formPattern);
		fileAppender.setFilePath(fullLogPath);
		fileAppender.setLevel(fileLevel);
		
		
		try 
		{
			fileAppender.open();
		} catch (IOException e) 
		{
//			e.printStackTrace();
		}
		

	}
	
	
	
	private static void mkdirIfNeeded(String directoryName)
	{
		File theDir = new File(directoryName); 
		if (!theDir.exists())
		    theDir.mkdirs();
	}

}












