package com.evplatform.chainofresponsibility;

import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.User;

import java.util.HashMap;
import java.util.Map;

public class VehicleCompatibilityHandler implements ChargingRequestHandler {
    private ChargingRequestHandler next;

    // Map to define which car types are compatible with different power ranges
    private static final Map<User.CarType, Double> MAX_POWER_LIMIT = new HashMap<>();

    static {
        // Initialize compatibility rules
        MAX_POWER_LIMIT.put(User.CarType.COMPACT, 50.0); // Compact cars up to 50kW
        MAX_POWER_LIMIT.put(User.CarType.SEDAN, 100.0);  // Sedans up to 100kW
        MAX_POWER_LIMIT.put(User.CarType.SUV, 150.0);    // SUVs up to 150kW
        MAX_POWER_LIMIT.put(User.CarType.VAN, 100.0);    // Vans up to 100kW
        MAX_POWER_LIMIT.put(User.CarType.TRUCK, 200.0);  // Trucks up to 200kW
        MAX_POWER_LIMIT.put(User.CarType.LUXURY, 350.0); // Luxury cars up to 350kW
        MAX_POWER_LIMIT.put(User.CarType.SPORTS, 350.0); // Sports cars up to 350kW
    }

    @Override
    public void setNextHandler(ChargingRequestHandler next) {
        this.next = next;
    }

    @Override
    public boolean handleRequest(User user, ChargingStation station, double estimatedCost) {
        User.CarType carType = user.getCarType();
        double stationPower = station.getMaxPowerKw();

        // Check if car type is compatible with this station
        Double maxPowerLimit = MAX_POWER_LIMIT.get(carType);

        if (maxPowerLimit != null && stationPower > maxPowerLimit) {
            System.out.println("Vehicle type " + carType + " is not compatible with this charging station. " +
                    "Maximum power supported: " + maxPowerLimit + "kW, Station power: " + stationPower + "kW");
            return false;
        }

        // This is the final handler in our chain, so we'll apply the charging
        System.out.println("All checks passed. Starting charging session...");

        // Apply the charging - modify station and deduct funds
        station.setStatus(ChargingStation.ChargingStationStatus.OCCUPIED);
        station.setCurrentUserEmail(user.getEmail());
        user.setBalance(user.getBalance() - estimatedCost);

        System.out.println("Charging started successfully for user " + user.getName() +
                " at station " + station.getName() +
                " (Cost: $" + estimatedCost + ")");

        return true;
    }
}