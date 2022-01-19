package net.junhabaek.springapidocs.infra;

import net.junhabaek.springapidocs.domain.aggregate.Store;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends CrudRepository<Store, Long> {

    Store findByName(String name);
}
