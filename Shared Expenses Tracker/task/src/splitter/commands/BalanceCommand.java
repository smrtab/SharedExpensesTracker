package splitter.commands;

import splitter.config.Utils;
import splitter.entities.Transaction;
import splitter.entities.User;
import splitter.exceptions.CommandException;
import splitter.models.BalanceHandler;
import splitter.models.ConsoleCommandMember;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

public class BalanceCommand extends Command {

    private LocalDate date;
    private String type;
    private List<String> members;

    public BalanceCommand(
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

        var debts = BalanceHandler.getDebtMap(members, balanceDate);

        Set<String> outputs = new TreeSet<>();
        for (Map.Entry<User, Map<User, BigDecimal>> entry : debts.entrySet()) {

            User originator = entry.getKey();
            for (Map.Entry<User, BigDecimal> innerEntry : entry.getValue().entrySet()) {
                User target = innerEntry.getKey();
                BigDecimal debt = innerEntry.getValue();

                if (debt.compareTo(BigDecimal.ZERO) > 0) {
                    outputs.add(originator.getName() + " owes " + target.getName() + " " + debt);
                }
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
