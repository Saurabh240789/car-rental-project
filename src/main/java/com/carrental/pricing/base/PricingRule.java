package com.carrental.pricing.base;

import com.carrental.model.enums.VehicleCategory;
import com.carrental.pricing.PricingContext;

import java.math.BigDecimal;

public interface PricingRule {

    boolean supports(PricingContext context);

    VehicleCategory category();

    BigDecimal calculate(PricingContext context);
}