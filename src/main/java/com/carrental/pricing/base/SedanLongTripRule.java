package com.carrental.pricing.base;

import com.carrental.config.PricingProperties;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.pricing.PricingContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SedanLongTripRule implements PricingRule {

    private final PricingProperties properties;

    public SedanLongTripRule(PricingProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean supports(PricingContext context) {
        return context.category() == VehicleCategory.SEDAN && context.days() >= properties.getSedan().getLongDayThreshold();
    }

    @Override
    public VehicleCategory category() {
        return VehicleCategory.SEDAN;
    }

    @Override
    public BigDecimal calculate(PricingContext context) {
        return BigDecimal.valueOf(properties.getSedan().getLongDayRate() * context.days());
    }
}