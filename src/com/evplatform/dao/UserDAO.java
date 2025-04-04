package com.evplatform.dao;

import com.evplatform.dao.interfaces.UserDAOInterface;
import com.evplatform.vao.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of UserDAOInterface using in-memory List collection.
 * Uses Singleton pattern to ensure only one instance exists.
 */
public class UserDAO implements UserDAOInterface {

    // Singleton instance with volatile for thread safety
    private static volatile UserDAO instance;

    // Thread-safe in-memory storage for users
    private final List<User> users = Collections.synchronizedList(new ArrayList<>());
    private int nextId = 1;

    // Private constructor for Singleton pattern
    private UserDAO() {
        // Private constructor prevents instantiation from outside
    }

    /**
     * Get the singleton instance of UserDAO using double-checked locking
     * @return UserDAO singleton instance
     */
    public static UserDAO getInstance() {
        if (instance == null) { // First check (without locking)
            synchronized (UserDAO.class) { // Lock only for first call
                if (instance == null) { // Second check (with locking)
                    instance = new UserDAO();
                }
            }
        }
        return instance;
    }

    @Override
    public int add(User user) {
        synchronized (users) {
            user.setId(nextId++);
            users.add(user);
            return user.getId();
        }
    }

    @Override
    public User getById(int id) {
        return findById(id).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        synchronized (users) {
            return users.stream()
                    .filter(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email))
                    .findFirst()
                    .orElse(null);
        }
    }

    @Override
    public List<User> getAll() {
        synchronized (users) {
            return new ArrayList<>(users); // Return a copy to prevent ConcurrentModificationException
        }
    }

    @Override
    public boolean update(User user) {
        synchronized (users) {
            Optional<User> existingUser = findById(user.getId());

            if (existingUser.isPresent()) {
                int index = users.indexOf(existingUser.get());
                users.set(index, user);
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        synchronized (users) {
            return users.removeIf(user -> user.getId() == id);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        synchronized (users) {
            return users.stream()
                    .filter(u -> u.getId() == id)
                    .findFirst();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        synchronized (users) {
            return users.stream()
                    .filter(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email))
                    .findFirst();
        }
    }
}