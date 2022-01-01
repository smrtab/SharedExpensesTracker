package splitter.commands;

import org.springframework.beans.factory.annotation.Autowired;
import splitter.entities.Group;
import splitter.entities.User;
import splitter.exceptions.CommandException;
import splitter.repositories.GroupRepository;
import splitter.repositories.UserRepository;

import java.util.Optional;

public class GroupShowCommand extends Command {

    private String name;

    public GroupShowCommand(
        String name
    ) {
        this.name = name;
    }

    @Override
    public void execute() throws CommandException {

        Optional<Group> optionalGroup = groupRepository.findByName(name);
        if (optionalGroup.isEmpty()) {
            throw new CommandException("Unknown group");
        }

        if (optionalGroup.get().getUsers().isEmpty()) {
            throw new CommandException("Group is empty");
        }

        for (User user : optionalGroup.get().getUsers()) {
            System.out.println(user.getName());
        }
    }
}
