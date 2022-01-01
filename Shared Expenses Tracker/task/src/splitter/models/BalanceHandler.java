package splitter.models;

import splitter.config.SpringContext;
import splitter.config.Utils;
import splitter.entities.Transaction;
import splitter.entities.User;
import splitter.exceptions.CommandException;
import splitter.repositories.GroupRepository;
import splitter.repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BalanceHandler {

    public static Map<User, Map<User, BigDecimal>> getDebtMap(
        List<String> members,
        LocalDate balanceDate
    ) throws CommandException {

        UserRepository userRepository = SpringContext.getBean(UserRepository.class);

        List<User> usersList = userRepository.findAll();
        final Map<Integer, List<String>> filteredMembers = ConsoleCommandMember.filter(members);

        if (!members.isEmpty() && filteredMembers.get(0).isEmpty()) {
            throw new CommandException("Group is empty");
        }

        if (!filteredMembers.get(0).isEmpty()) {

            List<String> participants = filteredMembers.get(0);
            participants.removeAll(filteredMembers.get(1));

            usersList = usersList.stream()
                .filter(user -> participants.contains(user.getName()))
                .collect(Collectors.toList());
        }

        return usersList.stream()
            .flatMap(user -> user.getTransactions().stream())
            .filter(trn -> !trn.getDate().isAfter(balanceDate))
            .filter(trn -> trn.getPartner() != null)
            .collect(Collectors.groupingBy(
                Transaction::getUser,
                Collectors.groupingBy(
                    Transaction::getPartner,
                    Collectors.reducing(
                        Utils.createBigDecimal(BigDecimal.ZERO),
                        Transaction::getAmount,
                        BigDecimal::add
                    )
                )
            ));

    }
}
