package com.carrental.pricing;

import com.carrental.model.enums.VehicleCategory;

public interface PricingStrategy {

    VehicleCategory supportedCategory();

    double calculatePrice(
            int durationDays,
            int dailyMileage);
}