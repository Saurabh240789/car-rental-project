package com.carrental.model;

import com.carrental.model.enums.VehicleCategory;

import java.util.List;

public final class VehicleSeedData {

    private VehicleSeedData() {
    }

    public static List<Vehicle> vehicles() {
        return List.of(
                new Vehicle("SEDAN-1", VehicleCategory.SEDAN),
                new Vehicle("SEDAN-2", VehicleCategory.SEDAN),
                new Vehicle("SUV-1", VehicleCategory.SUV),
                new Vehicle("SUV-2", VehicleCategory.SUV),
                new Vehicle("VAN-1", VehicleCategory.VAN),
                new Vehicle("PICKUP-1", VehicleCategory.PICKUP_TRUCK)
        );
    }
}