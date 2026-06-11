package com.carrental.dto;

import com.carrental.validation.DateRangeAware;
import com.carrental.validation.ValidDateRange;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@ValidDateRange
public record ModifyReservationRequest(

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate
) implements DateRangeAware {
}