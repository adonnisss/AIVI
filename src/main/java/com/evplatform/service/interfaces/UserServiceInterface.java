package com.evplatform.service.interfaces;

import com.evplatform.vao.User;
import jakarta.ejb.Local;
import java.util.List;
import java.util.Optional;

@Local
public interface UserServiceInterface {
    int addUser(User user) throws IllegalArgumentException;
    User getUserById(int id);
    User getUserByEmail(String email);
    Optional<User> findUserById(int id);
    Optional<User> findUserByEmail(String email);
    List<User> getAllUsers();
    boolean updateUser(User user) throws IllegalArgumentException;
    boolean deleteUser(int id);
    double addFunds(int userId, double amount) throws IllegalArgumentException;
    boolean deductFunds(int userId, double amount) throws IllegalArgumentException;
}