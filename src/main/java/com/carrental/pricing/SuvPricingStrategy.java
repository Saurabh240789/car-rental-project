package com.carrental.pricing;

import com.carrental.model.enums.VehicleCategory;
import org.springframework.stereotype.Component;

@Component
public class SuvPricingStrategy
        implements PricingStrategy {

    @Override
    public VehicleCategory supportedCategory() {
        return VehicleCategory.SUV;
    }

    @Override
    public double calculatePrice(int durationDays, int dailyMileage) {

        double baseAmount = 15.0 * durationDays;

        double mileageCost = 0.50 * dailyMileage * durationDays;

        return baseAmount + mileageCost;
    }
}