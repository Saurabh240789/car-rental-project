package com.carrental.exception;

public class VehicleNotFoundException extends CarRentalException {

    public VehicleNotFoundException(String vehicleId) {
        super("Vehicle not found : " + vehicleId);
    }
}