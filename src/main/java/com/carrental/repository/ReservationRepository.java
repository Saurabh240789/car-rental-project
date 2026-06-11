package com.carrental.repository;

import com.carrental.model.Reservation;

import java.util.Optional;

public interface ReservationRepository {

    boolean reserveVehicle(String vehicleId, String userId);

    void cancelReservation(String reservationId);

    void save(Reservation reservation);

    Optional<Reservation> findById(String reservationId);

    void update(Reservation reservation);
}