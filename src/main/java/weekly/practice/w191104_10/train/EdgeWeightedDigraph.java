package weekly.practice.w191104_10.train;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class EdgeWeightedDigraph {
    private int V;
    private int E;
    private Map<String, List<DirectedEdge>> adj;

    public EdgeWeightedDigraph(int V) {
        this.V = V;
        this.E = 0;
        adj = new HashMap<>(V);
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
        this.adj = new HashMap<>(vertexSet.size());
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
        adj.compute(e.from(), (k, v) -> {
            if (null == v) {
                List<DirectedEdge> edges = new ArrayList<>();
                edges.add(e);
                return edges;
            }
            v.add(e);
            return v;
        });
        E++;
    }

    public Iterable<DirectedEdge> adj(String v) {
        return adj.get(v);
    }

    public Iterable<DirectedEdge> edges() {
        List<DirectedEdge> edges = new ArrayList<>();
        for (List<DirectedEdge> edge : adj.values()) {
            edges.addAll(edge);
        }
        return edges;
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public List<String> vertexes() {
        return new ArrayList<>(adj.keySet());
    }

    public int getDistance(String... vertexArray) {
        int dis = 0;
        for (int i = 0; i + 1 < vertexArray.length; i++) {
            List<DirectedEdge> edgeList = adj.get(vertexArray[i]);
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

    /**
     * To be Modify
     */
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

    /**
     * 深度遍历
     */
    public int getNumWithLimit(String from, String to, int minStop, int maxStop, LimitType type) {
        //根据深度范围
        List<List<String>> allTrace = getNumRecur(from, to, minStop, maxStop, 0, type);
        //未找到
        if (allTrace == null) {
            return 0;
        }
        //打印结果
        for (List<String> eachTrace : allTrace) {
            System.out.println(eachTrace);
        }
        return allTrace.size();
    }

    private List<List<String>> getNumRecur(String start, String end, int min, int max,
                                           int current, LimitType type) {
        //超过扫描深度
        if (current > max) {
            return null;
        }
        //查找子节点
        List<DirectedEdge> edgeList = adj.get(start);
        //无子节点
        if (edgeList == null) {
            return null;
        }
        List<List<String>> result = new ArrayList<>();
        //如果已经找到
        if (current >= min && start.equals(end)) {
            List<String> trace = new LinkedList<>();
            trace.add(start);
            result.add(trace);
            //虽然找到但是只要没有超过深度限制，依然继续搜索
        }
        //添加所有子结果
        for (DirectedEdge edge : edgeList) {
            //访问子节点
            List<List<String>> subList = getNumRecur(edge.to(), end, min, max,
                    type.equals(LimitType.STOP) ? current + 1 : current + edge.weight(), type);
            if (subList == null) {
                continue;
            }
            //子节点路径前加上自己
            for (List<String> eachTrace : subList) {
                eachTrace.add(0, start);
            }
            result.addAll(subList);
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    /**
     * 最短路劲普通算法
     */
    public Integer shortestDirectedEdge(String start, String stop) {
        /**
         * 最短路径使用广度遍历更合适
         **/
        //最短的路径
        List<DirectedEdge> shortestPath = null;
        //最短的路径长度
        int shortestLength = Integer.MAX_VALUE;
        //初始化起点路径对象
        List<List<DirectedEdge>> paths = findFirst(start);
        while (true) {
            paths = findNext(paths, shortestLength);
            if (paths.isEmpty()) {
                break;
            }
            //检查是否找到
            for (List<DirectedEdge> path : paths) {
                //找到
                if (path.get(path.size() - 1).to().equals(stop)) {
                    //比较并赋值
                    int length = getTraceLength(path);
                    if (length < shortestLength) {
                        shortestPath = path;
                        shortestLength = length;
                    }
                }
            }
        }
        //打印结果
        System.out.println(shortestPath);
        return shortestLength;
    }

    /**
     * 起始节点路径集合
     */
    private List<List<DirectedEdge>> findFirst(String start) {
        List<List<DirectedEdge>> result = new LinkedList<>();
        //每个edge的最后一个节点
        List<DirectedEdge> nextEdges = adj.get(start);
        if (nextEdges == null) {
            return result;
        }
        for (DirectedEdge edge : nextEdges) {
            List<DirectedEdge> newPath = new LinkedList<>();
            newPath.add(edge);
            result.add(newPath);
        }
        return result;
    }

    /**
     * 查找下一个节点路径集合（超过长度的路径不会被返回）
     */
    private List<List<DirectedEdge>> findNext(List<List<DirectedEdge>> paths, int maxLength) {
        List<List<DirectedEdge>> result = new LinkedList<>();
        for (List<DirectedEdge> eachPath : paths) {
            //每个edge的最后一个节点
            DirectedEdge lastEdge = eachPath.get(eachPath.size() - 1);
            List<DirectedEdge> nextEdges = adj.get(lastEdge.to());
            if (nextEdges == null) {
                continue;
            }
            for (DirectedEdge edge : nextEdges) {
                List<DirectedEdge> newPath = new LinkedList<>(eachPath);
                newPath.add(edge);
                // 小于已找到的最小路径长度
                if (getTraceLength(newPath) < maxLength) {
                    result.add(newPath);
                }
            }
        }
        return result;
    }

    /**
     * 计算路径长度
     */
    private int getTraceLength(List<DirectedEdge> trace) {
        int result = 0;
        for (DirectedEdge eachDirectedEdge : trace) {
            result += eachDirectedEdge.weight();
        }
        return result;
    }

}
