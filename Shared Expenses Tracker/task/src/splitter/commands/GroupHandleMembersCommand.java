package splitter.commands;

import splitter.config.Utils;
import splitter.entities.Group;
import splitter.entities.User;
import splitter.exceptions.CommandException;
import splitter.models.ConsoleCommandMember;

import java.util.*;

public class GroupHandleMembersCommand extends Command {

    public static final int ACTION_CREATE = 0;
    public static final int ACTION_ADD = 1;
    public static final int ACTION_REMOVE = 2;

    private int groupAction;
    private Group group;
    private List<String> members;

    public GroupHandleMembersCommand(
        int groupAction,
        Group group,
        List<String> members
    ) {
        this.groupAction = groupAction;
        this.group = group;
        this.members = members;
    }

    @Override
    public void execute() throws CommandException {

        Map<Integer, List<String>> filteredMembers = ConsoleCommandMember.filter(members);

        List<String> usersToAddList = filteredMembers.get(0);
        List<String> usersToRemoveList = filteredMembers.get(1);

        Set<String> participants = new TreeSet<>();
        switch (groupAction) {
            case ACTION_REMOVE: {

                List<String> itemsToRemove
                    = Utils.disjunction(usersToRemoveList, usersToAddList);

                Utils.log("itemsToRemove: " + itemsToRemove);
                for (String item : itemsToRemove) {
                    groupRepository.deleteByName(item);
                    group.getUsers().removeIf(user -> user.getName().equals(item));
                }
                break;
            }
            case ACTION_CREATE:
            case ACTION_ADD: {

                usersToAddList.removeAll(usersToRemoveList);

                for (String item : usersToRemoveList) {
                    groupRepository.deleteByName(item);
                    group.getUsers().removeIf(user -> user.getName().equals(item));
                }
                participants.addAll(usersToAddList);
                break;
            }
        }

        Utils.log(participants);

        for (String name : participants) {
            User user = userRepository.findOrCreate(name);
            userRepository.save(user);

            group.getUsers().add(user);
        }

        groupRepository.save(group);
        Utils.log(group);
    }
}
