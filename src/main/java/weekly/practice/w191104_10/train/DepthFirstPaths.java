package weekly.practice.w191104_10.train;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class DepthFirstPaths {

    private Map<String, Boolean> marked;
    private Map<String, String> edgeTo;
    private final String s;

    public DepthFirstPaths(EdgeWeightedDigraph G, String s) {
        this.marked = new HashMap<>(G.V());
        this.edgeTo = new HashMap<>(G.V());
        this.s = s;
        dfs(G, s);
    }

    private void dfs(EdgeWeightedDigraph G, String v) {
        marked.put(v, true);
        for (DirectedEdge edge : G.adj(v)) {
            String w = edge.to();
            if (marked.get(w)) {
                continue;
            }
            edgeTo.put(w, v);
            dfs(G, w);
        }
    }

    public boolean hasPathTo(String v) {
        return marked.get(v);
    }

    public Iterable<String> pathTo(String v) {
        if (!hasPathTo(v)) {
            return null;
        }
        Stack<String> path = new Stack<>();
        for (String x = v; !x.equals(s); x = edgeTo.get(x)) {
            path.push(x);
        }
        path.push(s);
        return path;
    }

}
