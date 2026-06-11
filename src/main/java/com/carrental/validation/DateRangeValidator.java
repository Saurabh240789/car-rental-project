package com.carrental.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator
        implements ConstraintValidator<ValidDateRange,
        DateRangeAware> {

    @Override
    public boolean isValid(DateRangeAware request,
                           ConstraintValidatorContext context) {

        if (request.startDate() == null ||
                request.endDate() == null) {
            return true;
        }

        return !request.startDate().isAfter(request.endDate());
    }
}