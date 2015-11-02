package uet.nlp.wordsegmentation;

import java.util.ArrayList;
import java.util.List;

public class Graph {

	private int n;
	private boolean[] isReached;
	private int[] adjList;
	private int[] index;
	private int[] trace;
	private double[] distance;
	private List<Edge> edges;

	public Graph(int n, List<Edge> edges) {
		this.n = n;
		this.edges = edges;
		init();
		calculateIndex(false);
		buildAdjList();
		// for (int i=1; i<=n; i++) {
		// System.out.println("---------------");
		// System.out.println(index[i]);
		// for (int j = index[i-1]; j< index[i]; j++)
		// System.out.print(adjList[j]+" ");
		// System.out.println();
		// }
	}

	private void init() {
		isReached = new boolean[n + 1];
		index = new int[n + 1];

		for (int i = 0; i <= n; i++) {
			index[i] = 0;
			isReached[i] = false;
		}
	}

	private void calculateIndex(boolean withOOV) {
		for (Edge edge : edges) {
			index[edge.getSrc()]++;
		}
		if (withOOV)
			for (int i = 1; i <= n; i++) {
				if (index[i] == 0) {
					index[i] = 1;
					edges.add(new Edge(i, i + 1, 1));
				}
			}
		for (int i = 1; i <= n; i++)
			index[i] = index[i - 1] + index[i];

	}

	private void buildAdjList() {
		int[] tmp = new int[n + 1];
		for (int i = 0; i <= n; i++)
			tmp[i] = index[i];
		adjList = new int[index[n]];
		for (Edge edge : edges) {
			int src = edge.getSrc();
			int des = edge.getDes();
			tmp[src]--;
			adjList[tmp[src]] = des;
		}
	}

	public void resolveOOV() {
		init();
		calculateIndex(true);
		buildAdjList();
	}

	public List<Integer> getShortestPath() {
		trace = new int[n + 1];
		distance = new double[n + 1];
		for (int i = 1; i <= n; i++)
			distance[i] = 10000;
		distance[1] = 0;
		while (true) {
			double min = 10000;
			int minVt = -1;
			for (int i = 1; i <= n; i++)
				if (!isReached[i] && distance[i] < min) {
					min = distance[i];
					minVt = i;
				}
			if (minVt == -1)
				return null;
			isReached[minVt] = true;
			if (minVt == n) {
				int vt = n;
				List<Integer> path = new ArrayList<Integer>();
				while (vt != 1) {
					path.add(vt);
					vt = trace[vt];
				}
				path.add(1);
				return path;
			}
			for (int i = index[minVt - 1]; i < index[minVt]; i++) {
				int j = adjList[i];
				if (!isReached[j]
						&& distance[j] > distance[minVt] + 1
								/ (double) (j - minVt)) {
					distance[j] = distance[minVt] + 1 / (double) (j - minVt);
					trace[j] = minVt;
				}
			}

		}
	}
}
