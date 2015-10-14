package uet.nlp.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import uet.nlp.utils.StrUtils;

public class Dictionary {
	
	public static HashSet<String> dict;
	
	public Dictionary(String dictPath){
		FileReader fr;
		BufferedReader br;
		dict = new HashSet<String>();
		try{
			fr = new FileReader(new File(dictPath));
			br = new BufferedReader(fr);
			String line;			
			while ( (line = br.readLine())!= null){
				dict.add( StrUtils.normalizeString(line) );				
			}
			
		} catch (IOException ioException){
			System.err.println("File not found/File is invalid");
			ioException.printStackTrace();
		}
	}
	
	public boolean contains(String word){ 		
		word = StrUtils.normalizeString(word);		
		return dict.contains(word);
	}
	
		
}
