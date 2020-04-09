package SWRE.ruleGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/*
 * INSTEAD OF SIMPLE RULES AND RULE NODE IN XML, MENTION A PREFIX IN CONFIG
 * AND APPEND THAT HERE IN FRONT OF ALL THE NODE NAMES
 */

public class RuleBox {

	private static String xmlFilename;
	private static File xmlFile;

	public RuleBox() {
		xmlFilename = null;
		xmlFile = null;
	}
	
	public void init() throws Exception {
		
		// Reading rules file of XML from dbconfig
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dbconfig.properties");
		Properties property = new Properties();
		property.load(inputStream);
		xmlFilename = (String)property.get("RULE_STORE");
		System.out.println(xmlFilename);
		try {
			xmlFile = new File(xmlFilename);
			boolean flag = xmlFile.createNewFile();
			System.out.println(flag);
			if(flag) {
				FileWriter initXML = new FileWriter(xmlFilename);
				initXML.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Rules>\n</Rules>");
				initXML.close();
			}
			else {
				System.out.print("File Exists");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void addRule(String Antecedent[], String Consequent[]) throws SAXException, IOException, TransformerConfigurationException {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document document = documentBuilder.parse(xmlFilename);
		//Fetches the root element
	    Element rootElement = document.getDocumentElement();
	    
	    /*
	     * Type 0: A subject
	     * Type 1: A predicate
	     * Type 2: An object
	     * Type 3: A connector
	     */
	    int type = 0;
	    // Consequent length always 3
	    int antecedentLength = Antecedent.length;
	    
	    Element rule = document.createElement("Rule");
		rootElement.appendChild(rule);
		Element antecedent = document.createElement("Antecendent");
		Element consequent = document.createElement("Consequent");
	    
	    for(int i = 0; i < antecedentLength; i++) {
	    	
	    	switch(type) {
	    		case 0:
	    			Element subject = document.createElement("Subject");
	    			subject.appendChild(document.createTextNode(Antecedent[i]));
	    			antecedent.appendChild(subject);
	    			type = 1;
	    			break;
	    		case 1:
	    			Element predicate = document.createElement("Predicate");
	    			predicate.appendChild(document.createTextNode(Antecedent[i]));
	    			antecedent.appendChild(predicate);
	    			type = 2;
	    			break;
	    		case 2:
	    			Element object = document.createElement("Object");
	    			object.appendChild(document.createTextNode(Antecedent[i]));
	    			antecedent.appendChild(object);
	    			type = 3;
	    			break;
	    		case 3:
	    			Element connector = document.createElement("Connector");
	    			connector.appendChild(document.createTextNode(Antecedent[i]));
	    			antecedent.appendChild(connector);
	    			type = 0;
	    			break;
	    	}
	    }
	    
	    Element subject = document.createElement("Subject");
		subject.appendChild(document.createTextNode(Consequent[0]));
		consequent.appendChild(subject);
		Element predicate = document.createElement("Predicate");
		predicate.appendChild(document.createTextNode(Consequent[1]));
		consequent.appendChild(predicate);
		Element object = document.createElement("Object");
		object.appendChild(document.createTextNode(Consequent[2]));
		consequent.appendChild(object);
	    
	    rule.appendChild(antecedent);
	    rule.appendChild(consequent);
	    
	    DOMSource source = new DOMSource(document);
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    StreamResult result = new StreamResult(xmlFilename);
	    try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<ArrayList<String>> getRules() throws SAXException, IOException{
		
		ArrayList<ArrayList<String>> ruleList = new ArrayList<ArrayList<String>>();
		
		xmlFile = new File(xmlFilename);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("Rule");
        int len = nList.getLength();
        
        for( int itr = 0; itr < len; itr++) {
        	
        	ArrayList<String> rule = new ArrayList<String>();
        	
        	Node nNode = nList.item(itr);
        	Element nElement = (Element) nNode;
        	
        	Node antecedent = nElement.getElementsByTagName("Antecendent").item(0);
    	    Node consequent = nElement.getElementsByTagName("Consequent").item(0);
    	    
    	    Element eElement = (Element) antecedent; 
    	    Element cElement = (Element) consequent;
    	    
    	    NodeList subject = eElement.getElementsByTagName("Subject");
    	    NodeList predicate = eElement.getElementsByTagName("Predicate");
    	    NodeList object = eElement.getElementsByTagName("Object");
    	    NodeList connector = eElement.getElementsByTagName("Connector");
    	    
    	    int subjectLength = subject.getLength();
    	    int predicateLength = predicate.getLength();
    	    int objectLength = object.getLength();
    	    int connectorLength = connector.getLength();
    	    int tempLength = subjectLength;

    	    int loop = 0;
    	    while(loop<tempLength) {

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    rule.add(eElement.getElementsByTagName("Subject").item(loop).getTextContent());

                    if (loop < predicateLength)
                        rule.add(eElement.getElementsByTagName("Predicate").item(loop).getTextContent());
                    if (loop < objectLength)
                        rule.add(eElement.getElementsByTagName("Object").item(loop).getTextContent());
                    if (loop < connectorLength)
                        rule.add(eElement.getElementsByTagName("Connector").item(loop).getTextContent());
                    loop++;
                }
            }
    	    rule.add(cElement.getElementsByTagName("Subject").item(0).getTextContent());
    	    rule.add(cElement.getElementsByTagName("Predicate").item(0).getTextContent());
    	    rule.add(cElement.getElementsByTagName("Object").item(0).getTextContent());
    	    ruleList.add(rule);
        }
		return ruleList;
	}
}