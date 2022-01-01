package splitter.commands;

import splitter.exceptions.CommandException;
import splitter.repositories.GroupRepository;
import splitter.repositories.TransactionRepository;
import splitter.repositories.UserRepository;
import splitter.config.SpringContext;

public abstract class Command {

    protected UserRepository userRepository;
    protected GroupRepository groupRepository;
    protected TransactionRepository transactionRepository;

    {
        this.userRepository = SpringContext.getBean(UserRepository.class);
        this.groupRepository = SpringContext.getBean(GroupRepository.class);
        this.transactionRepository = SpringContext.getBean(TransactionRepository.class);
    }

    public abstract void execute() throws CommandException;
}
