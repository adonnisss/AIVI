package com.evplatform.service;

import com.evplatform.dao.ChargingStationDAO;
import com.evplatform.dao.ProviderDAO;
import com.evplatform.dao.interfaces.ChargingStationDAOInterface;
import com.evplatform.dao.interfaces.ProviderDAOInterface;
import com.evplatform.service.interfaces.ProviderServiceInterface;
import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ProviderServiceInterface.
 * Contains business logic for Provider operations.
 */
public class ProviderService implements ProviderServiceInterface {

    private static volatile ProviderService instance;
    private final ProviderDAOInterface providerDAO;
    private final ChargingStationDAOInterface stationDAO;

    /**
     * Private constructor for Singleton pattern
     */
    private ProviderService() {
        this.providerDAO = ProviderDAO.getInstance();
        this.stationDAO = ChargingStationDAO.getInstance();
    }

    /**
     * Get the singleton instance of ProviderService
     * @return ProviderService singleton instance
     */
    public static ProviderService getInstance() {
        if (instance == null) {
            synchronized (ProviderService.class) {
                if (instance == null) {
                    instance = new ProviderService();
                }
            }
        }
        return instance;
    }

    /**
     * Validates provider data
     * @param provider Provider to validate
     * @throws IllegalArgumentException if data is invalid
     */
    private void validateProvider(Provider provider) throws IllegalArgumentException {
        if (provider == null) {
            throw new IllegalArgumentException("Provider cannot be null");
        }

        if (provider.getName() == null || provider.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Provider name cannot be empty");
        }

        if (provider.getEmail() != null && !provider.getEmail().isEmpty() && !provider.getEmail().contains("@")) {
            throw new IllegalArgumentException("Provider email is invalid");
        }

        // Additional validation can be added here
    }

    @Override
    public int addProvider(Provider provider) throws IllegalArgumentException {
        validateProvider(provider);
        // Create a clean list to avoid references to non-saved stations
        provider.setChargingStations(new ArrayList<>());
        return providerDAO.add(provider);
    }

    @Override
    public Provider getProviderById(int id) {
        Provider provider = providerDAO.getById(id);
        if (provider != null) {
            // Load all charging stations for this provider
            List<ChargingStation> stations = stationDAO.getByProviderId(id);
            for (ChargingStation station : stations) {
                station.setProvider(provider);
            }
        }
        return provider;
    }

    @Override
    public Optional<Provider> findProviderById(int id) {
        return providerDAO.findById(id);
    }

    @Override
    public List<Provider> getAllProviders() {
        List<Provider> providers = providerDAO.getAll();

        // For each provider, load its charging stations
        for (Provider provider : providers) {
            List<ChargingStation> stations = stationDAO.getByProviderId(provider.getId());
            for (ChargingStation station : stations) {
                station.setProvider(provider);
            }
        }

        return providers;
    }

    @Override
    public boolean updateProvider(Provider provider) throws IllegalArgumentException {
        validateProvider(provider);
        return providerDAO.update(provider);
    }

    @Override
    public boolean deleteProvider(int id) throws IllegalStateException {
        // Check if provider has charging stations
        if (!stationDAO.getByProviderId(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete provider with ID " + id +
                    " because it has associated charging stations. Delete the charging stations first.");
        }

        return providerDAO.delete(id);
    }
}