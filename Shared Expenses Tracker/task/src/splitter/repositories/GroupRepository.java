package splitter.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import splitter.entities.Group;
import splitter.entities.User;

import javax.transaction.Transactional;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
@Transactional
public interface GroupRepository extends CrudRepository<Group, Long> {
    Optional<Group> findByName(String name);
    List<Group> findAll();

    void deleteByName(String name);

    default Group create(String name) {

        Group group = new Group();
        group.setName(name);
        Group savedGroup = save(group);

        return savedGroup;
    }

    default List<String> getCollection(String input) {

        Optional<Group> optionalGroup = findByName(input);

        Group actionGroup = null;
        if (optionalGroup.isPresent()) {
            actionGroup = optionalGroup.get();
        } else if ("AGROUP".equals(input)) {
            actionGroup = create(input);
        }

        List<String> collection = new ArrayList<>();
        if (actionGroup != null) {
            collection.addAll(actionGroup.getUsersNames());
        } else {
            collection.add(input);
        }

        return collection;
    }
}
