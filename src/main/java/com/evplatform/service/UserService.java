package com.evplatform.service;

import com.evplatform.dao.interfaces.UserDAOInterface;
import com.evplatform.service.interfaces.UserServiceInterface;
import com.evplatform.vao.User;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.Optional;

@Stateless
public class UserService implements UserServiceInterface {

    @EJB
    private UserDAOInterface userDAO;

    public UserService() {
    }

    private void validateUser(User user) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be empty");
        }
        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("User email is invalid");
        }
        if (user.getCarType() == null) {
            throw new IllegalArgumentException("Car type cannot be null");
        }
        if (user.getId() == 0) {
            User existingUser = userDAO.getByEmail(user.getEmail());
            if (existingUser != null) {
                throw new IllegalArgumentException("Email already in use: " + user.getEmail());
            }
        } else {
            User existingUser = userDAO.getByEmail(user.getEmail());
            if (existingUser != null && existingUser.getId() != user.getId()) {
                throw new IllegalArgumentException("Email already in use: " + user.getEmail());
            }
        }
    }

    @Override
    public int addUser(User user) throws IllegalArgumentException {
        validateUser(user);
        return userDAO.add(user);
    }

    @Override
    public User getUserById(int id) {
        return userDAO.getById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDAO.getByEmail(email);
    }

    @Override
    public Optional<User> findUserById(int id) {
        return userDAO.findById(id);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    @Override
    public boolean updateUser(User user) throws IllegalArgumentException {
        validateUser(user);
        return userDAO.update(user);
    }

    @Override
    public boolean deleteUser(int id) {
        return userDAO.delete(id);
    }

    @Override
    public double addFunds(int userId, double amount) throws IllegalArgumentException {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot add negative amount");
        }
        User user = userDAO.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        user.addFunds(amount);
        userDAO.update(user);
        return user.getBalance();
    }

    @Override
    public boolean deductFunds(int userId, double amount) throws IllegalArgumentException {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot deduct negative amount");
        }
        User user = userDAO.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        boolean success = user.deductFunds(amount);
        if (success) {
            userDAO.update(user);
        }
        return success;
    }
}