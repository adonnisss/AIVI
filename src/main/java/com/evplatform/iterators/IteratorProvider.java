package com.evplatform.iterators;

import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.Provider;
import com.evplatform.service.ProviderService;

import java.util.Iterator;
import java.util.List;


public class IteratorProvider {

    private static volatile IteratorProvider instance;
    private final ProviderService providerService;

    private IteratorProvider() {
        this.providerService = ProviderService.getInstance();
    }

    public static IteratorProvider getInstance() {
        if (instance == null) {
            synchronized (IteratorProvider.class) {
                if (instance == null) {
                    instance = new IteratorProvider();
                }
            }
        }
        return instance;
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