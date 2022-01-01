package splitter.models;

import splitter.config.SpringContext;
import splitter.config.Utils;
import splitter.exceptions.CommandException;
import splitter.repositories.GroupRepository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class ConsoleCommandMember {

    public static final int ADD = 0;
    public static final int REMOVE = 1;

    private final String action;
    private final String item;

    public ConsoleCommandMember(
        String action,
        String item
    ) {
        this.action = action;
        this.item = item;
    }

    public int getAction() {
        return action.equals("-") ? REMOVE : ADD;
    }

    public String getName() {
        return item;
    }

    public static List<String> parse(String members) {
        return List.of(members.split(",\\s*"));
    }



    public static Map<Integer, List<String>> filter(List<String> members) throws CommandException {

        Pattern pattern = Pattern.compile("(\\+|\\-|)?([a-zA-Z]+)");
        GroupRepository groupRepository = SpringContext.getBean(GroupRepository.class);

        List<ConsoleCommandMember> preparedCollection = members.stream()
            .map(pattern::matcher)
            .filter(Matcher::find)
            .flatMap(matcher ->
                groupRepository
                    .getCollection(matcher.group(2))
                    .stream()
                    .map(name -> new ConsoleCommandMember(
                        matcher.group(1),
                        name
                    ))
            )
            .distinct()
            .collect(toList());

        Pattern groupPattern = Pattern.compile("[A-Z]+");
        boolean unknownGroupExists = preparedCollection.stream()
            .map(instance -> groupPattern.matcher(instance.getName()))
            .anyMatch(Matcher::matches);

        if (unknownGroupExists) {
            throw new CommandException("Group does not exist");
        }

        Map<Integer, List<String>> filteredMembers = preparedCollection.stream()
            .collect(Collectors.groupingBy(
                ConsoleCommandMember::getAction,
                Collectors.mapping(
                    ConsoleCommandMember::getName,
                    Collectors.collectingAndThen(
                        toList(),
                        list -> list.stream()
                            .sorted()
                            .collect(Collectors.toList())
                    )
                )
            ));

        filteredMembers.computeIfAbsent(0, k -> new ArrayList<>());
        filteredMembers.computeIfAbsent(1, k -> new ArrayList<>());

        Utils.log("usersToRemoveList: " + filteredMembers.get(1));
        Utils.log("usersToAddList: " + filteredMembers.get(0));

        return filteredMembers;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        ConsoleCommandMember member = (ConsoleCommandMember) obj;

        return Objects.equals(getName(), member.getName())
                && Objects.equals(getAction(), member.getAction());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + getName().hashCode();
        result = 31 * result + getAction();
        return result;
    }
}
