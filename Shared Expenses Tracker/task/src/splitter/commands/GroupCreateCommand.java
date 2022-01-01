package splitter.commands;

import splitter.entities.Group;
import splitter.exceptions.CommandException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class GroupCreateCommand extends Command {

    private String name;
    private List<String> members;

    public GroupCreateCommand(
        String name,
        List<String> members
    ) {
        this.name = name;
        this.members = members;
    }

    @Override
    public void execute() throws CommandException {

        Optional<Group> optionalGroup = groupRepository.findByName(name);
        if (optionalGroup.isPresent()) {
            groupRepository.deleteByName(name);
        }

        Group group = groupRepository.create(name);

        GroupHandleMembersCommand command = new GroupHandleMembersCommand(
            GroupHandleMembersCommand.ACTION_CREATE,
            group,
            members
        );
        command.execute();
    }
}
