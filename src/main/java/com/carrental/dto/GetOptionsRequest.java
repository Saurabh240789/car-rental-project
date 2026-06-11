package com.carrental.dto;

import com.carrental.model.enums.LicenseType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GetOptionsRequest(

        @Positive(
                message = "Duration must be greater than zero")
        int durationDays,

        @Min(
                value = 0,
                message = "Mileage cannot be negative")
        int dailyMileage,

        @NotNull
        LicenseType licenseType) {
}