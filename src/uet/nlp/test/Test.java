package uet.nlp.test;

import java.util.List;

import uet.nlp.wordsegmentation.Segmenter;

public class Test {
	public static void main(String[] args){
		Segmenter segmenter = new Segmenter();
		List<String> tokens = segmenter.segment("Gửi email cho nguyenthacthong@gmail.com được không\n");
		for (String token : tokens){
			System.out.println(token);
		}
	}
}
