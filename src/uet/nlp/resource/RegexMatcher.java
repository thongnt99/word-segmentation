package uet.nlp.resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RegexMatcher {	
	List<Regex> regexs;
	public RegexMatcher(String regexPath){		
		regexs = new ArrayList<Regex>();
		try {
			loadRegexs(regexPath);	
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public void loadRegexs(String regexPath) throws ParserConfigurationException, SAXException, IOException{
		File xmlFile = new File(regexPath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder bBuilder = dbFactory.newDocumentBuilder();
		Document doc = bBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("group");
		for (int i=0; i < nList.getLength(); i++){
			
			Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE){
				Element regexGroup = (Element)nNode;
				String type = regexGroup.getAttribute("type");
				NodeList regexSubList = regexGroup.getElementsByTagName("re");
				for (int j=0; j < regexSubList.getLength(); j++){
					Node regexNode = regexSubList.item(j);	
					Regex regex = new Regex(regexNode.getTextContent(),type);
					
					regexs.add(regex);
				}
							
			}
		}
	}
	public Regex getMatchedRegex(String input){
		Pattern pattern;
		Matcher matcher;
		for (Regex regex : regexs){
			pattern = Pattern.compile(regex.getString());
			matcher = pattern.matcher(input);
			if (matcher.find()) return regex;
		}
		return null;
	}
}
