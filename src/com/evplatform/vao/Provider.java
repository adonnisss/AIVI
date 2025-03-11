package com.evplatform.vao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Value Access Object (VAO) representing a provider of electric charging stations.
 * Contains only data without business logic.
 */
public class Provider {
    private int id;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private List<ChargingStation> chargingStations = new ArrayList<>();

    /**
     * Default constructor
     */
    public Provider() {
    }

    /**
     * Parameterized constructor
     *
     * @param id Provider's unique identifier
     * @param name Provider's company name
     * @param contactPerson Name of the main contact person
     * @param email Contact email address
     * @param phone Contact phone number
     * @param address Physical address of the provider
     */
    public Provider(int id, String name, String contactPerson, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    /**
     * Get provider ID
     * @return The unique identifier of the provider
     */
    public int getId() {
        return id;
    }

    /**
     * Set provider ID
     * @param id The unique identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get provider name
     * @return The name of the provider
     */
    public String getName() {
        return name;
    }

    /**
     * Set provider name
     * @param name The name to set
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Provider name cannot be empty");
        }
        this.name = name;
    }

    /**
     * Get contact person name
     * @return The name of the contact person
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * Set contact person name
     * @param contactPerson The contact person to set
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    /**
     * Get email address
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set email address
     * @param email The email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get phone number
     * @return The phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set phone number
     * @param phone The phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Get address
     * @return The physical address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set address
     * @param address The address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get all charging stations owned by this provider
     * @return List of charging stations
     */
    public List<ChargingStation> getChargingStations() {
        return new ArrayList<>(chargingStations); // Return a defensive copy
    }

    /**
     * Set charging stations for this provider
     * @param chargingStations List of charging stations
     */
    public void setChargingStations(List<ChargingStation> chargingStations) {
        this.chargingStations = new ArrayList<>(chargingStations); // Create a defensive copy
    }

    /**
     * Add a charging station to this provider
     * @param station The charging station to add
     */
    public void addChargingStation(ChargingStation station) {
        if (station != null) {
            chargingStations.add(station);
            // Set the provider reference in the charging station if not already set
            if (station.getProvider() != this) {
                station.setProvider(this);
            }
        }
    }

    /**
     * Remove a charging station from this provider
     * @param station The charging station to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeChargingStation(ChargingStation station) {
        boolean removed = chargingStations.remove(station);
        // Clear the provider reference in the charging station if removed
        if (removed && station.getProvider() == this) {
            station.setProvider(null);
        }
        return removed;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", chargingStations=" + chargingStations.size() + // Just show the count to avoid recursion
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Provider provider = (Provider) o;
        return id == provider.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}