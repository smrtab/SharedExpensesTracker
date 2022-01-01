package splitter.commands;

import splitter.config.Utils;
import splitter.entities.Transaction;
import splitter.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionCommand extends Command {

    private LocalDate date;
    private String fromName;
    private String toName;
    private BigDecimal amount;

    public TransactionCommand(
        LocalDate date,
        String fromName,
        String toName,
        BigDecimal amount
    ) {
        this.date = date;
        this.fromName = fromName;
        this.toName = toName;
        this.amount = amount;
    }

    @Override
    public void execute() {

        User from = userRepository.findOrCreate(fromName, true);
        Transaction fromTrn = create(date, from,  amount.negate());
        from.addTransaction(fromTrn);

        Utils.log(from);

        if (toName != null) {
            User to = userRepository.findOrCreate(toName, true);
            Transaction toTrn = create(date, to, amount);
            to.addTransaction(toTrn);

            fromTrn.setPartner(to);
            toTrn.setPartner(from);

            transactionRepository.save(fromTrn);
            transactionRepository.save(toTrn);

            Utils.log(to);
        }
    }

    private Transaction create(
        LocalDate date,
        User owner,
        BigDecimal amount
    ) {
        Transaction transaction = new Transaction();
        transaction.setDate(date);
        transaction.setUser(owner);
        transaction.setAmount(amount);

        return transaction;
    }
}
