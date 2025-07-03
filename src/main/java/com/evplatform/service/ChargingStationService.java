package com.evplatform.service;

import com.evplatform.dao.interfaces.ChargingStationDAOInterface;
import com.evplatform.dao.interfaces.ProviderDAOInterface;
import com.evplatform.observers.ObserverManager;
import com.evplatform.service.interfaces.ChargingStationServiceInterface;
import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.Provider;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.Optional;

@Stateless
public class ChargingStationService implements ChargingStationServiceInterface {

    @EJB
    private ChargingStationDAOInterface stationDAO;

    @EJB
    private ProviderDAOInterface providerDAO;

    @EJB // Injected the ObserverManager
    private ObserverManager observerManager;

    public ChargingStationService() {
    }

    private void validateChargingStation(ChargingStation station) throws IllegalArgumentException, IllegalStateException {
        if (station == null) {
            throw new IllegalArgumentException("Charging station cannot be null");
        }
        if (station.getName() == null || station.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Charging station name cannot be empty");
        }
        if (station.getNumberOfConnectors() <= 0) {
            throw new IllegalArgumentException("Number of connectors must be greater than zero");
        }
        if (station.getMaxPowerKw() <= 0) {
            throw new IllegalArgumentException("Maximum power must be greater than zero");
        }
        if (station.getStatus() == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (station.getProviderId() > 0 && providerDAO.getById(station.getProviderId()) == null) {
            throw new IllegalStateException("Provider with ID " + station.getProviderId() + " does not exist");
        }
    }

    @Override
    public int addChargingStation(ChargingStation station) throws IllegalArgumentException, IllegalStateException {
        validateChargingStation(station);
        Provider provider = station.getProvider();
        if (provider != null) {
            station.setProviderId(provider.getId());
        } else if (station.getProviderId() > 0) {
            Provider existingProvider = providerDAO.getById(station.getProviderId());
            if (existingProvider != null) {
                station.setProvider(existingProvider);
            }
        }
        int id = stationDAO.add(station);

        // Use the injected observerManager instance
        observerManager.registerObservers(station);

        return id;
    }

    @Override
    public ChargingStation getChargingStationById(int id) {
        ChargingStation station = stationDAO.getById(id);
        if (station != null && station.getProviderId() > 0) {
            Provider provider = providerDAO.getById(station.getProviderId());
            if (provider != null) {
                station.setProvider(provider);
            }
        }
        return station;
    }

    @Override
    public Optional<ChargingStation> findChargingStationById(int id) {
        return stationDAO.findById(id);
    }

    @Override
    public List<ChargingStation> getAllChargingStations() {
        List<ChargingStation> stations = stationDAO.getAll();
        for (ChargingStation station : stations) {
            if (station.getProviderId() > 0) {
                Provider provider = providerDAO.getById(station.getProviderId());
                if (provider != null) {
                    station.setProvider(provider);
                }
            }
        }
        return stations;
    }

    @Override
    public boolean updateChargingStation(ChargingStation station) throws IllegalArgumentException, IllegalStateException {
        validateChargingStation(station);
        return stationDAO.update(station);
    }

    @Override
    public boolean deleteChargingStation(int id) {
        return stationDAO.delete(id);
    }

    @Override
    public List<ChargingStation> getChargingStationsByProviderId(int providerId) throws IllegalArgumentException {
        Provider provider = providerDAO.getById(providerId);
        if (provider == null) {
            throw new IllegalArgumentException("Provider with ID " + providerId + " does not exist");
        }
        return stationDAO.getByProviderId(providerId);
    }

    @Override
    public boolean updateChargingStationStatus(int id, ChargingStation.ChargingStationStatus status) throws IllegalArgumentException {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        return stationDAO.updateStatus(id, status);
    }
}