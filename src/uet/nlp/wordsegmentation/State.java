package uet.nlp.wordsegmentation;

import java.util.ArrayList;

public class State {

	private float prob;
	private ArrayList<Integer> indexes;

	public State() {
		prob = 0;
		indexes = new ArrayList<>();
	}

	public State(State s) {
		this();
		if (s != null)
			indexes.addAll(s.getIndexes());
	}

	public float getProb() {
		return prob;
	}

	public void setProb(float prob) {
		this.prob = prob;
	}

	public ArrayList<Integer> getIndexes() {
		return indexes;
	}

	public void setIndexes(ArrayList<Integer> indexes) {
		this.indexes = indexes;
	}

	public void addIndex(int i) {
		indexes.add(i);
	}

}
