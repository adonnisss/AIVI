package com.evplatform.service.interfaces;

import com.evplatform.vao.ChargingStation;
import java.util.List;
import java.util.Optional;

/**
 * Interface for ChargingStation business logic operations.
 */
public interface ChargingStationServiceInterface {

    /**
     * Add a new charging station with validation
     * @param station ChargingStation to add
     * @return ID of the newly added charging station
     * @throws IllegalArgumentException if station data is invalid
     * @throws IllegalStateException if the provider does not exist
     */
    int addChargingStation(ChargingStation station) throws IllegalArgumentException, IllegalStateException;

    /**
     * Get a charging station by ID
     * @param id ChargingStation ID
     * @return ChargingStation object or null if not found
     */
    ChargingStation getChargingStationById(int id);

    /**
     * Find a charging station by ID and return as Optional
     * @param id ChargingStation ID
     * @return Optional containing the charging station if found
     */
    Optional<ChargingStation> findChargingStationById(int id);

    /**
     * Get all charging stations
     * @return List of all charging stations
     */
    List<ChargingStation> getAllChargingStations();

    /**
     * Update an existing charging station with validation
     * @param station Updated charging station object
     * @return true if update was successful, false otherwise
     * @throws IllegalArgumentException if station data is invalid
     * @throws IllegalStateException if the provider does not exist
     */
    boolean updateChargingStation(ChargingStation station) throws IllegalArgumentException, IllegalStateException;

    /**
     * Delete a charging station by ID
     * @param id ID of the charging station to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteChargingStation(int id);

    /**
     * Get all charging stations for a specific provider
     * @param providerId ID of the provider
     * @return List of charging stations owned by the specified provider
     * @throws IllegalArgumentException if provider ID is invalid
     */
    List<ChargingStation> getChargingStationsByProviderId(int providerId) throws IllegalArgumentException;

    /**
     * Update the status of a charging station
     * @param id ID of the charging station
     * @param status New status
     * @return true if update was successful, false otherwise
     * @throws IllegalArgumentException if status is null
     */
    boolean updateChargingStationStatus(int id, ChargingStation.ChargingStationStatus status) throws IllegalArgumentException;
}