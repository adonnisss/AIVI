package com.evplatform.vao;

import com.evplatform.observers.ChargingStationObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ChargingStation {
    private int id;
    private String name;
    private String location;
    private String coordinates; // GPS coordinates in format "latitude,longitude"
    private ChargingStationStatus status;
    private Provider provider; // Reference to the provider object
    private int providerId; // ID of the provider (maintained for backward compatibility)
    private int numberOfConnectors;
    private double maxPowerKw;
    private String currentUserEmail; // Email of the user currently using this station
    private final List<ChargingStationObserver> observers = new ArrayList<>(); // Observers list


    public enum ChargingStationStatus {
        AVAILABLE,
        OCCUPIED,
        OUT_OF_SERVICE,
        MAINTENANCE
    }


    public ChargingStation() {
    }


    public ChargingStation(int id, String name, String location, String coordinates,
                           ChargingStationStatus status, int providerId, int numberOfConnectors, double maxPowerKw) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.coordinates = coordinates;
        this.status = status;
        this.providerId = providerId;
        this.numberOfConnectors = numberOfConnectors;
        this.maxPowerKw = maxPowerKw;
        // Provider object will be set separately
    }


    public ChargingStation(int id, String name, String location, String coordinates,
                           ChargingStationStatus status, Provider provider, int numberOfConnectors, double maxPowerKw) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.coordinates = coordinates;
        this.status = status;
        setProvider(provider); // This will also set providerId
        this.numberOfConnectors = numberOfConnectors;
        this.maxPowerKw = maxPowerKw;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Charging station name cannot be empty");
        }
        this.name = name;
    }


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }


    public String getCoordinates() {
        return coordinates;
    }


    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }


    public ChargingStationStatus getStatus() {
        return status;
    }


    public void setStatus(ChargingStationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        // Store the old status for notification
        ChargingStationStatus oldStatus = this.status;

        // Update the status
        this.status = status;

        // If status changes from OCCUPIED to AVAILABLE, clear the current user email
        if (oldStatus == ChargingStationStatus.OCCUPIED && status == ChargingStationStatus.AVAILABLE) {
            this.currentUserEmail = null;
        }

        // Notify observers of the status change
        notifyObservers(oldStatus);
    }


    public int getProviderId() {
        return providerId;
    }


    public void setProviderId(int providerId) {
        if (providerId < 0) {
            throw new IllegalArgumentException("Provider ID cannot be negative");
        }
        this.providerId = providerId;
    }


    public Provider getProvider() {
        return provider;
    }


    public void setProvider(Provider provider) {
        // Remove this station from previous provider if exists
        if (this.provider != null && this.provider != provider) {
            this.provider.removeChargingStation(this);
        }

        this.provider = provider;

        // Update the providerId when provider is set
        if (provider != null) {
            this.providerId = provider.getId();

            // Add this station to the new provider's list if not already there
            if (!provider.getChargingStations().contains(this)) {
                provider.addChargingStation(this);
            }
        }
    }


    public int getNumberOfConnectors() {
        return numberOfConnectors;
    }


    public void setNumberOfConnectors(int numberOfConnectors) {
        if (numberOfConnectors < 0) {
            throw new IllegalArgumentException("Number of connectors cannot be negative");
        }
        this.numberOfConnectors = numberOfConnectors;
    }


    public double getMaxPowerKw() {
        return maxPowerKw;
    }


    public void setMaxPowerKw(double maxPowerKw) {
        if (maxPowerKw < 0) {
            throw new IllegalArgumentException("Maximum power cannot be negative");
        }
        this.maxPowerKw = maxPowerKw;
    }


    public String getCurrentUserEmail() {
        return currentUserEmail;
    }


    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
    }


    public void addObserver(ChargingStationObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }


    public boolean removeObserver(ChargingStationObserver observer) {
        return observers.remove(observer);
    }


    private void notifyObservers(ChargingStationStatus oldStatus) {
        for (ChargingStationObserver observer : observers) {
            observer.update(this, oldStatus);
        }
    }

    @Override
    public String toString() {
        return "ChargingStation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", coordinates='" + coordinates + '\'' +
                ", status=" + status +
                ", providerId=" + providerId +
                ", providerName=" + (provider != null ? provider.getName() : "null") +
                ", numberOfConnectors=" + numberOfConnectors +
                ", maxPowerKw=" + maxPowerKw +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChargingStation station = (ChargingStation) o;
        return id == station.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}