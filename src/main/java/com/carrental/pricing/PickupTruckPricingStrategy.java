package com.carrental.pricing;

import com.carrental.model.enums.VehicleCategory;
import org.springframework.stereotype.Component;

@Component
public class PickupTruckPricingStrategy implements PricingStrategy {

    @Override
    public VehicleCategory supportedCategory() {
        return VehicleCategory.PICKUP_TRUCK;
    }

    @Override
    public double calculatePrice(int durationDays, int dailyMileage) {
        return 30.0 * durationDays;
    }
}