package com.carrental.service;

import com.carrental.exception.BookingCancellationException;
import com.carrental.model.Reservation;
import com.carrental.model.enums.ReservationStatus;
import com.carrental.repository.ReservationRepository;
import com.carrental.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Slf4j
@Service
public class CancellationService {
    private final ReservationRepository reservationRepository;
    private final InventoryService inventoryService;

    public CancellationService(ReservationRepository reservationRepository, InventoryService inventoryService) {
        this.reservationRepository = reservationRepository;
        this.inventoryService = inventoryService;
    }

    public void cancel(String reservationId) {
        log.info("Received cancellation Request for reservation id : {} ", reservationId);
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new BookingCancellationException("Reservation not found"));

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            log.error("Requested reservation id : {} is already cancelled", reservationId);
            throw new BookingCancellationException("Booking already cancelled");
        }
        Set<LocalDate> dates = DateUtils.getDates(reservation.getStartDate(), reservation.getEndDate());
        inventoryService.releaseDates(reservation.getVehicleCategory(), dates);

        reservation.setStatus(ReservationStatus.CANCELLED);

        reservationRepository.save(reservation);
    }
}
