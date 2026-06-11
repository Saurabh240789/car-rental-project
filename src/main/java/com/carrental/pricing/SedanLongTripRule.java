package com.carrental.pricing;

import com.carrental.config.PricingProperties;
import com.carrental.model.enums.VehicleCategory;
import org.springframework.stereotype.Component;

@Component
public class SedanLongTripRule implements PricingRule {

    private final PricingProperties properties;

    public SedanLongTripRule(PricingProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean supports(PricingContext context) {
        return context.category() == VehicleCategory.VAN;
    }
    
    @Override
    public VehicleCategory category() {
        return VehicleCategory.SEDAN;
    }

    @Override
    public double calculate(PricingContext context) {
        return properties.getSedan().getLongDayRate()
                * context.days();
    }
}