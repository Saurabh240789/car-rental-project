package com.carrental.pricing.base;

import com.carrental.config.PricingProperties;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.pricing.PricingContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PickupTruckPricingRule implements PricingRule {

    private final PricingProperties properties;

    public PickupTruckPricingRule(PricingProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean supports(PricingContext context) {
        return context.category() == VehicleCategory.PICKUP_TRUCK;
    }

    @Override
    public VehicleCategory category() {
        return VehicleCategory.PICKUP_TRUCK;
    }

    @Override
    public BigDecimal calculate(PricingContext context) {
        return BigDecimal.valueOf(properties.getPickup().getBaseDayRate() * context.days());
    }
}