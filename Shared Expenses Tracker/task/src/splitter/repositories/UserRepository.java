package splitter.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import splitter.config.Utils;
import splitter.entities.Group;
import splitter.entities.User;

import java.util.*;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByName(String name);
    List<User> findAll();

    default User findOrCreate(String name) {
        return findOrCreate(name, false);
    }

    default User findOrCreate(String name, Boolean save) {

        name = name.trim();
        Optional<User> optionalUser = findByName(name);

        User user;
        if (optionalUser.isEmpty()) {
            user = new User();
            user.setName(name);

            if (save) {
                save(user);
            }

        } else {
            user = optionalUser.get();
        }

        return user;
    }
}
