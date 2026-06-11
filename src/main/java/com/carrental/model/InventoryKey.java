package com.carrental.model;

import com.carrental.model.enums.VehicleCategory;

import java.time.LocalDate;

public record InventoryKey(
        VehicleCategory vehicleCategory,
        LocalDate date) {
}