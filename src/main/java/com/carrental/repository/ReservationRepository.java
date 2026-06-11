package com.carrental.repository;

import com.carrental.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ReservationRepository {
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

    public Reservation save(Reservation reservation) {
        reservations.put(reservation.getReservationId(), reservation);

        return reservation;
    }

    public Optional<Reservation> findById(String reservationId) {

        return Optional.ofNullable(reservations.get(reservationId));
    }
}