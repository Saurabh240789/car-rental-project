package com.carrental.pricing;

import com.carrental.model.enums.VehicleCategory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PricingEngine {

    private final Map<VehicleCategory, List<PricingRule>> ruleMap;

    public PricingEngine(List<PricingRule> rules) {
        this.ruleMap = rules.stream()
                .collect(Collectors.groupingBy(PricingRule::category));
    }

    public double calculate(PricingContext context) {
        VehicleCategory category = context.category();

        List<PricingRule> rules = ruleMap.get(category);

        if (rules == null || rules.isEmpty()) {
            throw new IllegalStateException(
                    "No pricing rules found for category: " + category
            );
        }

        return rules.stream()
                .filter(rule -> rule.supports(context))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No matching pricing rule for category: "
                                        + category
                                        + " with given context"
                        ))
                .calculate(context);
    }
}