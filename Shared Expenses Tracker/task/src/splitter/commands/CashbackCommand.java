package splitter.commands;

import splitter.exceptions.CommandException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CashbackCommand extends Command {

    private LocalDate date;
    private String payerName;
    private String itemName;
    private BigDecimal amount;
    private String[] members;

    public CashbackCommand(
        LocalDate date,
        String payerName,
        String itemName,
        BigDecimal amount,
        String[] members
    ) {
        this.date = date;
        this.payerName = payerName;
        this.itemName = itemName;
        this.amount = amount;
        this.members = members;
    }

    @Override
    public void execute() throws CommandException {

    }
}
