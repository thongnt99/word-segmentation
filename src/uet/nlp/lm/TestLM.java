package uet.nlp.lm;

public class TestLM {

	public static void main(String agrs[]) {
		 long start = System.currentTimeMillis();
		 LanguageModel lm = LanguageModel.getInstance();
		
//		 lm.loadModel("test.arpa");
//		 lm.loadModel("test2.arpa");
		 lm.loadModel("resource/lm/model.arpa");
		
		System.out.println("Load model in "	+ (System.currentTimeMillis() - start) + " ms");
		lm.setDebug(true);
		 
		System.out.println(lm.prob("học_sinh học_sinh học", true));
		System.out.println();
		System.out.println(lm.prob("học_sinh học sinh_học", true));
				
//		System.out.println(lm.prob("học sinh học sinh học", false) + " " + lm.prob("học sinh học sinh học", true));
//		System.out.println(lm.prob("học_sinh học sinh học", false) + " " + lm.prob("học_sinh học sinh học", true));
//		System.out.println(lm.prob("học_sinh học_sinh học", false) + " " + lm.prob("học_sinh học_sinh học", true)); // best
//		System.out.println(lm.prob("học_sinh học sinh_học", false) + " " + lm.prob("học_sinh học sinh_học", true));
//		System.out.println(lm.prob("học sinh_học sinh học", false) + " " + lm.prob("học sinh_học sinh học", true));
//		System.out.println(lm.prob("học sinh_học sinh_học", false) + " " + lm.prob("học sinh_học sinh_học", true));
//		System.out.println(lm.prob("học sinh học_sinh học", false) + " " + lm.prob("học sinh học_sinh học", true));
//		System.out.println(lm.prob("học sinh học sinh_học", false) + " " + lm.prob("học sinh học sinh_học", true));

		
//		long start = System.currentTimeMillis();
//		Model lm = new Model("model_2gram.arpa");
//		System.out.println("Load model in "
//				+ (System.currentTimeMillis() - start) + " ms");
//
//		System.out.println(lm.getProb("học sinh học sinh học"));
//		System.out.println(lm.getProb("học_sinh học sinh học"));
//		System.out.println(lm.getProb("học_sinh học_sinh học")); // best
//		System.out.println(lm.getProb("học_sinh học sinh_học"));
//		System.out.println(lm.getProb("học sinh_học sinh học"));
//		System.out.println(lm.getProb("học sinh_học sinh_học"));
//		System.out.println(lm.getProb("học sinh học_sinh học"));
//		System.out.println(lm.getProb("học sinh học sinh_học"));
	}

}
