package com.evplatform.chainofresponsibility;

import com.evplatform.service.UserService;
import com.evplatform.service.ChargingStationService;
import com.evplatform.vao.User;
import com.evplatform.vao.ChargingStation;

public class ChargingRequestProcessor {
    private static ChargingRequestProcessor instance;
    private final ChargingRequestHandler chain;
    private final UserService userService;
    private final ChargingStationService stationService;

    private ChargingRequestProcessor() {
        // Set up the chain
        StationAvailabilityHandler availabilityHandler = new StationAvailabilityHandler();
        UserBalanceHandler balanceHandler = new UserBalanceHandler();
        VehicleCompatibilityHandler compatibilityHandler = new VehicleCompatibilityHandler();

        // Connect the handlers
        availabilityHandler.setNextHandler(balanceHandler);
        balanceHandler.setNextHandler(compatibilityHandler);

        // Set first handler as the start of the chain
        this.chain = availabilityHandler;

        // Get service instances
        this.userService = UserService.getInstance();
        this.stationService = ChargingStationService.getInstance();
    }

    public static ChargingRequestProcessor getInstance() {
        if (instance == null) {
            synchronized (ChargingRequestProcessor.class) {
                if (instance == null) {
                    instance = new ChargingRequestProcessor();
                }
            }
        }
        return instance;
    }

    public boolean processChargingRequest(int userId, int stationId, double estimatedCost) {
        User user = userService.getUserById(userId);
        ChargingStation station = stationService.getChargingStationById(stationId);

        if (user == null) {
            System.out.println("User not found with ID: " + userId);
            return false;
        }

        if (station == null) {
            System.out.println("Charging station not found with ID: " + stationId);
            return false;
        }

        // Process the request through the chain
        return chain.handleRequest(user, station, estimatedCost);
    }

    public boolean stopCharging(int stationId) {
        ChargingStation station = stationService.getChargingStationById(stationId);

        if (station == null) {
            System.out.println("Charging station not found with ID: " + stationId);
            return false;
        }

        if (station.getStatus() != ChargingStation.ChargingStationStatus.OCCUPIED) {
            System.out.println("Charging station is not occupied: " + stationId);
            return false;
        }

        // Update station status to available
        station.setStatus(ChargingStation.ChargingStationStatus.AVAILABLE);
        station.setCurrentUserEmail(null);
        stationService.updateChargingStation(station);

        System.out.println("Charging stopped successfully at station " + station.getName());
        return true;
    }
}