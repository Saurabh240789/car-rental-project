package com.carrental.model;

import com.carrental.model.enums.ReservationStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Reservation {

    private final String reservationId;
    private final String vehicleId;
    private final String userId;

    private LocalDate startDate;
    private LocalDate endDate;
    private int dailyMileage;
    private double amount;

    private ReservationStatus status;

    public Reservation(
            String vehicleId,
            String userId,
            LocalDate startDate,
            LocalDate endDate,
            int dailyMileage,
            double amount
    ) {
        this.reservationId = UUID.randomUUID().toString();
        this.vehicleId = vehicleId;
        this.userId = userId;

        validateDates(startDate, endDate);

        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyMileage = dailyMileage;
        this.amount = amount;
        this.status = ReservationStatus.ACTIVE;
    }

    public void modify(
            LocalDate newStartDate,
            LocalDate newEndDate,
            int newDailyMileage
    ) {
        ensureActive();
        validateDates(newStartDate, newEndDate);

        this.startDate = newStartDate;
        this.endDate = newEndDate;
        this.dailyMileage = newDailyMileage;
    }

    public void updateAmount(double newAmount) {
        ensureActive();

        if (newAmount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        this.amount = newAmount;
    }

    public void cancel() {
        ensureActive();
        this.status = ReservationStatus.CANCELLED;
    }

    public boolean isActive() {
        return this.status == ReservationStatus.ACTIVE;
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    private void ensureActive() {
        if (this.status != ReservationStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Operation allowed only on ACTIVE reservation"
            );
        }
    }

}