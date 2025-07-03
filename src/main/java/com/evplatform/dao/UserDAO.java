package com.evplatform.dao;

import com.evplatform.dao.interfaces.UserDAOInterface;
import com.evplatform.vao.User;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
public class UserDAO implements UserDAOInterface {

    private final List<User> users = Collections.synchronizedList(new ArrayList<>());
    private int nextId = 1;

    public UserDAO() {
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
            return new ArrayList<>(users);
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