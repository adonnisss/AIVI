package com.evplatform.iterators;

import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.Provider;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RegionStationIterator implements Iterator<ChargingStation> {

    private final Iterator<ChargingStation> iterator;
    private ChargingStation nextStation;
    private final String region;


    public RegionStationIterator(Provider provider, String region) {
        this.iterator = provider.getChargingStations().iterator();
        this.region = region;
        advance();
    }


    private void advance() {
        while (iterator.hasNext()) {
            ChargingStation potential = iterator.next();
            if (potential.getLocation() != null &&
                    potential.getLocation().toLowerCase().contains(region.toLowerCase())) {
                nextStation = potential;
                return;
            }
        }
        nextStation = null;
    }

    @Override
    public boolean hasNext() {
        return nextStation != null;
    }

    @Override
    public ChargingStation next() {
        if (nextStation == null) {
            throw new NoSuchElementException();
        }

        ChargingStation current = nextStation;
        advance();
        return current;
    }
}