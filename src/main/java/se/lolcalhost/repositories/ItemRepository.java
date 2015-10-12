package se.lolcalhost.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.lolcalhost.models.Item;
import se.lolcalhost.models.User;

/**
 * Created by jonmar on 2015-10-12.
 */
@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {
}
