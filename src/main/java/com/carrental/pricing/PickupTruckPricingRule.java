package com.carrental.pricing;

import com.carrental.config.PricingProperties;
import com.carrental.model.enums.VehicleCategory;
import org.springframework.stereotype.Component;

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
    public double calculate(PricingContext context) {
        return properties.getPickup().getBaseDayRate()
                * context.days();
    }
}