package com.evplatform.dao.interfaces;

import com.evplatform.vao.User;
import jakarta.ejb.Local; // <- DODAJTE
import java.util.List;
import java.util.Optional;

@Local
public interface UserDAOInterface {
    int add(User user);
    User getById(int id);
    User getByEmail(String email);
    List<User> getAll();
    boolean update(User user);
    boolean delete(int id);
    Optional<User> findById(int id);
    Optional<User> findByEmail(String email);
}