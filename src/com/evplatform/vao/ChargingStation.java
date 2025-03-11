package com.evplatform.vao;

import java.util.Objects;

/**
 * Value Access Object (VAO) representing an electric charging station.
 * Contains only data without business logic.
 */
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

    /**
     * Enum for charging station status
     */
    public enum ChargingStationStatus {
        AVAILABLE,
        OCCUPIED,
        OUT_OF_SERVICE,
        MAINTENANCE
    }

    /**
     * Default constructor
     */
    public ChargingStation() {
    }

    /**
     * Parameterized constructor with provider ID
     *
     * @param id Unique identifier for the charging station
     * @param name Name/label of the charging station
     * @param location Human-readable location description
     * @param coordinates GPS coordinates in "latitude,longitude" format
     * @param status Current operational status
     * @param providerId ID of the provider who owns this station
     * @param numberOfConnectors Number of connectors available
     * @param maxPowerKw Maximum power output in kilowatts
     */
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

    /**
     * Parameterized constructor with provider object
     *
     * @param id Unique identifier for the charging station
     * @param name Name/label of the charging station
     * @param location Human-readable location description
     * @param coordinates GPS coordinates in "latitude,longitude" format
     * @param status Current operational status
     * @param provider Provider who owns this station
     * @param numberOfConnectors Number of connectors available
     * @param maxPowerKw Maximum power output in kilowatts
     */
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

    /**
     * Get station ID
     * @return The unique identifier of the station
     */
    public int getId() {
        return id;
    }

    /**
     * Set station ID
     * @param id The unique identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get station name
     * @return The name of the station
     */
    public String getName() {
        return name;
    }

    /**
     * Set station name
     * @param name The name to set
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Charging station name cannot be empty");
        }
        this.name = name;
    }

    /**
     * Get location description
     * @return The location description
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set location description
     * @param location The location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Get GPS coordinates
     * @return The coordinates in "latitude,longitude" format
     */
    public String getCoordinates() {
        return coordinates;
    }

    /**
     * Set GPS coordinates
     * @param coordinates The coordinates to set (in "latitude,longitude" format)
     */
    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Get current station status
     * @return The status enum value
     */
    public ChargingStationStatus getStatus() {
        return status;
    }

    /**
     * Set station status
     * @param status The status to set
     * @throws IllegalArgumentException if status is null
     */
    public void setStatus(ChargingStationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    /**
     * Get provider ID
     * @return The ID of the provider who owns this station
     */
    public int getProviderId() {
        return providerId;
    }

    /**
     * Set provider ID
     * @param providerId The provider ID to set
     * @throws IllegalArgumentException if providerId is negative
     */
    public void setProviderId(int providerId) {
        if (providerId < 0) {
            throw new IllegalArgumentException("Provider ID cannot be negative");
        }
        this.providerId = providerId;
    }

    /**
     * Get provider object
     * @return The provider who owns this station
     */
    public Provider getProvider() {
        return provider;
    }

    /**
     * Set provider object
     * @param provider The provider to set
     */
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

    /**
     * Get number of connectors
     * @return The number of connectors available
     */
    public int getNumberOfConnectors() {
        return numberOfConnectors;
    }

    /**
     * Set number of connectors
     * @param numberOfConnectors The number to set
     * @throws IllegalArgumentException if number of connectors is negative
     */
    public void setNumberOfConnectors(int numberOfConnectors) {
        if (numberOfConnectors < 0) {
            throw new IllegalArgumentException("Number of connectors cannot be negative");
        }
        this.numberOfConnectors = numberOfConnectors;
    }

    /**
     * Get maximum power output
     * @return The maximum power in kilowatts
     */
    public double getMaxPowerKw() {
        return maxPowerKw;
    }

    /**
     * Set maximum power output
     * @param maxPowerKw The maximum power to set in kilowatts
     * @throws IllegalArgumentException if power is negative
     */
    public void setMaxPowerKw(double maxPowerKw) {
        if (maxPowerKw < 0) {
            throw new IllegalArgumentException("Maximum power cannot be negative");
        }
        this.maxPowerKw = maxPowerKw;
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