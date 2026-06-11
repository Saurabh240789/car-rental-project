package com.carrental.pricing.base;

import com.carrental.config.PricingProperties;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.pricing.PricingContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
    public BigDecimal calculate(PricingContext context) {

        PricingProperties.Suv suv = properties.getSuv();

        return BigDecimal.valueOf((suv.getBaseDayRate() * context.days())
                + (suv.getMileageRate()
                * context.dailyMileage()
                * context.days()));
    }
}