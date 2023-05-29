package com.thelobsterpot.userauthorization.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Extends JpaRepository for basic CRUD operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /** Find a user by username */
    User findByUsername(String username);
    /** Find a user by email */
    User findByEmail(String email);
}
