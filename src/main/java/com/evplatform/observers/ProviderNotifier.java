package com.evplatform.observers;

import com.evplatform.vao.ChargingStation;

public class ProviderNotifier implements ChargingStationObserver {
    @Override
    public void update(ChargingStation station, ChargingStation.ChargingStationStatus oldStatus) {
        // Only notify if status actually changed
        if (station.getStatus() != oldStatus) {
            String status = getStatusText(station.getStatus());
            String providerName = (station.getProvider() != null) ? station.getProvider().getName() : "neznan ponudnik";

            System.out.println("\n🏢 Ponudnik obveščen: Polnilnica " + station.getName() +
                    " pri ponudniku " + providerName +
                    " je zdaj " + status + ".");
        }
    }

    private String getStatusText(ChargingStation.ChargingStationStatus status) {
        switch (status) {
            case AVAILABLE:
                return "prosta";
            case OCCUPIED:
                return "zasedena";
            case OUT_OF_SERVICE:
                return "izven uporabe";
            case MAINTENANCE:
                return "v vzdrževanju";
            default:
                return "v neznanem stanju";
        }
    }
}