package splitter.commands;

import splitter.exceptions.CommandException;

import java.time.LocalDate;

public class WriteOffCommand extends Command {

    private LocalDate date;

    public WriteOffCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute() throws CommandException {
        transactionRepository.deleteByDateLessThanEqual(date);
    }
}
