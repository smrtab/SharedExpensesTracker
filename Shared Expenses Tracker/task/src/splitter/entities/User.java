package splitter.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "`user`")
@EqualsAndHashCode(exclude = {"transactions"})
public class User implements Comparable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    //@ManyToOne
    //@JoinColumn(name = "group_id")
    //private Group group;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER, targetEntity = Group.class)
    @SortNatural
    SortedSet<Group> groups = new TreeSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction trn) {
        transactions.add(trn);
    }

    @Override
    public String toString() {
        return this.getId() + ". Name: " + this.getName() + ". Transactions: " + this.transactions;
    }

    @Override
    public int compareTo(User o) {
        if (this.getName() == null || o.getName() == null) {
            return 0;
        }
        return this.getName().compareTo(o.getName());
    }
}
