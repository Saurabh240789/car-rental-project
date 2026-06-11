package com.carrental.exception;

public class VehicleAlreadyReservedException extends CarRentalException {

    public VehicleAlreadyReservedException(String vehicleId) {
        super("Vehicle already reserved : " + vehicleId);
    }
}