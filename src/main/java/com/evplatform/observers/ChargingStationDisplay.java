package com.evplatform.observers;

import com.evplatform.service.interfaces.ChargingStationServiceInterface;
import com.evplatform.vao.ChargingStation;

import java.util.List;
import java.util.stream.Collectors;

public class ChargingStationDisplay implements ChargingStationObserver {

    private final ChargingStationServiceInterface stationService;

    // The service is now passed in via the constructor
    public ChargingStationDisplay(ChargingStationServiceInterface stationService) {
        this.stationService = stationService;
    }

    @Override
    public void update(ChargingStation station, ChargingStation.ChargingStationStatus oldStatus) {
        if (station.getStatus() != oldStatus) {
            updateDisplay();
        }
    }

    private void updateDisplay() {
        List<ChargingStation> allStations = stationService.getAllChargingStations();

        List<ChargingStation> availableStations = allStations.stream()
                .filter(s -> s.getStatus() == ChargingStation.ChargingStationStatus.AVAILABLE)
                .collect(Collectors.toList());

        List<ChargingStation> occupiedStations = allStations.stream()
                .filter(s -> s.getStatus() == ChargingStation.ChargingStationStatus.OCCUPIED)
                .collect(Collectors.toList());

        String availableNames = availableStations.stream()
                .map(ChargingStation::getName)
                .collect(Collectors.joining(", "));

        String occupiedNames = occupiedStations.stream()
                .map(ChargingStation::getName)
                .collect(Collectors.joining(", "));

        System.out.println("\n📟 [Zaslon polnilne postaje] Trenutno stanje polnilnic:");
        System.out.println("✅ Proste polnilnice: " + (availableNames.isEmpty() ? "Ni prostih polnilnic" : availableNames));
        System.out.println("⛔ Zasedene polnilnice: " + (occupiedNames.isEmpty() ? "Ni zasedenih polnilnic" : occupiedNames));
    }
}