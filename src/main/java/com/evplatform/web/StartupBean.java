package com.evplatform.web;

import com.evplatform.observers.ObserverManager;
import com.evplatform.service.ChargingStationService;
import com.evplatform.service.ProviderService;
import com.evplatform.service.UserService;
import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.Provider;
import com.evplatform.vao.User;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.logging.Logger;

@Named
@ApplicationScoped
@Singleton
@Startup
public class StartupBean {

    private static final Logger logger = Logger.getLogger(StartupBean.class.getName());
    private static boolean initialized = false;

    @PostConstruct
    public void init() {
        if (!initialized) {
            logger.info("Initializing application with test data...");
            initializeTestData();
            initialized = true;
        }
    }

    public void initializeTestData() {
        try {
            // Create and add sample providers
            createSampleProviders();

            // Create and add sample users
            createSampleUsers();

            // Register observers if needed
            ObserverManager.getInstance().registerAllObservers();

            logger.info("Test data initialized successfully");
        } catch (Exception e) {
            logger.severe("Error initializing test data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createSampleProviders() {
        // Create sample providers
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

        Provider provider3 = new Provider();
        provider3.setName("EcoVolt");
        provider3.setContactPerson("Mark Johnson");
        provider3.setEmail("mark@ecovolt.com");
        provider3.setPhone("+386 40 333 444");
        provider3.setAddress("Celje, Slovenia");

        Provider provider4 = new Provider();
        provider4.setName("PowerPlugs");
        provider4.setContactPerson("Maria Rodriguez");
        provider4.setEmail("maria@powerplugs.eu");
        provider4.setPhone("+386 51 555 666");
        provider4.setAddress("Koper, Slovenia");

        Provider provider5 = new Provider();
        provider5.setName("VoltVenture");
        provider5.setContactPerson("Alex Peterson");
        provider5.setEmail("alex@voltventure.com");
        provider5.setPhone("+386 70 777 888");
        provider5.setAddress("Kranj, Slovenia");

        // Save providers
        int provider1Id = ProviderService.getInstance().addProvider(provider1);
        int provider2Id = ProviderService.getInstance().addProvider(provider2);
        int provider3Id = ProviderService.getInstance().addProvider(provider3);
        int provider4Id = ProviderService.getInstance().addProvider(provider4);
        int provider5Id = ProviderService.getInstance().addProvider(provider5);

        // Get the providers with IDs assigned
        provider1 = ProviderService.getInstance().getProviderById(provider1Id);
        provider2 = ProviderService.getInstance().getProviderById(provider2Id);
        provider3 = ProviderService.getInstance().getProviderById(provider3Id);
        provider4 = ProviderService.getInstance().getProviderById(provider4Id);
        provider5 = ProviderService.getInstance().getProviderById(provider5Id);

        // Create sample charging stations for each provider
        createSampleStations(provider1);
        createSampleStations(provider2);
        createSampleStations(provider3);
        createSampleStations(provider4);
        createSampleStations(provider5);

        logger.info("Sample providers and charging stations created");
    }

    private void createSampleStations(Provider provider) {
        // Station 1 - Available
        ChargingStation station1 = new ChargingStation();
        station1.setName(provider.getName() + " City Center");
        station1.setLocation("City Center, " + provider.getAddress().split(",")[0]);
        station1.setCoordinates("46.05" + Math.random() + ",14.50" + Math.random());
        station1.setStatus(ChargingStation.ChargingStationStatus.AVAILABLE);
        station1.setProvider(provider);
        station1.setNumberOfConnectors(4);
        station1.setMaxPowerKw(22.0);

        // Station 2 - Occupied
        ChargingStation station2 = new ChargingStation();
        station2.setName(provider.getName() + " Mall Station");
        station2.setLocation("Shopping Mall, " + provider.getAddress().split(",")[0]);
        station2.setCoordinates("46.06" + Math.random() + ",14.51" + Math.random());
        station2.setStatus(ChargingStation.ChargingStationStatus.OCCUPIED);
        station2.setProvider(provider);
        station2.setNumberOfConnectors(2);
        station2.setMaxPowerKw(50.0);

        // Station 3 - Out of Service
        ChargingStation station3 = new ChargingStation();
        station3.setName(provider.getName() + " Airport");
        station3.setLocation("Airport, " + provider.getAddress().split(",")[0]);
        station3.setCoordinates("46.22" + Math.random() + ",14.45" + Math.random());
        station3.setStatus(ChargingStation.ChargingStationStatus.OUT_OF_SERVICE);
        station3.setProvider(provider);
        station3.setNumberOfConnectors(6);
        station3.setMaxPowerKw(150.0);

        // Save stations
        ChargingStationService stationService = ChargingStationService.getInstance();
        stationService.addChargingStation(station1);
        stationService.addChargingStation(station2);
        stationService.addChargingStation(station3);
    }

    private void createSampleUsers() {
        // User 1 - Sedan
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");
        user1.setBalance(120.50);
        user1.setCarType(User.CarType.SEDAN);

        // User 2 - SUV
        User user2 = new User();
        user2.setName("Jane Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setBalance(85.75);
        user2.setCarType(User.CarType.SUV);

        // User 3 - Compact
        User user3 = new User();
        user3.setName("Robert Johnson");
        user3.setEmail("robert@example.com");
        user3.setBalance(50.25);
        user3.setCarType(User.CarType.COMPACT);

        // User 4 - Luxury
        User user4 = new User();
        user4.setName("Emma Davis");
        user4.setEmail("emma@example.com");
        user4.setBalance(200.00);
        user4.setCarType(User.CarType.LUXURY);

        // User 5 - Sports
        User user5 = new User();
        user5.setName("Michael Wilson");
        user5.setEmail("michael@example.com");
        user5.setBalance(150.30);
        user5.setCarType(User.CarType.SPORTS);

        // Save users
        UserService userService = UserService.getInstance();
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        userService.addUser(user4);
        userService.addUser(user5);

        logger.info("Sample users created");
    }
}