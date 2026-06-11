package com.carrental.repository;

import com.carrental.model.Reservation;
import com.carrental.model.enums.ReservationStatus;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

    private final Map<String, ReentrantLock> vehicleLocks = new ConcurrentHashMap<>();

    @Override
    public boolean reserveVehicle(String vehicleId, String userId) {

        ReentrantLock lock =
                vehicleLocks.computeIfAbsent(vehicleId, v -> new ReentrantLock());

        lock.lock();
        try {
            boolean alreadyReserved =
                    reservations.values().stream()
                            .anyMatch(r ->
                                    r.getVehicleId().equals(vehicleId)
                                            && r.getStatus() == ReservationStatus.ACTIVE);

            if (alreadyReserved) {
                return false;
            }

            return true;

        } finally {
            lock.unlock();
        }
    }

    @Override
    public void save(Reservation reservation) {
        reservations.put(reservation.getReservationId(), reservation);
    }

    @Override
    public Optional<Reservation> findById(String reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }

    @Override
    public void update(Reservation reservation) {
        reservations.put(reservation.getReservationId(), reservation);
    }

    @Override
    public void cancelReservation(String reservationId) {

        Reservation reservation = reservations.get(reservationId);

        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found: " + reservationId);
        }

        reservation.cancel();
    }
}