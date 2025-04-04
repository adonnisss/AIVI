package com.evplatform.service.interfaces;

import com.evplatform.vao.User;
import java.util.List;
import java.util.Optional;

/**
 * Interface for User business logic operations.
 */
public interface UserServiceInterface {

    /**
     * Add a new user with validation
     * @param user User to add
     * @return ID of the newly added user
     * @throws IllegalArgumentException if user data is invalid
     */
    int addUser(User user) throws IllegalArgumentException;

    /**
     * Get a user by ID
     * @param id User ID
     * @return User object or null if not found
     */
    User getUserById(int id);

    /**
     * Get a user by email
     * @param email User email
     * @return User object or null if not found
     */
    User getUserByEmail(String email);

    /**
     * Find a user by ID and return as Optional
     * @param id User ID
     * @return Optional containing the user if found
     */
    Optional<User> findUserById(int id);

    /**
     * Find a user by email and return as Optional
     * @param email User email
     * @return Optional containing the user if found
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Get all users
     * @return List of all users
     */
    List<User> getAllUsers();

    /**
     * Update an existing user with validation
     * @param user Updated user object
     * @return true if update was successful, false otherwise
     * @throws IllegalArgumentException if user data is invalid
     */
    boolean updateUser(User user) throws IllegalArgumentException;

    /**
     * Delete a user by ID
     * @param id ID of the user to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteUser(int id);

    /**
     * Add funds to user's balance
     * @param userId ID of the user
     * @param amount Amount to add
     * @return Updated user balance
     * @throws IllegalArgumentException if amount is negative or user not found
     */
    double addFunds(int userId, double amount) throws IllegalArgumentException;

    /**
     * Deduct funds from user's balance
     * @param userId ID of the user
     * @param amount Amount to deduct
     * @return true if deduction was successful, false if insufficient funds
     * @throws IllegalArgumentException if amount is negative or user not found
     */
    boolean deductFunds(int userId, double amount) throws IllegalArgumentException;
}