package org.usfirst.frc.team223.AdvancedX.robotParser;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class serves as a parser for GXML configuration files saved by the LabVIEW dashboard.
 * 
 * @author Brian Duemmer
 *
 */
public class GXMLparser 
{
	// References for the active document and XPath engine
	private Document doc;
	private XPath xpath;
	
	// Types that can be parsed in GXML
	public enum BASIC_TYPE
	{
		DOUBLE,
		INT,
		STRING,
		BOOL,
		ENUM
	}
	
	
	
	
	
	/**
	 * Creates a new instance of of the parser.
	 * 
	 * <i> NOTE:</i> There should only be one GXMLparser for
	 * each XML file
	 * @param path the path to the target XML file
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public GXMLparser(String path) throws SAXException, IOException, ParserConfigurationException
	{
		// initialize the document
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(new File(path));
		
		//initialize xpath
		XPathFactory xpf = XPathFactory.newInstance();
		xpath = xpf.newXPath();
	}
	
	
	
	/**
	 * Print a string out. May eventually also print to a file or something
	 * @param str
	 */
	private void printString(String str)
	{
		System.out.println(str);
	}
	
	
	
	
	/**
	 * Parses a single key out of the xml file, 
	 * @return an {@link Object} that contains the key data. Cast it to a double, int, etc. as needed.
	 * @throws XPathExpressionException 
	 */
	public Object getKeyByPath(String path, BASIC_TYPE type) throws XPathExpressionException
	{
		Object ret = null;
		String data;
		
		XPathExpression expr = xpath.compile("GXML_Root/" + path);
		Node key = (Node)expr.evaluate(doc, XPathConstants.NODE);
		
		data = key.getTextContent();
		
		if(type == BASIC_TYPE.BOOL)
			ret = new Boolean(data);
		
		if(type == BASIC_TYPE.INT)
			ret = new Integer(data);
		
		if(type == BASIC_TYPE.DOUBLE)
			ret = new Double(data);
		
		if(type == BASIC_TYPE.STRING)
			ret = data;
		
		if(type == BASIC_TYPE.ENUM)
		{
			int val = new Integer(data);
			ret = new EnumPair(key.getAttributes().getNamedItem("sel").getTextContent(), val);
		}
		
		return ret;
	}
	
	
	
	
	/**
	 * Gets an atomic element value given the direct parent and the name of the element.

	 * @return An Object containing the data found by the search, or null if not found.
	 * Add a cast statement to cast it to whatever type that was parsed
	 * 
	 */
	public Object getElementByName(String name, Node parent, BASIC_TYPE type)
	{
		// Child Node
		Node child = parent.getFirstChild();
		
		// Type value
		String typeVal = null;
		
		// Data value
		String dataVal = null;
		
		// Return value
		Object ret;
		
		do
		{
			// if it is an element node, and the name is correct, proceed
			if(child.getNodeType() == Node.ELEMENT_NODE && name.equals(child.getNodeName()))
			{
				// Get the type and break out of the loop
				typeVal = child.getAttributes().getNamedItem("type").getTextContent();
				break;
			}
			
			child = child.getNextSibling();
		}   
		while(child != null);
		
		// proceed only if the type attribute was found on a matching element
		if(typeVal != null)
		{
			// get the data
			dataVal = child.getTextContent();
			
			// Parse it as the proper value
			if(typeVal.equals("DBL") || typeVal.equals("SGL") || typeVal.equals("EXT") && type == BASIC_TYPE.DOUBLE)
			{
				ret = new Double(dataVal);
				return ret;
			}
			
			
			if	(	typeVal.equals("U8") || 
					typeVal.equals("U16") || 
					typeVal.equals("U32") || 
					typeVal.equals("U64") || 
					typeVal.equals("I8") || 
					typeVal.equals("I16") || 
					typeVal.equals("I32") || 
					typeVal.equals("I64") &&
					type == BASIC_TYPE.INT
				)
			{
				ret = new Integer(dataVal);
				return ret;
			}
			
			
			if(typeVal.equals("String") || typeVal.equals("Path") && type == BASIC_TYPE.STRING)
			{
				return dataVal;
			}
			
			
			if(typeVal.equals("Bool") && type == BASIC_TYPE.BOOL)
			{
				ret = new Boolean(dataVal);
				return ret;
			}
			
			
			if(typeVal.equals("Enum U8") || typeVal.equals("Enum U16") || typeVal.equals("Enum U32") && type == BASIC_TYPE.ENUM)
			{
				String sel = child.getAttributes().getNamedItem("sel").getTextContent();
				ret = new EnumPair(sel, new Integer(dataVal));
				return ret;
			}
		}
		
		// if nothing is found, return the default value for the specified type
		if(type == BASIC_TYPE.DOUBLE)
			return new Double(0);
		
		if(type == BASIC_TYPE.BOOL)
			return new Boolean(false);
		
		if(type == BASIC_TYPE.ENUM)
			return new EnumPair("", -1);
		
		if(type == BASIC_TYPE.INT)
			return new Integer(0);
		
		if(type == BASIC_TYPE.STRING)
			return "";
		
		//unreachable statement to satisfy compiler
		return null;
		
	}
	
	
	
	
	
	/**
	 * Parses a PID loop from the document, and returns a PIDData object containing the data
	 * @param path the path to the PID Element in the document, without the root "GXML_Root" tag
	 * @return a data element containing the PID data
	 * @throws XPathExpressionException 
	 */
	public PIDData parsePID(String path) throws XPathExpressionException
	{
		// PIDData element
		PIDData data = new PIDData();
		
		// Expression to find the PID node
		XPathExpression pidExp;
		
		//PID node
		Node pidNode;
		

		pidExp = xpath.compile("GXML_Root/" + path);
		pidNode = (Node) pidExp.evaluate(doc, XPathConstants.NODE);

		
		// Make sure that the node is found
		if(pidNode == null)
		{
			printString("Failed to find a PID with path \"" + path + "\"");
			return data;
		}
		
		// parse out the elements
		data.kp = (Double)getElementByName("kp", pidNode, BASIC_TYPE.DOUBLE);
		data.ki = (Double)getElementByName("ki", pidNode, BASIC_TYPE.DOUBLE);
		data.kd = (Double)getElementByName("kd", pidNode, BASIC_TYPE.DOUBLE);
		data.kf = (Double)getElementByName("kf", pidNode, BASIC_TYPE.DOUBLE);
		data.period = (Double)getElementByName("period", pidNode, BASIC_TYPE.DOUBLE);
		data.tolerance = (Double)getElementByName("tolerance", pidNode, BASIC_TYPE.DOUBLE);
		
		// return the parsed values
		return data;
	}
	
	
	
	
	/**
	 * Parses a motor from the document, and returns a MotorData object containing the data
	 * @param path the path to the motor Element in the document, without the root "GXML_Root" tag
	 * @return a data element containing the motor data
	 * @throws XPathExpressionException 
	 */
	public MotorData parseMotor(String path) throws XPathExpressionException
	{
		// MotorData element
		MotorData data = new MotorData();
		
		// Expression to find the motor node
		XPathExpression motorExp;
		
		//motor node
		Node motorNode;
		

		motorExp = xpath.compile("GXML_Root/" + path);
		motorNode = (Node) motorExp.evaluate(doc, XPathConstants.NODE);

		
		// Make sure that the node is found
		if(motorNode == null)
		{
			printString("Failed to find a motor with path \"" + path + "\"");
			return data;
		}
		
		// parse out the elements
		data.brake = (Boolean)getElementByName("brake", motorNode, BASIC_TYPE.BOOL);
		data.id = (Integer)getElementByName("id", motorNode, BASIC_TYPE.INT);
		data.invert = (Boolean)getElementByName("invert", motorNode, BASIC_TYPE.BOOL);
		data.maxOut = (Double)getElementByName("maxOut", motorNode, BASIC_TYPE.DOUBLE);
		data.type = (EnumPair)getElementByName("type", motorNode, BASIC_TYPE.ENUM);
		
		// return the parsed values
		return data;
	}
	
	
	
	
	
	/**
	 * Parses an encoder from the document, and returns an EncoderData object containing the data
	 * @param path the path to the encoder Element in the document, without the root "GXML_Root" tag
	 * @return a data element containing the encoder data
	 * @throws XPathExpressionException 
	 */
	public EncoderData parseEncoder(String path) throws XPathExpressionException
	{
		// MotorData element
		EncoderData data = new EncoderData();
		
		// Expression to find the motor node
		XPathExpression encoderExp;
		
		//motor node
		Node encoderNode;
		
		encoderExp = xpath.compile("GXML_Root/" + path);
		encoderNode = (Node) encoderExp.evaluate(doc, XPathConstants.NODE);
		
		// Make sure that the node is found
		if(encoderNode == null)
		{
			printString("Failed to find an encoder with path \"" + path + "\"");
			return data;
		}
		
		// parse out the elements
		data.Achannel = (Integer)getElementByName("Achannel", encoderNode, BASIC_TYPE.INT);
		data.Bchannel = (Integer)getElementByName("Bchannel", encoderNode, BASIC_TYPE.INT);
		data.IDXchannel = (Integer)getElementByName("IDXchannel", encoderNode, BASIC_TYPE.INT);
		data.invert = (Boolean)getElementByName("invert", encoderNode, BASIC_TYPE.BOOL);
		data.distPerCount = (Double)getElementByName("distPerCount", encoderNode, BASIC_TYPE.DOUBLE);
		
		// return the parsed values
		return data;
	}
	
	
	
	
	
	
	/**
	 * Parses a limit switch from the document, and returns a LimitData object containing the data
	 * @param path the path to the limit switch Element in the document, without the root "GXML_Root" tag
	 * @return a data element containing the limit data
	 * @throws XPathExpressionException 
	 */
	public LimitData parseLimit(String path) throws XPathExpressionException
	{
		// MotorData element
		LimitData data = new LimitData();
		
		// Expression to find the motor node
		XPathExpression limitExp;
		
		//motor node
		Node limitNode;
		

		limitExp = xpath.compile("GXML_Root/" + path);
		limitNode = (Node) limitExp.evaluate(doc, XPathConstants.NODE);

		
		// Make sure that the node is found
		if(limitNode == null)
		{
			printString("Failed to find an encoder with path \"" + path + "\"");
			return data;
		}
		
		// parse out the elements
		data.debounceTime = (Double)getElementByName("debounceTime", limitNode, BASIC_TYPE.DOUBLE);
		data.id= (Integer)getElementByName("id", limitNode, BASIC_TYPE.INT);
		data.interruptEdge = (EnumPair)getElementByName("interruptEdge", limitNode, BASIC_TYPE.ENUM);
		data.normallyOpen = (Boolean)getElementByName("normallyOpen", limitNode, BASIC_TYPE.BOOL);
		
		// return the parsed values
		return data;
	}
	
	
	
	
	/**
	 * Parses a limit switch from the document, and returns a LimitData object containing the data
	 * @param path the path to the limit switch Element in the document, without the root "GXML_Root" tag
	 * @return a data element containing the limit data
	 * @throws XPathExpressionException 
	 */
	public DriveSideData parseDriveSide(String path) throws XPathExpressionException
	{
		// MotorData element
		DriveSideData data = new DriveSideData();
		
		// Expression to find the driveSide node
		XPathExpression driveSideExp;
		
		//motor driveSide
		Node driveSideNode;
		
		
		driveSideExp = xpath.compile("GXML_Root/" + path);
		driveSideNode = (Node) driveSideExp.evaluate(doc, XPathConstants.NODE);

	
	
		// Make sure that the node is found
		if(driveSideNode == null)
		{
			printString("Failed to find a driveSide with path \"" + path + "\"");
			return data;
		}
		
		
		
		// Create an expression to find all of the motors
		XPathExpression motorsExp;
		NodeList motors;
		
		motorsExp = xpath.compile("GXML_Root/" + path + "/Motors/motor");
		motors = (NodeList)motorsExp.evaluate(doc, XPathConstants.NODESET);
		
		
		// iterate through the motors and add them to the driveSide
		for(int i = 1; i <= motors.getLength(); i++)
			data.motors.add(parseMotor(path + "/Motors/motor[" + i + "]"));
		
		
		// populate the data variable
		data.pid = parsePID(path + "/PID");
		data.encoder = parseEncoder(path + "/encoder");
		
		
		// return the parsed values
		return data;
		
	}
	
	
	
	
	
	/**
	 * Parses a Tank Cascade Controller from the document, and returns a TankCascadeData object containing the data
	 * @param path the path to the Tank Cascade Controller Element in the document, without the root "GXML_Root" tag
	 * @return a data element containing the Tank Cascade Controller data
	 * @throws XPathExpressionException 
	 */
	public TankCascadeData parseTankCascade(String path) throws XPathExpressionException
	{
		// TankCascadeData element
		TankCascadeData data = new TankCascadeData();
		
		// Expression to find the TankCascadeData node
		XPathExpression tankCascadeExp;
		
		
		Node tankCascadeNode;
		
		tankCascadeExp = xpath.compile("GXML_Root/" + path);
		tankCascadeNode = (Node) tankCascadeExp.evaluate(doc, XPathConstants.NODE);

	
	
		// Make sure that the node is found
		if(tankCascadeNode == null)
		{
			printString("Failed to find a driveSide with path \"" + path + "\"");
			return data;
		}
		
		
		// parse the individual elements
		data.leftData = parseDriveSide(path + "/leftSide");
		data.rightData = parseDriveSide(path + "/rightSide");
		data.anglePID = parsePID(path + "/anglePID");
		data.distancePID = parsePID(path + "/distancePID");
		data.wheelBaseWidth = (Double)getElementByName("wheelBaseWidth", tankCascadeNode, BASIC_TYPE.DOUBLE);
		
		// return the parsed values
		return data;
		
	}
	
	
	
	/**
	 * Parses a setpoint from a setpoint list specified by <code>path</code> with the name <code>name</code>
	 * @return the value of the specified setpoint
	 * @throws XPathExpressionException 
	 */
	public double parseSetpoint(String path, String name) throws XPathExpressionException
	{
		// get the proper value node
		// expression: GXML_Root/<path>/Setpoint[name='<name>']/value
		XPathExpression expr = xpath.compile("GXML_Root/" + path + "/Setpoint[name=\'" + name + "\']/value");
		Node setpoint = (Node)expr.evaluate(doc, XPathConstants.NODE);
		
		// parse out the content of the value node, and return
		double ret = new Double(setpoint.getTextContent());
		return ret;
	}
}

















