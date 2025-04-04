package com.evplatform.iterators;

import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.Provider;

import java.util.*;


public class AllStationsSortedIterator implements Iterator<ChargingStation> {

    private final Iterator<ChargingStation> iterator;


    public AllStationsSortedIterator(List<Provider> providers) {
        // Collect all stations from all providers
        List<ChargingStation> allStations = new ArrayList<>();
        for (Provider provider : providers) {
            allStations.addAll(provider.getChargingStations());
        }

        // Sort stations by name
        allStations.sort(Comparator.comparing(ChargingStation::getName));

        this.iterator = allStations.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public ChargingStation next() {
        return iterator.next();
    }
}