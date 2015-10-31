package uet.nlp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StrUtils {

	public static String rmSpaceDup(String input) {
		String output = input.replaceAll("\\s+", " ");
		return output;
	}

	public static String toLowercase(String input) {
		String output = input.toLowerCase();
		return output;
	}

	public static String normalizeString(String input) {
		String output = StrUtils.rmSpaceDup(input);
		output = StrUtils.toLowercase(output);
		return output.trim();
	}

	public static List<String> tokenizeString(String input) {
		StringTokenizer strTokenizer = new StringTokenizer(input);
		List<String> output = new ArrayList<String>();
		while (strTokenizer.hasMoreTokens()) {
			output.add(strTokenizer.nextToken());
		}
		return output;
	}

	public static List<String> tokenizeString(String input, String delimiter) {
		StringTokenizer strTokenizer = new StringTokenizer(input, delimiter);
		List<String> output = new ArrayList<String>();
		while (strTokenizer.hasMoreTokens()) {
			output.add(strTokenizer.nextToken());
		}
		return output;
	}
}
