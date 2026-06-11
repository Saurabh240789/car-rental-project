package com.carrental.service;

import com.carrental.dto.ModifyReservationRequest;
import com.carrental.dto.ReserveRequest;
import com.carrental.exception.CarRentalException;
import com.carrental.exception.OptimisticLockingException;
import com.carrental.exception.VehicleNotFoundException;
import com.carrental.model.DailyInventory;
import com.carrental.model.InventoryKey;
import com.carrental.model.Reservation;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.pricing.PricingContext;
import com.carrental.pricing.PricingEngine;
import com.carrental.repository.InventoryRepository;
import com.carrental.repository.ReservationRepository;
import com.carrental.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class BookingService {

    private static final int MAX_RETRIES = 3;
    private final InventoryRepository inventoryRepository;
    private final ReservationRepository reservationRepository;
    private final PricingEngine pricingEngine;
    private final InventoryService inventoryService;

    public BookingService(InventoryRepository inventoryRepository, ReservationRepository reservationRepository, PricingEngine pricingEngine, InventoryService inventoryService) {
        this.inventoryRepository = inventoryRepository;
        this.reservationRepository = reservationRepository;
        this.pricingEngine = pricingEngine;
        this.inventoryService = inventoryService;
    }

    public Reservation reserve(ReserveRequest request) {

        log.info("Received reserve request: {}", request);
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                return reserveInventoryInternal(request);
            } catch (OptimisticLockingException ex) {
                log.warn("Optimistic locking conflict. Attempt {}/{} for request: {}",
                        attempt, MAX_RETRIES, request, ex);

                if (attempt == MAX_RETRIES) {
                    log.error("Reservation failed after {} attempts for request: {}",
                            MAX_RETRIES, request);
                    throw new CarRentalException("Failed to book car!");
                }
            }
        }

        throw new CarRentalException("Failed to book car!");
    }

    private Reservation reserveInventoryInternal(ReserveRequest request) {

        VehicleCategory category = request.vehicleCategory();
        List<DailyInventory> inventoryRows = inventoryRepository.findBetween(category, request.startDate(), request.endDate());

        if (inventoryRows.isEmpty()) {
            log.error("Requested vehicle is not available : {} ", request);
            throw new VehicleNotFoundException("Vehicle not found!");
        }

        Map<InventoryKey, Long> versions = new HashMap<>();
        for (DailyInventory row : inventoryRows) {
            if (row.getAvailableCount() <= 0) {
                log.error("Requested vehicle is not available : {} ", request);
                throw new RuntimeException("Inventory unavailable");
            }

            versions.put(new InventoryKey(row.getVehicleCategory(), row.getInventoryDate()), row.getVersion());
        }
        for (DailyInventory row : inventoryRows) {

            InventoryKey key = new InventoryKey(row.getVehicleCategory(), row.getInventoryDate());
            Long expectedVersion = versions.get(key);

            if (!Objects.equals(expectedVersion, row.getVersion())) {
                throw new OptimisticLockingException("Inventory modified");
            }
        }
        for (DailyInventory row : inventoryRows) {
            row.setAvailableCount(row.getAvailableCount() - 1);
            row.incrementVersion();

            inventoryRepository.save(row);
        }
        int licenceIssuedYears = Period.between(request.driverInfo().licenseIssueDate(), LocalDate.now()).getYears();
        boolean surChargeEnabled = category.equals(VehicleCategory.PICKUP_TRUCK) && licenceIssuedYears <= 3;
        boolean cleaningFeeApplicable = category.equals(VehicleCategory.VAN);
        Reservation reservation = new Reservation(category, request.driverInfo(), request.startDate(), request.endDate(), request.dailyMileage(),
                pricingEngine.calculate(new PricingContext(category,
                        DateUtils.days(request.startDate(), request.endDate()), surChargeEnabled, cleaningFeeApplicable, request.dailyMileage())));

        reservationRepository.save(reservation);

        return reservation;
    }

    public Reservation modify(String reservationId, ModifyReservationRequest request) {
        log.info("Received modify reservation Request : {} ", request);
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found"));
        Set<LocalDate> oldDates = DateUtils.getDates(reservation.getStartDate(), reservation.getEndDate());
        Set<LocalDate> newDates = DateUtils.getDates(request.startDate(), request.endDate());
        Set<LocalDate> datesToRelease = new HashSet<>(oldDates);

        datesToRelease.removeAll(newDates);

        Set<LocalDate> datesToReserve = new HashSet<>(newDates);

        datesToReserve.removeAll(oldDates);

        try {
            inventoryService.releaseDates(reservation.getVehicleCategory(), datesToRelease);
            inventoryService.reserveDates(reservation.getVehicleCategory(), datesToReserve);
        } catch (Exception ex) {
            log.error("Failed to modify reservation Request : {} with error :", request, ex);
            inventoryService.reserveDates(reservation.getVehicleCategory(), datesToRelease);
            throw new CarRentalException(ex.getMessage());
        }

        reservation.setStartDate(request.startDate());
        reservation.setEndDate(request.endDate());

        reservationRepository.save(reservation);

        return reservation;
    }
}
