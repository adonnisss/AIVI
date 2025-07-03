package com.evplatform.iterators;

import com.evplatform.service.interfaces.ProviderServiceInterface;
import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.Provider;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.Iterator;
import java.util.List;

@Stateless
public class IteratorProvider {

    @EJB
    private ProviderServiceInterface providerService;

    public IteratorProvider() {
    }

    public Iterator<ChargingStation> getActiveStationIterator(int providerId) {
        Provider provider = providerService.getProviderById(providerId);
        if (provider == null) {
            throw new IllegalArgumentException("Provider not found with ID: " + providerId);
        }
        return new ActiveStationIterator(provider);
    }

    public Iterator<ChargingStation> getSpeedStationIterator(int providerId, double minPower) {
        Provider provider = providerService.getProviderById(providerId);
        if (provider == null) {
            throw new IllegalArgumentException("Provider not found with ID: " + providerId);
        }
        return new SpeedStationIterator(provider, minPower);
    }

    public Iterator<ChargingStation> getRegionStationIterator(int providerId, String region) {
        Provider provider = providerService.getProviderById(providerId);
        if (provider == null) {
            throw new IllegalArgumentException("Provider not found with ID: " + providerId);
        }
        return new RegionStationIterator(provider, region);
    }

    public Iterator<ChargingStation> getAllStationsSortedIterator() {
        List<Provider> providers = providerService.getAllProviders();
        return new AllStationsSortedIterator(providers);
    }
}