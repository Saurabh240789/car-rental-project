package com.carrental.pricing.additional;

import com.carrental.pricing.PricingContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CleaningFeeRule implements AdditionalPricingRule {
    @Override
    public int order() {
        return 1;
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingContext context) {
        if (!context.cleaningFeeApplicable()) {
            return currentPrice;
        }

        return currentPrice.multiply(new BigDecimal("1.10"));
    }
}
