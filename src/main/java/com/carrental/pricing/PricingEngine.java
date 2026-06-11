package com.carrental.pricing;

import com.carrental.exception.CarRentalException;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.pricing.additional.AdditionalPricingRule;
import com.carrental.pricing.base.PricingRule;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PricingEngine {

    private final Map<VehicleCategory, List<PricingRule>> ruleMap;

    @Autowired
    private List<AdditionalPricingRule> additionalPricingRules;

    public PricingEngine(List<PricingRule> rules) {
        this.ruleMap = rules.stream()
                .collect(Collectors.groupingBy(PricingRule::category));
    }

    public BigDecimal calculate(PricingContext context) {
        VehicleCategory category = context.category();

        List<PricingRule> rules = ruleMap.get(category);

        if (rules == null || rules.isEmpty()) {
            throw new IllegalStateException("No pricing rules found for category: " + category);
        }

        BigDecimal totalPrice = rules.stream()
                .filter(rule -> rule.supports(context))
                .findFirst()
                .orElseThrow(() -> new CarRentalException("No matching pricing rule for category: " + category + " with given context"))
                .calculate(context);

        List<AdditionalPricingRule> orderedRules =
                additionalPricingRules.stream()
                        .sorted(Comparator.comparingInt(AdditionalPricingRule::order))
                        .toList();

        for (AdditionalPricingRule rule : orderedRules) {
            totalPrice = rule.apply(totalPrice, context);
        }
        return totalPrice;
    }
}