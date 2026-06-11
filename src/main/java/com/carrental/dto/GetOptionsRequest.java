package com.carrental.dto;

import com.carrental.model.DriverInfo;
import com.carrental.validation.DateRangeAware;
import com.carrental.validation.ValidDateRange;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@ValidDateRange
public record GetOptionsRequest(
        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate,

        @Min(
                value = 0,
                message = "Mileage cannot be negative")
        int dailyMileage,

        @NotNull
        DriverInfo driverInfo) implements DateRangeAware {
}