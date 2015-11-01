package uet.nlp.utils;

import java.util.List;

import uet.nlp.tokenizer.Tokenizer;

public class Utils {

	public static String tokenize(String s) {
		String[] lines = s.split("\n");
		StringBuilder sb = new StringBuilder();
		for (String line: lines){
			List<String> tokens = Tokenizer.tokenize(line);
			for (int i = 0; i < tokens.size() - 1; i++)
				sb.append(tokens.get(i) + " ");
			sb.append(tokens.get(tokens.size() - 1) + "\n");
		}
		return sb.toString();
	}

}
