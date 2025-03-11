package com.evplatform.service.interfaces;

import com.evplatform.vao.Provider;
import java.util.List;
import java.util.Optional;

/**
 * Interface for Provider business logic operations.
 */
public interface ProviderServiceInterface {

    /**
     * Add a new provider with validation
     * @param provider Provider to add
     * @return ID of the newly added provider
     * @throws IllegalArgumentException if provider data is invalid
     */
    int addProvider(Provider provider) throws IllegalArgumentException;

    /**
     * Get a provider by ID
     * @param id Provider ID
     * @return Provider object or null if not found
     */
    Provider getProviderById(int id);

    /**
     * Find a provider by ID and return as Optional
     * @param id Provider ID
     * @return Optional containing the provider if found
     */
    Optional<Provider> findProviderById(int id);

    /**
     * Get all providers
     * @return List of all providers
     */
    List<Provider> getAllProviders();

    /**
     * Update an existing provider with validation
     * @param provider Updated provider object
     * @return true if update was successful, false otherwise
     * @throws IllegalArgumentException if provider data is invalid
     */
    boolean updateProvider(Provider provider) throws IllegalArgumentException;

    /**
     * Delete a provider by ID
     * @param id ID of the provider to delete
     * @return true if deletion was successful, false otherwise
     * @throws IllegalStateException if provider has associated charging stations
     */
    boolean deleteProvider(int id) throws IllegalStateException;
}