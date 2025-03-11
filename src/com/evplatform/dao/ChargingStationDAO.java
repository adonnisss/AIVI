package com.evplatform.dao;

import com.evplatform.dao.interfaces.ChargingStationDAOInterface;
import com.evplatform.vao.ChargingStation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of ChargingStationDAOInterface using in-memory List collection.
 * Uses Singleton pattern to ensure only one instance exists.
 */
public class ChargingStationDAO implements ChargingStationDAOInterface {

    // Singleton instance with volatile for thread safety
    private static volatile ChargingStationDAO instance;

    // Thread-safe in-memory storage for charging stations
    private final List<ChargingStation> stations = Collections.synchronizedList(new ArrayList<>());
    private int nextId = 1;

    // Private constructor for Singleton pattern
    public ChargingStationDAO() {
        // Private constructor prevents instantiation from outside
    }

    /**
     * Get the singleton instance of ChargingStationDAO using double-checked locking
     * @return ChargingStationDAO singleton instance
     */
    public static ChargingStationDAO getInstance() {
        if (instance == null) { // First check (without locking)
            synchronized (ChargingStationDAO.class) { // Lock only for first call
                if (instance == null) { // Second check (with locking)
                    instance = new ChargingStationDAO();
                }
            }
        }
        return instance;
    }

    @Override
    public int add(ChargingStation station) {
        synchronized (stations) {
            station.setId(nextId++);
            stations.add(station);
            return station.getId();
        }
    }

    @Override
    public ChargingStation getById(int id) {
        return findById(id).orElse(null);
    }

    @Override
    public List<ChargingStation> getAll() {
        synchronized (stations) {
            return new ArrayList<>(stations); // Return a copy to prevent ConcurrentModificationException
        }
    }

    @Override
    public boolean update(ChargingStation station) {
        synchronized (stations) {
            Optional<ChargingStation> existingStation = findById(station.getId());

            if (existingStation.isPresent()) {
                int index = stations.indexOf(existingStation.get());
                stations.set(index, station);
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        synchronized (stations) {
            return stations.removeIf(station -> station.getId() == id);
        }
    }

    @Override
    public List<ChargingStation> getByProviderId(int providerId) {
        synchronized (stations) {
            return stations.stream()
                    .filter(station -> station.getProviderId() == providerId)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean updateStatus(int id, ChargingStation.ChargingStationStatus status) {
        synchronized (stations) {
            return findById(id)
                    .map(station -> {
                        station.setStatus(status);
                        return true;
                    })
                    .orElse(false);
        }
    }

    /**
     * Find a charging station by ID
     * @param id ChargingStation ID
     * @return Optional containing the charging station if found
     */
    @Override
    public Optional<ChargingStation> findById(int id) {
        synchronized (stations) {
            return stations.stream()
                    .filter(s -> s.getId() == id)
                    .findFirst();
        }
    }
}