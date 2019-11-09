//package weekly.practice.w191104_10.train;
//
//import java.util.HashMap;
//import java.util.PriorityQueue;
//
//public class DijkstraShortestPath extends ShortestPath {
//    /**
//     * 索引优先队列
//     */
//    private PriorityQueue<String> pq;
//
//    public DijkstraShortestPath(EdgeWeightedDigraph G, String s) {
//        edgeTo = new HashMap<>(G.V());
//        distTo = new HashMap<>(G.V());
//        pq = new PriorityQueue<>(G.V());
//        for (String v : G.vertexes()) {
//            distTo.put(v, Integer.MAX_VALUE);
//        }
//        distTo.put(s, 0);
//        pq.add(s);
//        while (!pq.isEmpty()) {
//            relax(G, pq.poll());
//        }
//    }
//
//    public void relax(EdgeWeightedDigraph G, String v) {
//        for (DirectedEdge edge : G.adj(v)) {
//            String w = edge.to();
//            if (distTo.get(w) > distTo.get(v) + edge.weight()) {
//                distTo.put(w, distTo.get(v) + edge.weight());
//                edgeTo.put(w, edge);
//                if (pq.contains(w)) {
////                    pq.change(w, distTo.get(w));
//                } else {
////                    pq.insert(w, distTo.get(w));
//                }
//            }
//        }
//    }
//
//}
