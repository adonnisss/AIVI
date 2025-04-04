package com.evplatform.web;

import com.evplatform.service.ChargingStationService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.util.logging.Logger;

@Named
@RequestScoped
public class ChargingStationBean {
    private static final Logger logger = Logger.getLogger(ChargingStationBean.class.getName());

    // Method to print all charging stations to console
    public void printStationsToConsole() {
        logger.info("===== ALL CHARGING STATIONS =====");

        ChargingStationService.getInstance().getAllChargingStations().forEach(station -> {
            String providerName = station.getProvider() != null ?
                    station.getProvider().getName() : "Unknown";

            logger.info("Station ID: " + station.getId() +
                    ", Name: " + station.getName() +
                    ", Status: " + station.getStatus() +
                    ", Provider: " + providerName +
                    ", Power: " + station.getMaxPowerKw() + " kW");
        });

        logger.info("=================================");
    }
}