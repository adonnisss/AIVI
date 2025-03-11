package com.evplatform.dao;

import com.evplatform.dao.interfaces.ProviderDAOInterface;
import com.evplatform.vao.Provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of ProviderDAOInterface using in-memory List collection.
 * Uses Singleton pattern to ensure only one instance exists.
 */
public class ProviderDAO implements ProviderDAOInterface {

    // Singleton instance with volatile for thread safety
    private static volatile ProviderDAO instance;

    // Thread-safe in-memory storage for providers
    private final List<Provider> providers = Collections.synchronizedList(new ArrayList<>());
    private int nextId = 1;

    // Private constructor for Singleton pattern
    public ProviderDAO() {
        // Private constructor prevents instantiation from outside
    }

    /**
     * Get the singleton instance of ProviderDAO using double-checked locking
     * @return ProviderDAO singleton instance
     */
    public static ProviderDAO getInstance() {
        if (instance == null) { // First check (without locking)
            synchronized (ProviderDAO.class) { // Lock only for first call
                if (instance == null) { // Second check (with locking)
                    instance = new ProviderDAO();
                }
            }
        }
        return instance;
    }

    @Override
    public int add(Provider provider) {
        synchronized (providers) {
            provider.setId(nextId++);
            providers.add(provider);
            return provider.getId();
        }
    }

    @Override
    public Provider getById(int id) {
        return findById(id).orElse(null);
    }

    @Override
    public List<Provider> getAll() {
        synchronized (providers) {
            return new ArrayList<>(providers); // Return a copy to prevent ConcurrentModificationException
        }
    }

    @Override
    public boolean update(Provider provider) {
        synchronized (providers) {
            Optional<Provider> existingProvider = findById(provider.getId());

            if (existingProvider.isPresent()) {
                int index = providers.indexOf(existingProvider.get());
                providers.set(index, provider);
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        synchronized (providers) {
            return providers.removeIf(provider -> provider.getId() == id);
        }
    }

    /**
     * Find a provider by ID
     * @param id Provider ID
     * @return Optional containing the provider if found
     */
    @Override
    public Optional<Provider> findById(int id) {
        synchronized (providers) {
            return providers.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst();
        }
    }
}