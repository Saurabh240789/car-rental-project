package com.carrental.pricing;

import com.carrental.model.enums.VehicleCategory;
import org.springframework.stereotype.Component;

@Component
public class VanPricingStrategy implements PricingStrategy {

    @Override
    public VehicleCategory supportedCategory() {
        return VehicleCategory.VAN;
    }

    @Override
    public double calculatePrice(int durationDays, int dailyMileage) {
        return 22.0 * durationDays;
    }
}