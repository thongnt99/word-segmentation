package uet.nlp.resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import uet.nlp.utils.StrUtils;

public class Dictionary {

	private static HashMap<String, Dictionary> loadedDict = new HashMap<>();

	private HashSet<String> dict;

	public static Dictionary getDictionary(String dictPath) {
		if (loadedDict.containsKey(dictPath))
			return loadedDict.get(dictPath);

		Dictionary newDict = new Dictionary(dictPath);
		loadedDict.put(dictPath, newDict);

		return newDict;
	}

	private Dictionary(String dictPath) {
		dict = new HashSet<String>();
		try {
			loadDictionary(dictPath);
		} catch (IOException ioException) {
			System.err.println("File not found/File is invalid");
			ioException.printStackTrace();
		}
	}

	private void loadDictionary(String dictPath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(dictPath));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#")) continue;
			dict.add(StrUtils.normalizeString(line));
		}
		reader.close();
	}

	public boolean contains(String word) {
		word = StrUtils.normalizeString(word);
		return dict.contains(word);
	}

}
