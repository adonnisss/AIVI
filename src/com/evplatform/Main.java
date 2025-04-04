package com.evplatform;

// Java standard library imports
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

// Application imports
import com.evplatform.chainofresponsibility.ChargingRequestProcessor;
import com.evplatform.iterators.IteratorProvider;
import com.evplatform.observers.ObserverManager;
import com.evplatform.service.ChargingStationService;
import com.evplatform.service.ProviderService;
import com.evplatform.service.UserService;
import com.evplatform.service.interfaces.ChargingStationServiceInterface;
import com.evplatform.service.interfaces.ProviderServiceInterface;
import com.evplatform.service.interfaces.UserServiceInterface;
import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.Provider;
import com.evplatform.vao.User;

public class Main {
    // Service interfaces
    private static final ProviderServiceInterface providerService = ProviderService.getInstance();
    private static final ChargingStationServiceInterface stationService = ChargingStationService.getInstance();
    private static final UserServiceInterface userService = UserService.getInstance();
    private static final ChargingRequestProcessor chargingProcessor = ChargingRequestProcessor.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Initialize sample data
        initSampleData();

        // Initialize sample users
        initSampleUsers();

        // Initialize observers
        initObservers();

        // Test singleton and data centralization
        testDataCentralization();

        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    handleProviderMenu();
                    break;
                case 2:
                    handleChargingStationMenu();
                    break;
                case 3:
                    handleIteratorsMenu();
                    break;
                case 4:
                    simulateCharging();
                    break;
                case 5:
                    handleUserMenu();
                    break;
                case 6:
                    handleChainOfResponsibilityDemo();
                    break;
                case 0:
                    exit = true;
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void initSampleUsers() {
        try {
            // Add sample users with different car types and balances
            User user1 = new User(0, "John Doe", "john@example.com", 100.0, User.CarType.SEDAN);
            User user2 = new User(0, "Jane Smith", "jane@example.com", 20.0, User.CarType.SUV);
            User user3 = new User(0, "Bob Brown", "bob@example.com", 50.0, User.CarType.COMPACT);
            User user4 = new User(0, "Alice Johnson", "alice@example.com", 200.0, User.CarType.LUXURY);
            User user5 = new User(0, "Charlie Davis", "charlie@example.com", 75.0, User.CarType.SPORTS);

            // Save all users
            userService.addUser(user1);
            userService.addUser(user2);
            userService.addUser(user3);
            userService.addUser(user4);
            userService.addUser(user5);

            System.out.println("Sample users initialized successfully.");
        } catch (Exception e) {
            System.out.println("Error initializing sample users: " + e.getMessage());
        }
    }

    private static void initObservers() {
        System.out.println("\n===== Initializing Observers =====");
        ObserverManager.getInstance().registerAllObservers();
        System.out.println("Observers registered successfully.");
    }

    private static void simulateCharging() {
        System.out.println("\n===== Simulate Charging =====");

        // Get all charging stations
        List<ChargingStation> stations = stationService.getAllChargingStations();
        if (stations.isEmpty()) {
            System.out.println("No charging stations available.");
            return;
        }

        // List available stations
        System.out.println("Available stations:");
        List<ChargingStation> availableStations = stations.stream()
                .filter(s -> s.getStatus() == ChargingStation.ChargingStationStatus.AVAILABLE)
                .collect(Collectors.toList());

        if (availableStations.isEmpty()) {
            System.out.println("No available stations to start charging.");
            return;
        }

        for (int i = 0; i < availableStations.size(); i++) {
            System.out.println((i + 1) + ". " + availableStations.get(i).getName());
        }

        // Choose a station
        int stationChoice = getIntInput("Choose a station to start charging (1-" + availableStations.size() + "): ");
        if (stationChoice < 1 || stationChoice > availableStations.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        ChargingStation selectedStation = availableStations.get(stationChoice - 1);

        // Get user email
        String email = getStringInput("Enter your email: ");

        // Start charging
        selectedStation.setCurrentUserEmail(email);
        selectedStation.setStatus(ChargingStation.ChargingStationStatus.OCCUPIED);

        System.out.println("Charging started at " + selectedStation.getName());

        // Option to stop charging
        boolean stopCharging = getYesNoInput("Do you want to stop charging? (y/n): ");
        if (stopCharging) {
            // Stop charging
            selectedStation.setStatus(ChargingStation.ChargingStationStatus.AVAILABLE);
            System.out.println("Charging stopped at " + selectedStation.getName());
        }
    }

    private static boolean getYesNoInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    private static void testDataCentralization() {
        System.out.println("\n===== Testing Data Centralization =====");

        // Get the provider service from a different "part" of the application
        ProviderServiceInterface anotherProviderService = ProviderService.getInstance();

        // Get the charging station service from a different "part" of the application
        ChargingStationServiceInterface anotherStationService = ChargingStationService.getInstance();

        // Verify the providers are the same across different service instances
        List<Provider> providers1 = providerService.getAllProviders();
        List<Provider> providers2 = anotherProviderService.getAllProviders();

        System.out.println("Providers from main service: " + providers1.size());
        System.out.println("Providers from another service: " + providers2.size());
        System.out.println("Data is centralized: " + (providers1.size() == providers2.size()));

        // Verify the charging stations are the same across different service instances
        List<ChargingStation> stations1 = stationService.getAllChargingStations();
        List<ChargingStation> stations2 = anotherStationService.getAllChargingStations();

        System.out.println("Charging stations from main service: " + stations1.size());
        System.out.println("Charging stations from another service: " + stations2.size());
        System.out.println("Data is centralized: " + (stations1.size() == stations2.size()));

        System.out.println("===== Test Complete =====\n");
    }

    private static void initSampleData() {
        try {
            // Add sample providers
            Provider provider1 = new Provider(0, "ElectroDrive", "John Smith", "john@electrodrive.com", "+386 31 123 456", "Ljubljana, Slovenia");
            Provider provider2 = new Provider(0, "GreenCharge", "Ana Novak", "ana@greencharge.eu", "+386 41 789 123", "Maribor, Slovenia");

            int provider1Id = providerService.addProvider(provider1);
            int provider2Id = providerService.addProvider(provider2);

            // Retrieve the providers with their assigned IDs
            provider1 = providerService.getProviderById(provider1Id);
            provider2 = providerService.getProviderById(provider2Id);

            // Add sample charging stations for provider1 with various statuses and powers
            ChargingStation station1 = new ChargingStation(0, "City Center CS1", "Ljubljana Center", "46.056946,14.505751",
                    ChargingStation.ChargingStationStatus.AVAILABLE, provider1, 4, 22.0);

            ChargingStation station2 = new ChargingStation(0, "BTC CS2", "Ljubljana BTC City", "46.066512,14.537937",
                    ChargingStation.ChargingStationStatus.OCCUPIED, provider1, 2, 50.0);

            ChargingStation station3 = new ChargingStation(0, "Airport Station", "Ljubljana Airport", "46.223689,14.454486",
                    ChargingStation.ChargingStationStatus.AVAILABLE, provider1, 6, 150.0);

            ChargingStation station4 = new ChargingStation(0, "Highway A1 Station", "Ljubljana-Maribor Highway", "46.119328,14.694024",
                    ChargingStation.ChargingStationStatus.OUT_OF_SERVICE, provider1, 8, 350.0);

            // Add sample charging stations for provider2 with various statuses and powers
            ChargingStation station5 = new ChargingStation(0, "Maribor Central", "Maribor Main Square", "46.557455,15.645981",
                    ChargingStation.ChargingStationStatus.AVAILABLE, provider2, 3, 11.0);

            ChargingStation station6 = new ChargingStation(0, "Maribor Mall", "Maribor Shopping Center", "46.562054,15.640486",
                    ChargingStation.ChargingStationStatus.MAINTENANCE, provider2, 4, 22.0);

            ChargingStation station7 = new ChargingStation(0, "Ptuj Station", "Ptuj City", "46.420128,15.869872",
                    ChargingStation.ChargingStationStatus.AVAILABLE, provider2, 2, 75.0);

            ChargingStation station8 = new ChargingStation(0, "Drava River", "Maribor Riverside", "46.554468,15.647492",
                    ChargingStation.ChargingStationStatus.OCCUPIED, provider2, 1, 43.0);

            // Save all charging stations
            stationService.addChargingStation(station1);
            stationService.addChargingStation(station2);
            stationService.addChargingStation(station3);
            stationService.addChargingStation(station4);
            stationService.addChargingStation(station5);
            stationService.addChargingStation(station6);
            stationService.addChargingStation(station7);
            stationService.addChargingStation(station8);

            System.out.println("Sample data initialized successfully.");
        } catch (Exception e) {
            System.out.println("Error initializing sample data: " + e.getMessage());
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n===== EV Charging Station Management System =====");
        System.out.println("1. Provider Management");
        System.out.println("2. Charging Station Management");
        System.out.println("3. Iterators Demonstration");
        System.out.println("4. Simulate Charging");
        System.out.println("5. User Management");
        System.out.println("6. Chain of Responsibility Demo");
        System.out.println("0. Exit");
        System.out.println("================================================");
    }

    private static void handleProviderMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== Provider Management =====");
            System.out.println("1. List All Providers");
            System.out.println("2. Add New Provider");
            System.out.println("3. View Provider Details");
            System.out.println("4. Update Provider");
            System.out.println("5. Delete Provider");
            System.out.println("0. Back to Main Menu");
            System.out.println("=============================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    listAllProviders();
                    break;
                case 2:
                    addProvider();
                    break;
                case 3:
                    viewProviderDetails();
                    break;
                case 4:
                    updateProvider();
                    break;
                case 5:
                    deleteProvider();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleChargingStationMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== Charging Station Management =====");
            System.out.println("1. List All Charging Stations");
            System.out.println("2. Add New Charging Station");
            System.out.println("3. View Charging Station Details");
            System.out.println("4. Update Charging Station");
            System.out.println("5. Delete Charging Station");
            System.out.println("6. List Charging Stations by Provider");
            System.out.println("7. Update Charging Station Status");
            System.out.println("0. Back to Main Menu");
            System.out.println("=======================================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    listAllChargingStations();
                    break;
                case 2:
                    addChargingStation();
                    break;
                case 3:
                    viewChargingStationDetails();
                    break;
                case 4:
                    updateChargingStation();
                    break;
                case 5:
                    deleteChargingStation();
                    break;
                case 6:
                    listChargingStationsByProvider();
                    break;
                case 7:
                    updateChargingStationStatus();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleIteratorsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== Iterators Demonstration =====");
            System.out.println("1. Show Active Charging Stations");
            System.out.println("2. Show High-Power Charging Stations");
            System.out.println("3. Show Charging Stations by Region");
            System.out.println("4. Show All Charging Stations Sorted by Name");
            System.out.println("0. Back to Main Menu");
            System.out.println("==================================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    showActiveChargingStations();
                    break;
                case 2:
                    showHighPowerChargingStations();
                    break;
                case 3:
                    showChargingStationsByRegion();
                    break;
                case 4:
                    showAllChargingStationsSorted();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleUserMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== User Management =====");
            System.out.println("1. List All Users");
            System.out.println("2. Add New User");
            System.out.println("3. View User Details");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("6. Add Funds to User Account");
            System.out.println("0. Back to Main Menu");
            System.out.println("===========================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    listAllUsers();
                    break;
                case 2:
                    addUser();
                    break;
                case 3:
                    viewUserDetails();
                    break;
                case 4:
                    updateUser();
                    break;
                case 5:
                    deleteUser();
                    break;
                case 6:
                    addFundsToUser();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleChainOfResponsibilityDemo() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== Chain of Responsibility Demo =====");
            System.out.println("1. Scenario 1: Station Occupied");
            System.out.println("2. Scenario 2: Insufficient Funds");
            System.out.println("3. Scenario 3: Incompatible Vehicle");
            System.out.println("4. Scenario 4: Successful Charging");
            System.out.println("0. Back to Main Menu");
            System.out.println("=======================================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    demoStationOccupied();
                    break;
                case 2:
                    demoInsufficientFunds();
                    break;
                case 3:
                    demoIncompatibleVehicle();
                    break;
                case 4:
                    demoSuccessfulCharging();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Provider management methods

    private static void listAllProviders() {
        System.out.println("\n===== All Providers =====");
        List<Provider> providers = providerService.getAllProviders();
        if (providers.isEmpty()) {
            System.out.println("No providers found.");
        } else {
            for (Provider provider : providers) {
                System.out.println("ID: " + provider.getId() + " | Name: " + provider.getName() +
                        " | Contact: " + provider.getContactPerson());
            }
        }
    }

    private static void addProvider() {
        System.out.println("\n===== Add New Provider =====");

        Provider provider = new Provider();

        // Get name with validation
        boolean nameSet = false;
        while (!nameSet) {
            try {
                String name = getStringInput("Enter provider name: ");
                provider.setName(name);
                nameSet = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        provider.setContactPerson(getStringInput("Enter contact person name: "));
        provider.setEmail(getStringInput("Enter email: "));
        provider.setPhone(getStringInput("Enter phone number: "));
        provider.setAddress(getStringInput("Enter address: "));

        try {
            int id = providerService.addProvider(provider);
            System.out.println("Provider added successfully with ID: " + id);
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding provider: " + e.getMessage());
        }
    }

    private static void viewProviderDetails() {
        int id = getIntInput("Enter provider ID to view details: ");
        Provider provider = providerService.getProviderById(id);

        if (provider == null) {
            System.out.println("Provider not found with ID: " + id);
        } else {
            System.out.println("\n===== Provider Details =====");
            System.out.println("ID: " + provider.getId());
            System.out.println("Name: " + provider.getName());
            System.out.println("Contact Person: " + provider.getContactPerson());
            System.out.println("Email: " + provider.getEmail());
            System.out.println("Phone: " + provider.getPhone());
            System.out.println("Address: " + provider.getAddress());

            // List provider's charging stations
            try {
                List<ChargingStation> stations = provider.getChargingStations();
                System.out.println("\nCharging Stations (" + stations.size() + "):");
                for (ChargingStation station : stations) {
                    System.out.println("  - ID: " + station.getId() + " | Name: " + station.getName() +
                            " | Status: " + station.getStatus());
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error retrieving charging stations: " + e.getMessage());
            }
        }
    }

    private static void updateProvider() {
        int id = getIntInput("Enter provider ID to update: ");
        Provider provider = providerService.getProviderById(id);

        if (provider == null) {
            System.out.println("Provider not found with ID: " + id);
        } else {
            System.out.println("\n===== Update Provider =====");
            System.out.println("Enter new values (or leave empty to keep current value):");

            // Update name with validation
            boolean nameSet = false;
            while (!nameSet) {
                try {
                    String name = getStringInputWithDefault("Name (" + provider.getName() + "): ", provider.getName());
                    provider.setName(name);
                    nameSet = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            String contactPerson = getStringInputWithDefault("Contact Person (" + provider.getContactPerson() + "): ",
                    provider.getContactPerson());
            String email = getStringInputWithDefault("Email (" + provider.getEmail() + "): ", provider.getEmail());
            String phone = getStringInputWithDefault("Phone (" + provider.getPhone() + "): ", provider.getPhone());
            String address = getStringInputWithDefault("Address (" + provider.getAddress() + "): ", provider.getAddress());

            provider.setContactPerson(contactPerson);
            provider.setEmail(email);
            provider.setPhone(phone);
            provider.setAddress(address);

            try {
                boolean success = providerService.updateProvider(provider);
                if (success) {
                    System.out.println("Provider updated successfully.");
                } else {
                    System.out.println("Failed to update provider.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error updating provider: " + e.getMessage());
            }
        }
    }

    private static void deleteProvider() {
        int id = getIntInput("Enter provider ID to delete: ");

        try {
            boolean success = providerService.deleteProvider(id);
            if (success) {
                System.out.println("Provider deleted successfully.");
            } else {
                System.out.println("Provider not found with ID: " + id);
            }
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Charging Station management methods

    private static void listAllChargingStations() {
        System.out.println("\n===== All Charging Stations =====");
        List<ChargingStation> stations = stationService.getAllChargingStations();
        if (stations.isEmpty()) {
            System.out.println("No charging stations found.");
        } else {
            for (ChargingStation station : stations) {
                // Use direct provider reference instead of looking it up separately
                String providerName = (station.getProvider() != null) ? station.getProvider().getName() : "Unknown";

                System.out.println("ID: " + station.getId() + " | Name: " + station.getName() +
                        " | Status: " + station.getStatus() + " | Provider: " + providerName);
            }
        }
    }

    private static void addChargingStation() {
        System.out.println("\n===== Add New Charging Station =====");

        // Check if there are any providers
        List<Provider> providers = providerService.getAllProviders();
        if (providers.isEmpty()) {
            System.out.println("No providers found. Please add a provider first.");
            return;
        }

        // List available providers
        System.out.println("Available Providers:");
        for (Provider provider : providers) {
            System.out.println("ID: " + provider.getId() + " | Name: " + provider.getName());
        }

        ChargingStation station = new ChargingStation();

        // Get name with validation
        boolean nameSet = false;
        while (!nameSet) {
            try {
                String name = getStringInput("Enter charging station name: ");
                station.setName(name);
                nameSet = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        station.setLocation(getStringInput("Enter location (address): "));
        station.setCoordinates(getStringInput("Enter GPS coordinates (latitude,longitude): "));

        // Set provider with validation
        boolean providerSet = false;
        while (!providerSet) {
            try {
                int providerId = getIntInput("Enter provider ID: ");
                Provider provider = providerService.getProviderById(providerId);
                if (provider == null) {
                    System.out.println("Provider not found. Please enter a valid provider ID.");
                    continue;
                }
                station.setProvider(provider); // Set provider object instead of just ID
                providerSet = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Set number of connectors with validation
        boolean connectorsSet = false;
        while (!connectorsSet) {
            try {
                int connectors = getIntInput("Enter number of connectors: ");
                station.setNumberOfConnectors(connectors);
                connectorsSet = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Set max power with validation
        boolean powerSet = false;
        while (!powerSet) {
            try {
                double power = getDoubleInput("Enter maximum power (kW): ");
                station.setMaxPowerKw(power);
                powerSet = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Set status
        System.out.println("Available statuses:");
        ChargingStation.ChargingStationStatus[] statuses = ChargingStation.ChargingStationStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + ". " + statuses[i]);
        }

        int statusChoice;
        while (true) {
            statusChoice = getIntInput("Select status (1-" + statuses.length + "): ");
            if (statusChoice >= 1 && statusChoice <= statuses.length) {
                break;
            }
            System.out.println("Invalid choice. Please try again.");
        }

        try {
            station.setStatus(statuses[statusChoice - 1]);

            int id = stationService.addChargingStation(station);
            System.out.println("Charging station added successfully with ID: " + id);
        } catch (Exception e) {
            System.out.println("Error adding charging station: " + e.getMessage());
        }
    }

    private static void viewChargingStationDetails() {
        int id = getIntInput("Enter charging station ID to view details: ");
        ChargingStation station = stationService.getChargingStationById(id);

        if (station == null) {
            System.out.println("Charging station not found with ID: " + id);
        } else {
            Provider provider = station.getProvider(); // Use direct provider reference
            String providerName = (provider != null) ? provider.getName() : "Unknown";

            System.out.println("\n===== Charging Station Details =====");
            System.out.println("ID: " + station.getId());
            System.out.println("Name: " + station.getName());
            System.out.println("Location: " + station.getLocation());
            System.out.println("Coordinates: " + station.getCoordinates());
            System.out.println("Status: " + station.getStatus());
            System.out.println("Provider: " + providerName + " (ID: " + station.getProviderId() + ")");
            System.out.println("Number of Connectors: " + station.getNumberOfConnectors());
            System.out.println("Maximum Power: " + station.getMaxPowerKw() + " kW");

            // Display user information if occupied
            if (station.getStatus() == ChargingStation.ChargingStationStatus.OCCUPIED) {
                System.out.println("Current User: " + (station.getCurrentUserEmail() != null ?
                        station.getCurrentUserEmail() : "Unknown"));
            }
        }
    }

    private static void updateChargingStation() {
        int id = getIntInput("Enter charging station ID to update: ");
        ChargingStation station = stationService.getChargingStationById(id);

        if (station == null) {
            System.out.println("Charging station not found with ID: " + id);
        } else {
            System.out.println("\n===== Update Charging Station =====");
            System.out.println("Enter new values (or leave empty to keep current value):");

            // Update name with validation
            boolean nameSet = false;
            while (!nameSet) {
                try {
                    String name = getStringInputWithDefault("Name (" + station.getName() + "): ", station.getName());
                    station.setName(name);
                    nameSet = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            station.setLocation(getStringInputWithDefault("Location (" + station.getLocation() + "): ",
                    station.getLocation()));
            station.setCoordinates(getStringInputWithDefault("Coordinates (" + station.getCoordinates() + "): ",
                    station.getCoordinates()));

            // Update number of connectors with validation
            boolean connectorsSet = false;
            while (!connectorsSet) {
                try {
                    int connectors = getIntInputWithDefault(
                            "Number of connectors (" + station.getNumberOfConnectors() + "): ",
                            station.getNumberOfConnectors());
                    station.setNumberOfConnectors(connectors);
                    connectorsSet = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            // Update max power with validation
            boolean powerSet = false;
            while (!powerSet) {
                try {
                    double power = getDoubleInputWithDefault(
                            "Maximum power in kW (" + station.getMaxPowerKw() + "): ",
                            station.getMaxPowerKw());
                    station.setMaxPowerKw(power);
                    powerSet = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            try {
                boolean success = stationService.updateChargingStation(station);
                if (success) {
                    System.out.println("Charging station updated successfully.");
                } else {
                    System.out.println("Failed to update charging station.");
                }
            } catch (Exception e) {
                System.out.println("Error updating charging station: " + e.getMessage());
            }
        }
    }

    private static void deleteChargingStation() {
        int id = getIntInput("Enter charging station ID to delete: ");

        try {
            boolean success = stationService.deleteChargingStation(id);
            if (success) {
                System.out.println("Charging station deleted successfully.");
            } else {
                System.out.println("Charging station not found with ID: " + id);
            }
        } catch (Exception e) {
            System.out.println("Error deleting charging station: " + e.getMessage());
        }
    }

    private static void listChargingStationsByProvider() {
        int providerId = getIntInput("Enter provider ID: ");

        try {
            // Get the provider
            Provider provider = providerService.getProviderById(providerId);
            if (provider == null) {
                System.out.println("Provider not found with ID: " + providerId);
                return;
            }

            System.out.println("\n===== Charging Stations for Provider: " + provider.getName() + " =====");

            // Use the provider's charging stations directly
            List<ChargingStation> stations = provider.getChargingStations();

            if (stations.isEmpty()) {
                System.out.println("No charging stations found for this provider.");
            } else {
                for (ChargingStation station : stations) {
                    System.out.println("ID: " + station.getId() + " | Name: " + station.getName() +
                            " | Status: " + station.getStatus() + " | Connectors: " + station.getNumberOfConnectors());
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateChargingStationStatus() {
        int id = getIntInput("Enter charging station ID to update status: ");

        try {
            ChargingStation station = stationService.getChargingStationById(id);

            if (station == null) {
                System.out.println("Charging station not found with ID: " + id);
                return;
            }

            System.out.println("Current status: " + station.getStatus());
            System.out.println("Available statuses:");
            ChargingStation.ChargingStationStatus[] statuses = ChargingStation.ChargingStationStatus.values();
            for (int i = 0; i < statuses.length; i++) {
                System.out.println((i + 1) + ". " + statuses[i]);
            }

            int statusChoice;
            while (true) {
                statusChoice = getIntInput("Select new status (1-" + statuses.length + "): ");
                if (statusChoice >= 1 && statusChoice <= statuses.length) {
                    break;
                }
                System.out.println("Invalid choice. Please try again.");
            }

            ChargingStation.ChargingStationStatus newStatus = statuses[statusChoice - 1];

            // Set user email if changing to OCCUPIED
            if (newStatus == ChargingStation.ChargingStationStatus.OCCUPIED &&
                    station.getStatus() != ChargingStation.ChargingStationStatus.OCCUPIED) {
                String email = getStringInput("Enter user email for occupied station: ");
                station.setCurrentUserEmail(email);
            }

            boolean success = stationService.updateChargingStationStatus(id, newStatus);

            if (success) {
                System.out.println("Status updated successfully.");
            } else {
                System.out.println("Failed to update status.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Iterator demonstration methods

    private static void showActiveChargingStations() {
        // Get provider ID
        int providerId = selectProvider();
        if (providerId == -1) return;

        try {
            System.out.println("\n===== Active Charging Stations =====");
            IteratorProvider iteratorProvider = IteratorProvider.getInstance();

            // Get active stations iterator
            Iterator<ChargingStation> iterator = iteratorProvider.getActiveStationIterator(providerId);

            if (!iterator.hasNext()) {
                System.out.println("No active charging stations found for this provider.");
                return;
            }

            // Iterate and display active stations
            while (iterator.hasNext()) {
                ChargingStation station = iterator.next();
                System.out.println("ID: " + station.getId() + " | Name: " + station.getName() +
                        " | Status: " + station.getStatus() + " | Power: " + station.getMaxPowerKw() + " kW");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showHighPowerChargingStations() {
        // Get provider ID
        int providerId = selectProvider();
        if (providerId == -1) return;

        // Get minimum power threshold
        double minPower = getDoubleInput("Enter minimum charging power (kW): ");

        try {
            System.out.println("\n===== High-Power Charging Stations (>" + minPower + " kW) =====");
            IteratorProvider iteratorProvider = IteratorProvider.getInstance();

            // Get high-power stations iterator
            Iterator<ChargingStation> iterator = iteratorProvider.getSpeedStationIterator(providerId, minPower);

            if (!iterator.hasNext()) {
                System.out.println("No charging stations found with power above " + minPower + " kW for this provider.");
                return;
            }

            // Iterate and display high-power stations
            while (iterator.hasNext()) {
                ChargingStation station = iterator.next();
                System.out.println("ID: " + station.getId() + " | Name: " + station.getName() +
                        " | Status: " + station.getStatus() + " | Power: " + station.getMaxPowerKw() + " kW");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showChargingStationsByRegion() {
        // Get provider ID
        int providerId = selectProvider();
        if (providerId == -1) return;

        // Get region
        String region = getStringInput("Enter region (partial location match): ");

        try {
            System.out.println("\n===== Charging Stations in Region: " + region + " =====");
            IteratorProvider iteratorProvider = IteratorProvider.getInstance();

            // Get region-filtered stations iterator
            Iterator<ChargingStation> iterator = iteratorProvider.getRegionStationIterator(providerId, region);

            if (!iterator.hasNext()) {
                System.out.println("No charging stations found in region '" + region + "' for this provider.");
                return;
            }

            // Iterate and display stations in the region
            while (iterator.hasNext()) {
                ChargingStation station = iterator.next();
                System.out.println("ID: " + station.getId() + " | Name: " + station.getName() +
                        " | Location: " + station.getLocation() + " | Status: " + station.getStatus());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showAllChargingStationsSorted() {
        try {
            System.out.println("\n===== All Charging Stations (Sorted by Name) =====");
            IteratorProvider iteratorProvider = IteratorProvider.getInstance();

            // Get all sorted stations iterator
            Iterator<ChargingStation> iterator = iteratorProvider.getAllStationsSortedIterator();

            if (!iterator.hasNext()) {
                System.out.println("No charging stations found in the system.");
                return;
            }

            // Iterate and display all stations
            while (iterator.hasNext()) {
                ChargingStation station = iterator.next();
                Provider provider = station.getProvider();
                String providerName = (provider != null) ? provider.getName() : "Unknown";

                System.out.println("Name: " + station.getName() +
                        " | Provider: " + providerName +
                        " | Status: " + station.getStatus() +
                        " | Power: " + station.getMaxPowerKw() + " kW");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // User management methods

    private static void listAllUsers() {
        System.out.println("\n===== All Users =====");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            for (User user : users) {
                System.out.println("ID: " + user.getId() + " | Name: " + user.getName() +
                        " | Email: " + user.getEmail() + " | Balance: $" + user.getBalance() +
                        " | Car Type: " + user.getCarType());
            }
        }
    }

    private static void addUser() {
        System.out.println("\n===== Add New User =====");

        User user = new User();

        // Get name with validation
        boolean nameSet = false;
        while (!nameSet) {
            try {
                String name = getStringInput("Enter user name: ");
                user.setName(name);
                nameSet = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Get email with validation
        boolean emailSet = false;
        while (!emailSet) {
            try {
                String email = getStringInput("Enter user email: ");
                user.setEmail(email);
                emailSet = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Get initial balance
        double initialBalance;
        while (true) {
            try {
                initialBalance = getDoubleInput("Enter initial balance: ");
                if (initialBalance < 0) {
                    System.out.println("Balance cannot be negative.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
        user.setBalance(initialBalance);

        // Get car type
        System.out.println("Available car types:");
        User.CarType[] carTypes = User.CarType.values();
        for (int i = 0; i < carTypes.length; i++) {
            System.out.println((i + 1) + ". " + carTypes[i]);
        }

        int carTypeChoice;
        while (true) {
            carTypeChoice = getIntInput("Select car type (1-" + carTypes.length + "): ");
            if (carTypeChoice >= 1 && carTypeChoice <= carTypes.length) {
                break;
            }
            System.out.println("Invalid choice. Please try again.");
        }
        user.setCarType(carTypes[carTypeChoice - 1]);

        try {
            int id = userService.addUser(user);
            System.out.println("User added successfully with ID: " + id);
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    private static void viewUserDetails() {
        int id = getIntInput("Enter user ID to view details: ");
        User user = userService.getUserById(id);

        if (user == null) {
            System.out.println("User not found with ID: " + id);
        } else {
            System.out.println("\n===== User Details =====");
            System.out.println("ID: " + user.getId());
            System.out.println("Name: " + user.getName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Balance: $" + user.getBalance());
            System.out.println("Car Type: " + user.getCarType());
        }
    }

    private static void updateUser() {
        int id = getIntInput("Enter user ID to update: ");
        User user = userService.getUserById(id);

        if (user == null) {
            System.out.println("User not found with ID: " + id);
        } else {
            System.out.println("\n===== Update User =====");
            System.out.println("Enter new values (or leave empty to keep current value):");

            // Update name with validation
            boolean nameSet = false;
            while (!nameSet) {
                try {
                    String name = getStringInputWithDefault("Name (" + user.getName() + "): ", user.getName());
                    user.setName(name);
                    nameSet = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            // Update email with validation
            boolean emailSet = false;
            while (!emailSet) {
                try {
                    String email = getStringInputWithDefault("Email (" + user.getEmail() + "): ", user.getEmail());
                    user.setEmail(email);
                    emailSet = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            // Car type can be updated
            System.out.println("Current car type: " + user.getCarType());
            System.out.println("Available car types:");
            User.CarType[] carTypes = User.CarType.values();
            for (int i = 0; i < carTypes.length; i++) {
                System.out.println((i + 1) + ". " + carTypes[i]);
            }

            String input = getStringInput("Select new car type (1-" + carTypes.length + ") or leave empty to keep current: ");
            if (!input.isEmpty()) {
                try {
                    int carTypeChoice = Integer.parseInt(input);
                    if (carTypeChoice >= 1 && carTypeChoice <= carTypes.length) {
                        user.setCarType(carTypes[carTypeChoice - 1]);
                    } else {
                        System.out.println("Invalid choice. Keeping current car type.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice. Keeping current car type.");
                }
            }

            try {
                boolean success = userService.updateUser(user);
                if (success) {
                    System.out.println("User updated successfully.");
                } else {
                    System.out.println("Failed to update user.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error updating user: " + e.getMessage());
            }
        }
    }

    private static void deleteUser() {
        int id = getIntInput("Enter user ID to delete: ");

        try {
            boolean success = userService.deleteUser(id);
            if (success) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("User not found with ID: " + id);
            }
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private static void addFundsToUser() {
        int id = getIntInput("Enter user ID: ");
        User user = userService.getUserById(id);

        if (user == null) {
            System.out.println("User not found with ID: " + id);
            return;
        }

        System.out.println("Current balance: $" + user.getBalance());
        double amount = getDoubleInput("Enter amount to add: ");

        try {
            double newBalance = userService.addFunds(id, amount);
            System.out.println("Funds added successfully. New balance: $" + newBalance);
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding funds: " + e.getMessage());
        }
    }

    // Chain of Responsibility demo methods

    private static void demoStationOccupied() {
        System.out.println("\n===== Scenario 1: Station Occupied =====");

        // First, make sure we have a user
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found. Please add a user first.");
            return;
        }

        // List users to choose from
        System.out.println("Available users:");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.println((i + 1) + ". " + user.getName() + " (Balance: $" + user.getBalance() + ")");
        }

        int userChoice = getIntInput("Choose a user (1-" + users.size() + "): ");
        if (userChoice < 1 || userChoice > users.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        User selectedUser = users.get(userChoice - 1);

        // Find a station that is already occupied
        List<ChargingStation> stations = stationService.getAllChargingStations();
        List<ChargingStation> occupiedStations = new ArrayList<>();

        for (ChargingStation station : stations) {
            if (station.getStatus() == ChargingStation.ChargingStationStatus.OCCUPIED) {
                occupiedStations.add(station);
            }
        }

        if (occupiedStations.isEmpty()) {
            // If no occupied station, occupy one first
            System.out.println("No occupied stations found. Occupying a station first...");

            // Find an available station
            List<ChargingStation> availableStations = stations.stream()
                    .filter(s -> s.getStatus() == ChargingStation.ChargingStationStatus.AVAILABLE)
                    .collect(Collectors.toList());

            if (availableStations.isEmpty()) {
                System.out.println("No available stations to occupy.");
                return;
            }

            ChargingStation stationToOccupy = availableStations.get(0);
            stationToOccupy.setStatus(ChargingStation.ChargingStationStatus.OCCUPIED);
            occupiedStations.add(stationToOccupy);
            System.out.println("Station " + stationToOccupy.getName() + " is now occupied.");
        }

        // List occupied stations to choose from
        System.out.println("Occupied stations:");
        for (int i = 0; i < occupiedStations.size(); i++) {
            ChargingStation station = occupiedStations.get(i);
            System.out.println((i + 1) + ". " + station.getName());
        }

        int stationChoice = getIntInput("Choose an occupied station (1-" + occupiedStations.size() + "): ");
        if (stationChoice < 1 || stationChoice > occupiedStations.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        ChargingStation selectedStation = occupiedStations.get(stationChoice - 1);

        // Try to start charging - should be rejected due to station being occupied
        try {
            double estimatedCost = 25.0; // Example cost
            boolean success = chargingProcessor.processChargingRequest(
                    selectedUser.getId(),
                    selectedStation.getId(),
                    estimatedCost
            );

            System.out.println("\nCharging request result: " + (success ? "Approved" : "Rejected"));
        } catch (Exception e) {
            System.out.println("Error processing charging request: " + e.getMessage());
        }
    }

    private static void demoInsufficientFunds() {
        System.out.println("\n===== Scenario 2: Insufficient Funds =====");

        // Find or create a user with low balance
        User userWithLowBalance = null;
        List<User> users = userService.getAllUsers();

        for (User user : users) {
            if (user.getBalance() < 20.0) {
                userWithLowBalance = user;
                break;
            }
        }

        if (userWithLowBalance == null) {
            // Create a user with low balance if none exists
            try {
                userWithLowBalance = new User(0, "Low Balance User", "lowbalance@example.com", 5.0, User.CarType.SEDAN);
                int id = userService.addUser(userWithLowBalance);
                userWithLowBalance = userService.getUserById(id);
                System.out.println("Created a user with low balance: " + userWithLowBalance.getName() + " (Balance: $" + userWithLowBalance.getBalance() + ")");
            } catch (Exception e) {
                System.out.println("Error creating user with low balance: " + e.getMessage());
                return;
            }
        } else {
            System.out.println("Using user with low balance: " + userWithLowBalance.getName() + " (Balance: $" + userWithLowBalance.getBalance() + ")");
        }

        // Find an available station
        List<ChargingStation> stations = stationService.getAllChargingStations();
        List<ChargingStation> availableStations = stations.stream()
                .filter(s -> s.getStatus() == ChargingStation.ChargingStationStatus.AVAILABLE)
                .collect(Collectors.toList());

        if (availableStations.isEmpty()) {
            System.out.println("No available stations found. Please make a station available first.");
            return;
        }

        // Choose the first available station
        ChargingStation selectedStation = availableStations.get(0);
        System.out.println("Using available station: " + selectedStation.getName());

        // Try to start charging with a cost higher than the user's balance
        double estimatedCost = userWithLowBalance.getBalance() + 10.0; // Ensure it's higher than the balance
        System.out.println("User balance: $" + userWithLowBalance.getBalance());
        System.out.println("Estimated charging cost: $" + estimatedCost);

        try {
            boolean success = chargingProcessor.processChargingRequest(
                    userWithLowBalance.getId(),
                    selectedStation.getId(),
                    estimatedCost
            );

            System.out.println("\nCharging request result: " + (success ? "Approved" : "Rejected"));
        } catch (Exception e) {
            System.out.println("Error processing charging request: " + e.getMessage());
        }
    }

    private static void demoIncompatibleVehicle() {
        System.out.println("\n===== Scenario 3: Incompatible Vehicle =====");

        // Find or create a user with a compact car
        User userWithCompactCar = null;
        List<User> users = userService.getAllUsers();

        for (User user : users) {
            if (user.getCarType() == User.CarType.COMPACT) {
                userWithCompactCar = user;
                break;
            }
        }

        if (userWithCompactCar == null) {
            // Create a user with a compact car if none exists
            try {
                userWithCompactCar = new User(0, "Compact Car User", "compact@example.com", 100.0, User.CarType.COMPACT);
                int id = userService.addUser(userWithCompactCar);
                userWithCompactCar = userService.getUserById(id);
                System.out.println("Created a user with a compact car: " + userWithCompactCar.getName());
            } catch (Exception e) {
                System.out.println("Error creating user with compact car: " + e.getMessage());
                return;
            }
        } else {
            System.out.println("Using user with compact car: " + userWithCompactCar.getName());
        }

        // Find a high-power station (compact cars can only use up to 50kW stations)
        List<ChargingStation> stations = stationService.getAllChargingStations();
        List<ChargingStation> highPowerStations = stations.stream()
                .filter(s -> s.getStatus() == ChargingStation.ChargingStationStatus.AVAILABLE && s.getMaxPowerKw() > 100.0)
                .collect(Collectors.toList());

        if (highPowerStations.isEmpty()) {
            System.out.println("No high-power available stations found. Please make a high-power station available first.");
            return;
        }

        // Choose the first high-power station
        ChargingStation selectedStation = highPowerStations.get(0);
        System.out.println("Using high-power station: " + selectedStation.getName() + " (" + selectedStation.getMaxPowerKw() + "kW)");

        // Try to start charging - should be rejected due to incompatible vehicle
        double estimatedCost = 20.0; // Example cost within user's balance
        System.out.println("User car type: " + userWithCompactCar.getCarType());
        System.out.println("Station max power: " + selectedStation.getMaxPowerKw() + "kW");

        try {
            boolean success = chargingProcessor.processChargingRequest(
                    userWithCompactCar.getId(),
                    selectedStation.getId(),
                    estimatedCost
            );

            System.out.println("\nCharging request result: " + (success ? "Approved" : "Rejected"));
        } catch (Exception e) {
            System.out.println("Error processing charging request: " + e.getMessage());
        }
    }

    private static void demoSuccessfulCharging() {
        System.out.println("\n===== Scenario 4: Successful Charging =====");

        // Find a user with sufficient balance and compatible car type
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found. Please add a user first.");
            return;
        }

        // List users to choose from
        System.out.println("Available users:");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.println((i + 1) + ". " + user.getName() + " (Balance: $" + user.getBalance() + ", Car: " + user.getCarType() + ")");
        }

        int userChoice = getIntInput("Choose a user (1-" + users.size() + "): ");
        if (userChoice < 1 || userChoice > users.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        User selectedUser = users.get(userChoice - 1);

        // Find available stations that are compatible with the user's car type
        List<ChargingStation> stations = stationService.getAllChargingStations();
        List<ChargingStation> availableStations = stations.stream()
                .filter(s -> s.getStatus() == ChargingStation.ChargingStationStatus.AVAILABLE)
                .collect(Collectors.toList());

        if (availableStations.isEmpty()) {
            System.out.println("No available stations found. Please make a station available first.");
            return;
        }

        // List available stations to choose from
        System.out.println("Available stations:");
        for (int i = 0; i < availableStations.size(); i++) {
            ChargingStation station = availableStations.get(i);
            System.out.println((i + 1) + ". " + station.getName() + " (Power: " + station.getMaxPowerKw() + "kW)");
        }

        int stationChoice = getIntInput("Choose a station (1-" + availableStations.size() + "): ");
        if (stationChoice < 1 || stationChoice > availableStations.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        ChargingStation selectedStation = availableStations.get(stationChoice - 1);

        // Get estimated cost (make sure it's within the user's balance)
        double maxCost = selectedUser.getBalance() * 0.9; // 90% of user's balance
        double estimatedCost = getDoubleInputWithinRange("Enter estimated cost (max $" + maxCost + "): ", 1.0, maxCost);

        // Try to start charging - should be successful
        try {
            boolean success = chargingProcessor.processChargingRequest(
                    selectedUser.getId(),
                    selectedStation.getId(),
                    estimatedCost
            );

            System.out.println("\nCharging request result: " + (success ? "Approved" : "Rejected"));

            if (success) {
                // Check the station status after charging
                ChargingStation updatedStation = stationService.getChargingStationById(selectedStation.getId());
                System.out.println("Station status: " + updatedStation.getStatus());

                // Check the user's balance after charging
                User updatedUser = userService.getUserById(selectedUser.getId());
                System.out.println("Updated user balance: $" + updatedUser.getBalance());

                // Ask if user wants to stop charging
                boolean stopCharging = getYesNoInput("Do you want to stop charging? (y/n): ");
                if (stopCharging) {
                    boolean stopped = chargingProcessor.stopCharging(selectedStation.getId());
                    if (stopped) {
                        System.out.println("Charging stopped successfully.");
                        // Check the station status after stopping
                        updatedStation = stationService.getChargingStationById(selectedStation.getId());
                        System.out.println("Station status: " + updatedStation.getStatus());
                    } else {
                        System.out.println("Failed to stop charging.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing charging request: " + e.getMessage());
        }
    }

    // Helper methods

    private static int selectProvider() {
        // Get all providers
        List<Provider> providers = providerService.getAllProviders();
        if (providers.isEmpty()) {
            System.out.println("No providers found in the system.");
            return -1;
        }

        // Display available providers
        System.out.println("\nAvailable Providers:");
        for (Provider provider : providers) {
            System.out.println(provider.getId() + ". " + provider.getName() +
                    " (" + provider.getChargingStations().size() + " stations)");
        }

        return getIntInput("Enter provider ID or 0 to cancel: ");
    }

    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static String getStringInputWithDefault(String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }

    private static int getIntInputWithDefault(String prompt, int defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Using default value: " + defaultValue);
            return defaultValue;
        }
    }

    private static double getDoubleInputWithDefault(String prompt, double defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Using default value: " + defaultValue);
            return defaultValue;
        }
    }

    private static double getDoubleInputWithinRange(String prompt, double min, double max) {
        while (true) {
            double value = getDoubleInput(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Please enter a value between " + min + " and " + max + ".");
        }
    }
}