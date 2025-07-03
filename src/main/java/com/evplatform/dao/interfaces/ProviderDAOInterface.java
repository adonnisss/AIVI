package com.evplatform.dao.interfaces;

import com.evplatform.vao.Provider;
import jakarta.ejb.Local;
import java.util.List;
import java.util.Optional;
/**
 * Interface for Provider data access operations.
 * Defines CRUD methods for Provider entities.
 */
@Local
public interface ProviderDAOInterface {

    /**
     * Add a new provider
     * @param provider Provider to add
     * @return ID of the newly added provider
     */
    int add(Provider provider);

    /**
     * Get a provider by ID
     * @param id Provider ID
     * @return Provider object or null if not found
     */
    Provider getById(int id);

    /**
     * Get all providers
     * @return List of all providers
     */
    List<Provider> getAll();

    /**
     * Update an existing provider
     * @param provider Updated provider object
     * @return true if update was successful, false otherwise
     */
    boolean update(Provider provider);

    /**
     * Delete a provider by ID
     * @param id ID of the provider to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(int id);

    /**
     * Find a provider by ID
     * @param id Provider ID
     * @return Optional containing the provider if found
     */
    default Optional<Provider> findById(int id) {
        Provider provider = getById(id);
        return Optional.ofNullable(provider);
    }
}