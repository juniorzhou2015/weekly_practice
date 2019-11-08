package weekly.practice.w191104_10.train;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class EdgeWeightedDigraph {
    private int V;
    private int E;
    private List<String> vertexes;
    private List<DirectedEdge>[] adj;

    public EdgeWeightedDigraph(int V) {
        this.V = V;
        this.E = 0;
        adj = new ArrayList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
    }

    public EdgeWeightedDigraph(String edgeStr) {
        if (null == edgeStr || 0 == edgeStr.length()) {
            return;
        }
        List<String> edges =
                Arrays.stream(edgeStr.split(",")).filter(s -> 3 == s.trim().length()).map(String::trim).collect(Collectors.toList());
        Set<String> vertexSet = new TreeSet<>();
        edges.forEach(s -> {
            for (int i = 0; i <= 1; i++) {
                vertexSet.add(String.valueOf(s.charAt(0)));
            }
        });
        this.V = vertexSet.size();
        this.E = 0;
        this.adj = new ArrayList[vertexSet.size()];
        for (int v = 0; v < vertexSet.size(); v++) {
            adj[v] = new ArrayList<>();
        }
        this.vertexes = new ArrayList<>(vertexSet);
        for (String edge : edges) {
            System.out.println(edge + " " + edge.charAt(0) + " " + edge.charAt(1) + " " + edge.charAt(2));
            DirectedEdge e = new DirectedEdge(String.valueOf(edge.charAt(0)),
                    String.valueOf(edge.charAt(1)), Integer.parseInt(String.valueOf(edge.charAt(2))));
            addEdge(e);
        }
    }

    public void addEdge(DirectedEdge e) {
        /**
         * 异常情况
         */
        adj[vertexes.indexOf(e.from())].add(e);
        E++;
    }

    public Iterable<DirectedEdge> adj(String v) {
        return adj[vertexes.indexOf(v)];
    }

    public Iterable<DirectedEdge> edges() {
        List<DirectedEdge> edges = new ArrayList<>();
        for (int v = 0; v < V; v++) {
            edges.addAll(adj[v]);
        }
        return edges;
    }

    public int getDistance(String... vertexArray) {
        int dis = 0;
        for (int i = 0; i + 1 < vertexArray.length; i++) {
            List<DirectedEdge> edgeList = adj[vertexes.indexOf(vertexArray[i])];
            String to = vertexArray[i + 1];
            Optional<DirectedEdge> edgeOptional =
                    edgeList.stream().filter(e -> e.to().equals(to)).findFirst();
            if (!edgeOptional.isPresent()) {
                return -1;
            }
            dis += edgeOptional.get().weight();
        }
        return dis;
    }

    public int getNumWithMaxStops(String from, String to, int max) {
        int num = 0, level = 0, stops = 0;
        Queue<String> queue = new LinkedBlockingQueue<>();
        queue.offer(from);
        while (!queue.isEmpty() && ++stops <= max) {
            String v = queue.poll();
            for (DirectedEdge e : adj(v)) {
                String t = e.to();
                queue.offer(t);
                if (t.equals(to)) {
                    num++;
                }
            }
        }
        return num;
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public List<String> vertexes() {
        return vertexes;
    }

}
