package splitter.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import splitter.config.Utils;

import java.math.BigDecimal;
import java.util.*;

@Data
public class DinicsNetworkFlow {

    final private Graph graph;

    final public void reset() {

        Deque<Vertex> vertices = new ArrayDeque<>();
        vertices.offer(graph.getSource());
        graph.getSource().setLevel(-1);

        while (!vertices.isEmpty()) {
            Vertex node = vertices.poll();
            for (Edge edge: node.getEdges()) {
                edge.setEnabled(true);
                if (edge.getTo().levelIsSet()) {
                    edge.getTo().setLevel(-1);
                    vertices.offer(edge.getTo());
                }
            }
        }
    }

    final public boolean sinkIsReachable() {

        reset();

        Deque<Vertex> vertices = new ArrayDeque<>();
        vertices.offer(graph.getSource());
        graph.getSource().setLevel(0);

        while (!vertices.isEmpty()) {
            Vertex node = vertices.poll();
            for (Edge edge: node.getEdgesWithRemainingCapacity()) {
                if (edge.getTo().equals(graph.getSink())) {
                    edge.getTo().setLevel(
                        node.getLevel() + 1
                    );
                } else if (!edge.getTo().levelIsSet()) {
                    edge.getTo().setLevel(
                        node.getLevel() + 1
                    );
                    vertices.offer(edge.getTo());
                } else {
                    edge.setEnabled(false);
                }
            }
        }

        return graph.getSink().levelIsSet();
    }

    final public List<Edge> getAugmentingPath() {

        List<Edge> path = new ArrayList<>();
        Deque<Edge> edges = new ArrayDeque<>(
            graph.getSource().getEdgesWithRemainingCapacity()
        );

        boolean isBlockedFlow = true;
        while (!edges.isEmpty()) {

            Edge currentEdge = edges.poll();
            List<Edge> nextEdges = currentEdge
                .getTo()
                .getEdgesWithRemainingCapacity();

            if (!nextEdges.isEmpty()) {
                path.add(currentEdge);
            }

            if (currentEdge.getTo().equals(graph.getSink())) {
                isBlockedFlow = false;
                break;
            }

            for (Edge edge : nextEdges) {
                edges.push(edge);
            }
        }

        Utils.log("Path=" + path);

        return isBlockedFlow ? null : path;
    }
}
