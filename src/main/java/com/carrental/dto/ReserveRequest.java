package com.carrental.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReserveRequest(

        @NotBlank(message = "Vehicle id is required")
        String vehicleId,

        @NotBlank(message = "User id is required")
        String userId,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate,

        @Min(value = 0, message = "Mileage cannot be negative")
        int dailyMileage) {
}