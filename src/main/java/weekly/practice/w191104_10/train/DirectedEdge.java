package weekly.practice.w191104_10.train;

public class DirectedEdge {

    private final String v;
    private final String w;
    private final int weight;

    public DirectedEdge(String v, String w, int weight) {
        System.out.println(v + " " + w + " " + weight);
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public String from() {
        return v;
    }

    public String to() {
        return w;
    }

    public double weight() {
        return weight;
    }

    @Override
    public String toString() {
        return String.format("%s->%s %d", v, w, weight);
    }
}
