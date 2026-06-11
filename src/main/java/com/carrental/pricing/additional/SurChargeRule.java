package com.carrental.pricing.additional;

import com.carrental.pricing.PricingContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SurChargeRule implements AdditionalPricingRule {
    @Override
    public int order() {
        return 2;
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingContext context) {
        if (!context.surChargeApplicable()) {
            return currentPrice;
        }

        return currentPrice.multiply(new BigDecimal("1.10"));
    }
}
