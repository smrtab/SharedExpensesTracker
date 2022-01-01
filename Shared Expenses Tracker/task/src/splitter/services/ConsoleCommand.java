package splitter.services;

import splitter.config.Utils;
import splitter.exceptions.CommandException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public enum ConsoleCommand {
    SECRET_SANTA(
        "secretSanta",
        "(secretSanta)",
        "(secretSanta)\\s+([A-Z]+)"
    ),
    WRITE_OFF(
        "writeOff",
        "(writeOff)",
        "([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})?\\s*(writeOff)"
    ),
    GROUP_CREATE(
        "group",
        "(group create)",
        "(group\\s+create)\\s+([A-Z]+)\\s+\\(([^()]+)\\)?"
    ),
    GROUP_ADD(
        "group",
        "(group add)",
        "(group\\s+add)\\s+([A-Z]+)\\s+\\(([^()]+)\\)"
    ),
    GROUP_REMOVE(
        "group",
        "(group remove)",
        "(group\\s+remove)\\s+([A-Z]+)\\s+\\(([^()]+)\\)"
    ),
    GROUP_SHOW(
        "group",
        "(group show)",
        "(group\\s+show)\\s+([A-Z]+)"
    ),
    CASHBACK(
        "cashBack",
        "(cashBack)",
        "([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})?\\s*(cashBack)\\s+([a-zA-Z]+)\\s+([a-zA-Z]+)\\s+([0-9.]+)\\s+\\(([^()]+)\\)"
    ),
    PURCHASE(
        "purchase",
        "(purchase)",
        "([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})?\\s*(purchase)\\s+([a-zA-Z]+)\\s+([a-zA-Z]+)\\s+([0-9.]+)\\s+\\(([^()]+)\\)"
    ),
    BALANCE(
        "balance",
        "(balance)\\s*",
        "([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})?\\s*(balance)\\s*(open|close)?\\s*(\\(([^()]+)\\))?"
    ),
    BALANCE_PERFECT(
        "balancePerfect",
        "(balancePerfect)\\s*",
        "([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})?\\s*(balancePerfect)\\s*(open|close)?\\s*(\\(([^()]+)\\))?"
    ),
    BORROW(
        "borrow",
        "(borrow)",
        "([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})?\\s*(borrow)\\s+([a-zA-Z]+)\\s+([a-zA-Z]+)\\s+([0-9.]+)"
    ),
    REPAY(
        "repay",
        "(repay)",
        "([0-9]{4}\\.[0-9]{2}\\.[0-9]{2})?\\s*(repay)\\s+([a-zA-Z]+)\\s+([a-zA-Z]+)\\s+([0-9.]+)"
    ),
    EXIT(
        "exit",
        "(exit)",
        "(exit)"
    ),
    HELP(
        "help",
        "(help)",
        "(help)"
    );

    private String name;
    private String commandNamePattern;
    private String commandPattern;

    ConsoleCommand(
        String name,
        String commandNamePattern,
        String commandPattern
    ) {
        this.name = name;
        this.commandNamePattern = commandNamePattern;
        this.commandPattern = commandPattern;
    }

    public String getName() {
        return name;
    }

    public String getCommandNamePattern() {
        return commandNamePattern;
    }

    public String getCommandPattern() {
        return commandPattern;
    }

    public static ConsoleCommand getByName(String name) throws CommandException {
        for (ConsoleCommand cmd : values()) {
            if (cmd.getCommandNamePattern().contains(name)) {
                return cmd;
            }
        }
        throw new CommandException("Unknown command. Print help to show commands list");
    }

    public static Optional<ConsoleCommand> validate(String input) throws CommandException {
        ConsoleCommand commandRepository = null;
        for (ConsoleCommand cmd : values()) {
            Pattern pattern = Pattern.compile(cmd.getCommandNamePattern());
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                commandRepository = ConsoleCommand.getByName(matcher.group(1));
            }
        }
        return Optional.ofNullable(commandRepository);
    }

    public static Matcher parse(String input) {
        for (ConsoleCommand cmd : values()) {
            Pattern pattern = Pattern.compile(cmd.getCommandPattern());
            Matcher matcher = pattern.matcher(input);

            if (matcher.matches()) {
                return matcher;
            }
        }
        throw new IllegalArgumentException("Illegal command arguments");
    }
}
