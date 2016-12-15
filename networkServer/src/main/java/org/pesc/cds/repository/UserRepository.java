package org.pesc.cds.repository;

import org.pesc.cds.model.Credentials;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by prabhu on 11/10/16.
 */
@Repository
public interface UserRepository extends CrudRepository<Credentials, Integer>, JpaSpecificationExecutor {

    @Query("from Credentials where name = ?1")
    List<Credentials> findByName(String name);

    @Query("from Credentials where username = ?1")
    List<Credentials> findByUserName(String username);
}
