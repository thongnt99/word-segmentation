package uet.nlp.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import uet.nlp.utils.StrUtils;
import uet.nlp.wordsegmentation.Evaluation;
import uet.nlp.wordsegmentation.Segmenter;

public class WrongSegmentExporter {
	public static Segmenter wSegmenter;
	public static BufferedWriter bw;
	public static void main(String[] args){
		wSegmenter = new Segmenter();				
		try{
			Writer writer =
				      new OutputStreamWriter(
				         new FileOutputStream("wronglist.txt"), "UTF-8");
			bw = new BufferedWriter(writer);
		} catch (IOException e){
			e.printStackTrace();
		}
		
				
		File folder = new File("data/test/seg_human");
		if (!folder.exists() || !folder.isDirectory()) return;		
		for (File file : folder.listFiles()){			
			try{
				exportFile(file);
			} catch (Exception e){
				e.printStackTrace();
			}						
		}				
	}	
	public static void exportFile(File file) throws IOException{
		Reader reader =
			      new InputStreamReader(
			         new FileInputStream(file),"UTF-8");
		BufferedReader br = new BufferedReader(reader);
		String line;
		while ( (line= br.readLine()) != null){
			if (!line.startsWith("<")) {
				export(line);
			}
		}
		br.close();
	}
	public static void export(String segmentedString) throws IOException{
		
		List<String> accurateWords = StrUtils.tokenizeString(segmentedString);
		String input = segmentedString.replace("_"," ");		
		List<String> predictedWords  = wSegmenter.segment(input);
		List<String> wrongWords = new ArrayList<String>();
		int[] accIndex = Evaluation.calculateIndex(accurateWords);
		int[] preIndex = Evaluation.calculateIndex(predictedWords);
		
		for (int i=0; i< predictedWords.size(); i++){
			boolean check = false;
			for (int j=0; j < accurateWords.size(); j++ ){
				if ( predictedWords.get(i).equals(accurateWords.get(j)) && preIndex[i] == accIndex[j] ){
					check = true;
					break;
				}
			}
			if (!check) wrongWords.add(predictedWords.get(i));
		}
		if (wrongWords.size() > 0 ){
			
			bw.write("--------------------------------------------------------------\n");
			bw.write("Predict  : \n");
			for (String word : predictedWords) bw.write(word+" ");
			bw.write("\n");
			bw.write("\n");
			bw.write("Accurate : \n");
			for (String word : accurateWords) bw.write(word+" ");
			bw.write("\n");
			bw.write("\n");
			bw.write("Wrong words: \n");
			for (String word : wrongWords) bw.write(word+"\n");						
		}
		
	}	
}
