package com.evplatform.dao.interfaces;

import com.evplatform.vao.ChargingStation;
import java.util.List;
import java.util.Optional;

/**
 * Interface for ChargingStation data access operations.
 * Defines CRUD methods for ChargingStation entities.
 */
public interface ChargingStationDAOInterface {

    /**
     * Add a new charging station
     * @param station ChargingStation to add
     * @return ID of the newly added charging station
     */
    int add(ChargingStation station);

    /**
     * Get a charging station by ID
     * @param id ChargingStation ID
     * @return ChargingStation object or null if not found
     */
    ChargingStation getById(int id);

    /**
     * Get all charging stations
     * @return List of all charging stations
     */
    List<ChargingStation> getAll();

    /**
     * Update an existing charging station
     * @param station Updated charging station object
     * @return true if update was successful, false otherwise
     */
    boolean update(ChargingStation station);

    /**
     * Delete a charging station by ID
     * @param id ID of the charging station to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(int id);

    /**
     * Get all charging stations for a specific provider
     * @param providerId ID of the provider
     * @return List of charging stations owned by the specified provider
     */
    List<ChargingStation> getByProviderId(int providerId);

    /**
     * Update the status of a charging station
     * @param id ID of the charging station
     * @param status New status
     * @return true if update was successful, false otherwise
     */
    boolean updateStatus(int id, ChargingStation.ChargingStationStatus status);

    /**
     * Find a charging station by ID
     * @param id ChargingStation ID
     * @return Optional containing the charging station if found
     */
    default Optional<ChargingStation> findById(int id) {
        ChargingStation station = getById(id);
        return Optional.ofNullable(station);
    }
}