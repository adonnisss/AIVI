package com.evplatform.chainofresponsibility;

import com.evplatform.service.interfaces.ChargingProcessorRemote;
import com.evplatform.service.interfaces.ChargingStationServiceInterface;
import com.evplatform.service.interfaces.UserServiceInterface;
import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.User;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class ChargingRequestProcessor implements ChargingProcessorRemote {

    @EJB
    private UserServiceInterface userService;

    @EJB
    private ChargingStationServiceInterface stationService;

    private final ChargingRequestHandler chain;

    public ChargingRequestProcessor() {
        StationAvailabilityHandler availabilityHandler = new StationAvailabilityHandler();
        UserBalanceHandler balanceHandler = new UserBalanceHandler();
        VehicleCompatibilityHandler compatibilityHandler = new VehicleCompatibilityHandler();

        availabilityHandler.setNextHandler(balanceHandler);
        balanceHandler.setNextHandler(compatibilityHandler);

        this.chain = availabilityHandler;
    }

    @Override
    public boolean processChargingRequest(int userId, int stationId, double estimatedCost) {
        // Logika uporablja injicirane servise namesto klicev getInstance().
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

        return chain.handleRequest(user, station, estimatedCost);
    }

    @Override
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

        station.setStatus(ChargingStation.ChargingStationStatus.AVAILABLE);
        station.setCurrentUserEmail(null);
        stationService.updateChargingStation(station); // Uporabimo servis za posodobitev vira podatkov.

        System.out.println("Charging stopped successfully at station " + station.getName());
        return true;
    }
}