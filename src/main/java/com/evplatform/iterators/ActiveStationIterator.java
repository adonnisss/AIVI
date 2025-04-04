package com.evplatform.iterators;

import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.Provider;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ActiveStationIterator implements Iterator<ChargingStation> {

    private final Iterator<ChargingStation> iterator;
    private ChargingStation nextStation;


    public ActiveStationIterator(Provider provider) {
        this.iterator = provider.getChargingStations().iterator();
        advance();
    }

    private void advance() {
        while (iterator.hasNext()) {
            ChargingStation potential = iterator.next();
            if (potential.getStatus() == ChargingStation.ChargingStationStatus.AVAILABLE) {
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