package com.evplatform.vao;

import jakarta.json.bind.annotation.JsonbTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Provider {
    private int id;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private List<ChargingStation> chargingStations = new ArrayList<>();

    public Provider() {
    }

    public Provider(int id, String name, String contactPerson, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.address = address;
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
            throw new IllegalArgumentException("Provider name cannot be empty");
        }
        this.name = name;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JsonbTransient
    public List<ChargingStation> getChargingStations() {
        return new ArrayList<>(chargingStations);
    }

    public void setChargingStations(List<ChargingStation> chargingStations) {
        this.chargingStations = new ArrayList<>(chargingStations);
    }

    public void addChargingStation(ChargingStation station) {
        if (station != null) {
            chargingStations.add(station);
            if (station.getProvider() != this) {
                station.setProvider(this);
            }
        }
    }

    public boolean removeChargingStation(ChargingStation station) {
        boolean removed = chargingStations.remove(station);
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
                ", chargingStations=" + chargingStations.size() +
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