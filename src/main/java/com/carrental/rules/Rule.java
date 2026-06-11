package com.carrental.rules;

public interface Rule {

    RuleResult validate(ReservationContext context);
}