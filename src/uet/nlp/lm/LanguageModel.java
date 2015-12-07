package uet.nlp.lm;

public class LanguageModel {

	static {
		System.loadLibrary("kenlm_ngram_query_x64");
	}

	public native void loadModel(String filePath);
	public native float prob(String s, boolean sentenceContext);
	public native void setDebug(boolean debug);

	static LanguageModel instance = null;

	private LanguageModel() {
	}

	public static LanguageModel getInstance() {
		if (instance == null)
			instance = new LanguageModel();
		return instance;
	}
	
}
