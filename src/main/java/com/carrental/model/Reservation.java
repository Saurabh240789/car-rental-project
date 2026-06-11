package com.carrental.model;

import com.carrental.model.enums.ReservationStatus;
import com.carrental.model.enums.VehicleCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class Reservation {

    private final String reservationId;
    private final VehicleCategory vehicleCategory;
    private final DriverInfo driverInfo;

    private LocalDate startDate;
    private LocalDate endDate;
    private int dailyMileage;
    private BigDecimal amount;

    private ReservationStatus status;

    public Reservation(VehicleCategory vehicleCategory, DriverInfo driverInfo, LocalDate startDate, LocalDate endDate, int dailyMileage, BigDecimal amount) {
        this.reservationId = UUID.randomUUID().toString();
        this.vehicleCategory = vehicleCategory;
        this.driverInfo = driverInfo;

        validateDates(startDate, endDate);

        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyMileage = dailyMileage;
        this.amount = amount;
        this.status = ReservationStatus.RESERVED;
    }

    public void modify(LocalDate newStartDate, LocalDate newEndDate, int newDailyMileage) {
        ensureActive();
        validateDates(newStartDate, newEndDate);

        this.startDate = newStartDate;
        this.endDate = newEndDate;
        this.dailyMileage = newDailyMileage;
    }

    public void updateAmount(BigDecimal newAmount) {
        ensureActive();

        if (newAmount.compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        this.amount = newAmount;
    }

    public void cancel() {
        ensureActive();
        this.status = ReservationStatus.CANCELLED;
    }

    public boolean isActive() {
        return this.status == ReservationStatus.RESERVED;
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
        if (this.status != ReservationStatus.RESERVED) {
            throw new IllegalStateException(
                    "Operation allowed only on ACTIVE reservation"
            );
        }
    }

}