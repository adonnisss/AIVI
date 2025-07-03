package com.evplatform.web;

import com.evplatform.service.interfaces.ProviderServiceInterface;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.logging.Logger;

@Named
@RequestScoped
public class ProviderBean {
    private static final Logger logger = Logger.getLogger(ProviderBean.class.getName());

    @EJB
    private ProviderServiceInterface providerService;

    public void printProvidersToConsole() {
        logger.info("===== ALL PROVIDERS =====");

        providerService.getAllProviders().forEach(provider -> {
            logger.info("Provider ID: " + provider.getId() +
                    ", Name: " + provider.getName() +
                    ", Contact: " + provider.getContactPerson() +
                    ", Email: " + provider.getEmail());

            logger.info("  Number of charging stations: " + provider.getChargingStations().size());
        });

        logger.info("=========================");
    }
}