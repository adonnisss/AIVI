package com.evplatform.service.interfaces;

import com.evplatform.vao.ChargingStation;
import jakarta.ejb.Local; // <- DODAJTE
import java.util.List;
import java.util.Optional;

@Local // <- DODAJTE
public interface ChargingStationServiceInterface {
    int addChargingStation(ChargingStation station) throws IllegalArgumentException, IllegalStateException;
    ChargingStation getChargingStationById(int id);
    Optional<ChargingStation> findChargingStationById(int id);
    List<ChargingStation> getAllChargingStations();
    boolean updateChargingStation(ChargingStation station) throws IllegalArgumentException, IllegalStateException;
    boolean deleteChargingStation(int id);
    List<ChargingStation> getChargingStationsByProviderId(int providerId) throws IllegalArgumentException;
    boolean updateChargingStationStatus(int id, ChargingStation.ChargingStationStatus status) throws IllegalArgumentException;
}