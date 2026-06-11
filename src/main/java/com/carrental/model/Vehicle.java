package com.carrental.model;

import com.carrental.model.enums.VehicleCategory;

public record Vehicle(String vehicleId, VehicleCategory category) {
}