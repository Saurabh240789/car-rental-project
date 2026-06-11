package com.carrental.rules;

public record RuleResult(
        boolean success,
        String message) {


    public boolean validate() {
        return new RuleResult(
                true,
                "SUCCESS").success();
    }

    public RuleResult failure(
            String message) {

        return new RuleResult(
                false,
                message);
    }
}