package splitter.commands;

import splitter.config.Utils;
import splitter.entities.Group;
import splitter.entities.User;
import splitter.exceptions.CommandException;

import java.util.*;

public class SecretSantaCommand extends Command{

    private String name;

    public SecretSantaCommand(
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

        List<User> users = new ArrayList<>(optionalGroup.get().getUsers());
        List<User> recipients = new ArrayList<>(users);

        Collections.rotate(recipients, Utils.getRandomDistance(1, users.size() - 1));

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            User recipient = recipients.get(i);
            System.out.println(user.getName() + " gift to " + recipient.getName());
        }
    }
}
