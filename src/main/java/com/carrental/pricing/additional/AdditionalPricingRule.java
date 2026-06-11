package com.carrental.pricing.additional;

import com.carrental.pricing.PricingContext;

import java.math.BigDecimal;

public interface AdditionalPricingRule {

    int order();

    BigDecimal apply(BigDecimal currentPrice, PricingContext context);
}