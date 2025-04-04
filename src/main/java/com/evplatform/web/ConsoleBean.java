package com.evplatform.web;

import com.evplatform.service.ProviderService;
import com.evplatform.service.ChargingStationService;
import com.evplatform.vao.Provider;
import com.evplatform.vao.ChargingStation;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.logging.Logger;

@Named
@RequestScoped
public class ConsoleBean {

    private static final Logger logger = Logger.getLogger(ConsoleBean.class.getName());

    public String printProviders() {
        logger.info("===== PROVIDERS =====");
        for (Provider provider : ProviderService.getInstance().getAllProviders()) {
            logger.info("ID: " + provider.getId() + ", Name: " + provider.getName() +
                    ", Contact: " + provider.getContactPerson());
        }
        logger.info("====================");
        return null;
    }

    public String printChargingStations() {
        logger.info("===== CHARGING STATIONS =====");
        for (ChargingStation station : ChargingStationService.getInstance().getAllChargingStations()) {
            String providerName = (station.getProvider() != null) ?
                    station.getProvider().getName() : "Unknown";
            logger.info("ID: " + station.getId() + ", Name: " + station.getName() +
                    ", Status: " + station.getStatus() +
                    ", Provider: " + providerName);
        }
        logger.info("===========================");
        return null;
    }
}