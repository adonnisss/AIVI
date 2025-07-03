package com.evplatform.dao;

import com.evplatform.dao.interfaces.ChargingStationDAOInterface;
import com.evplatform.vao.ChargingStation;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class ChargingStationDAO implements ChargingStationDAOInterface {

    private final List<ChargingStation> stations = Collections.synchronizedList(new ArrayList<>());
    private int nextId = 1;

    public ChargingStationDAO() {
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
            return new ArrayList<>(stations);
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

    @Override
    public Optional<ChargingStation> findById(int id) {
        synchronized (stations) {
            return stations.stream()
                    .filter(s -> s.getId() == id)
                    .findFirst();
        }
    }
}