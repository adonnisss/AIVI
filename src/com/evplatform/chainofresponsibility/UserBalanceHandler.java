package com.evplatform.chainofresponsibility;

import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.User;

public class UserBalanceHandler implements ChargingRequestHandler {
    private ChargingRequestHandler next;

    @Override
    public void setNextHandler(ChargingRequestHandler next) {
        this.next = next;
    }

    @Override
    public boolean handleRequest(User user, ChargingStation station, double estimatedCost) {
        if (user.getBalance() < estimatedCost) {
            System.out.println("Insufficient funds. User balance: $" + user.getBalance() +
                    ", Estimated cost: $" + estimatedCost);
            return false;
        }

        return next != null ? next.handleRequest(user, station, estimatedCost) : true;
    }
}