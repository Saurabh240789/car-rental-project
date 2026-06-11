package com.carrental.pricing;

import com.carrental.model.enums.VehicleCategory;

public record PricingContext(
        VehicleCategory category,
        int days,
        int dailyMileage
) {
}