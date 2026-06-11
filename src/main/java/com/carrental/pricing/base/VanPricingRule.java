package com.carrental.pricing.base;

import com.carrental.config.PricingProperties;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.pricing.PricingContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class VanPricingRule implements PricingRule {

    private final PricingProperties properties;

    public VanPricingRule(PricingProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean supports(PricingContext context) {
        return context.category() == VehicleCategory.VAN;
    }

    @Override
    public VehicleCategory category() {
        return VehicleCategory.VAN;
    }

    @Override
    public BigDecimal calculate(PricingContext context) {
        return BigDecimal.valueOf(properties.getVan().getBaseDayRate() * context.days());
    }
}