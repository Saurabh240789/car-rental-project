package com.carrental.pricing;

import com.carrental.model.enums.VehicleCategory;
import org.springframework.stereotype.Component;

@Component
public class SedanPricingStrategy
        implements PricingStrategy {

    @Override
    public VehicleCategory supportedCategory() {
        return VehicleCategory.SEDAN;
    }

    @Override
    public double calculatePrice(
            int durationDays,
            int dailyMileage) {

        double dailyRate =
                durationDays < 10
                        ? 20
                        : 15;

        return dailyRate * durationDays;
    }
}