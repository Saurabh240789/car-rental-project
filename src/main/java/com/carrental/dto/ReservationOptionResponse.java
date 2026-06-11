package com.carrental.dto;

import com.carrental.model.enums.VehicleCategory;

public record ReservationOptionResponse(

        VehicleCategory category,

        double totalAmount,

        boolean available

) {
}