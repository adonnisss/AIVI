package com.evplatform.chainofresponsibility;

import com.evplatform.vao.User;
import com.evplatform.vao.ChargingStation;

public interface ChargingRequestHandler {
    void setNextHandler(ChargingRequestHandler next);
    boolean handleRequest(User user, ChargingStation station, double estimatedCost);
}