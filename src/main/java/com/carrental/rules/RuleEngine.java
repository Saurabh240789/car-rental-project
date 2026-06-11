package com.carrental.rules;

import com.carrental.exception.RuleViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleEngine {

    private final List<Rule> rules;

    public RuleEngine(List<Rule> rules) {
        this.rules = rules;
    }

    public void validate(ReservationContext context) {
        for (Rule rule : rules) {
            RuleResult result = rule.validate(context);

            if (!result.success()) {
                throw new RuleViolationException(result.message());
            }
        }
    }
}