package splitter.models;

import lombok.Data;
import splitter.config.Utils;
import splitter.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Vertex implements Comparable<Vertex>  {

    final private User user;
    final private List<Edge> edges = new ArrayList<>();
    private int level = -1;

    public Vertex(User user) {
        this.user = user;
    }

    final public void addEdge(Edge edge) {
        edges.add(edge);
    }

    final public boolean levelIsSet() {
        return level > -1;
    }

    final public List<Edge> getEdgesWithRemainingCapacity() {
        return edges
            .stream()
            .filter(Edge::isEnabled)
            .filter(Edge::hasRemainingCapacity)
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    Collections.shuffle(list);
                    return list;
                }
            ));
    }

    final public List<Edge> getEnabledEdges() {
        return edges
            .stream()
            .filter(Edge::isEnabled)
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    Collections.shuffle(list);
                    return list;
                }
            ));
    }

    @Override
    public int compareTo(Vertex o) {
        return getUser().compareTo(o.getUser());
    }
}
