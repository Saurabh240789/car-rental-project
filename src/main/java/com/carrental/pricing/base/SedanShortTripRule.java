package com.carrental.pricing.base;

import com.carrental.config.PricingProperties;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.pricing.PricingContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Primary
public class SedanShortTripRule implements PricingRule {

    private final PricingProperties properties;

    public SedanShortTripRule(PricingProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean supports(PricingContext context) {
        return context.category() == VehicleCategory.SEDAN && context.days() < properties.getSedan().getLongDayThreshold();
    }

    @Override
    public VehicleCategory category() {
        return VehicleCategory.SEDAN;
    }

    @Override
    public BigDecimal calculate(PricingContext context) {
        return BigDecimal.valueOf(properties.getSedan().getShortDayRate() * context.days());
    }
}