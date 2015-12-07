package uet.nlp.lm;

import java.util.List;

import kylm.model.LanguageModel;
import kylm.model.ngram.reader.ArpaNgramReader;
import kylm.model.ngram.reader.NgramReader;
import kylm.model.ngram.reader.SerializedNgramReader;

public class Model {

	private static final String DEFAULT_MODEL = "data/default_model.arpa";
	
	private LanguageModel lm;

	public Model() {
		readModel(DEFAULT_MODEL);
	}

	public Model(String modelPath) {
		readModel(modelPath);
	}

	private void readModel(String modelPath) {
		try {
			String ext = getExtension(modelPath);
			
			NgramReader reader = null;
			if (ext.equals("arpa")){
				reader = new ArpaNgramReader();
			}else if (ext.equals("bin")){
				reader = new SerializedNgramReader();
			}
			
			lm = reader.read(modelPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getExtension(String path) {
		int p = path.lastIndexOf('.');
		if (p != -1)
			return path.substring(p + 1);
		return null;
	}

	public float getProb(String s){
		String sent[] = s.toLowerCase().split("\\s+");
		return lm.getSentenceEntropy(sent);
	}
		
	public float getProb(List<String> s){
		String sent[] = s.toArray(new String[s.size()]);
		return lm.getSentenceEntropy(sent);
	}
	
}
