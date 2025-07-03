package com.evplatform.service;

import com.evplatform.dao.interfaces.ChargingStationDAOInterface;
import com.evplatform.dao.interfaces.ProviderDAOInterface;
import com.evplatform.service.interfaces.ProviderServiceInterface;
import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.Provider;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
public class ProviderService implements ProviderServiceInterface {

    @EJB
    private ProviderDAOInterface providerDAO;

    @EJB
    private ChargingStationDAOInterface stationDAO;

    public ProviderService() {
    }

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
    }

    @Override
    public int addProvider(Provider provider) throws IllegalArgumentException {
        validateProvider(provider);
        provider.setChargingStations(new ArrayList<>());
        return providerDAO.add(provider);
    }

    @Override
    public Provider getProviderById(int id) {
        Provider provider = providerDAO.getById(id);
        if (provider != null) {
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
        if (!stationDAO.getByProviderId(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete provider with ID " + id +
                    " because it has associated charging stations. Delete the charging stations first.");
        }
        return providerDAO.delete(id);
    }
}