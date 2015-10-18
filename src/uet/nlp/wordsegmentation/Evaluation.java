package uet.nlp.wordsegmentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import uet.nlp.utils.StrUtils;

public class Evaluation {
	public static int humanCount;
	public static int modelCount;
	public static int matchedCount;
	public static int totalFiles;
	public static int totalLines;
	public static void main(String[] args){
		init();
		File folder = new File("data/test");
		if (!folder.exists() || !folder.isDirectory()) return;		
		for (File file : folder.listFiles()){			
			try{
				evalFile(file);
				totalFiles ++;
			} catch (Exception e){
				e.printStackTrace();
			}						
		}
		System.out.println("Human\tModel\tMatch\tPrecision\tRecall\tF1");
		double precision = (double) matchedCount / (double) modelCount;
		double recall = (double) matchedCount / (double) humanCount;
		double f1 = 2*precision*recall / (precision + recall);
		System.out.println(humanCount+"\t"+modelCount+"\t"+matchedCount+"\t"+precision
				+"\t"+recall+"\t"+f1);
	}
	public static void init(){
		humanCount = 0;
		modelCount = 0;
		matchedCount = 0;
		totalFiles = 0;
		totalLines = 0;
	}
	public static void evalFile(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ( (line= br.readLine()) != null){
			if (!line.startsWith("<")) {
				evalSen(line);				
				totalLines ++;
			}
		}
		br.close();
	}
	public static int[] calculateIndex(List<String> words){
		int[] indexArr = new int[words.size()];
		int index = 0;
		for (int i = 0; i < words.size(); i++ ){
			indexArr[i] = index;
			index += StrUtils.tokenizeString(words.get(i),"_").size();
		}
		return indexArr;
	}
	public static void evalSen(String sen){
		
		String unSegmentedStr = sen.replace("_"," ");
		
		List<String> accurateWords = StrUtils.tokenizeString(sen);
		int[] accWIndex = calculateIndex(accurateWords);
		List<String> predictedwords = new Segmenter().segment(unSegmentedStr);
		int[] preWIndex = calculateIndex(predictedwords);
		
		for (int i = 0; i < accurateWords.size(); i++){
			String accWord = accurateWords.get(i);
			for (int j=0; j < predictedwords.size(); j++ ){
				String preWord = predictedwords.get(j);
				if (accWord.equals(preWord) && accWIndex[i] == preWIndex[j]){
					matchedCount ++;
				}
				if (preWIndex[j] > accWIndex[i]) break;
			}
		}
		humanCount += accurateWords.size();
		modelCount += predictedwords.size();
						
	}	
}
