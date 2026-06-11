package com.carrental.dto;

import com.carrental.model.enums.VehicleCategory;

public record GetOptionResponse(

        VehicleCategory category,

        double amount

) {
}