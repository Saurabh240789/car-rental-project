package com.carrental.model;

import com.carrental.model.enums.VehicleCategory;

import java.math.BigDecimal;

public record VehicleAvailability(
        VehicleCategory category,
        BigDecimal amount,
        int availableCount) {
}