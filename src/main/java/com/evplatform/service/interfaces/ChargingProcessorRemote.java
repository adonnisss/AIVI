package com.evplatform.service.interfaces;

import jakarta.ejb.Remote;

@Remote
public interface ChargingProcessorRemote {

    boolean processChargingRequest(int userId, int stationId, double estimatedCost);
    boolean stopCharging(int stationId);
}