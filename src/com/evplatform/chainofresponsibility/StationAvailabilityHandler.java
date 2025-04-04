package com.evplatform.chainofresponsibility;

import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.User;

public class StationAvailabilityHandler implements ChargingRequestHandler {
    private ChargingRequestHandler next;

    @Override
    public void setNextHandler(ChargingRequestHandler next) {
        this.next = next;
    }

    @Override
    public boolean handleRequest(User user, ChargingStation station, double estimatedCost) {
        if (station == null) {
            System.out.println("Charging station not found");
            return false;
        }

        if (station.getStatus() != ChargingStation.ChargingStationStatus.AVAILABLE) {
            System.out.println("Charging station is not available. Current status: " + station.getStatus());
            return false;
        }

        return next != null ? next.handleRequest(user, station, estimatedCost) : true;
    }
}