package splitter.models;

import lombok.Data;
import splitter.config.Utils;
import splitter.entities.User;

import java.math.BigDecimal;
import java.util.*;

@Data
public class Graph {

    private HashMap<Long, Vertex> vertices = new HashMap<>();
    private Vertex source;
    private Vertex sink;

    public Vertex createVertex(User user) {
        if (!getVertices().containsKey(user.getId())) {
            getVertices().put(
                user.getId(),
                new Vertex(user)
            );
        }
        return getVertices().get(user.getId());
    }

    public static Graph of(Map<User, Map<User, BigDecimal>> debts) {

        Graph graph = new Graph();
        for (Map.Entry<User, Map<User, BigDecimal>> entry : debts.entrySet()) {

            Vertex from = graph.createVertex(entry.getKey());
            if (graph.getSource() == null) {
                graph.setSource(from);
                from.setLevel(0);
            }

            for (Map.Entry<User, BigDecimal> innerEntry
                    : entry.getValue().entrySet()) {

                BigDecimal debt = innerEntry.getValue();
                if (debt.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

                Vertex to = graph.createVertex(innerEntry.getKey());

                Edge fromToEdge = new Edge(from, to, debt);
                from.addEdge(fromToEdge);

                if (to.equals(graph.getSource())) {
                    graph.setSink(to);
                }

                if (!to.levelIsSet()) {
                    int level = from.getLevel() + 1;
                    to.setLevel(level);

                    if (graph.getSink() == null) {
                        graph.setSink(to);
                    }
                }

                Utils.log("Source=" + graph.getSource().getUser().getName()
                    + ", Sink="
                    + graph.getSink().getUser().getName()
                );
            }
        }

        return graph;
    }

    final public void print() {

        if (Utils.DEBUG_LEVEL == 0) {
            return;
        }

        Utils.log("Source=" + getSource().getUser().getName()
                + ", Sink="
                + getSink().getUser().getName()
        );

        Deque<Vertex> vertices = new ArrayDeque<>();
        vertices.offer(getSource());

        Map<String, List<String>> output = new LinkedHashMap<>();
        while (!vertices.isEmpty()) {
            Vertex node = vertices.poll();
            if (output.containsKey(node.getUser().getName())) {
                continue;
            }

            output.put(node.getUser().getName(), new ArrayList<>());

            for (Edge edge: node.getEdges()) {
                output.get(node.getUser().getName()).add(edge.toString());
                vertices.offer(edge.getTo());
            }
        }

        for (var entry : output.entrySet()) {
            for (var edge : entry.getValue()) {
                Utils.log(edge);
            }
        }
        Utils.log("=========================");
    }
}
