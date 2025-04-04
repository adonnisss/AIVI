package com.evplatform.observers;

import com.evplatform.service.ChargingStationService;
import com.evplatform.vao.ChargingStation;

import java.util.List;
import java.util.stream.Collectors;


public class ChargingStationDisplay implements ChargingStationObserver {
    private final ChargingStationService stationService;

    public ChargingStationDisplay() {
        this.stationService = ChargingStationService.getInstance();
    }

    @Override
    public void update(ChargingStation station, ChargingStation.ChargingStationStatus oldStatus) {
        // Only update the display if status has changed
        if (station.getStatus() != oldStatus) {
            updateDisplay();
        }
    }

    private void updateDisplay() {
        List<ChargingStation> allStations = stationService.getAllChargingStations();

        // Group stations by status
        List<ChargingStation> availableStations = allStations.stream()
                .filter(s -> s.getStatus() == ChargingStation.ChargingStationStatus.AVAILABLE)
                .collect(Collectors.toList());

        List<ChargingStation> occupiedStations = allStations.stream()
                .filter(s -> s.getStatus() == ChargingStation.ChargingStationStatus.OCCUPIED)
                .collect(Collectors.toList());

        // Format station names as comma-separated strings
        String availableNames = availableStations.stream()
                .map(ChargingStation::getName)
                .collect(Collectors.joining(", "));

        String occupiedNames = occupiedStations.stream()
                .map(ChargingStation::getName)
                .collect(Collectors.joining(", "));

        // Display the status
        System.out.println("\n📟 [Zaslon polnilne postaje] Trenutno stanje polnilnic:");
        System.out.println("✅ Proste polnilnice: " + (availableNames.isEmpty() ? "Ni prostih polnilnic" : availableNames));
        System.out.println("⛔ Zasedene polnilnice: " + (occupiedNames.isEmpty() ? "Ni zasedenih polnilnic" : occupiedNames));
    }
}