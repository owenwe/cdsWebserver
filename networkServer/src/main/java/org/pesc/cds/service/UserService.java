package org.pesc.cds.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pesc.cds.repository.StringUtils;
import org.pesc.cds.model.AuthUser;
import org.pesc.cds.model.Credentials;
import org.pesc.cds.model.Role;
import org.pesc.cds.repository.RolesRepository;
import org.pesc.cds.repository.UserRepository;
import org.pesc.cds.web.AppController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Prabhu Rajendran (prajendran@ccctechcenter.org) on 11/10/16.
 */
@Service
public class UserService {

    private static final Log log = LogFactory.getLog(UserService.class);

    protected EntityManagerFactory entityManagerFactory;
    private List<Role> roles;

    public static final String PASSWORD_REQUIREMENTS = "The password must be at least 15 characters long, contain 1 upper case letter, 1 lower case letter, 1 number and 1 special character @$ _!%*#?&.";

    private Pattern passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$ _!%*#?&])[A-Za-z\\d$@$ _!%*#?&]{15,}$");

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    private Role systemAdminRole;
    private Role counselor;


    @Autowired
    public UserService(EntityManagerFactory entityManagerFactory, RolesRepository rolesRepo) {

        this.entityManagerFactory = entityManagerFactory;

        roles = (List<Role>)rolesRepo.findAll();
        systemAdminRole = rolesRepo.findByName("ADMIN");
        counselor = rolesRepo.findByName("COUNSELOR");

    }


    public void validatePassword(String password) {
        if (!passwordPattern.matcher(password).matches()) {
            throw new IllegalArgumentException(PASSWORD_REQUIREMENTS);
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updatePassword(Integer userID, String password) throws IllegalArgumentException {

        validatePassword(password);
        jdbcTemplate.update(
                "update users set password = ? where id = ?", passwordEncoder.encode(password), userID);
    }


    @Autowired
    private UserRepository userRepository;

    public List<Role> getRoles() {
        return roles;
    }

    @Transactional(readOnly=true)
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<Credentials> findAll(){
        return this.userRepository.findAll();
    }

    @Transactional(readOnly=false,propagation = Propagation.REQUIRED)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Credentials user)  {

        this.userRepository.delete(user);
    }

    @Transactional(readOnly=false,propagation = Propagation.REQUIRED)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Integer id)  {
        this.userRepository.delete(id);
    }

    @Transactional(readOnly=true,propagation = Propagation.REQUIRED)
    public List<Credentials> findByName(String name)  {
        return this.userRepository.findByName(name);
    }


    @Transactional(readOnly=true,propagation = Propagation.REQUIRED)
    public List<Credentials> findByUsername(String username)  {
        return this.userRepository.findByUserName(username);
    }


    @Transactional(readOnly=true,propagation = Propagation.REQUIRED)
    public Credentials findById(Integer id)  {
        return this.userRepository.findOne(id);
    }

    void validateRoles(Credentials user) {
        if (user.getRoles().isEmpty()) {
            throw new IllegalArgumentException("A user must have at least one assigned role.");
        }
    }

    void checkPermissionsForRoleAssignment(Credentials user) {

        AuthUser auth = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if (!AppController.hasRole(auth.getAuthorities(), "ADMIN")) {
            if (user.getRoles().contains(systemAdminRole)) {
                throw new IllegalArgumentException("You do not have the authority to create a system administrator.") ;
            }
            else if ( user.getRoles().contains(counselor)){
                throw new IllegalArgumentException("You do not have the authority to create a counselor.") ;
            }
        }

    }

    /**
     * create new user
     * @param user
     * @return
     */
    @Transactional(readOnly=false,propagation = Propagation.REQUIRED)
    @PreAuthorize("hasRole('ADMIN')")
    public Credentials create(Credentials user)  {
        checkPermissionsForRoleAssignment(user);
        validateRoles(user);
        return unsecuredCreate(user);
    }

    /**
     * Used during registration
     * @param user
     * @return
     */
    public Credentials unsecuredCreate(Credentials user) {
        validatePassword(user.getPassword());

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        user.setEnabled(true);

        return this.userRepository.save(user);
    }

    /**
     *  update a user
     * @param user
     * @return
     */
    @Transactional(readOnly=false,propagation = Propagation.REQUIRED)
    @PreAuthorize("hasRole('ADMIN')")
    public Credentials update(Credentials user)  {
        checkPermissionsForRoleAssignment(user);
        validateRoles(user);
        return userRepository.save(user);
    }


    /**
     * @param name
     * @return
     */
    @Transactional(readOnly=true,propagation = Propagation.REQUIRED)
    public List<Credentials> search(String name) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {


            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            CriteriaQuery<Credentials> cq = cb.createQuery(Credentials.class);
            Root<Credentials> user = cq.from(Credentials.class);
            cq.select(user);

            List<Predicate> predicates = new LinkedList<Predicate>();

            if (name != null) {
                predicates.add(cb.like(cb.lower(user.get("name")), "%" + name.trim().toLowerCase() + "%"));
            }

            Predicate[] predicateArray = new Predicate[predicates.size()];
            predicates.toArray(predicateArray);

            cq.where(predicateArray);
            TypedQuery<Credentials> q = entityManager.createQuery(cq);

            return q.getResultList();


        } catch(Exception ex) {
            log.error("Failed to execute user search query.", ex);
        }
        finally {
           entityManager.close();
        }
        return new ArrayList<Credentials>();
    }

}
