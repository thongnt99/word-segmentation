package uet.nlp.test;

import java.util.List;

import uet.nlp.wordsegmentation.Segmenter;

public class Test {
	
	public static void main(String[] args) {
		Segmenter segmenter = new Segmenter();
		List<String> tokens = segmenter.segment("tính từ bổ nghĩa cho động từ");
		for (String token : tokens)
			System.out.println(token);
	}
}
