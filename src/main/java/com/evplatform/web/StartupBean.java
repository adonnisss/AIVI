package com.evplatform.web;

import com.evplatform.service.ProviderService;
import com.evplatform.service.ChargingStationService;
import com.evplatform.vao.Provider;
import com.evplatform.vao.ChargingStation;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.util.logging.Logger;

@Named
@ApplicationScoped
public class StartupBean {

    private static final Logger logger = Logger.getLogger(StartupBean.class.getName());
    private static boolean initialized = false;

    @PostConstruct
    public void postConstruct() {
        logger.info("StartupBean @PostConstruct method called");
        initializeTestData();
    }

    public void initializeTestData() {
        // Check if already initialized to prevent duplicate data
        if (initialized) {
            return;
        }

        synchronized (StartupBean.class) {
            if (initialized) {
                return;
            }

            logger.info("Initializing test data");
            try {
                // First check if we already have data
                if (!ProviderService.getInstance().getAllProviders().isEmpty()) {
                    logger.info("Data already exists in the system - skipping initialization");
                    initialized = true;
                    return;
                }

                // Create test providers
                Provider provider1 = new Provider();
                provider1.setName("ElectroDrive");
                provider1.setContactPerson("John Smith");
                provider1.setEmail("john@electrodrive.com");
                provider1.setPhone("+386 31 123 456");
                provider1.setAddress("Ljubljana, Slovenia");

                Provider provider2 = new Provider();
                provider2.setName("GreenCharge");
                provider2.setContactPerson("Ana Novak");
                provider2.setEmail("ana@greencharge.eu");
                provider2.setPhone("+386 41 789 123");
                provider2.setAddress("Maribor, Slovenia");

                // Save providers
                logger.info("Adding provider: " + provider1.getName());
                int provider1Id = ProviderService.getInstance().addProvider(provider1);
                logger.info("Provider added with ID: " + provider1Id);

                logger.info("Adding provider: " + provider2.getName());
                int provider2Id = ProviderService.getInstance().addProvider(provider2);
                logger.info("Provider added with ID: " + provider2Id);

                // Retrieve saved providers
                provider1 = ProviderService.getInstance().getProviderById(provider1Id);
                provider2 = ProviderService.getInstance().getProviderById(provider2Id);

                // Create charging stations
                ChargingStation station1 = new ChargingStation();
                station1.setName("City Center CS1");
                station1.setLocation("Ljubljana Center");
                station1.setCoordinates("46.056946,14.505751");
                station1.setStatus(ChargingStation.ChargingStationStatus.AVAILABLE);
                station1.setProvider(provider1);
                station1.setNumberOfConnectors(4);
                station1.setMaxPowerKw(22.0);

                ChargingStation station2 = new ChargingStation();
                station2.setName("Maribor Central");
                station2.setLocation("Maribor Main Square");
                station2.setCoordinates("46.557455,15.645981");
                station2.setStatus(ChargingStation.ChargingStationStatus.AVAILABLE);
                station2.setProvider(provider2);
                station2.setNumberOfConnectors(3);
                station2.setMaxPowerKw(11.0);

                // Save stations
                logger.info("Adding station: " + station1.getName());
                int station1Id = ChargingStationService.getInstance().addChargingStation(station1);
                logger.info("Station added with ID: " + station1Id);

                logger.info("Adding station: " + station2.getName());
                int station2Id = ChargingStationService.getInstance().addChargingStation(station2);
                logger.info("Station added with ID: " + station2Id);

                // Verify data was loaded
                int providerCount = ProviderService.getInstance().getAllProviders().size();
                int stationCount = ChargingStationService.getInstance().getAllChargingStations().size();

                logger.info("Test data initialized successfully: " + providerCount + " providers, " + stationCount + " stations");

                // Mark as initialized to prevent duplicate initialization
                initialized = true;
            } catch (Exception e) {
                logger.severe("Error initializing test data: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}