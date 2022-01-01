package splitter.commands;

import org.springframework.beans.factory.annotation.Autowired;
import splitter.entities.Group;
import splitter.exceptions.CommandException;
import splitter.repositories.GroupRepository;
import splitter.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class GroupRemoveCommand extends Command {

    private String name;
    private List<String> members;

    public GroupRemoveCommand(
        String name,
        List<String> members
    ) {
        this.name = name;
        this.members = members;
    }

    @Override
    public void execute() throws CommandException {

        Optional<Group> optionalGroup = groupRepository.findByName(name);
        if (optionalGroup.isEmpty()) {
            throw new CommandException("Unknown group");
        }

        GroupHandleMembersCommand command = new GroupHandleMembersCommand(
            GroupHandleMembersCommand.ACTION_REMOVE,
            optionalGroup.get(),
            members
        );
        command.execute();
    }
}
