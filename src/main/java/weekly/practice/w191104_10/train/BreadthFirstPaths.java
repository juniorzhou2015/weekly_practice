package weekly.practice.w191104_10.train;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

public class BreadthFirstPaths {

    private Map<String, Boolean> marked;
    private Map<String, String> edgeTo;
    private final String s;

    public BreadthFirstPaths(EdgeWeightedDigraph G, String s) {
        this.marked = new HashMap<>(G.V());
        this.edgeTo = new HashMap<>(G.V());
        this.s = s;
        bfs(G, s);
    }

    private void bfs(EdgeWeightedDigraph G, String v) {
        Queue<String> queue = new LinkedBlockingQueue<>();
        marked.put(v, true);
        queue.offer(v);
        while (!queue.isEmpty()) {
            v = queue.poll();
            for (DirectedEdge edge : G.adj(v)) {
                String w = edge.to();
                if (marked.get(w)) {
                    continue;
                }
                edgeTo.put(w, v);
                marked.put(w, true);
                queue.offer(w);
            }
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
