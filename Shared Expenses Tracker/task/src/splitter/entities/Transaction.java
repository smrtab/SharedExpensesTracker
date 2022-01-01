package splitter.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "transaction")
public class Transaction implements Comparable<Transaction> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`date`")
    private LocalDate date;

    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private User partner;

    @Override
    public int compareTo(Transaction o) {
        return getDate().compareTo(o.getDate());
    }

    @Override
    public String toString() {
        return this.getDate() + " " + this.getUser().getName() + " " + this.getAmount();
    }
}
