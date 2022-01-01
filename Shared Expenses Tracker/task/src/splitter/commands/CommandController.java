package splitter.commands;

import splitter.exceptions.CommandException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandController {

    private List<Command> commands = new ArrayList<>();

    public void addCommand(Command command) {
        this.commands.add(command);
    }

    public void invoke() throws CommandException {
        for (Command command: commands) {
            command.execute();
        }
    }
}