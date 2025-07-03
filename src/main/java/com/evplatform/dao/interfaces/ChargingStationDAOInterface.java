package com.evplatform.dao.interfaces;

import com.evplatform.vao.ChargingStation;
import jakarta.ejb.Local;
import java.util.List;
import java.util.Optional;

@Local
public interface ChargingStationDAOInterface {
    int add(ChargingStation station);
    ChargingStation getById(int id);
    List<ChargingStation> getAll();
    boolean update(ChargingStation station);
    boolean delete(int id);
    List<ChargingStation> getByProviderId(int providerId);
    boolean updateStatus(int id, ChargingStation.ChargingStationStatus status);
    Optional<ChargingStation> findById(int id);
}