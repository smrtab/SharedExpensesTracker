package splitter.commands;

import splitter.config.Utils;
import splitter.entities.User;
import splitter.exceptions.CommandException;
import splitter.models.ConsoleCommandMember;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

public class PurchaseCommand extends Command {

    final private LocalDate date;
    final private String payerName;
    final private String itemName;
    final private BigDecimal amount;
    final private List<String> members;

    public PurchaseCommand(
        LocalDate date,
        String payerName,
        String itemName,
        BigDecimal amount,
        List<String> members
    ) {
        this.date = date;
        this.payerName = payerName;
        this.itemName = itemName;
        this.amount = amount;
        this.members = members;
    }

    @Override
    public void execute() throws CommandException {

        User payer = userRepository.findOrCreate(payerName, true);
        Map<Integer, List<String>> filteredMembers = ConsoleCommandMember.filter(members);

        if (filteredMembers.get(0).isEmpty()) {
            System.out.println("Group is empty");
            return;
        }

        final List<String> participants = filteredMembers.get(0);
        participants.removeAll(filteredMembers.get(1));

        BigDecimal size = new BigDecimal(participants.size());
        BigDecimal payment = amount.divide(size, 2, RoundingMode.DOWN);

        BigDecimal reminder = Utils.createBigDecimal(
            amount.subtract(payment.multiply(size))
        );

        Utils.log("Payment: " + payment + " | Reminder: " + reminder);

        final CommandController commandController = new CommandController();
        for (String to : participants) {
            if (to.equals(payer.getName())) {
                to = null;
            }
            commandController.addCommand(
                new TransactionCommand(date, payer.getName(), to, payment)
            );
        }

        BigDecimal step = Utils.createBigDecimal("0.01");
        BigDecimal zero = Utils.createBigDecimal("0.00");

        if (reminder.compareTo(reminder.ZERO) < 0) {
            step = step.negate();
        }

        outerloop:
        while (reminder.compareTo(reminder.ZERO) != 0) {
            for (String to : participants) {

                if (reminder.compareTo(zero) == 0) {
                    break outerloop;
                }

                reminder = Utils.createBigDecimal(
                    reminder.subtract(step)
                );

                Utils.log(reminder);

                if (to.equals(payer.getName())) {
                    to = null;
                }
                commandController.addCommand(
                    new TransactionCommand(date, payer.getName(), to, step)
                );
            }
        }

        commandController.invoke();

        Utils.log(participants);
    }
}
