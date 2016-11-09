package org.pesc.cds.repository;

import org.pesc.cds.model.Credentials;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by prabhu on 11/8/16.
 */
@Repository
public interface CredentialsRepository extends CrudRepository<Credentials, Integer> {

    @Query("from Credentials where username = ?1")
    List<Credentials> findByUserName(String username);

}
