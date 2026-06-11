package com.carrental.dto;

import com.carrental.model.DriverInfo;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.validation.DateRangeAware;
import com.carrental.validation.ValidDateRange;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@ValidDateRange
public record ReserveRequest(

        @NotNull(message = "Vehicle category is required")
        VehicleCategory vehicleCategory,

        @NotNull(message = "Driver Info is mandatory")
        DriverInfo driverInfo,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate,

        @Min(value = 0, message = "Mileage cannot be negative")
        int dailyMileage) implements DateRangeAware {
}