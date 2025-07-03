package com.evplatform.web;

import com.evplatform.service.interfaces.ChargingStationServiceInterface;
import com.evplatform.service.interfaces.ProviderServiceInterface;
import com.evplatform.service.interfaces.UserServiceInterface;
import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.Provider;
import com.evplatform.vao.User;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import java.util.logging.Logger;

@Singleton
@Startup
public class StartupBean {

    private static final Logger logger = Logger.getLogger(StartupBean.class.getName());
    private static boolean initialized = false;

    @EJB
    private ProviderServiceInterface providerService;

    @EJB
    private ChargingStationServiceInterface stationService;

    @EJB
    private UserServiceInterface userService;

    @PostConstruct
    public void init() {
        // This flag prevents the data from being initialized more than once
        if (!initialized) {
            logger.info("Initializing application with test data...");
            initializeTestData();
            initialized = true;
        }
    }

    public void initializeTestData() {
        try {
            createSampleProviders();
            createSampleUsers();
            // The ObserverManager is now a @Singleton @Startup bean itself
            // and will register observers automatically. No need to call it from here.
            logger.info("Test data initialized successfully.");
        } catch (Exception e) {
            logger.severe("Error initializing test data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createSampleProviders() {
        Provider provider1 = new Provider(0, "ElectroDrive", "John Smith", "john@electrodrive.com", "+386 31 123 456", "Ljubljana, Slovenia");
        Provider provider2 = new Provider(0, "GreenCharge", "Ana Novak", "ana@greencharge.eu", "+386 41 789 123", "Maribor, Slovenia");
        Provider provider3 = new Provider(0, "EcoVolt", "Mark Johnson", "mark@ecovolt.com", "+386 40 333 444", "Celje, Slovenia");
        Provider provider4 = new Provider(0, "PowerPlugs", "Maria Rodriguez", "maria@powerplugs.eu", "+386 51 555 666", "Koper, Slovenia");
        Provider provider5 = new Provider(0, "VoltVenture", "Alex Peterson", "alex@voltventure.com", "+386 70 777 888", "Kranj, Slovenia");

        int provider1Id = providerService.addProvider(provider1);
        int provider2Id = providerService.addProvider(provider2);
        int provider3Id = providerService.addProvider(provider3);
        int provider4Id = providerService.addProvider(provider4);
        int provider5Id = providerService.addProvider(provider5);

        provider1 = providerService.getProviderById(provider1Id);
        provider2 = providerService.getProviderById(provider2Id);
        provider3 = providerService.getProviderById(provider3Id);
        provider4 = providerService.getProviderById(provider4Id);
        provider5 = providerService.getProviderById(provider5Id);

        createSampleStations(provider1);
        createSampleStations(provider2);
        createSampleStations(provider3);
        createSampleStations(provider4);
        createSampleStations(provider5);

        logger.info("Sample providers and charging stations created.");
    }

    private void createSampleStations(Provider provider) {
        ChargingStation station1 = new ChargingStation(0, provider.getName() + " City Center", "City Center, " + provider.getAddress().split(",")[0], "46.05" + Math.random() + ",14.50" + Math.random(), ChargingStation.ChargingStationStatus.AVAILABLE, provider, 4, 22.0);
        ChargingStation station2 = new ChargingStation(0, provider.getName() + " Mall Station", "Shopping Mall, " + provider.getAddress().split(",")[0], "46.06" + Math.random() + ",14.51" + Math.random(), ChargingStation.ChargingStationStatus.OCCUPIED, provider, 2, 50.0);
        ChargingStation station3 = new ChargingStation(0, provider.getName() + " Airport", "Airport, " + provider.getAddress().split(",")[0], "46.22" + Math.random() + ",14.45" + Math.random(), ChargingStation.ChargingStationStatus.OUT_OF_SERVICE, provider, 6, 150.0);

        stationService.addChargingStation(station1);
        stationService.addChargingStation(station2);
        stationService.addChargingStation(station3);
    }

    private void createSampleUsers() {
        User user1 = new User(0, "John Doe", "john.doe@example.com", 120.50, User.CarType.SEDAN);
        User user2 = new User(0, "Jane Smith", "jane.smith@example.com", 85.75, User.CarType.SUV);
        User user3 = new User(0, "Robert Johnson", "robert@example.com", 50.25, User.CarType.COMPACT);
        User user4 = new User(0, "Emma Davis", "emma@example.com", 200.00, User.CarType.LUXURY);
        User user5 = new User(0, "Michael Wilson", "michael@example.com", 150.30, User.CarType.SPORTS);

        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        userService.addUser(user4);
        userService.addUser(user5);

        logger.info("Sample users created.");
    }
}