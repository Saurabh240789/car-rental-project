package com.carrental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ModifyReservationRequest(

        @NotBlank(message = "ReservationId cannot be empty")
        String reservationId,

        @NotBlank(message = "VehicleId cannot be empty")
        String vehicleId,

        @NotBlank(message = "UserId cannot be empty")
        String userId,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate,

        @Positive(message = "Duration must be greater than 0")
        int durationDays,

        @Positive(message = "Daily Mileage must be greater than 0")
        int dailyMileage

) {
}