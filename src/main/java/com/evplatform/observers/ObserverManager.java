package com.evplatform.observers;

import com.evplatform.service.interfaces.ChargingStationServiceInterface;
import com.evplatform.vao.ChargingStation;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.util.List;

@Singleton
@Startup
public class ObserverManager {

    @EJB
    private ChargingStationServiceInterface stationService;

    private UserNotifier userNotifier;
    private ProviderNotifier providerNotifier;
    private ChargingStationDisplay stationDisplay;

    @PostConstruct
    private void init() {
        // Create instances of observers, injecting the service where needed
        this.userNotifier = new UserNotifier();
        this.providerNotifier = new ProviderNotifier();
        this.stationDisplay = new ChargingStationDisplay(stationService);

        // Register observers for all existing stations at startup
        registerAllObservers();
    }

    public void registerAllObservers() {
        List<ChargingStation> stations = stationService.getAllChargingStations();
        for (ChargingStation station : stations) {
            registerObservers(station);
        }
        System.out.println("All observers have been registered for existing stations.");
    }

    public void registerObservers(ChargingStation station) {
        station.addObserver(userNotifier);
        station.addObserver(providerNotifier);
        station.addObserver(stationDisplay);
    }
}