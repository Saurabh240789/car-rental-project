package com.carrental.model;

import java.time.LocalDate;

public record ReservationContext(
        DriverInfo driverInfo,
        Vehicle vehicle,
        LocalDate startDate,
        LocalDate endDate,
        int dailyMileage) {
}