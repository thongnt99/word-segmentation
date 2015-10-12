package uet.nlp.wordsegmentation;

import java.util.ArrayList;
import java.util.List;

import uet.nlp.constant.Paths;
import uet.nlp.resource.Regex;
import uet.nlp.resource.RegexMatcher;

public class Segmenter {
	public RegexMatcher regexMatcher;
	public Segmenter(){
		this.regexMatcher = new RegexMatcher(Paths.REGEX_PATH);
	}
	public List<String> segment(String input){
		List<String> tokens = new ArrayList<String>();
		Regex matchingRes;
		int matchedEnd;
		String matchedPart;
		while (input.length() > 0){
			matchingRes = regexMatcher.getLongestMatchedRegex(input);			
			matchedEnd = matchingRes.getlgstAclen();
			matchedPart = input.substring(0, matchedEnd).trim();
			input = input.substring(matchedEnd).trim();
			tokens.add(matchedPart);					
		}
		return tokens;
	}
}
