package splitter.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SortNatural;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "`group`")
@EqualsAndHashCode(exclude = {"users"})
public class Group implements Comparable<Group> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    //@OneToMany(mappedBy = "group", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    //@SortNatural
    //private SortedSet<User> users = new TreeSet<>();

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = User.class)
    @SortNatural
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    SortedSet<User> users = new TreeSet<>();

    public void addUser(User user) {
        users.add(user);
    }

    public List<String> getUsersNames() {
        return getUsers().stream()
            .map(User::getName)
            .sorted()
            .collect(Collectors.toList());
    }

    @Override
    public int compareTo(Group o) {
        if (this.getName() == null || o.getName() == null) {
            return 0;
        }
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return this.getName() + ". Users: " + this.users;
    }
}
