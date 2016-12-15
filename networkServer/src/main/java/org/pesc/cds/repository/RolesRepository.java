package org.pesc.cds.repository;

import org.pesc.cds.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by Prabhu Rajendran (prajendran@ccctechcenter.org) on 3/21/16.
 */
@Repository
public interface RolesRepository extends CrudRepository<Role, Integer> {
    @Query("from Role where name = ?1")
    Role findByName(String name);

}
