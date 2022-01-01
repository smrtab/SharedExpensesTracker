package splitter.commands;

import splitter.services.ConsoleCommand;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class HelpCommand extends Command {

    @Override
    public void execute() {

        List<String> commands = Arrays.stream(ConsoleCommand.values())
            .map(ConsoleCommand::getName)
            .sorted()
            .distinct()
            .collect(Collectors.toList());

        for (String cmdName: commands) {
            System.out.println(cmdName);
        }
    }
}
