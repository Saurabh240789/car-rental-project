package com.carrental.pricing;

import com.carrental.model.enums.VehicleCategory;

public interface PricingRule {

    boolean supports(PricingContext context);

    VehicleCategory category();

    double calculate(PricingContext context);
}