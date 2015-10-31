package uet.nlp.wordsegmentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Evaluation {

	private static final String TEXT_DIR = "data/test/text";
	private static final String SEG_MODEL_DIR = "data/test/seg_model";
	private static final String SEG_HUMAN_DIR = "data/test/seg_human";

	private static int totalHumanCount, totalModelCount, totalMatchCount;
	private static int totalLines;
	private static int totalTime;

	private static int humanCount, modelCount, matchCount;

	private static Segmenter segmenter;

	public static void main(String args[]) {
		long initTime = init();
		System.out.println("Initialize time: " + initTime);

		System.out.println("--------------------\n");
		System.out.println("File\tLines\tTime\t" + "Human\tModel\tMatch\t"
				+ "Pre\tRecall\tF1");

		File textDir = new File(TEXT_DIR);
		for (String s : textDir.list()) {
			String fileName = s.substring(0, s.lastIndexOf('.'));
			process(fileName);
		}

		System.out.println();
		double precision = round((double) totalMatchCount / totalModelCount, 4);
		double recall = round((double) totalMatchCount / totalHumanCount, 4);
		double f1 = round(2 * precision * recall / (precision + recall), 4);
		System.out.println("Total\t" + totalLines + "\t" + totalTime + "\t"
				+ totalHumanCount + "\t" + totalModelCount + "\t"
				+ totalMatchCount + "\t" + precision + "\t" + recall + "\t"
				+ f1);
	}

	private static long init() {
		long start = System.currentTimeMillis();

		totalHumanCount = totalModelCount = totalMatchCount = 0;
		totalLines = 0;
		totalTime = 0;
		segmenter = new Segmenter();

		return (System.currentTimeMillis() - start);
	}

	private static void process(String fileName) {
		try {
			String textPath = TEXT_DIR + "/" + fileName + ".txt";
			BufferedReader reader = new BufferedReader(new FileReader(textPath));
			String line;

			int lineCount = 0;
			List<List<String>> segmentRes = new ArrayList<>();

			long start = System.currentTimeMillis();

			while ((line = reader.readLine()) != null) {
				segmentRes.add(segmenter.segment(line));
				lineCount++;
			}

			long time = System.currentTimeMillis() - start;

			reader.close();

			saveSegment(segmentRes, fileName);
			evaluate(segmentRes, fileName);

			double precision = round((double) matchCount / modelCount, 4);
			double recall = round((double) matchCount / humanCount, 4);
			double f1 = round(2 * precision * recall / (precision + recall), 4);
			System.out.println(fileName + "\t" + lineCount + "\t" + time + "\t"
					+ humanCount + "\t" + modelCount + "\t" + matchCount + "\t"
					+ precision + "\t" + recall + "\t" + f1);

			totalLines += lineCount;
			totalTime += time;
			totalHumanCount += humanCount;
			totalModelCount += modelCount;
			totalMatchCount += matchCount;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static double round(double x, int n) {
		if (n < 0)
			return x;

		long factor = 1;
		for (int i = 0; i < n; i++)
			factor *= 10;
		x *= factor;

		return (double) Math.round(x) / factor;
	}

	private static void saveSegment(List<List<String>> seg, String fileName) {
		try {
			StringBuilder res = new StringBuilder();
			for (List<String> line : seg) {
				StringBuilder sb = new StringBuilder();
				for (String word : line)
					sb.append(word + " ");
				res.append(sb.toString().trim() + "\n");
			}

			String outFilePath = SEG_MODEL_DIR + "/" + fileName + ".seg";
			FileWriter writer = new FileWriter(outFilePath);
			writer.write(res.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void evaluate(List<List<String>> modelSeg, String fileName) {
		try {
			humanCount = modelCount = matchCount = 0;

			String humanSeg = SEG_HUMAN_DIR + "/" + fileName + ".seg";
			BufferedReader reader = new BufferedReader(new FileReader(humanSeg));

			for (List<String> modelWords : modelSeg) {
				String line = reader.readLine();
				if (line == null)
					break;
				String[] humanWords = line.split(" ");

				humanCount += humanWords.length;
				modelCount += modelWords.size();
				matchCount += getMatchCount(humanWords, modelWords);
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int[] calculateIndex(String[] words) {
		int[] indexArr = new int[words.length];
		int index = 0;
		for (int i = 0; i < words.length; i++) {
			indexArr[i] = index;
			index += words[i].replace("_", "").length();
		}
		return indexArr;
	}

	private static int[] calculateIndex(List<String> words) {
		int[] indexArr = new int[words.size()];
		int index = 0;
		for (int i = 0; i < words.size(); i++) {
			indexArr[i] = index;
			index += words.get(i).replace("_", "").length();
		}
		return indexArr;
	}

	private static int getMatchCount(String[] humanWords,
			List<String> modelWords) {
		int[] humanIndex = calculateIndex(humanWords);
		int[] modelIndex = calculateIndex(modelWords);

		int matchCount = 0;
		for (int i = 0, j = 0; i < humanWords.length; i++) {
			while (j < modelIndex.length - 1 && humanIndex[i] > modelIndex[j])
				j++;
			if (humanIndex[i] == modelIndex[j])
				if (humanWords[i].equals(modelWords.get(j)))
					matchCount++;
		}

		return matchCount;
	}

}
