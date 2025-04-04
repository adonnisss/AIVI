package com.evplatform.web;

import com.evplatform.service.ProviderService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.util.logging.Logger;

@Named
@RequestScoped
public class ProviderBean {
    private static final Logger logger = Logger.getLogger(ProviderBean.class.getName());

    // Method to print all providers to console
    public void printProvidersToConsole() {
        logger.info("===== ALL PROVIDERS =====");

        ProviderService.getInstance().getAllProviders().forEach(provider -> {
            logger.info("Provider ID: " + provider.getId() +
                    ", Name: " + provider.getName() +
                    ", Contact: " + provider.getContactPerson() +
                    ", Email: " + provider.getEmail());

            logger.info("  Number of charging stations: " + provider.getChargingStations().size());
        });

        logger.info("=========================");
    }
}