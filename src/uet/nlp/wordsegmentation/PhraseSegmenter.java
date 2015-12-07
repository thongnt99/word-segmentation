package uet.nlp.wordsegmentation;

import java.util.ArrayList;
import java.util.HashMap;

import uet.nlp.lm.LanguageModel;
import uet.nlp.resource.Dictionary;

public class PhraseSegmenter {

	private static final String DEFAULT_DICT_PATH = "resources/dictionary/vietlexDict.txt";
	private static final String DEFAULT_MODEL_PATH = "resources/arpa/model_9.arpa";
	private static final String _ = "_";
	public static PhraseSegmenter phraseSegmenter = new PhraseSegmenter();

	private Dictionary dict;
	private LanguageModel model;
	private int N = 3; // N gram(s)

	public PhraseSegmenter() {
		dict = Dictionary.getDictionary(DEFAULT_DICT_PATH);
		model = LanguageModel.getInstance();
		model.loadModel(DEFAULT_MODEL_PATH);
	}

	public ArrayList<String> segment(String text) {
		String token[] = text.split("\\s");
		int n = token.length;

		@SuppressWarnings("unchecked")
		ArrayList<State>[] f = new ArrayList[n];

		for (int i = 0; i < n; i++) {
			f[i] = new ArrayList<>();

			ArrayList<Integer> wordIndex = findWord(token, i + 1);
			for (int j : wordIndex) {
				
				String curWord = getWord_(token, j, i + 1);
				HashMap<ArrayList<Integer>, State> mapNgramState = new HashMap<>();
				HashMap<ArrayList<Integer>, Float> mapNgramProb = new HashMap<>();
				
				if (j == 0) {
					ArrayList<Integer> boo = new ArrayList<>();
					mapNgramState.put(boo, null);
					mapNgramProb.put(boo, model.prob(curWord, false));
				} else {
					for (State prev : f[j - 1]) {
						ArrayList<Integer> index = prev.getIndexes();
						ArrayList<Integer> ngram = new ArrayList<Integer>(index.subList(Math.max(0, index.size() - N), index.size()));
						if (!mapNgramState.containsKey(ngram)) {
							mapNgramState.put(ngram, prev);
							mapNgramProb.put(ngram, getProb(token, prev.getIndexes(), curWord));
						} else {
							if (prev.getProb() > mapNgramState.get(ngram).getProb())
								mapNgramState.put(ngram, prev);
						}
					}
				}
				
				for (ArrayList<Integer> ngram : mapNgramState.keySet()) {
					State prev = mapNgramState.get(ngram);
					State s = new State(prev);
					float prob = mapNgramProb.get(ngram);
					if (prev != null)
						prob += prev.getProb();
					s.setProb(prob);
					s.addIndex(i + 1);
					f[i].add(s);
				}

				
//				String curWord = getWord_(token, j, i + 1);
//				State bestState = null;
//				float bestProb = Float.NEGATIVE_INFINITY;
//
//				if (j == 0) {
//					bestProb = model.prob(curWord, false);
//				} else {
//					for (State prev : f[j - 1]) {
//						float p = getProb(token, prev.getIndexes(), curWord);
//						if (p + prev.getProb() > bestProb) {
//							bestProb = p + prev.getProb();
//							bestState = prev;
//						}
//					}
//				}
//
//				State s = new State(bestState);
//				s.addIndex(i + 1);
//				s.setProb(bestProb);
//				f[i].add(s);
			}
		}

		State best = f[n - 1].get(0);
		for (State s : f[n - 1])
			if (s.getProb() > best.getProb())
				best = s;
		// System.out.println(best.getProb());

		ArrayList<String> res = new ArrayList<>();
		for (int i = 0; i < best.getIndexes().size(); i++) {
			int begin = (i == 0) ? 0 : best.getIndexes().get(i - 1);
			res.add(getWord_(token, begin, best.getIndexes().get(i)));
		}

		return res;
	}

	private ArrayList<ArrayList<Integer>> findNWord(ArrayList<Integer>[] allWord, int end) {
		ArrayList<ArrayList<Integer>> res = new ArrayList<>();
		
		// find 1 word
		for (Integer i: allWord[end]){
			ArrayList<Integer> a = new ArrayList<>();
			a.add(i);
			res.add(a);
		}
		
		// find N-1 word
		for (int k = 0; k < N - 1; k ++){
			
		}
		
		return res;
	}

	private ArrayList<Integer>[] initAllWord(String token[]) {
		@SuppressWarnings("unchecked")
		ArrayList<Integer> res[] = new ArrayList[token.length];
		
		for (int end = 0; end < token.length; end++)
			res[end] = findWord(token, end + 1);
		return res;
	}

	private float getProb(String[] token, ArrayList<Integer> prevIndex,
			String curWord) {
		StringBuilder sb = new StringBuilder();
		for (int i = Math.max(0, prevIndex.size() - N + 1); i < prevIndex
				.size(); i++) {
			int begin = (i == 0) ? 0 : prevIndex.get(i - 1);
			sb.append(getWord_(token, begin, prevIndex.get(i))).append(" ");
		}
		sb.append(curWord);

		return model.prob(sb.toString(), false);
	}

	private ArrayList<Integer> findWord(String[] token, int end) {
		ArrayList<Integer> res = new ArrayList<>();
		StringBuilder word = new StringBuilder();

		int begin = end - 1;
		word.append(token[begin]);
		res.add(begin);

		for (begin = end - 2; begin >= Math.max(0, end - 5); begin--) {
			word.insert(0, token[begin] + " ");
			if (dict.contains(word.toString()))
				res.add(begin);
		}

		return res;
	}

	private String getWord_(String token[], int begin, int end) {
		StringBuilder sb = new StringBuilder();
		sb.append(token[begin]);
		for (int i = begin + 1; i < end; i++)
			sb.append(_).append(token[i]);
		return sb.toString();
	}

	private String getWord(String token[], int begin, int end) {
		StringBuilder sb = new StringBuilder();
		sb.append(token[begin]);
		for (int i = begin + 1; i < end; i++)
			sb.append(" ").append(token[i]);
		return sb.toString();
	}

	public static void main(String args[]) {
		PhraseSegmenter seg = new PhraseSegmenter();

		String test[] = {
				"học sinh học sinh học",
				"tính từ bổ nghĩa cho động từ",
				"tính từ đây ra hồ tây là 10 km",
				"tính từ mép nước vào bờ",
				"hươu là loài vật được con người thuần dưỡng đã hàng trăm năm .",
				"rượu tiết hươu màu đỏ tươi , vị ngọt , mát , bổ dưỡng dùng cho những người mùa hè hay chảy máu cam , người bị mụn nhọt , lở ngứa mà đông y gọi là nóng gan , nhiệt thận .",
				"điểm dễ nhận thấy sau khi các đại biểu cùng nhau mổ xẻ sáu \" đôi giày \" đến từ Úc , Thuỵ Điển , Thái Lan , Trung Quốc , Lào và VN là : giày của nước chủ nhà quá rộng so với giày các nước : giám sát của QH VN bao quát không chỉ cơ quan hành pháp mà còn \" ôm \" luôn cả cơ quan tư pháp , các cấp chính quyền địa phương … với phạm vi \" gần như là vô tận \" .",
				"- Tôi không nghĩ như vậy .",
				"thế nhưng , muốn đưa luật đi thật sâu vào cuộc sống thì QH cần có thêm những công cụ thiết thực .",
				"nhân viên thu phí tay cầm bảng báo stop , miệng giải thích .",
				"xúc tiến hợp tác xây dựng đường xe điện ngầm",
				"phát triển vận tải công cộng và xe điện",
				"đề nghị hỗ trợ tiền sử dụng đất",
				"trên cơ sở sáp nhập các doanh nghiệp" };

		seg.N = 2;
		for (String s : test)
			System.out.println(seg.segment(s));

		System.out.println();

		seg.N = 3;
		for (String s : test)
			System.out.println(seg.segment(s));
	}
}
