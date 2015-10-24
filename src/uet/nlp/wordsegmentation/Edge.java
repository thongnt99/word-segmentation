package uet.nlp.wordsegmentation;

public class Edge {
	private final int src;
	private final int des;
	private final double weight;

	public Edge(int src, int des, double weight) {
		this.src = src;
		this.des = des;
		this.weight = weight;
	}

	public int getSrc() {
		return src;
	}

	public int getDes() {
		return des;
	}

	public double getWeight() {
		return weight;
	}
}
