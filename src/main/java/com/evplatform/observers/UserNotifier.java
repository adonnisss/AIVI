package com.evplatform.observers;

import com.evplatform.vao.ChargingStation;

public class UserNotifier implements ChargingStationObserver {
    @Override
    public void update(ChargingStation station, ChargingStation.ChargingStationStatus oldStatus) {
        String userEmail = station.getCurrentUserEmail();

        // Only send notifications if we have a user email
        if (userEmail != null && !userEmail.isEmpty()) {
            // Status changed to OCCUPIED (charging started)
            if (station.getStatus() == ChargingStation.ChargingStationStatus.OCCUPIED &&
                    oldStatus != ChargingStation.ChargingStationStatus.OCCUPIED) {
                sendChargingStartedEmail(station, userEmail);
            }

            // Status changed from OCCUPIED to AVAILABLE (charging ended)
            else if (station.getStatus() == ChargingStation.ChargingStationStatus.AVAILABLE &&
                    oldStatus == ChargingStation.ChargingStationStatus.OCCUPIED) {
                sendChargingEndedEmail(station, userEmail);
            }
        }
    }

    private void sendChargingStartedEmail(ChargingStation station, String userEmail) {
        System.out.println("\n📩 [EMAIL] Od: noreply@chargingstations.com");
        System.out.println("📩 Za: " + userEmail);
        System.out.println("📩 Zadeva: Polnjenje se je začelo! ⚡");
        System.out.println();
        System.out.println("Pozdravljeni,");
        System.out.println();
        System.out.println("vaše polnjenje na polnilnici **" + station.getName() + "** se je uspešno začelo.");
        System.out.println("🚗 Moč polnjenja: " + station.getMaxPowerKw() + " kW");
        System.out.println();
        System.out.println("Lep pozdrav,");
        System.out.println("[" + (station.getProvider() != null ? station.getProvider().getName() : "Ponudnik") + "]");
        System.out.println("-------------------------------------------------");
    }

    private void sendChargingEndedEmail(ChargingStation station, String userEmail) {
        System.out.println("\n📩 [EMAIL] Od: noreply@chargingstations.com");
        System.out.println("📩 Za: " + userEmail);
        System.out.println("📩 Zadeva: Polnjenje končano! ✅");
        System.out.println();
        System.out.println("Pozdravljeni,");
        System.out.println();
        System.out.println("vaše polnjenje na polnilnici **" + station.getName() + "** je končano.");
        System.out.println("🔌");
        System.out.println();
        System.out.println("Lep pozdrav,");
        System.out.println("[" + (station.getProvider() != null ? station.getProvider().getName() : "Ponudnik") + "]");
    }
}