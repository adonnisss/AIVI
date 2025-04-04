package com.evplatform.iterators;

import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.Provider;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class SpeedStationIterator implements Iterator<ChargingStation> {

    private final Iterator<ChargingStation> iterator;
    private ChargingStation nextStation;
    private final double minPower;


    public SpeedStationIterator(Provider provider, double minPower) {
        this.iterator = provider.getChargingStations().iterator();
        this.minPower = minPower;
        advance();
    }


    private void advance() {
        while (iterator.hasNext()) {
            ChargingStation potential = iterator.next();
            if (potential.getMaxPowerKw() >= minPower) {
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