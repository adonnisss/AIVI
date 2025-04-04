package com.evplatform.observers;

import com.evplatform.vao.ChargingStation;

public interface ChargingStationObserver {
    void update(ChargingStation station, ChargingStation.ChargingStationStatus oldStatus);
}