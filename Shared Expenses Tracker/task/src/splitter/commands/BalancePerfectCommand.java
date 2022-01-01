package splitter.commands;

import splitter.config.Utils;
import splitter.entities.Transaction;
import splitter.entities.User;
import splitter.exceptions.CommandException;
import splitter.models.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

public class BalancePerfectCommand extends Command {

    private LocalDate date;
    private String type;
    private List<String> members;

    public BalancePerfectCommand(
        LocalDate date,
        String type,
        List<String> members
    ) {
        this.date = date;
        this.type = type == null ? "close" : type;
        this.members = members;
    }

    @Override
    public void execute() throws CommandException {

        final LocalDate balanceDate;
        if (type.equals("open")) {
            balanceDate = date
                .minusMonths(1)
                .with(TemporalAdjusters.lastDayOfMonth());
        } else {
            balanceDate = date;
        }

        Utils.log("BalanceDate: " + balanceDate);

        Graph graph = Graph.of(
            BalanceHandler.getDebtMap(members, balanceDate)
        );

        DinicsNetworkFlow dinicsNetworkFlow = new DinicsNetworkFlow(graph);
        while (dinicsNetworkFlow.sinkIsReachable()) {

            graph.print();
            List<Edge> path = dinicsNetworkFlow.getAugmentingPath();

            if (path != null) {
                BigDecimal minFlow = path.stream()
                    .map(edge -> edge.getRemainingCapacity())
                    .min(Comparator.naturalOrder())
                    .orElse(BigDecimal.ZERO);

                Utils.log("MinFlow=" + minFlow);

                for (Edge edge : path) {
                    edge.augment(minFlow);
                }
            } else {
                break;
            }

            graph.print();
        }

        dinicsNetworkFlow.reset();

        Deque<Vertex> queue = new ArrayDeque<>();
        queue.offer(graph.getSource());

        graph.print();

        Set<String> outputs = new TreeSet<>();
        while (!queue.isEmpty()) {
            Vertex node = queue.poll();
            for (Edge edge: node.getEnabledEdges()) {
                if (edge.hasRemainingCapacity()) {
                    outputs.add(edge.getFrom().getUser().getName()
                        + " owes " + edge.getTo().getUser().getName()
                        + " " + edge.getRemainingCapacity()
                    );
                }
                edge.setEnabled(false);
                queue.offer(edge.getTo());
            }
        }

        Utils.log(outputs);

        if (outputs.isEmpty()) {
            System.out.println("No repayments");
        } else {
            outputs.forEach(System.out::println);
        }

    }
}
