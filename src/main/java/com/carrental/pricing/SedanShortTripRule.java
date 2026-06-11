package com.carrental.pricing;

import com.carrental.config.PricingProperties;
import com.carrental.model.enums.VehicleCategory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class SedanShortTripRule implements PricingRule {

    private final PricingProperties properties;

    public SedanShortTripRule(PricingProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean supports(PricingContext context) {
        return context.category() == VehicleCategory.SEDAN
                && context.days() < 10;
    }
    
    @Override
    public VehicleCategory category() {
        return VehicleCategory.SEDAN;
    }

    @Override
    public double calculate(PricingContext context) {
        return properties.getSedan().getShortDayRate()
                * context.days();
    }
}