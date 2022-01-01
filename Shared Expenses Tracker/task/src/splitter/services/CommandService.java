package splitter.services;

import org.springframework.stereotype.Service;
import splitter.config.Utils;
import splitter.commands.*;
import splitter.exceptions.CommandException;
import splitter.exceptions.ExitException;
import splitter.models.ConsoleCommandMember;
import splitter.repositories.GroupRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

@Service
@Transactional
public class CommandService {

    private GroupRepository groupRepository;

    public CommandService(
        GroupRepository groupRepository
    ) {
        this.groupRepository = groupRepository;
    }

    public Command get(String input) throws CommandException, ExitException {

        Optional<ConsoleCommand> optionalCommandRepository = ConsoleCommand.validate(input);
        if (optionalCommandRepository.isEmpty()) {
            throw new IllegalArgumentException("Unknown command");
        }

        ConsoleCommand commandRepository = optionalCommandRepository.get();
        Matcher params = ConsoleCommand.parse(input);
        List<String> commandParams = Utils.toList(params);

        Utils.log("CommandParams: " + commandParams);

        LocalDate date;
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            date = LocalDate.parse(commandParams.get(0), dateTimeFormatter);

            //remove date
            commandParams.remove(0);
        } catch (NullPointerException e) {
            date = LocalDate.now();

            //remove date
            commandParams.remove(0);
        } catch (DateTimeParseException e) {
            date = LocalDate.now();
        }

        //remove command
        commandParams.remove(0);

        Command command;
        switch (commandRepository) {
            case BORROW: {
                command = new TransactionCommand(
                    date,
                    commandParams.get(1),
                    commandParams.get(0),
                    new BigDecimal(commandParams.get(2))
                );
                break;
            }
            case REPAY: {
                command = new TransactionCommand(
                    date,
                    commandParams.get(0),
                    commandParams.get(1),
                    new BigDecimal(commandParams.get(2))
                );
                break;
            }
            case BALANCE: {

                String type = "close";
                if (!commandParams.isEmpty()) {
                    type = commandParams.get(0);
                }

                List<String> members = new ArrayList<>();
                if (commandParams.get(2) != null) {
                    members = ConsoleCommandMember.parse(commandParams.get(1));
                }

                command = new BalanceCommand(date, type, members);
                break;
            }
            case BALANCE_PERFECT: {

                String type = "close";
                if (!commandParams.isEmpty()) {
                    type = commandParams.get(0);
                }

                List<String> members = new ArrayList<>();
                if (commandParams.get(2) != null) {
                    members = ConsoleCommandMember.parse(commandParams.get(1));
                }

                command = new BalancePerfectCommand(date, type, members);
                break;
            }
            case SECRET_SANTA: {
                command = new SecretSantaCommand(
                    commandParams.get(0)
                );
                break;
            }
            case WRITE_OFF: {
                command = new WriteOffCommand(
                    date
                );
                break;
            }
            case CASHBACK: {
                command = new PurchaseCommand(
                    date,
                    commandParams.get(0),
                    commandParams.get(1),
                    (new BigDecimal(commandParams.get(2))).negate(),
                    ConsoleCommandMember.parse(commandParams.get(3))
                );
                break;
            }
            case GROUP_CREATE: {
                command = new GroupCreateCommand(
                    commandParams.get(0),
                    ConsoleCommandMember.parse(commandParams.get(1))
                );
                break;
            }
            case GROUP_ADD: {
                command = new GroupAddCommand(
                    commandParams.get(0),
                    ConsoleCommandMember.parse(commandParams.get(1))
                );
                break;
            }
            case GROUP_REMOVE: {
                command = new GroupRemoveCommand(
                    commandParams.get(0),
                    ConsoleCommandMember.parse(commandParams.get(1))
                );
                break;
            }
            case GROUP_SHOW: {
                command = new GroupShowCommand(
                    commandParams.get(0)
                );
                break;
            }
            case PURCHASE: {
                command = new PurchaseCommand(
                    date,
                    commandParams.get(0),
                    commandParams.get(1),
                    new BigDecimal(commandParams.get(2)),
                    ConsoleCommandMember.parse(commandParams.get(3))
                );
                break;
            }
            case HELP: {
                command = new HelpCommand();
                break;
            }
            case EXIT: {
                throw new ExitException();
            }
            default: {
                throw new CommandException("Unknown command. Print help to show commands list");
            }
        }

        return command;
    }
}