package splitter.models;

import lombok.Data;
import splitter.config.Utils;

import java.math.BigDecimal;

@Data
public class Edge implements Comparable<Edge> {

    private Vertex from;
    private Vertex to;
    private BigDecimal capacity;
    private BigDecimal flow = BigDecimal.ZERO;
    private boolean enabled = true;

    public Edge(Vertex from, Vertex to, BigDecimal capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
    }

    public BigDecimal getRemainingCapacity() {
        return Utils.createBigDecimal(capacity.subtract(flow));
    }

    public boolean hasRemainingCapacity() {
        return getRemainingCapacity().compareTo(BigDecimal.ZERO) > 0;
    }

    public void augment(BigDecimal value) {
        flow = flow.add(value);
    }

    @Override
    public int compareTo(Edge o) {
        return getCapacity().compareTo(o.getCapacity());
    }

    @Override
    public String toString() {
        return "Edge{" +
            "from=" + from.getUser().getName() +
            "(Level=" + from.getLevel() + ")" +
            ", to=" + to.getUser().getName() +
            "(Level=" + to.getLevel() + ")" +
            ", capacity=" + capacity +
            ", flow=" + flow +
            ", enabled=" + enabled +
            '}';
    }
}
