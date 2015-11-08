package uet.nlp.wordsegmentation;

import java.util.ArrayList;
import java.util.List;

import uet.nlp.constant.Paths;
import uet.nlp.lm.MyLM;
import uet.nlp.resource.Dictionary;
import uet.nlp.resource.Regex;
import uet.nlp.resource.RegexMatcher;
import uet.nlp.utils.StrUtils;

public class Segmenter {

	private RegexMatcher regexMatcher;
	private Dictionary vDict;
	private Dictionary nPrefix;
	private Dictionary firstNameDict;
	private Dictionary lastNameDict;
	private MyLM languageModel;	

	public Segmenter() {
		regexMatcher = new RegexMatcher(Paths.REGEX_PATH);
		vDict = Dictionary.getDictionary(Paths.VILEX_DICT_PATH);
		nPrefix = Dictionary.getDictionary(Paths.NAME_PREFIX);
		firstNameDict = Dictionary.getDictionary(Paths.FIRST_NAME);
		lastNameDict = Dictionary.getDictionary(Paths.LAST_NAME);
		languageModel = new MyLM();
	}

	private boolean isNameComponent(String str) {
		return (firstNameDict.contains(str) || lastNameDict.contains(str));
	}

	public List<String> segment(String input) {
		List<String> segs = new ArrayList<String>();
		Regex matchingRes;
		int matchedEnd;
		String matchedPart;
		while (input.length() > 0) {
			matchingRes = regexMatcher.getLongestMatchedRegex(input);
			matchedEnd = matchingRes.getlgstAclen();
			matchedPart = input.substring(0, matchedEnd).trim();
			input = input.substring(matchedEnd).trim();
			if (matchingRes.isPhrase()) {
				List<String> subSegs = segmentPhrase(matchedPart);
				segs.addAll(subSegs);
			} else if (matchingRes.isName()) {
				String firtsToken = StrUtils.tokenizeString(matchedPart).get(0);
				String secondToken = matchedPart.substring(firtsToken.length())
						.trim();
				if (nPrefix.contains(firtsToken)) {
					segs.add(firtsToken);
					input = secondToken + " " + input;
				} else {
					segs.add(matchedPart.replace(' ', '_'));
				}

			} else if (matchingRes.isAllCap()) {
				List<String> tokens = StrUtils.tokenizeString(matchedPart);
				if (isNameComponent(tokens.get(0))
						|| isNameComponent(tokens.get(tokens.size() - 1))) {
					segs.add(matchedPart.replace(" ", "_"));
				} else {
					for (String token : tokens) {
						segs.add(token);
					}
				}
			} else
				segs.add(matchedPart.replace(' ', '_'));
		}
		return segs;
	}

	private List<String> path2WordList(List<Integer> path,String sentence){
		List<String> tokens = StrUtils.tokenizeString(sentence);		
		List<String> words = new ArrayList<String>();
		for (int i = 1; i < path.size(); i++) {
			int start = path.get(i-1) - 1;
			int end = path.get(i) - 1;
			StringBuilder word = new StringBuilder(tokens.get(start));
			for (int j = start + 1; j < end; j++)
				word.append("_" + tokens.get(j));			
			words.add(word.toString());
		}				
		return words;
	}
	private List<String> resolveAmbiguity(List<List<Integer>> allPaths,String phrase){
		float bestProb = -1000000000;
		List<String> bestWSeg = null;
		for (int i=0; i < allPaths.size(); i++){
			List<String> words = path2WordList(allPaths.get(i), phrase);
			float currentProb = languageModel.getProb(words);
			if (currentProb > bestProb){
				bestProb = currentProb;
				bestWSeg =words;
			}			
		}
		return bestWSeg;
	}
	private List<String> segmentPhrase(String phrase) {
		Graph wordGraph = buildGraph(phrase);		
		List<String> bestSeg = null;
		double shortestDistance = wordGraph.calculateShortestDistance();
		if (shortestDistance == -1) {
			wordGraph.resolveOOV();
			shortestDistance = wordGraph.calculateShortestDistance();
		}
		if (shortestDistance != -1) {
			List<List<Integer>> allShortestPaths = wordGraph
					.getAllShortestPaths();
			if (allShortestPaths.size() == 1){							
				bestSeg =path2WordList(allShortestPaths.get(0),phrase);
			} else 							
				bestSeg = resolveAmbiguity(allShortestPaths,phrase);
		}			
		return bestSeg;
	}

	private Graph buildGraph(String phrase) {
		List<String> tokens = StrUtils.tokenizeString(phrase);
		int size = tokens.size();

		List<Edge> edges = new ArrayList<Edge>();
		for (int i = 0; i < size; i++) {
			StringBuilder candidate = new StringBuilder();
			for (int j = i; j < size; j++) {
				int wordLen = (j - i + 1);
				if (wordLen > 4)
					break;
				candidate.append(" " + tokens.get(j));
				if (vDict.contains(candidate.toString())) {
					Edge word = new Edge(i + 1, j + 2, 1.0 / wordLen);
					edges.add(word);
				}
			}
		}

		Graph graph = new Graph(size + 1, edges);
		return graph;
	}
}
