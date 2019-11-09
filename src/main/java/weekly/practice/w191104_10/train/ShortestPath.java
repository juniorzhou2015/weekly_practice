package weekly.practice.w191104_10.train;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ShortestPath {
    /**
     * 最短路径怎么计算出来的？
     * 有多条最短路径时，edgeTo怎么保存？
     */
    protected Map<String, DirectedEdge> edgeTo;
    protected Map<String, Integer> distTo;
    private EdgeWeightedDigraph graph;

    public ShortestPath(EdgeWeightedDigraph G, String s) {
        this.edgeTo = new HashMap<>(G.V());
        this.distTo = new HashMap<>(G.V());
        this.graph = G;
        for (String v : G.vertexes()) {
            distTo.put(v, Integer.MAX_VALUE);
        }
        distTo.put(s, 0);
    }

    private int getIndex(String s) {
        return graph.vertexes().indexOf(s);
    }

    public int distTo(String v) {
        return distTo.get(v);
    }

    public boolean hasPathTo(String v) {
        return distTo(v) < Integer.MAX_VALUE;
    }

    public Iterable<DirectedEdge> pathTo(String v) {
        if (!hasPathTo(v)) {
            return null;
        }
        Stack<DirectedEdge> path = new Stack<>();
        for (DirectedEdge e = edgeTo.get(v); e != null; e = edgeTo.get(e.from())) {
            path.push(e);
        }
        return path;
    }

}
