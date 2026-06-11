package com.carrental.pricing;

import com.carrental.model.enums.VehicleCategory;

public record PricingContext(VehicleCategory category,
                             long days,
                             boolean surChargeApplicable,
                             boolean cleaningFeeApplicable,
                             int dailyMileage) {
}