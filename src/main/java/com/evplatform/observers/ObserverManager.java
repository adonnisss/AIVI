package com.evplatform.observers;

import com.evplatform.vao.ChargingStation;
import com.evplatform.service.ChargingStationService;
import java.util.List;


public class ObserverManager {
    private static volatile ObserverManager instance;
    private final UserNotifier userNotifier;
    private final ProviderNotifier providerNotifier;
    private final ChargingStationDisplay stationDisplay;


    private ObserverManager() {
        this.userNotifier = new UserNotifier();
        this.providerNotifier = new ProviderNotifier();
        this.stationDisplay = new ChargingStationDisplay();
    }


    public static ObserverManager getInstance() {
        if (instance == null) {
            synchronized (ObserverManager.class) {
                if (instance == null) {
                    instance = new ObserverManager();
                }
            }
        }
        return instance;
    }


    public void registerAllObservers() {
        List<ChargingStation> stations = ChargingStationService.getInstance().getAllChargingStations();

        for (ChargingStation station : stations) {
            registerObservers(station);
        }
    }


    public void registerObservers(ChargingStation station) {
        station.addObserver(userNotifier);
        station.addObserver(providerNotifier);
        station.addObserver(stationDisplay);
    }


    public UserNotifier getUserNotifier() {
        return userNotifier;
    }


    public ProviderNotifier getProviderNotifier() {
        return providerNotifier;
    }


    public ChargingStationDisplay getStationDisplay() {
        return stationDisplay;
    }
}