package com.carrental.pricing;

import com.carrental.config.PricingProperties;
import com.carrental.model.enums.VehicleCategory;
import org.springframework.stereotype.Component;

@Component
public class SuvPricingRule implements PricingRule {

    private final PricingProperties properties;

    public SuvPricingRule(PricingProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean supports(PricingContext context) {
        return context.category() == VehicleCategory.SUV;
    }
    
    @Override
    public VehicleCategory category() {
        return VehicleCategory.SUV;
    }

    @Override
    public double calculate(PricingContext context) {

        PricingProperties.Suv suv = properties.getSuv();

        return (suv.getBaseDayRate() * context.days())
                + (suv.getMileageRate()
                * context.dailyMileage()
                * context.days());
    }
}