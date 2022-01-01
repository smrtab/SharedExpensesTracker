package splitter.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import splitter.entities.Group;
import splitter.entities.Transaction;
import splitter.entities.User;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    /**
     * This method deletes all the records whose 'date' is less than given value
     */
    @Modifying
    @Transactional // Make sure to import org.springframework.transaction.annotation.Transactional
    void deleteByDateLessThanEqual(LocalDate date);
}

