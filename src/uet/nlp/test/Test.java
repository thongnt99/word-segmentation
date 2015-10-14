package uet.nlp.test;

import java.util.List;

import uet.nlp.wordsegmentation.Segmenter;

public class Test {
	public static void main(String[] args){
		Segmenter segmenter = new Segmenter();
		List<String> tokens = segmenter.segment("Cô có sử dụng kem trộn không ?");
		for (String token : tokens)
			System.out.println(token);
	}
}
