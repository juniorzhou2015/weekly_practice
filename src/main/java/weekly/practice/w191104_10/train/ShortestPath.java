package weekly.practice.w191104_10.train;

import java.util.Stack;

public class ShortestPath {

    private DirectedEdge[] edgeTo;
    private int[] distTo;
    private EdgeWeightedDigraph graph;

    public ShortestPath(EdgeWeightedDigraph G, String s) {
        edgeTo = new DirectedEdge[G.V()];
        distTo = new int[G.V()];
        graph = G;
        for (int v = 0; v < G.V(); v++) {
            distTo[v] = Integer.MAX_VALUE;
            distTo[getIndex(s)] = 0;
        }
    }

    private int getIndex(String s) {
        return graph.vertexes().indexOf(s);
    }

    public int distTo(String v) {
        return distTo[getIndex(v)];
    }

    public boolean hasPathTo(String v) {
        return distTo(v) < Integer.MAX_VALUE;
    }

    public Iterable<DirectedEdge> pathTo(String v) {
        if (!hasPathTo(v)) {
            return null;
        }
        Stack<DirectedEdge> path = new Stack<>();
        for (DirectedEdge e = edgeTo[getIndex(v)]; e != null; e = edgeTo[getIndex(e.from())]) {
            path.push(e);
        }
        return path;
    }

}
