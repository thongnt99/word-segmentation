package uet.nlp.resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

	private ArrayList<Regex> regexes;
	private ArrayList<Pattern> patterns;

	public RegexMatcher(String regexPath) {
		regexes = new ArrayList<>();
		patterns = new ArrayList<>();
		try {
			loadRegexes(regexPath);
			makePatterns();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadRegexes(String regexPath)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder bBuilder = dbFactory.newDocumentBuilder();

		File xmlFile = new File(regexPath);
		Document doc = bBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("w");

		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			Element regexEl = (Element) node;
			String regexType = regexEl.getAttribute("msd");
			String regexCont = regexEl.getTextContent();
			Regex regex = new Regex(regexCont, regexType);
			regexes.add(regex);
		}
	}

	private void makePatterns() {
		for (int i = 0; i < regexes.size(); i++) {
			patterns.add(Pattern.compile(regexes.get(i).getString()));
		}
	}

	public Regex getLongestMatchedRegex(String input) {
		int longestLen = -1;
		Regex res = null;
		for (int i = 0; i < patterns.size(); i++) {
			Matcher matcher = patterns.get(i).matcher(input);
			if (matcher.lookingAt()) {
				int matchedLen = matcher.end();
				if (matchedLen > longestLen) {
					longestLen = matchedLen;
					res = regexes.get(i);
				}
			}
		}
		if (res != null)
			res.setLgstAcLen(longestLen);
		return res;
	}
}
