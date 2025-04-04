package com.evplatform.dao.interfaces;

import com.evplatform.vao.User;
import java.util.List;
import java.util.Optional;

/**
 * Interface for User data access operations.
 * Defines CRUD methods for User entities.
 */
public interface UserDAOInterface {

    /**
     * Add a new user
     * @param user User to add
     * @return ID of the newly added user
     */
    int add(User user);

    /**
     * Get a user by ID
     * @param id User ID
     * @return User object or null if not found
     */
    User getById(int id);

    /**
     * Get a user by email
     * @param email User email
     * @return User object or null if not found
     */
    User getByEmail(String email);

    /**
     * Get all users
     * @return List of all users
     */
    List<User> getAll();

    /**
     * Update an existing user
     * @param user Updated user object
     * @return true if update was successful, false otherwise
     */
    boolean update(User user);

    /**
     * Delete a user by ID
     * @param id ID of the user to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(int id);

    /**
     * Find a user by ID
     * @param id User ID
     * @return Optional containing the user if found
     */
    default Optional<User> findById(int id) {
        User user = getById(id);
        return Optional.ofNullable(user);
    }

    /**
     * Find a user by email
     * @param email User email
     * @return Optional containing the user if found
     */
    default Optional<User> findByEmail(String email) {
        User user = getByEmail(email);
        return Optional.ofNullable(user);
    }
}