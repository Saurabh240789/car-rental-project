package com.carrental.dto;

import com.carrental.model.Reservation;

public final class ReservationMapper {

    private ReservationMapper() {
    }

    public static ReservationResponse toResponse(Reservation reservation) {

        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getVehicleId(),
                reservation.getUserId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getDailyMileage(),
                reservation.getAmount(),
                reservation.getStatus());
    }
}