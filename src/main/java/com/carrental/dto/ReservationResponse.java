package com.carrental.dto;

import com.carrental.model.enums.ReservationStatus;

import java.time.LocalDate;

public record ReservationResponse(

        String reservationId,

        String vehicleId,

        String userId,

        LocalDate startDate,

        LocalDate endDate,

        int dailyMileage,

        double amount,

        ReservationStatus status) {
}